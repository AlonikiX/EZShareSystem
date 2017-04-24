package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.CommandType;
import CommonLibs.Commands.PublishCommand;
import CommonLibs.Commands.ResourceCommand;
import CommonLibs.DataStructure.*;
import EZShare_Server.EZShareServer;
import EZShare_Server.ResourceList;
import org.json.JSONObject;

/**
 * Created by marsjc on 2017/04/17.
 */
public class PublishHandler extends Handler{

    public PublishHandler(Command cmd){
        super(cmd);
    }

    /**
     * Directly attempt to publish the resource
     */
    public void handle() {

        JSONObject obj = new JSONObject();

        // handle and get response
        boolean handleResult = resourceListManager.addResource(((PublishCommand)command).getResource());

        // generate message
        if (handleResult){
            obj.put(OptionField.response.getValue(), OptionField.success.getValue());
        } else {
            obj.put(OptionField.response.getValue(), OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(), "cannot publish resource");
        }

        String msg = obj.toString();

        // TODO connect to client and send message
        // or otherwise, change this method to String, and return the msg


    }



}
