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
    private Communicator communicator;
    private static ServerListManager severListManager;
    private int exchangeInterval;

    private ArrayList<ServerStructure> serverList;
    private ReadWriteLock rwlock;

    private ServerListManager() {
        this.communicator = new Communicator(ServerSetting.sharedSetting());
        this.serverList = new ArrayList<>();
        this.rwlock = new ReentrantReadWriteLock();
        this.exchangeInterval = 10000;

        runAutoExchange();
    }

    public static ServerListManager sharedServerListManager() {
        if (null == severListManager) {
            severListManager = new ServerListManager();
        }
        return severListManager;
    }

    public ArrayList<ServerStructure> getServerList() {
        return this.serverList;
    }

    /**
     * @description randomly choose a server and exchange server list with it automatically
     * @interval the exchanging interval time is based on server setting
     */
    public void runAutoExchange(){
        while (true) {
            //interval
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.rwlock.readLock().lock();
            int size = this.serverList.size();
            if (0 != size) {
                //randomly choose a server to exchange
                Random random = new Random();
                int index = random.nextInt(size-1);
                ServerStructure server = this.serverList.get(index);

                Command command = new ExchangeCommand(this.serverList);

                this.rwlock.readLock().unlock();

                //send exchange command to this server
                communicator.connectToServer(server.host, server.port);
                communicator.writeData(command.toJSON());
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
            } else {
                this.rwlock.readLock().unlock();
            }
        }
    }


    public void updateServerList(ArrayList<ServerStructure> serverList) {
        this.rwlock.writeLock().lock();
        for (ServerStructure server : serverList) {
            if (false == this.serverList.contains(server)) {
                this.serverList.add(server);
            }
        }
        this.rwlock.writeLock().unlock();
    }
}
