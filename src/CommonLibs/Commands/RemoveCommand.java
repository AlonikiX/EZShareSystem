package CommonLibs.Commands;

import CommonLibs.CliManager;
import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Created by apple on 17/04/2017.
 */
public class RemoveCommand extends ResourceCommand {
    public RemoveCommand(CliManager cli) {
        this.commonType = CommandType.REMOVE;
        this.toResource(cli);
    }

    @Override
    public String toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("command", this.commonType.getValue());
        obj.put("resource", this.toResourceJSONObject());

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }
}
