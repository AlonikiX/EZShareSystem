package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Created by apple on 17/04/2017.
 */
public class RemoveCommand extends ResourceCommand {
    public RemoveCommand(CliManager cli) {
        this.commandType = CommandType.REMOVE;
        this.toResource(cli);
    }

    public RemoveCommand(JSONObject obj) {
        this.commandType = CommandType.REMOVE;
        this.toResource(obj);
    }

    @Override
    public String toJSON() {
        JSONObject obj = new JSONObject();
        obj.put(OptionField.command.getValue(), this.commandType.getValue());
        if (resource != null) obj.put(OptionField.resource.getValue(), this.toResourceJSONObject());

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }
}
