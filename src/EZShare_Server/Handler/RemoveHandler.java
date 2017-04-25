package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.RemoveCommand;
import org.json.JSONObject;

/**
 * Created by marsjc on 2017/04/17.
 */
public class RemoveHandler extends Handler{
    public RemoveHandler(Command cmd){
        super(cmd);
    }

    /**
     * Attempt to remove the command directly
     */
    public void handle(){

        JSONObject obj = new JSONObject();

        //TODO if need to handle invalid resource,
        // remove white space
        // resource not given

        // handle and get response
        boolean handleResult = resourceListManager.removeResource(((RemoveCommand)command).getResource());

        // generate message
        if (handleResult){
            obj.put(OptionField.response.getValue(), OptionField.success.getValue());
        } else {
            obj.put(OptionField.response.getValue(), OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(), "cannot remove resource");
        }

        String msg = obj.toString();

        // TODO connect to client and send message
        // or otherwise, change this method to String, and return the msg

    }
}
