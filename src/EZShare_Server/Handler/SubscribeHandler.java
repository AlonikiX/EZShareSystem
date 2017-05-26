package EZShare_Server.Handler;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.SubscribeCommand;
import CommonLibs.DataStructure.Resource;
import EZShare_Server.ServerSetting;
import org.json.JSONArray;
import org.json.JSONObject;

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
    }

    public void handle() {

        // TODO validate query - [JiaCheng]
        // check syntax
        // check id
        // check template
        // check others??
        // TODO Debug Mode



        // initialize
        // register the first template
        templates.put(((SubscribeCommand)command).getId(),((SubscribeCommand)command).getResource());
        size = 1;

        // self register in server
        if (((SubscribeCommand)command).relay()){
            ServerSetting.sharedSetting().addRelay(this);
        } else {
            ServerSetting.sharedSetting().addNonRelay(this);
        }

        // TODO subscribe successful message - [JiaCheng]


        // TODO Debug Mode


        // TODO if relay() create new threads with relay off
        // need to check secure or not


        // listen to further commands
        while (!isEmpty()){

            String data = communicator.readData();
            Command cmd = Command.commandFactory(data);
            if (cmd.getCommandType().getValue().compareTo("SUBSCRIBE") == 0){

                // TODO Validations here? - [JiaCheng]
                // Not necessary if all queries are generated by system

                // TODO debug mode?


                // here it is assumed that Client will never subscribe more than one resources
                rwlock.writeLock().lock();
                templates.put(((SubscribeCommand)cmd).getId(),((SubscribeCommand)cmd).getResource());
                rwlock.writeLock().unlock();


                // TODO subscribe successful message - [JiaCheng]

                // TODO debug mode?

            } else if (cmd.getCommandType().getValue().compareTo("UNSUBSCRIBE") == 0){

                // TODO Validations here? [JiaCheng]
                // Still not quite necessary
                // TODO debug mode?

                rwlock.writeLock().lock();
                templates.remove(((SubscribeCommand)cmd).getId());
                rwlock.writeLock().unlock();


                // TODO subscribe successful message - [JiaCheng]
                // TODO debug mode?

            } // ignore any other cases

        }

        // TODO terminates - [JiaCheng]
        // print unsubscription message
        // print hits counts

        // TODO debug mode?

        // self unregister
        if (((SubscribeCommand)command).relay()){
            ServerSetting.sharedSetting().addRelay(this);
        } else {
            ServerSetting.sharedSetting().addNonRelay(this);
        }
        // TODO debug mode?

    }

    private void add(String id, Resource template){
        rwlock.writeLock().lock();
        templates.put(id,template);
        size ++;
        rwlock.writeLock().unlock();
    }

    private void remove(String id){
        rwlock.writeLock().lock();
        templates.remove(id);
        size --;
        rwlock.writeLock().unlock();
    }

    /**
     * When a new resource is known(publish, share or s-s subscribe, how about ?s-s query?),
     * check whether the resource should be sent, if so, send it.
     * @param direct whether this is a direct match:
     *               in case of an new resource is published or shared, then direct match is needed,
     *               (the owner and channel both match)
     *               in case of an new resource is known by relayed subscription, indirect match is
     *               needed, because the channel and owner are changed to in relayed subscribe command
     * @param resource the new resource
     */
    public void notify(Resource resource, boolean direct){
        if (isSubscribed(resource,direct)) snedResource(resource);
    }

    /**
     * Send a resource to client
     * @param resource the new resource
     */
    private void snedResource(Resource resource){
        rwlock.writeLock().lock();
        hits ++;
        JSONObject obj = new JSONObject();
        obj.put(OptionField.name.getValue(), resource.getName());
        obj.put(OptionField.description.getValue(), resource.getDescription());
        obj.put(OptionField.channel.getValue(), resource.getChannel());
        obj.put(OptionField.owner.getValue(),(0 == resource.getOwner().compareTo("")) ? "" : "*");
        obj.put(OptionField.uri.getValue(), resource.getUri());
        JSONArray tags = new JSONArray();
        for (String tag:resource.getTags()){
            tags.put(tag);
        }
        obj.put(OptionField.tags.getValue(),tags);
        obj.put(OptionField.ezserver.getValue(),(null == resource.getEzserver()) ?
                ServerSetting.sharedSetting().getAdvertisedHostName():resource.getEzserver());

        String msg = obj.toString();
        communicator.writeData(msg);
        printLog(msg);

        // TODO Debug Mode
        rwlock.writeLock().unlock();
    }

    private boolean isSubscribed(Resource resource, boolean direct){
        rwlock.readLock().lock();
        boolean b = false;
        for (Resource template: templates.values()) {
            if (matches(template, resource, direct)) {
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
     * @param direct whether this is a direct match (whether to check owner and channel)
     * @return true, if they match
     *          false, otherwise
     */
    private boolean matches(Resource template, Resource candidate, boolean direct){
        if ((!template.getChannel().isEmpty())
                && (0!= template.getChannel().compareTo(candidate.getChannel())) && direct) return false;
        if ((!template.getOwner().isEmpty())
                && (0!= template.getOwner().compareTo(candidate.getOwner())) && direct) return false;
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
