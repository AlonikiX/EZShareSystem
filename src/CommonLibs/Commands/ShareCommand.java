package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Created by apple on 17/04/2017.
 */
public class ShareCommand extends ResourceCommand {
    private String secret;

    public String getSecret() {
        return secret;
    }

    public ShareCommand(CliManager cli) {
        this.commandType = CommandType.SHARE;
        if (cli.hasOption(OptionField.secret.getValue())) {
            String secret = cli.getOptionValue(OptionField.secret.getValue());
            if (null != secret) {
                this.secret = secret;
            }
        }
        this.toResource(cli);
    }

    public ShareCommand(JSONObject obj) {
        this.commandType = CommandType.SHARE;
        if (obj.has(OptionField.secret.getValue())) {
            String secret = obj.getString(OptionField.secret.getValue());
            if (null != secret) {
                this.secret = secret;
            }
        }
        this.toResource(obj);
    }

    @Override
    public String toJSON() {
        JSONObject obj = new JSONObject();
        obj.put(OptionField.command.getValue(), this.commandType.getValue());
        obj.put(OptionField.secret.getValue(), this.secret);
        if (resource != null) obj.put(OptionField.resource.getValue(), this.toResourceJSONObject());

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }

}

