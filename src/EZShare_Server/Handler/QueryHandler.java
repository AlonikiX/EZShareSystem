package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.PublishCommand;
import CommonLibs.Commands.QueryCommand;
import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.IPAddress;
import CommonLibs.DataStructure.Resource;
import CommonLibs.DataStructure.ServerListManager;
import EZShare_Server.Server;
import EZShare_Server.ServerSetting;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.management.Query;
import javax.naming.event.ObjectChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by marsjc on 2017/04/17.
 */
public class QueryHandler extends Handler{

    public QueryHandler(Command cmd){
        super(cmd);
    }
    private ReadWriteLock rwlock = new ReentrantReadWriteLock();

    // record number of returned results
    private int resultSize = 0;

    public void handle(){

        JSONObject obj = new JSONObject();
        Resource template = ((QueryCommand)command).getResource();

        // if the template is not given, return error
        if (template == null){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.missingTemplate.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        // if the template is invalid, return error
        if (0 == template.getOwner().compareTo("*")){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.invalidTemplate.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        obj.put(OptionField.response.getValue(),OptionField.success.getValue());
        String msg = obj.toString();
        communicator.writeData(msg);

        ArrayList<Resource> results = resourceListManager.matchTemplate(template);
        resultSize += results.size();
        String thisServer = ServerSetting.sharedSetting().getHost() + ":"
                + ServerSetting.sharedSetting().getPort();

        for (Resource resource:results){

            obj = new JSONObject();
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
            obj.put(OptionField.ezserver.getValue(),thisServer);

            msg = obj.toString();
            communicator.writeData(msg);
        }

        if (((QueryCommand)command).relay()){

            QueryCommand relayCommand = ((QueryCommand)command).relayClone();
            String jsonMessage = relayCommand.toJSON();
            ArrayList<IPAddress> addressList = ServerListManager.sharedServerListManager().cloneServerList();

            for (IPAddress address:addressList){
                Thread thread = new Thread() {
                    public void run(){
                        Communicator queryCommunicator = new Communicator(ServerSetting.sharedSetting());
                        if (communicator.isLocalHost(address)) {
                            return;
                        }
                        if (queryCommunicator.connectToServer(address.hostname,address.port)){
                            queryCommunicator.writeData(jsonMessage);

                            boolean waitForMore = true;

//                            try {
                                // waiting for response
                                while (waitForMore){
                                    if (0<communicator.readableData()){
                                        String data = communicator.readData();
                                        JSONObject object = new JSONObject(data);

                                        // in these cases, the other server will nt reply with resources
                                        if (!object.has(OptionField.response.getValue())
                                                || (0 != object.getString(OptionField.response.getValue())
                                                .compareTo(OptionField.success.getValue()))
                                                ){
                                            waitForMore = false;
                                        } else {
                                            break;
                                        }

                                    }
                                }


                                System.err.println("Waiting for resource? " + waitForMore);



                                // waiting for resources
                                while (waitForMore){
                                    if (0<communicator.readableData()){
                                        String data = communicator.readData();
                                        JSONObject object = new JSONObject(data);



                                        System.err.println("resultSize? " + object.has(OptionField.resultSize.getValue()));


                                        if (object.has(OptionField.resultSize.getValue())){






                                            // the response ends
                                            waitForMore = false;




                                            System.err.println("Waiting for resource? " + waitForMore);




                                        } else {
                                            // this is a resource object
                                            // we reinforce the rule of encrypt owner
                                            if (object.has(OptionField.owner.getValue())) {
                                                object.put(OptionField.owner.getValue(),
                                                        (object.get(OptionField.owner.getValue()).equals(""))?"":"*");
                                            }
                                            String msg = object.toString();
                                            incresaResultSize();
                                            sendResource(msg);
                                        }
                                    }
                                }
//                            } catch (TimeoutException e){
//                                //TODO terminate this thread
//                            }
                        }

                        System.err.println("Loop end ");
                    }
                };
                try{
                    synchronized (this.getClass()){
                        thread.start();
                        thread.join();
                    }
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }

        obj = new JSONObject();
        obj.put(OptionField.resultSize.getValue(),resultSize);
        String resultSize = obj.toString();
        communicator.writeData(resultSize);
    }

    private void sendResource(String jsonResource){
        rwlock.writeLock().lock();
        communicator.writeData(jsonResource);
        rwlock.writeLock().unlock();
    }

    private void incresaResultSize(){
        rwlock.writeLock().lock();
        resultSize++;
        rwlock.writeLock().unlock();
    }
}