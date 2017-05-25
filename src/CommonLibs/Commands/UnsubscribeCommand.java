package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Created by Anson Chen on 2017/5/25.
 */
public class UnsubscribeCommand extends Command{

    private String id;

    public UnsubscribeCommand(){
        this.commandType = CommandType.UNSUBSCRIBE;
    }

    public UnsubscribeCommand(CliManager cli) {
        this.commandType = CommandType.SUBSCRIBE;
        this.id = cli.hasOption(OptionField.id.getValue()) ?
                (cli.getOptionValue(OptionField.id.getValue())): null;
    }

    public UnsubscribeCommand(JSONObject obj) {
        this.commandType = CommandType.SUBSCRIBE;
        this.id = obj.has(OptionField.id.getValue()) ?
                (obj.getString(OptionField.id.getValue())): null;
    }

    public String toJSON() {

        JSONObject obj = new JSONObject();
        obj.put(OptionField.command.getValue(), this.commandType.getValue());
        if (id != null) obj.put(OptionField.id.getValue(),this.id);

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

}
