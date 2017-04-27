package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.ExchangeCommand;
import CommonLibs.DataStructure.IPAddress;
import CommonLibs.DataStructure.Resource;
import CommonLibs.DataStructure.ServerListManager;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marsjc on 2017/04/17.
 */
public class ExchangeHandler extends Handler{

    ServerListManager serverlist;

    private static final String serverMissingOrInvalidError = "missing or invalid server list";

    public ExchangeHandler(Command cmd){
        super(cmd);
        serverlist = ServerListManager.sharedServerListManager();
    }

    /**
     * attempt to add the servers listed in the commands to the server list
     */
    public void handle(){

        JSONObject obj = new JSONObject();

        //TODO if need to handle invalid resource,
        // remove white space
        // server list is not given
        // server is not valid in structure
        // server ip/port is not valid
        ArrayList<IPAddress> serverList = ((ExchangeCommand)command).getServerList();

        // missing server list
        if (null == serverList
                || serverList.isEmpty()){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),serverMissingOrInvalidError);
            String msg = obj.toString();
            communicator.writeData(msg);
            return;
        }

        //handle successfully
        ExchangeCommand cmd = (ExchangeCommand)command;
        cmd.getServerList();
        serverlist.updateServerList(cmd.getServerList());

        obj.put(OptionField.response.getValue(), OptionField.success.getValue());
        String msg = obj.toString();
        communicator.writeData(msg);

    }

}
