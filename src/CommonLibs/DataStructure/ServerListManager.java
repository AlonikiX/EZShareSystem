package CommonLibs.DataStructure;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.ExchangeCommand;
import CommonLibs.Communication.Communicator;
import CommonLibs.Setting.SecurityMode;
import EZShare_Server.ServerSetting;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 20/04/2017.
 */
public class ServerListManager {
    private static ServerListManager serverListManager;
    private int exchangeInterval;

    private ArrayList<ISubscriber> subscriberList;
    private ReadWriteLock rsslock;

    private ArrayList<IPAddress> insecureServerList;
    private ArrayList<IPAddress> secureServerList;
    private ReadWriteLock rwlock;
    private ReadWriteLock srwlock;

    private ServerListManager() {
        this.subscriberList = new ArrayList<>();
        this.rsslock = new ReentrantReadWriteLock();

        this.insecureServerList = new ArrayList<IPAddress>();
        this.secureServerList = new ArrayList<IPAddress>();
        this.rwlock = new ReentrantReadWriteLock();
        this.srwlock = new ReentrantReadWriteLock();
        this.exchangeInterval = 12000;

        new Thread((Runnable) ()-> {
            runAutoExchange(SecurityMode.inSecure);
        }).start();

        new Thread((Runnable) ()-> {
            runAutoExchange(SecurityMode.secure);
        }).start();

    }

    public static ServerListManager sharedServerListManager() {
        if (null == serverListManager) {
            serverListManager = new ServerListManager();
        }
        return serverListManager;
    }

    public ArrayList<IPAddress> getInsecureServerList() {
        return this.insecureServerList;
    }
    public ArrayList<IPAddress> getSecureServerList() {
        return this.secureServerList;
    }

    /**
     * @description randomly choose a server and exchange server list with it automatically
     * @interval the exchanging interval time is based on server setting
     */
    private void runAutoExchange(SecurityMode securityMode){
        ArrayList<IPAddress> originList;
        ReadWriteLock lock;
        if (SecurityMode.inSecure == securityMode){
            originList = this.insecureServerList;
            lock = this.rwlock;
        }else {
            originList = this.secureServerList;
            lock = this.srwlock;
        }
        while (true) {
            //interval
            try {
                Thread.sleep(this.exchangeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.readLock().lock();
            int size = originList.size();
            if (0 != size) {
                //randomly choose a server to exchange
                IPAddress ipAddress;
                int index;
                do {
                    Random random = new Random();
                    index = random.nextInt(size);
                    ipAddress = originList.get(index);
                }while (isLocalHost(securityMode, ipAddress));

                Command command = new ExchangeCommand(originList);

                lock.readLock().unlock();

                //send exchange command to the chosen server
                Communicator communicator = new Communicator(ServerSetting.sharedSetting());
                communicator.setSecureMode(securityMode);
                if (communicator.connectToServer(ipAddress.hostname, ipAddress.port)) {//can connect to the chosen server
                    //send the server list to the chosen server
                    communicator.writeData(command.toJSON());
                    System.out.println("SENT TO "+ipAddress);
                    //handle results
                    while (true) {
                        if (0 < communicator.readableData()) {
                            String data = communicator.readData();
                            JSONObject object = new JSONObject(data);
                            if (object.has(OptionField.response.getValue())) {
                                String response = object.getString(OptionField.response.getValue());
                                if (0 == response.compareTo(OptionField.success.getValue())) {
                                    break;
                                }
                            }
                            if (object.has(OptionField.errorMessage.getValue())) {
                                break;
                            }
                        }
                    }
                } else {//fail to connect to the chosen server, remove it from the server list
                    lock.writeLock().lock();
                    originList.remove(index);
                    lock.writeLock().unlock();
                }
            }else {
                lock.readLock().unlock();
            }
        }
    }


    private boolean isLocalHost(SecurityMode securityMode, IPAddress ipAddress) {
        if (SecurityMode.inSecure == securityMode) {
            if (ServerSetting.sharedSetting().getPort() == ipAddress.port) {
                if (ServerSetting.sharedSetting().getHosts().contains(ipAddress.hostname)) {
                    return true;
                }
            }
        } else {
            if (ServerSetting.sharedSetting().getSecurePort() == ipAddress.port) {
                if (ServerSetting.sharedSetting().getHosts().contains(ipAddress.hostname)) {
                    return true;
                }
            }
        }
        return false;
    }

//    public void updateServerList(SecurityMode securityMode, ArrayList<IPAddress> serverList) {
//        ArrayList<IPAddress> originList;
//        ReadWriteLock lock;
//        if (SecurityMode.inSecure == securityMode) {
//            originList = this.insecureServerList;
//            lock = this.rwlock;
//        }else {
//            originList = this.secureServerList;
//            lock = this.srwlock;
//        }
//        lock.writeLock().lock();
//        for (IPAddress server : serverList) {
//            if (!checkExists(securityMode, server)) {
//                originList.add(server);
//            }
//        }
//        lock.writeLock().unlock();
//    }

    /**
     * update the server list
     * @param securityMode whether this update is through a secure mode
     * @param serverList the list containing the candidate addresses
     * @return the addresses really updated
     */
    public void updateServerList(SecurityMode securityMode, ArrayList<IPAddress> serverList) {
        ArrayList<IPAddress> originList;
        ReadWriteLock lock;
        if (SecurityMode.inSecure == securityMode) {
            originList = this.insecureServerList;
            lock = this.rwlock;
        }else {
            originList = this.secureServerList;
            lock = this.srwlock;
        }
        lock.writeLock().lock();
        for (IPAddress server : serverList) {
            if (!checkExists(securityMode, server)) {
                originList.add(server);
                push(securityMode, server);
            }
        }
        lock.writeLock().unlock();
    }

    public void register(ISubscriber subscriber){
        this.rsslock.writeLock().lock();
        this.subscriberList.add(subscriber);
        this.rsslock.writeLock().unlock();
    }

    private void push(SecurityMode securityMode, IPAddress address){
        for (ISubscriber subscriber : this.subscriberList){
            subscriber.pull(securityMode, address);
        }
    }


    private boolean checkExists(SecurityMode securityMode, IPAddress ipAddress) {
        ArrayList<IPAddress> originList;
        if (SecurityMode.inSecure == securityMode){
            originList = this.insecureServerList;
        }else {
            originList = this.secureServerList;
        }
        for (IPAddress server : originList) {
            if (server.port == ipAddress.port) {
                if (0 == server.hostname.compareTo(ipAddress.hostname)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<IPAddress> cloneServerList(SecurityMode securityMode){
        ArrayList<IPAddress> originList;
        ReadWriteLock lock;
        if (SecurityMode.inSecure == securityMode) {
            originList = this.insecureServerList;
            lock = this.rwlock;
        }else {
            originList = this.secureServerList;
            lock = this.srwlock;
        }

        ArrayList<IPAddress> copyList = new ArrayList<IPAddress>();
        lock.readLock().lock();
        copyList.addAll(originList);
        lock.readLock().unlock();

        return copyList;
    }
}
