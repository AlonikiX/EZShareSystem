package CommonLibs.Exception;

import CommonLibs.CommandLine.OptionField;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Anson Chen on 2017/4/28.
 */
public class UndefinedCommandException extends IOException {

    public UndefinedCommandException(){
        super();
    }

    public String errorMessage(){
        JSONObject obj = new JSONObject();
        obj.put(OptionField.response.getValue(),OptionField.error.getValue());
        obj.put(OptionField.errorMessage.getValue(),"missing or incorrect type for command");
        return obj.toString();
    }

}
