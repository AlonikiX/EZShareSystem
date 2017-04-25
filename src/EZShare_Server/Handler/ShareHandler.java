package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.ShareCommand;
import EZShare_Server.ServerSetting;
import org.json.JSONObject;

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

        //TODO if need to handle invalid resource,
        // e.g. name != "*",
        // resource.rui != doc,
        // resource.rui != null
        // remove white space
        // resource not given

        if (((ShareCommand)command).getSecret() == ServerSetting.sharedSetting().getSecret()){

            // if the secret is correct, attempt to share the resource
            boolean handleResult = resourceListManager.addResource(((ShareCommand)command).getResource());

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

        // TODO connect to client and send message
        // or otherwise, change this method to String, and return the msg

    }
}
