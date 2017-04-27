package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.FetchCommand;
import CommonLibs.Commands.QueryCommand;
import CommonLibs.DataStructure.Resource;
import EZShare_Server.ServerSetting;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by marsjc on 2017/04/17.
 */
public class FetchHandler extends Handler{


    public FetchHandler(Command cmd){
        super(cmd);
    }

    public void handle(){


        //TODO if need to handle invalid resource,
        // e.g. validate channel
        // validate uri (file uri)
        // resource template exists
        // resource structure is valid
        // remove white space

        JSONObject obj = new JSONObject();
        Resource template = ((FetchCommand)command).getResource();

        // if the template is not given, return error
        if (template == null){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.missingTemplate.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        // if the template is invalid, return error
        if (template.getOwner() == "*"){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.invalidTemplate.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        // TODO set limit when sending the success message

        obj.put(OptionField.response.getValue(), OptionField.success.getValue());
        String msg = obj.toString();
        communicator.writeData(msg);


        // check if the resource is in the resource list
        Resource resource = resourceListManager.findResource(((FetchCommand)command).getResource());
        if (resource == null){
            // case: the resource is not found in the resource list
            obj = new JSONObject();
            obj.put(OptionField.resultSize.getValue(), 0);
            String resultSzie = obj.toString();

            //TODO send the result size message

        } else {
            // case: the resource is found in the resource list
            String path = resource.getUri().substring(5);
            File file = new File(path);
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // in this case, the file is considered as not found too
                obj = new JSONObject();
                obj.put(OptionField.resultSize.getValue(), 0);
                String resultSzie = obj.toString();

                //TODO send the result size message

                return;
            }

            long length = file.length();
            String thisServer = ServerSetting.sharedSetting().getHost() + ":"
                    + ServerSetting.sharedSetting().getPort();

            obj = new JSONObject();
            obj.put(OptionField.name.getValue(), resource.getName());
            obj.put(OptionField.description.getValue(), resource.getDescription());
            obj.put(OptionField.channel.getValue(), resource.getChannel());
            obj.put(OptionField.owner.getValue(),"*");
            obj.put(OptionField.uri.getValue(), resource.getUri());
            JSONArray tags = new JSONArray();
            for (String tag:resource.getTags()){
                tags.put(tag);
            }
            obj.put(OptionField.tags.getValue(),tags);
            obj.put(OptionField.ezserver.getValue(),thisServer);
            obj.put(OptionField.resourceSize.getValue(), length);

            String resourceDetail = obj.toString();

            //TODO send the resource detail message


            //TODO send file by bytes
            // I'll check the communication then think about how to do it

//            // close streams
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            // send final words

            obj = new JSONObject();
            obj.put(OptionField.resultSize.getValue(), 1);
            String resultSzie = obj.toString();

            //TODO send the result size message


        }
    }
}
