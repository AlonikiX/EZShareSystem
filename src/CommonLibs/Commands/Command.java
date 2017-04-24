package CommonLibs.Commands;


import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import EZShare_Client.ClientSetting;
import org.json.JSONObject;

/**
 * Created by apple on 17/04/2017.
 */
public abstract class Command {
    protected ClientSetting clientSetting = ClientSetting.sharedClientSetting();
    protected CommandType commandType;

    public CommandType getCommandType() {
        return this.commandType;
    }



    /**
     * @description return a specific command based on the cli value
     * @param cli, an instance of CliManager
     * @return
     */
    public static Command commandFactory(CliManager cli) {
        if (cli.hasOption(OptionField.publish.getValue())) {
            return new PublishCommand(cli);
        }
        if (cli.hasOption(OptionField.query.getValue())) {
            return new QueryCommand(cli);
        }
        if (cli.hasOption(OptionField.remove.getValue())) {
            return new RemoveCommand(cli);
        }
        if (cli.hasOption(OptionField.share.getValue())) {
            return new ShareCommand(cli);
        }
        if (cli.hasOption(OptionField.fetch.getValue())) {
            return new FetchCommand(cli);
        }
        if (cli.hasOption(OptionField.exchange.getValue())) {
            return new ExchangeCommand(cli);
        }
        return null;
    }

    public static Command commandFactory(String json) {
        JSONObject obj = new JSONObject(json);
        if (obj.has(OptionField.command.getValue())) {
            CommandType commandType = CommandType.getEnum(obj.getString(OptionField.command.getValue()));
            switch (commandType) {
                case PUBLISH:
                    return new PublishCommand(obj);
                case QUERY:
                    return new QueryCommand(obj);
                case REMOVE:
                    return new RemoveCommand(obj);
                case SHARE:
                    return new ShareCommand(obj);
                case FETCH:
                    return new FetchCommand(obj);
                case EXCHANGE:
                    return new ExchangeCommand(obj);
            }
        }
        return null;
    }

    public abstract String toJSON();
}
