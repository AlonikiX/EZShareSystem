package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.PublishCommand;
import CommonLibs.Commands.ShareCommand;
import CommonLibs.DataStructure.Resource;
import EZShare_Server.ServerSetting;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by marsjc on 2017/04/17.
 */
public class ShareHandler extends Handler{
    public ShareHandler(Command cmd){
        super(cmd);
    }

    /**
     * Check the secret, then handle it
     */
    public void handle() {


        JSONObject obj = new JSONObject();

        Resource resource = ((ShareCommand)command).getResource();

        // if the resource is not given, return error
        if (resource == null||((ShareCommand)command).getSecret() == null){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),"missing resource and/or secret");
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        // if the resource is invalid, return error
        String path = resource.getUri().substring(5);
        File shareFile = new File(path);
        if ((resource.getOwner().compareTo("*"))==0
//                || resource.getUri() == null
                || (resource.getUri().compareTo(""))==0
                || !(isFile(resource.getUri()))
                || !shareFile .exists()
                ){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),OptionField.invalidResource.getValue());
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }


        if (0 == ((ShareCommand)command).getSecret().compareTo(ServerSetting.sharedSetting().getSecret())){

            // if the secret is correct, attempt to share the resource
            boolean handleResult = resourceListManager.addResource(resource);

            // generate message
            if (handleResult){
                obj.put(OptionField.response.getValue(), OptionField.success.getValue());
            } else {
                obj.put(OptionField.response.getValue(), OptionField.error.getValue());
                obj.put(OptionField.errorMessage.getValue(), "cannot share resource");
            }

        } else {
            // if the secret is incorrect, directly generate error message
            obj.put(OptionField.response.getValue(), OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),"incorrect secret");

        }

        String msg = obj.toString();
        communicator.writeData(msg);

    }
}
