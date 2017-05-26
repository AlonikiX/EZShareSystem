package CommonLibs.DataStructure;

import CommonLibs.Communication.Communicator;
import EZShare_Server.ServerSetting;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/26.
 */
public class ConnectionList {

    private HashMap<String,Communicator> list = new HashMap<String,Communicator>();
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    public ConnectionList(){

    }

    public Communicator connect(IPAddress address){
        String key = address.toString();
        Communicator communicator = null;
        rwlock.readLock().lock();
        if (list.containsKey(key)){
            communicator = list.get(key);
        } else {
            rwlock.writeLock().lock();
            communicator = new Communicator(ServerSetting.sharedSetting());
            communicator.connectToServer(address.hostname, address.port);
            list.put(key,communicator);
            rwlock.writeLock().unlock();
        }
        rwlock.readLock().unlock();
        return communicator;
    }

    public void disconnectAll(){
        rwlock.writeLock().lock();
        list.clear();
        rwlock.writeLock().unlock();
    }

    public void disconnect(String key){
        rwlock.writeLock().lock();
        list.remove(key);
        rwlock.writeLock().unlock();
    }

    public void disconnect(IPAddress address){
        String key = address.hostname + address.port;
        disconnect(key);
    }

}
