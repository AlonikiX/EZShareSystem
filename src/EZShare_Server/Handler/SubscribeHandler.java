package EZShare_Server.Handler;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.SubscribeCommand;
import CommonLibs.DataStructure.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/25.
 */
public class SubscribeHandler extends Handler {

    int hits = 0;
    int size = 1;
    LinkedList<Resource> templates = new LinkedList<Resource>();
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    public SubscribeHandler(Command cmd){

        super(cmd);
        rwlock.writeLock().lock();
        templates.add(((SubscribeCommand)cmd).getResource());
        rwlock.writeLock().unlock();

        // self register

    }

    private boolean isEmpty (){
        rwlock.readLock().lock();
        boolean b = size == 0;
        rwlock.readLock().unlock();
        return b;
    }

    public void handle() {

        // validate query



        // if relay() create new threads with relay off


        // wait for unsubscription



    }


    public void newResource(){

        hits ++;
        // send new resource

    }

}
