package CommonLibs.Commands;

import CommonLibs.CliManager;
import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Created by apple on 17/04/2017.
 */
public class QueryCommand extends ResourceCommand {
    public QueryCommand(CliManager cli) {
        this.commonType = CommandType.QUERY;
        this.toResource(cli);
    }

    @Override
    public String toJSON() {

        JSONObject obj = new JSONObject();
        obj.put("command", this.commonType.getValue());
        obj.put("resourceTemplate", this.toResourceJSONObject());

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }
}
