package CommonLibs.Commands;

import CommonLibs.CliManager;
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
        this.commonType = CommandType.SHARE;
        if (cli.hasOption("secret")) {
            String secret = cli.getOptionValue("secret");
            if (null != secret) {
                this.secret = secret;
            }
        }
        this.toResource(cli);
    }

    @Override
    public String toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("command", this.commonType.getValue());
        obj.put("secret", this.secret);
        obj.put("resource", this.toResourceJSONObject());

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }
}

