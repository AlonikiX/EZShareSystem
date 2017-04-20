package CommonLibs.Commands;


import CommonLibs.CliManager;
import EZShare_Client.ClientSetting;

/**
 * Created by apple on 17/04/2017.
 */
public abstract class Command {
    protected ClientSetting clientSetting = ClientSetting.sharedClientSetting();
    protected CommandType commonType;


    /**
     * @description return a specific command based on the cli value
     * @param cli, an instance of CliManager
     * @return
     */
    public static Command commandFactory(CliManager cli) {
        if (cli.hasOption("publish")) {
            return new PublishCommand(cli);
        }
        if (cli.hasOption("query")) {
            return new QueryCommand(cli);
        }
        if (cli.hasOption("remove")) {
            return new RemoveCommand(cli);
        }
        if (cli.hasOption("share")) {
            return new ShareCommand(cli);
        }
        if (cli.hasOption("fetch")) {
            return new FetchCommand(cli);
        }
        if (cli.hasOption("exchange")) {
            return new ExchangeCommand(cli);
        }
        return null;
    }

    public abstract String toJSON();
}
