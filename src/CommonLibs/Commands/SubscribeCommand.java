package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Created by Anson Chen on 2017/5/25.
 */
public class SubscribeCommand extends ResourceCommand {

    private boolean relay;
    private String id;

    private SubscribeCommand(){

        this.commandType = CommandType.SUBSCRIBE;

    }

    public SubscribeCommand(CliManager cli) {
        this.commandType = CommandType.SUBSCRIBE;
        this.relay = cli.hasOption(OptionField.relay.getValue()) ?
                (Boolean.parseBoolean(cli.getOptionValue(OptionField.relay.getValue()))) : true;
        this.toResource(cli);
    }

    public SubscribeCommand(JSONObject obj) {
        this.commandType = CommandType.SUBSCRIBE;
        this.id = obj.has(OptionField.id.getValue()) ?
                (obj.getString(OptionField.id.getValue())): null;
        this.relay = obj.has(OptionField.relay.getValue()) ?
                (obj.getBoolean(OptionField.relay.getValue())):true;
        this.toResource(obj);
    }

    @Override
    public String toJSON() {

        JSONObject obj = new JSONObject();
        obj.put(OptionField.command.getValue(), this.commandType.getValue());
        obj.put(OptionField.relay.getValue(), this.relay);
        if (id != null) obj.put(OptionField.id.getValue(),this.id);
        if (resource != null) obj.put(OptionField.resourceTemplate.getValue(), this.toResourceJSONObject());

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }

    /**
     * To clone this command for relied query only
     * @return a cloned query, with, however, owner and channel set to "", and with relay off
     */
    public SubscribeCommand relayClone(){
        SubscribeCommand clone = new SubscribeCommand();
        clone.commandType = CommandType.SUBSCRIBE;
        clone.relay = false;
        clone.id = this.id;
        clone.resource = this.resource.clone();
        clone.resource.setOwner("");
        clone.resource.setChannel("");
        return clone;
    }

    public boolean relay(){
        return relay;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }
}