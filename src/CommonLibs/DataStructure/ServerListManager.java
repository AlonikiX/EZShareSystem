package CommonLibs.DataStructure;

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

    private ArrayList<String> serverList;
    private ReadWriteLock rwlock;

    private ServerListManager() {
        this.serverList = new ArrayList<>();
        this.rwlock = new ReentrantReadWriteLock();
        exchangeInterval = 10000;
    }

    public static ServerListManager sharedServerListManager() {
        if (null == severListManager) {
            severListManager = new ServerListManager();
        }
        return severListManager;
    }

    public void runAutoExchange(){
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.rwlock.readLock().lock();
            int size = this.serverList.size();
            if (0 != size) {
                Random random = new Random();
                int index = random.nextInt(size-1);
                String server = this.serverList.get(index);
            }
            this.rwlock.readLock().unlock();
        }
    }

    public void updateServerList(ArrayList<String> serverList) {
        this.rwlock.readLock().lock();
        for (String server : serverList) {
            if (false == this.serverList.contains(server)) {
                this.serverList.add(server);
            }
        }
        this.rwlock.readLock().unlock();
    }
}
