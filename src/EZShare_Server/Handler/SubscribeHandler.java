package EZShare_Server.Handler;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.SubscribeCommand;
import CommonLibs.DataStructure.Resource;
import EZShare_Server.ServerSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/25.
 */
public class SubscribeHandler extends Handler {

    int hits = 0;
    int size = 1;
    HashMap<String,Resource> templates = new HashMap<String,Resource>();
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    public SubscribeHandler(Command cmd){

        super(cmd);
        rwlock.writeLock().lock();
        templates.put(((SubscribeCommand)command).getId(),((SubscribeCommand)command).getResource());
        rwlock.writeLock().unlock();

        // self register
        if (((SubscribeCommand)command).relay()){
            ServerSetting.sharedSetting().addRelay(this);
        } else {
            ServerSetting.sharedSetting().addNonRelay(this);
        }
    }

    public void handle() {

        // validate query




        // if relay() create new threads with relay off





        while (!isEmpty()){

            // listen to subscription or insubscription
            // ignore anything else

        }


        // wait for unsubscription



    }

    private void add(String id, Resource template){
        rwlock.writeLock().lock();
        templates.put(id,template);
        rwlock.writeLock().unlock();
    }

    private void remove(String id){
        rwlock.writeLock().lock();
        templates.remove(id);
        rwlock.writeLock().unlock();
    }

    public void notify(Resource resource){

        if (isSubscribed(resource)) snedResource(resource);

    }

    private void snedResource(Resource resouce){

        hits ++;
        // send new resource

    }

    private boolean isSubscribed(Resource resource){
        rwlock.readLock().lock();
        boolean b = false;
        for (Resource template: templates.values()) {
            if (matches(template, resource)) {
                b = true;
                break;
            }
        }
        rwlock.readLock().unlock();
        return b;
    }

    private boolean isEmpty (){
        rwlock.readLock().lock();
        boolean b = size == 0;
        rwlock.readLock().unlock();
        return b;
    }


    /**
     * To check if a template matches a candidate resource
     * @param template the template provided
     * @param candidate the candidate resource
     * @return true, if they match
     *          false, otherwise
     */
    private boolean matches(Resource template, Resource candidate){

        if ((!template.getChannel().isEmpty()) && 0 != template.getChannel().compareTo(candidate.getChannel())) return false;
        if ((!template.getOwner().isEmpty()) && (0 != template.getOwner().compareTo(candidate.getOwner()))) return false;
        if ((!template.getUri().isEmpty()) && (0 != template.getUri().compareTo((candidate.getUri())))) return false;
        ArrayList<String> tags = template.getTags();
        boolean tagsInterval = true;
        for (String tag:tags){
            if (!candidate.getTags().contains(tag)) {
                tagsInterval = false;
                break;
            }
        }
        if (!tagsInterval) return false;

//        if (template.getName() == "" && template.getDescription() == "") return true;
        if (candidate.getName().indexOf(template.getName()) > -1) return true;
        if (candidate.getDescription().indexOf(template.getDescription()) > -1) return true;
        return false;
    }


}
