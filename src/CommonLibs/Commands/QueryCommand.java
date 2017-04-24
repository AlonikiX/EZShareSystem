package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Created by apple on 17/04/2017.
 */
public class QueryCommand extends ResourceCommand {
    private String relay;

    public QueryCommand(CliManager cli) {
        this.commandType = CommandType.QUERY;
        this.relay = OptionField.no.getValue();
        this.toResource(cli);
    }

    public QueryCommand(JSONObject obj) {
        this.commandType = CommandType.QUERY;
        this.relay = OptionField.no.getValue();
        this.toResource(obj);
    }

    @Override
    public String toJSON() {

        JSONObject obj = new JSONObject();
        obj.put(OptionField.command.getValue(), this.commandType.getValue());
        obj.put(OptionField.relay.getValue(), this.relay);
        obj.put(OptionField.resourceTemplate.getValue(), this.toResourceJSONObject());

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }
}
