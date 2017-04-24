package EZShare_Server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * Created by Anson Chen on 2017/4/19.
 */

public class ServerList {

    private ReadWriteLock rtLock = new ReentrantReadWriteLock();
    private HashSet<EZShareServer> serverList = new HashSet<EZShareServer>();

    public ServerList(){

    }

    // TODO haven't add locks
    public boolean add(EZShareServer server){
        return serverList.add(server);
    }

    public boolean remove(EZShareServer server){
        return serverList.remove(server);
    }

    public boolean update(HashSet<EZShareServer> list){
        return serverList.addAll(list);
    }

    public HashSet<EZShareServer> getList(){
        return serverList;
    }

}
