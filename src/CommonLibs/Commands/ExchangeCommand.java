package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by apple on 17/04/2017.
 */
public class ExchangeCommand extends Command {
    private ArrayList<String> serverList;

    public ArrayList<String> getServerList() {
        return serverList;
    }

    public void setServerList(ArrayList<String> serverList) {
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
                this.serverList = new ArrayList<>(Arrays.asList(serverString.split(",")));
            }
        }
    }

    private void toServerList(JSONObject obj) {
        if (obj.has(OptionField.servers.getValue())) {
            String serverString = obj.getString(OptionField.servers.getValue());
            if (null != serverString) {
                this.serverList = new ArrayList<>(Arrays.asList(serverString.split(",")));
            }
        }
    }

    public ExchangeCommand(CliManager cli) {
        this.commandType = CommandType.EXCHANGE;
        this.toServerList(cli);
    }

    public ExchangeCommand(JSONObject obj) {
        this.commandType = CommandType.EXCHANGE;
        this.toServerList(obj);
    }

    @Override
    public String toJSON() {
        return null;
    }
}
