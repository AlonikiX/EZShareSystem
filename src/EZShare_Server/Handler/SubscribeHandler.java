package EZShare_Server.Handler;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.SubscribeCommand;
import CommonLibs.Commands.UnsubscribeCommand;
import CommonLibs.DataStructure.*;
import CommonLibs.Setting.SecurityMode;
import EZShare_Server.ServerSetting;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/25.
 */
public class SubscribeHandler extends Handler {

    int hits = 0;
    int size = 1;
    HashMap<String,Resource> templates = new HashMap<String,Resource>();
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
    boolean relay = false;

    public SubscribeHandler(Command cmd){
        super(cmd);
        this.relay = ((SubscribeCommand)command).relay();
    }

    public void handle() {

        JSONObject obj = new JSONObject();
        Resource template = ((SubscribeCommand)command).getResource();

        // if the template is not given, return error
        if (template == null){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.missingTemplate.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            printLog(msg);
            return;
        }

        // if the template is invalid, return error
        if (0 == template.getOwner().compareTo("*")){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.invalidTemplate.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            printLog(msg);
            return;
        }

        String id = ((SubscribeCommand)command).getId();

        // if id is not given, return error
        if (id == null){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.missingID.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            printLog(msg);
            return;
        }

        // if id is invalid, return error
        if (id.isEmpty()){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.invalidID.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            printLog(msg);
            return;
        }

        // initialize
        // register the first template
        templates.put(id,template);
        size = 1;

        // self register in server
        HandlerListManager.sharedHanderListManager().add(this,
                relay, this.securityMode == SecurityMode.secure);
        obj.put(OptionField.response.getValue(),OptionField.success.getValue());
        obj.put(OptionField.id.getValue(),id);
        String msg = obj.toString();
        communicator.writeData(msg);
        printLog(msg);

        // need to check secure or not
        if (relay){
            ArrayList<IPAddress> addressList = ServerListManager.sharedServerListManager().cloneServerList(securityMode);
            SubscribeCommand cmd = ((SubscribeCommand) command).relayClone();
            String message = cmd.toJSON();
            for (IPAddress address : addressList){
                Connection connection = ConnectionListManager.sharedConnectionListManager().connect(address,
                        securityMode == SecurityMode.secure);
                connection.writeData(message);
            }
        }

        // listen to further commands
        while (!isEmpty()){

            String data = communicator.readData();
            Command cmd = Command.commandFactory(data);
            if (cmd.getCommandType().getValue().compareTo("SUBSCRIBE") == 0){

                // TODO Validations here? - [JiaCheng]
                // Not necessary if all queries are generated by system

                String idsub = ((SubscribeCommand)command).getId();
                Resource templatesub = ((SubscribeCommand)command).getResource();

                // TODO debug mode?


                // here it is assumed that Client will never subscribe more than one resources
                // so subscription from the same thread is all secure or unsecure
                this.add(idsub,templatesub);

                obj = new JSONObject();
                obj.put(OptionField.response.getValue(),OptionField.success.getValue());
                obj.put(OptionField.id.getValue(),idsub);
                msg = obj.toString();
                communicator.writeData(msg);
                printLog(msg);


                // TODO debug mode?

            } else if (cmd.getCommandType().getValue().compareTo("UNSUBSCRIBE") == 0){

                // TODO Validations here? [JiaCheng]
                // Still not quite necessary
                // TODO debug mode?

                String idunsub = ((SubscribeCommand)command).getId();

                this.remove(idunsub);

                obj = new JSONObject();
                obj.put(OptionField.response.getValue(),OptionField.success.getValue());
                obj.put(OptionField.id.getValue(),idunsub);
                msg = obj.toString();
                communicator.writeData(msg);
                printLog(msg);

            } // ignore any other cases

        }

        // print hits counts
        obj = new JSONObject();
        obj.put(OptionField.resultSize.getValue(),hits);
        String resultSize = obj.toString();
        communicator.writeData(resultSize);
        printLog(resultSize);

        // TODO debug mode?

        // self unregister
        HandlerListManager.sharedHanderListManager().remove(this, relay,
                this.securityMode == SecurityMode.secure);
        // TODO debug mode?

    }

    private void add(String id, Resource template){
        rwlock.writeLock().lock();
        templates.put(id,template);
        size ++;
        // check relay
        // not needed in this project, because, using add means gain subscription from server, which must be non-relayed
        rwlock.writeLock().unlock();
    }

    private void remove(String id){
        rwlock.writeLock().lock();
        if (relay){
            ArrayList<IPAddress> addressList = ServerListManager.sharedServerListManager().cloneServerList(securityMode);
            UnsubscribeCommand cmd = ((SubscribeCommand)command).cancleCommand();
            String message = cmd.toJSON();
            for (IPAddress address : addressList){
                Connection connection = ConnectionListManager.sharedConnectionListManager().connect(address,
                        securityMode == SecurityMode.secure);
                connection.writeData(message);
            }
        }
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
        if (isSubscribed(resource,direct)) sendResource(resource);
    }

    /**
     * Send a resource to client
     * @param resource the new resource
     */
    private void sendResource(Resource resource){
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
        rwlock.writeLock().lock();
        communicator.writeData(msg);
        rwlock.writeLock().unlock();
        printLog(msg);
        hits ++;
        // TODO Debug Mode
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

    public void subscribeFrom(IPAddress address){
        // again, we only care the initial command, not all subscription in the list
        // because only client (single subscription) would be relayed
        SubscribeCommand cmd = ((SubscribeCommand) command).relayClone();
        String message = cmd.toJSON();
        Connection connection = ConnectionListManager.sharedConnectionListManager().connect(address,
                securityMode == SecurityMode.secure);
        connection.writeData(message);
    }
}
