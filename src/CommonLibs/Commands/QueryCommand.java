package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Created by apple on 17/04/2017.
 */
public class QueryCommand extends ResourceCommand {
    private boolean relay;

    public boolean relay(){
        return relay;
    }

    private QueryCommand(){

    }

    public QueryCommand(CliManager cli) {
        this.commandType = CommandType.QUERY;
        this.relay = cli.hasOption(OptionField.relay.getValue()) ?
                (Boolean.parseBoolean(cli.getOptionValue(OptionField.relay.getValue()))) : true;
        this.toResource(cli);
    }

    public QueryCommand(JSONObject obj) {
        this.commandType = CommandType.QUERY;
        this.relay = obj.has(OptionField.relay.getValue()) ?
                (obj.getBoolean(OptionField.relay.getValue())):true;
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

    /**
     * To clone this command for relied query only
     * @return a cloned query, with, however, owner and channel set to "", and with relay off
     */
    public QueryCommand relayClone(){
        QueryCommand clone = new QueryCommand();
        clone.commandType = CommandType.QUERY;
        clone.relay = false;
        clone.resource = this.resource.clone();
        clone.resource.setOwner("");
        clone.resource.setChannel("");
        return clone;
    }

}
