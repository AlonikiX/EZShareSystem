package EZShare_Server;

import EZShare_Client.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 26/04/2017.
 */
public class ClientListManager {
    private static ClientListManager clientListManager;
    private ServerSetting setting;
    private HashMap<String, Integer> clientList;
    private ReadWriteLock readWriteLock;

    private ClientListManager() {
        clientList = new HashMap<>();
        setting = ServerSetting.sharedSetting();
        readWriteLock = new ReentrantReadWriteLock();
    }

    public static ClientListManager shareClientListManager() {
        if (null == clientListManager) {
            clientListManager = new ClientListManager();
        }
        return clientListManager;
    }

    public boolean limitConnection(String client) {
        readWriteLock.readLock().lock();
        //limit a client connects to the server if it still has interval limit.
        if (clientList.containsKey(client)) {
            readWriteLock.readLock().unlock();
            return true;
        }else {
            readWriteLock.readLock().unlock();
            return false;
        }
    }

    public void checkClientInertvalLimit() {
        int connectionIntervalLimit = setting.getConnectionIntervalLimit();
        while (true) {
            try {//check interval limit for each client
                Thread.sleep(connectionIntervalLimit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //create a temporary list to store the items that need to be removed
            ArrayList<String> temp = new ArrayList<>();
            readWriteLock.readLock().lock();
            //calculate the remain limit time for each client
            for (String client : clientList.keySet()) {
                int interval = (Integer)clientList.get(client) - connectionIntervalLimit;
                if (interval <= 0) {
                    temp.add(client);
                }
            }
            readWriteLock.readLock().unlock();
            readWriteLock.writeLock().lock();
            //remove clients who have reached the interval limit
            for (String client : temp) {
                clientList.remove(client);
            }
            readWriteLock.writeLock().unlock();
        }
    }

    //add a new client to the limited list
    public void addIntervalLimitedClient(String client) {
        readWriteLock.writeLock().lock();
        clientList.put(client, setting.getConnectionIntervalLimit());
        readWriteLock.writeLock().unlock();
    }
}
