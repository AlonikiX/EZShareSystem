package CommonLibs.Commands;

import CommonLibs.CliManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        if (cli.hasOption("servers")) {
            String serverString = cli.getOptionValue("servers");
            if (null != serverString) {
                this.serverList = new HashSet<String>(Arrays.asList(serverString.split(",")));
            }
        }
    }

    public ExchangeCommand(CliManager cli) {
        this.commonType = CommandType.EXCHANGE;
        this.toServerList(cli);
    }

    @Override
    public String toJSON() {
        return null;
    }
}
