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
    private ArrayList<SubscribeHandler> secDirectList;
    private ArrayList<SubscribeHandler> secIndirectList;
    private ReentrantReadWriteLock drwlock;
    private ReentrantReadWriteLock irwlock;
    private ReentrantReadWriteLock sdrwlock;
    private ReentrantReadWriteLock sirwlock;

    private HandlerListManager() {
        directList = new ArrayList<SubscribeHandler>();
        indirectList = new ArrayList<SubscribeHandler>();
        secDirectList = new ArrayList<SubscribeHandler>();
        secIndirectList = new ArrayList<SubscribeHandler>();
        drwlock = new ReentrantReadWriteLock();
        irwlock = new ReentrantReadWriteLock();
        sdrwlock = new ReentrantReadWriteLock();
        sirwlock = new ReentrantReadWriteLock();
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

    public void add(SubscribeHandler handler, boolean direct, boolean secure){
        if (direct && secure){
            sdrwlock.writeLock().lock();
            secDirectList.add(handler);
            sdrwlock.writeLock().unlock();
        } else if (direct) {
            drwlock.writeLock().lock();
            directList.add(handler);
            drwlock.writeLock().unlock();
        } else if (secure){
            sirwlock.writeLock().lock();
            secIndirectList.add(handler);
            sirwlock.writeLock().unlock();
        } else {
            irwlock.writeLock().lock();
            indirectList.add(handler);
            irwlock.writeLock().unlock();
        }
    }

    public void remove(SubscribeHandler handler,boolean direct, boolean secure){
        if (direct && secure){
            sdrwlock.writeLock().lock();
            secDirectList.remove(handler);
            sdrwlock.writeLock().unlock();
        } else if (direct){
            drwlock.writeLock().lock();
            directList.remove(handler);
            drwlock.writeLock().unlock();
        } else if (secure) {
            sirwlock.writeLock().lock();
            secIndirectList.add(handler);
            sirwlock.writeLock().unlock();
        } else {
            irwlock.writeLock().lock();
            indirectList.remove(handler);
            irwlock.writeLock().unlock();
        }
    }

    public void notifyAll(Resource resource){
        for (SubscribeHandler handler:directList){
            handler.notify(resource,true);
        }

        for (SubscribeHandler handler:secDirectList){
            handler.notify(resource, true);
        }

        for (SubscribeHandler handler:indirectList){
            handler.notify(resource,false);
        }

        for (SubscribeHandler handler:secIndirectList){
            handler.notify(resource, false);
        }
    }


    /**
     * This is just used in indirect notification, and only direct requests are notified
     * @param resource the new resource
     * @param secure whether this resource comes via a secure link
     */
    public void notify(Resource resource, boolean secure){

        if (secure) {
            for (SubscribeHandler handler:secDirectList){
                handler.notify(resource, true);
            }
        } else {
            for (SubscribeHandler handler:directList){
                handler.notify(resource,true);
            }
        }
    }

}
