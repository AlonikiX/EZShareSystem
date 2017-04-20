package CommonLibs.Commands;


import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import EZShare_Client.ClientSetting;

/**
 * Created by apple on 17/04/2017.
 */
public abstract class Command {
    protected ClientSetting clientSetting = ClientSetting.sharedClientSetting();
    protected CommandType commandType;


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

    public abstract String toJSON();
}
