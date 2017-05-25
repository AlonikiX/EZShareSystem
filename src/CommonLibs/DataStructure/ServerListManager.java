package CommonLibs.DataStructure;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.ExchangeCommand;
import CommonLibs.Communication.Communicator;
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
    private static ServerListManager severListManager;
    private int exchangeInterval;

    private ArrayList<IPAddress> insecureServerList;
    private ArrayList<IPAddress> secureServerList;
    private ReadWriteLock rwlock;

    private ServerListManager() {
        this.insecureServerList = new ArrayList<IPAddress>();
        this.rwlock = new ReentrantReadWriteLock();
        this.exchangeInterval = 12000;

        new Thread((Runnable) ()-> {
            runAutoExchange();
        }).start();

    }

    public static ServerListManager sharedServerListManager() {
        if (null == severListManager) {
            severListManager = new ServerListManager();
        }
        return severListManager;
    }

    public ArrayList<IPAddress> getInsecureServerList() {
        return this.insecureServerList;
    }

    /**
     * @description randomly choose a server and exchange server list with it automatically
     * @interval the exchanging interval time is based on server setting
     */
    private void runAutoExchange(){
        while (true) {
            //interval
            try {
                Thread.sleep(this.exchangeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.rwlock.readLock().lock();
            int size = this.insecureServerList.size();
            if (0 != size) {
                //randomly choose a server to exchange
                IPAddress ipAddress;
                int index;
                do {
                    Random random = new Random();
                    index = random.nextInt(size);
                    ipAddress = this.insecureServerList.get(index);
                }while (isLocalHost(ipAddress));

                Command command = new ExchangeCommand(this.insecureServerList);

                this.rwlock.readLock().unlock();

                //send exchange command to the chosen server
                Communicator communicator = new Communicator(ServerSetting.sharedSetting());
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
                    this.rwlock.writeLock().lock();
                    this.insecureServerList.remove(index);
                    this.rwlock.writeLock().unlock();
                }
            }else {
                this.rwlock.readLock().unlock();
            }
        }
    }


    public boolean isLocalHost(IPAddress ipAddress) {
        if (ServerSetting.sharedSetting().getPort() == ipAddress.port) {
            if (ServerSetting.sharedSetting().getHosts().contains(ipAddress.hostname)) {
                return true;
            }
        }
        return false;
    }

    public void updateServerList(ArrayList<IPAddress> serverList) {
        this.rwlock.writeLock().lock();
        for (IPAddress server : serverList) {
            if (false == checkExists(server)) {
                this.insecureServerList.add(server);
            }
        }
        this.rwlock.writeLock().unlock();
    }

    public boolean checkExists(IPAddress ipAddress) {
        for (IPAddress server : insecureServerList) {
            if (server.port == ipAddress.port) {
                if (0 == server.hostname.compareTo(ipAddress.hostname)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<IPAddress> cloneServerList(){
        ArrayList<IPAddress> list = new ArrayList<IPAddress>();
        this.rwlock.readLock().lock();
        list.addAll(insecureServerList);
        this.rwlock.readLock().unlock();
        return list;
    }
}
