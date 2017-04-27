package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import org.json.JSONObject;

/**
 * Created by Anson Chen on 2017/4/24.
 */
public class UndefinedHandler extends Handler {

    public UndefinedHandler(Command cmd){
        super(cmd);
    }

    public void handle() {

        JSONObject obj = new JSONObject();
        obj.put(OptionField.response.getValue(),OptionField.error.getValue());
        obj.put(OptionField.errorMessage.getValue(), "missing or incorrect type for command");
        String msg = obj.toString();
        communicator.writeData(msg);
    }
}
