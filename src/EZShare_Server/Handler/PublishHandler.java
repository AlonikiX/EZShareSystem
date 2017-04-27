package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.PublishCommand;
import CommonLibs.DataStructure.Resource;
import org.json.JSONObject;

/**
 * Created by marsjc on 2017/04/17.
 */
public class PublishHandler extends Handler{

    public PublishHandler(Command cmd){
        super(cmd);
    }

    public void handle() {

        JSONObject obj = new JSONObject();

        Resource resource = ((PublishCommand)command).getResource();

        // if the resource is not given, return error
        if (resource == null){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.missingResource.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        // if the resource is invalid, return error
        if ((resource.getOwner().compareTo("*"))==0
//                || resource.getUri() == null
                || (resource.getUri().compareTo(""))==0
                || isFile(resource.getUri())
                ){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.invalidResource.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        // attempt to publish the resource, the result reflects whether the publish is successful
        boolean handleResult = resourceListManager.addResource(resource);

        if (handleResult){
            obj.put(OptionField.response.getValue(), OptionField.success.getValue());
        } else {
            obj.put(OptionField.response.getValue(), OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(), "cannot publish resource");
        }
        String msg = obj.toString();
        communicator.writeData(msg);

    }

}
