package EZShare_Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 26/04/2017.
 */
public class LimitedIPAddressListManager {
    private static LimitedIPAddressListManager limitedIPAddressListManager;
    private ServerSetting setting;
    private HashMap<String, Integer> clientList;
    private ReadWriteLock readWriteLock;

    private LimitedIPAddressListManager() {
        clientList = new HashMap<>();
        setting = ServerSetting.sharedSetting();
        readWriteLock = new ReentrantReadWriteLock();

        new Thread(()-> {
            checkIPAddressIntervalLimit();
        }).start();
    }

    public static LimitedIPAddressListManager shareClientListManager() {
        if (null == limitedIPAddressListManager) {
            limitedIPAddressListManager = new LimitedIPAddressListManager();
        }
        return limitedIPAddressListManager;
    }

    public boolean limitConnection(String ipAddress) {
        readWriteLock.readLock().lock();
        //limit a client connects to the server if it still has interval limit.
        if (clientList.containsKey(ipAddress)) {
            readWriteLock.readLock().unlock();
            return true;
        }else {
            readWriteLock.readLock().unlock();
            return false;
        }
    }

    public void checkIPAddressIntervalLimit() {
        int connectionIntervalLimit = setting.getConnectionIntervalLimit();
        int rate = 500;
        while (true) {
            try {//check interval limit for each IP address
                Thread.sleep(rate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //create a temporary list to store the items that need to be removed
            ArrayList<String> temp = new ArrayList<>();
            readWriteLock.readLock().lock();
            //calculate the remain limit time for each IP address
            for (String ipAddress : clientList.keySet()) {
                int interval = (Integer)clientList.get(ipAddress) - rate;
                clientList.replace(ipAddress, interval);
                if (interval <= 0) {
                    temp.add(ipAddress);
                }
            }
            readWriteLock.readLock().unlock();
            readWriteLock.writeLock().lock();
            //remove IP address who have reached the interval limit
            for (String ipAddress : temp) {
                clientList.remove(ipAddress);
//                System.out.println("remove limited ip address" + ipAddress);
            }
            readWriteLock.writeLock().unlock();
        }
    }

    //add a new IP address to the limited list
    public void addIntervalLimitedIPAddress(String ipAddress) {
        readWriteLock.writeLock().lock();
        clientList.put(ipAddress, setting.getConnectionIntervalLimit());
        readWriteLock.writeLock().unlock();
    }
}
