package CommonLibs.DataStructure;

import EZShare_Server.Handler.Handler;
import EZShare_Server.Handler.SubscribeHandler;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/26.
 */
public class HandlerList {

    private ArrayList<SubscribeHandler> list = new ArrayList<SubscribeHandler>();
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    public HandlerList(){

    }

    public void add(SubscribeHandler handler){
        rwlock.writeLock().lock();
        list.add(handler);
        rwlock.writeLock().unlock();
    }

    public void remove(SubscribeHandler handler){
        rwlock.writeLock().lock();
        list.remove(handler);
        rwlock.writeLock().unlock();
    }

    public void notify(Resource resource){


        // notify everyone!


    }

}
