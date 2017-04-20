package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by apple on 17/04/2017.
 */
public class ExchangeCommand extends Command {
    private HashSet<String> serverList;

    public HashSet<String> getServerList() {
        return serverList;
    }

    public void setServerList(HashSet<String> serverList) {
        this.serverList = serverList;
    }


    /**
     * @description get the server list from cli
     * @param cli, an instance of CliManager
     */
    private void toServerList(CliManager cli) {
        if (cli.hasOption(OptionField.servers.getValue())) {
            String serverString = cli.getOptionValue(OptionField.servers.getValue());
            if (null != serverString) {
                this.serverList = new HashSet<>(Arrays.asList(serverString.split(",")));
            }
        }
    }

    public ExchangeCommand(CliManager cli) {
        this.commandType = CommandType.EXCHANGE;
        this.toServerList(cli);
    }

    @Override
    public String toJSON() {
        return null;
    }
}
