package EZShare_Server;

import CommonLibs.DataStructure.Resource;
import java.util.HashSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/4/19.
 */
public class ResourceList {

    private ReadWriteLock rtLock = new ReentrantReadWriteLock();
    private HashSet<Resource> serverList = new HashSet<Resource>();

    public ResourceList(){

    }

    public boolean add(Resource resource){
        return serverList.add(resource);
    }

    public boolean remove(Resource resource){
        return serverList.remove(resource);
    }

    // TODO do we need this one?
    public boolean update(HashSet<Resource> list){
        return serverList.addAll(list);
    }

    public HashSet<Resource> getList(){
        return serverList;
    }

    public boolean contains(){
        // TODO
        return true;
    }


}
