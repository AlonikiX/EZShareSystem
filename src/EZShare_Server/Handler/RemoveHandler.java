package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.RemoveCommand;
import CommonLibs.DataStructure.Resource;
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

        Resource resource = ((RemoveCommand)command).getResource();

        // if the resource is not given, return error
        if (resource == null){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.missingResource.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            printLog(msg);
            return;
        }

        // if the resource is invalid, return error
        if ((resource.getOwner().compareTo("*"))==0
//                || resource.getUri() == null
                || (resource.getUri().compareTo(""))==0
                ) {
            obj.put(OptionField.response.getValue(), OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(), OptionField.invalidResource.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            printLog(msg);
            return;
        }

        // handle and get response
        boolean handleResult = resourceListManager.removeResource(resource);

        // generate message
        if (handleResult){
            obj.put(OptionField.response.getValue(), OptionField.success.getValue());
        } else {
            obj.put(OptionField.response.getValue(), OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(), "cannot remove resource");
        }

        String msg = obj.toString();
        communicator.writeData(msg);
        printLog(msg);

    }
}
