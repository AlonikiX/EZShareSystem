package CommonLibs.DataStructure;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.UnsubscribeCommand;
import CommonLibs.Communication.Communicator;
import CommonLibs.Setting.SecurityMode;
import EZShare_Server.ServerSetting;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.Name;
import java.security.Security;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/27.
 */
public class Connection implements Runnable {

    private IPAddress address;
    private Communicator communicator;
    private SecurityMode securityMode;
    private ReentrantReadWriteLock rwlock;
    private int subSize;

    public Connection(IPAddress address, boolean secure){
        if (secure){
            this.securityMode = SecurityMode.secure;
        } else {
            this.securityMode = SecurityMode.inSecure;
        }
        this.address = address;
        this.communicator = new Communicator(ServerSetting.sharedSetting());
        communicator.setSecureMode(this.securityMode);
        communicator.connectToServer(address.hostname,address.port);
        rwlock = new ReentrantReadWriteLock();
    }

    public Connection(String ip, int port, boolean secure){
        this(new IPAddress(ip,port), secure);
    }

    public void run(){

        while(true){

            String data = communicator.readData();
            JSONObject obj = new JSONObject(data);

            if (obj.has(OptionField.id.getValue())){

            } else if (obj.has(OptionField.errorMessage.getValue())){
                break;
            } else if (obj.has(OptionField.resultSize.getValue())){
                break;
            } else {
                Resource resource = new Resource();
                // well format assumed
                try {
                    resource.setName(obj.getString(OptionField.name.getValue()));
                    resource.setDescription(obj.getString(OptionField.description.getValue()));
                    resource.setOwner(obj.getString(OptionField.owner.getValue()));
                    resource.setUri(obj.getString(OptionField.uri.getValue()));
                    resource.setChannel(obj.getString(OptionField.channel.getValue()));
                    resource.setEzserver(obj.getString(OptionField.ezserver.getValue()));
                    JSONArray arr = obj.getJSONArray(OptionField.tags.getValue());
                    for (int i = 0; i < arr.length(); i++) {
                        resource.getTags().add(arr.getString(i).toLowerCase());
                    }
                } catch (NullPointerException e) {
                    continue;
                }
                // notify problems
                HandlerListManager.sharedHanderListManager().notify(resource,
                        this.securityMode == SecurityMode.secure);
            }
        }

        // unregister self
        ConnectionListManager.sharedConnectionListManager().disconnect(address,
                securityMode == SecurityMode.secure);
    }

    public void writeData(String s){
        rwlock.writeLock().lock();
        communicator.writeData(s);
        rwlock.writeLock().unlock();
    }

    public String readData(){
        rwlock.readLock().lock();
        String s = communicator.readData();
        rwlock.readLock().unlock();
        return s;
    }

}
