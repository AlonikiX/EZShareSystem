package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.PublishCommand;
import CommonLibs.Commands.QueryCommand;
import CommonLibs.DataStructure.IPAddress;
import CommonLibs.DataStructure.Resource;
import CommonLibs.DataStructure.ServerListManager;
import EZShare_Server.Server;
import EZShare_Server.ServerSetting;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.management.Query;
import java.util.ArrayList;

/**
 * Created by marsjc on 2017/04/17.
 */
public class QueryHandler extends Handler{

    public QueryHandler(Command cmd){
        super(cmd);
    }

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
        if (template.getOwner().equals("*")){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.invalidTemplate.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        obj.put(OptionField.response.getValue(),OptionField.success.getValue());
        String msg = obj.toString();
        communicator.writeData(msg);

        // record results
        int resultSize = 0;

        ArrayList<Resource> results = resourceListManager.matchTemplate(template);
        resultSize += results.size();
        String thisServer = ServerSetting.sharedSetting().getHost() + ":"
                + ServerSetting.sharedSetting().getPort();

        for (Resource resource:results){
            obj = new JSONObject();
            obj.put(OptionField.name.getValue(), resource.getName());
            obj.put(OptionField.description.getValue(), resource.getDescription());
            obj.put(OptionField.channel.getValue(), resource.getChannel());
            obj.put(OptionField.owner.getValue(),(resource.getOwner().equals("")) ? "" : "*");
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


        // if relay on, we need to do further work
        // send query with relay off
        // wait for response
        // increase resultSize
        // send resource results

        if (((QueryCommand)command).relay()){

            QueryCommand relayCommand = ((QueryCommand)command).relayClone();
            String jsonMessage = relayCommand.toJSON();
            ArrayList<IPAddress> addressList = ServerListManager.sharedServerListManager().cloneServerList();

            for (IPAddress address:addressList){


            }





            //TODO I'll do it later, ready to test for non-relay results




        }

        obj = new JSONObject();
        obj.put(OptionField.resultSize.getValue(),resultSize);
        communicator.writeData(msg);

    }
}
