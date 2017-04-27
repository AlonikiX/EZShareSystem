package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.DataStructure.IPAddress;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by apple on 17/04/2017.
 */
public class ExchangeCommand extends Command {
    private ArrayList<IPAddress> serverList;

    public ArrayList<IPAddress> getServerList() {
        return serverList;
    }

    public void setServerList(ArrayList<IPAddress> serverList) {
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
                    this.serverList.add(new IPAddress(server));
                }
            }
        }
    }

    private void toServerList(JSONObject obj) {
        if (obj.has(OptionField.serverList.getValue())) {
            JSONArray arr = obj.getJSONArray(OptionField.serverList.getValue());
            for (int i = 0; i < arr.length(); i++) {
                JSONObject element = arr.getJSONObject(i);
                String hostName = element.getString(OptionField.hostname.getValue());
                int port = element.getInt(OptionField.port.getValue());
                serverList.add(new IPAddress(hostName, port));
            }
//            String serverString = obj.getString(OptionField.serverList.getValue());
//            if (null != serverString) {
//                String[] servers = serverString.split(",");
//                for (String server : servers) {
//                    this.serverList.add(new IPAddress(server));
//                }
//
//            }
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

    public ExchangeCommand(ArrayList<IPAddress> serverList) {
        this.commandType = CommandType.EXCHANGE;
        this.serverList = serverList;
    }

    @Override
    public String toJSON() {

        JSONObject obj = new JSONObject();
        obj.put(OptionField.command.getValue(), this.commandType.getValue());

        JSONArray arr = new JSONArray();
        for (IPAddress server : this.serverList) {
            JSONObject serverObject = new JSONObject();
            serverObject.put("hostname", server.hostname);
            serverObject.put(OptionField.port.getValue(), server.port);
            arr.put(serverObject);
        }
        obj.put(OptionField.serverList.getValue(), arr);

        StringWriter out = new StringWriter();
        obj.write(out);

        return out.toString();
    }
}
