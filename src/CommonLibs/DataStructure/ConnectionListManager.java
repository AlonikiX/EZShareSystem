package CommonLibs.DataStructure;

import CommonLibs.Communication.Communicator;
import CommonLibs.Setting.SecurityMode;

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

    public Connection connect(IPAddress address, SecurityMode securityMode){
        String key = address.toString();
        Connection connection = null;
        if (SecurityMode.secure == securityMode){
            srwlock.readLock().lock();
            if (secureConList.containsKey(key)){
                connection = secureConList.get(key);
            } else {
                srwlock.writeLock().lock();
                if (!secureConList.containsKey(key)){
                    connection = new Connection(address,SecurityMode.secure);
                    secureConList.put(key,connection);
                    Thread thread =  new Thread(connection);
                    thread.run();
                } else {
                    connection = secureConList.get(key);
                }
                srwlock.writeLock().unlock();
            }
            srwlock.readLock().unlock();
            return connection;
        } else {
            rwlock.readLock().lock();
            if (!connectionList.containsKey(key)){
                connection = connectionList.get(key);
            } else {
                rwlock.writeLock().lock();
                if (connectionList.containsKey(key)){
                    connection = new Connection(address,SecurityMode.inSecure);
                    connectionList.put(key,connection);
                    Thread thread =  new Thread(connection);
                    thread.run();
                } else {
                    connection = connectionList.get(key);
                }
                rwlock.writeLock().unlock();
            }
            rwlock.readLock().unlock();
            return connection;
        }
    }

    public void disconnectAll(SecurityMode securityMode){
        if (SecurityMode.secure == securityMode){
            srwlock.writeLock().lock();
            secureConList.clear();
            srwlock.writeLock().unlock();
        } else {
            rwlock.writeLock().lock();
            connectionList.clear();
            rwlock.writeLock().unlock();
        }
    }

    public void disconnect(String key, SecurityMode securityMode){
        if (SecurityMode.secure == securityMode){
            srwlock.writeLock().lock();
            secureConList.remove(key);
            srwlock.writeLock().unlock();
        } else {
            rwlock.writeLock().lock();
            connectionList.remove(key);
            rwlock.writeLock().unlock();
        }
    }

    public void disconnect(IPAddress address, SecurityMode securityMode){
        String key = address.hostname + address.port;
        disconnect(key, securityMode);
    }
}
