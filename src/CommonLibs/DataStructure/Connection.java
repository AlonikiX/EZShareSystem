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
import java.util.ArrayList;
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

    public Connection(IPAddress address, SecurityMode securityMode){
        this.securityMode = securityMode;
        this.address = address;
        this.communicator = new Communicator(ServerSetting.sharedSetting());
        communicator.setSecureMode(securityMode);
        communicator.connectToServer(address.hostname,address.port);
        rwlock = new ReentrantReadWriteLock();
    }

    public Connection(String ip, int port, SecurityMode securityMode){
        this(new IPAddress(ip,port), securityMode);
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
            } else if (obj.has(OptionField.uri.getValue())){

                Resource resource = new Resource();
                // well format assumed

//                try {
                    resource.setName(obj.getString(OptionField.name.getValue()));
                    resource.setDescription(obj.getString(OptionField.description.getValue()));
                    resource.setOwner(obj.getString(OptionField.owner.getValue()));
                    resource.setUri(obj.getString(OptionField.uri.getValue()));
                    resource.setChannel(obj.getString(OptionField.channel.getValue()));
                    resource.setEzserver(obj.getString(OptionField.ezserver.getValue()));
                    JSONArray arr = obj.getJSONArray(OptionField.tags.getValue());
                    ArrayList<String> tags = new ArrayList<String>();
                    for (int i = 0; i < arr.length(); i++) {
                        tags.add(arr.getString(i));
                    }
                    resource.setTags(tags);

//                } catch (NullPointerException e) {
//                    continue;
//                }
                HandlerListManager.sharedHanderListManager().notify(resource,
                        this.securityMode);
            } // ignore other cases
        }

        // unregister self
        ConnectionListManager.sharedConnectionListManager().disconnect(address,
                securityMode);
    }

    public void writeData(String s){
//        rwlock.writeLock().lock();
        communicator.writeData(s);
//        rwlock.writeLock().unlock();
    }

    public String readData(){
//        rwlock.readLock().lock();
        String s = communicator.readData();
//        rwlock.readLock().unlock();
        return s;
    }

}
