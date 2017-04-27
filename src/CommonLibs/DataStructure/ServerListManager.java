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

    private ArrayList<IPAddress> serverList;
    private ReadWriteLock rwlock;

    private ServerListManager() {
        this.serverList = new ArrayList<IPAddress>();
        this.rwlock = new ReentrantReadWriteLock();
        this.exchangeInterval = 10000;

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

    public ArrayList<IPAddress> getServerList() {
        return this.serverList;
    }

    /**
     * @description randomly choose a server and exchange server list with it automatically
     * @interval the exchanging interval time is based on server setting
     */
    private void runAutoExchange(){
        while (true) {
            //interval
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.rwlock.readLock().lock();
            int size = this.serverList.size();
            if (0 != size) {
                //randomly choose a server to exchange
                Random random = new Random();
                int index = random.nextInt(size) == 0 ? 0 : size - 1;
                IPAddress server = this.serverList.get(index);

                Command command = new ExchangeCommand(this.serverList);

                this.rwlock.readLock().unlock();

                //send exchange command to the chosen server
                Communicator communicator = new Communicator(ServerSetting.sharedSetting());
                if (communicator.connectToServer(server.hostname, server.port)) {//can connect to the chosen server
                    //send the server list to the chosen server
                    communicator.writeData(command.toJSON());
                    System.out.println("SENT TO "+server);
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
                    this.serverList.remove(index);
                    this.rwlock.writeLock().unlock();
                }
            }else {
                this.rwlock.readLock().unlock();
            }
        }
    }


    public void updateServerList(ArrayList<IPAddress> serverList) {
        this.rwlock.writeLock().lock();
        for (IPAddress server : serverList) {
            if (false == this.serverList.contains(server)) {
                this.serverList.add(server);
            }
        }
        this.rwlock.writeLock().unlock();
    }

    public ArrayList<IPAddress> cloneServerList(){
        ArrayList<IPAddress> list = new ArrayList<IPAddress>();
        this.rwlock.readLock().lock();
        list.addAll(serverList);
        this.rwlock.readLock().unlock();
        return list;
    }
}
