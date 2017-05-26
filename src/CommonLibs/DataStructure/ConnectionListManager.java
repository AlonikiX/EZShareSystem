package CommonLibs.DataStructure;

import CommonLibs.Communication.Communicator;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/26.
 */
public class ConnectionListManager {

    private static ConnectionListManager connectionListManager;

    private HashMap<String,Connection> connectionList;
    private HashMap<String,Connection> secureConList;
    private ReentrantReadWriteLock rwlock;
    private ReentrantReadWriteLock srwlock;

    private ConnectionListManager() {
        connectionList = new HashMap<String,Connection>();
        secureConList = new HashMap<String,Connection>();
        rwlock = new ReentrantReadWriteLock();
        srwlock = new ReentrantReadWriteLock();
    }

    public static ConnectionListManager sharedConnectionListManager() {
        if (null == connectionListManager) {
            synchronized(ConnectionListManager.class){
                if(null == connectionListManager);
                connectionListManager = new ConnectionListManager();
            }
        }
        return connectionListManager;
    }

    public Connection connect(IPAddress address, boolean secure){
        String key = address.toString();
        Connection connection = null;
        if (secure){
            srwlock.readLock().lock();
            if (secureConList.containsKey(key)){
                connection = secureConList.get(key);
            } else {
                srwlock.writeLock().lock();
                connection = new Connection(address);
                secureConList.put(key,connection);
                srwlock.writeLock().unlock();
            }
            srwlock.readLock().unlock();
            return connection;
        } else {
            rwlock.readLock().lock();
            if (connectionList.containsKey(key)){
                connection = connectionList.get(key);
            } else {
                rwlock.writeLock().lock();
                connection = new Connection(address);
                connectionList.put(key,connection);
                rwlock.writeLock().unlock();
            }
            rwlock.readLock().unlock();
            return connection;
        }
    }

    public void disconnectAll(boolean secure){
        if (secure){
            srwlock.writeLock().lock();
            secureConList.clear();
            srwlock.writeLock().unlock();
        } else {
            rwlock.writeLock().lock();
            connectionList.clear();
            rwlock.writeLock().unlock();
        }
    }

    public void disconnect(String key, boolean secure){
        if (secure){
            srwlock.writeLock().lock();
            secureConList.remove(key);
            srwlock.writeLock().unlock();
        } else {
            rwlock.writeLock().lock();
            connectionList.remove(key);
            rwlock.writeLock().unlock();
        }
    }

    public void disconnect(IPAddress address, boolean secure){
        String key = address.hostname + address.port;
        disconnect(key, secure);
    }
}
