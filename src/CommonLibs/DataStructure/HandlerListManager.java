package CommonLibs.DataStructure;

import EZShare.Server;
import EZShare_Server.Handler.Handler;
import EZShare_Server.Handler.SubscribeHandler;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/26.
 */
public class HandlerListManager {

    private static HandlerListManager handlerListManager;

    private ArrayList<SubscribeHandler> directList;
    private ArrayList<SubscribeHandler> indirectList;
    private ReentrantReadWriteLock drwlock;
    private ReentrantReadWriteLock irwlock;

    private HandlerListManager() {
        directList = new ArrayList<SubscribeHandler>();
        indirectList = new ArrayList<SubscribeHandler>();
        drwlock = new ReentrantReadWriteLock();
        irwlock = new ReentrantReadWriteLock();
    }

    public static HandlerListManager sharedHanderListManager() {
        if (null == handlerListManager) {
            synchronized(HandlerListManager.class){
                if(null == handlerListManager);
                handlerListManager = new HandlerListManager();
            }
        }
        return handlerListManager;
    }

    public void add(SubscribeHandler handler, boolean direct){
        if (direct){
            drwlock.writeLock().lock();
            directList.add(handler);
            drwlock.writeLock().unlock();
        } else {
            irwlock.writeLock().lock();
            indirectList.add(handler);
            irwlock.writeLock().unlock();
        }
    }

    public void remove(SubscribeHandler handler,boolean direct){
        if (direct){
            drwlock.writeLock().lock();
            directList.remove(handler);
            drwlock.writeLock().unlock();
        } else {
            irwlock.writeLock().lock();
            indirectList.remove(handler);
            irwlock.writeLock().unlock();
        }
    }

    public void notify(Resource resource, boolean all){
        drwlock.readLock().lock();
        for (SubscribeHandler handler : directList){
            handler.notify(resource,true);
        }
        drwlock.readLock().unlock();

        if (all){
            irwlock.readLock().lock();
            for (SubscribeHandler handler : indirectList){
                handler.notify(resource,false);
            }
            irwlock.readLock().unlock();
        }
    }

}
