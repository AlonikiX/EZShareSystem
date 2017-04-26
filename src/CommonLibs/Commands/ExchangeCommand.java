package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.DataStructure.ServerListManager;
import CommonLibs.DataStructure.ServerStructure;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by apple on 17/04/2017.
 */
public class ExchangeCommand extends Command {
    private ArrayList<ServerStructure> serverList;

    public ArrayList<ServerStructure> getServerList() {
        return serverList;
    }

    public void setServerList(ArrayList<ServerStructure> serverList) {
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
                String[] servers = serverString.split(",");
                for (String server : servers) {
                    this.serverList.add(new ServerStructure(server));
                }
            }
        }
    }

    private void toServerList(JSONObject obj) {
        if (obj.has(OptionField.servers.getValue())) {
            String serverString = obj.getString(OptionField.servers.getValue());
            if (null != serverString) {
                String[] servers = serverString.split(",");
                for (String server : servers) {
                    this.serverList.add(new ServerStructure(server));
                }
            }
        }
    }

    public ExchangeCommand(CliManager cli) {
        this.commandType = CommandType.EXCHANGE;
        this.serverList = new ArrayList<>();
        this.toServerList(cli);
    }

    public ExchangeCommand(JSONObject obj) {
        this.commandType = CommandType.EXCHANGE;
        this.serverList = new ArrayList<>();
        this.toServerList(obj);
    }

    public ExchangeCommand(ArrayList<ServerStructure> serverList) {
        this.serverList = serverList;
    }

    @Override
    public String toJSON() {

        JSONObject obj = new JSONObject();
        obj.put(OptionField.command.getValue(), this.commandType.getValue());

        JSONArray arr = new JSONArray();
        for (ServerStructure server : this.serverList) {
            JSONObject serverObject = new JSONObject();
            serverObject.put("hostname", server.host);
            serverObject.put(OptionField.port.getValue(), server.port);
            arr.put(serverObject);
        }
        obj.put(OptionField.serverList.getValue(), arr);

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }
}
