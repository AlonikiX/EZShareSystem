package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.ExchangeCommand;
import CommonLibs.DataStructure.ServerListManager;
import org.json.JSONObject;

/**
 * Created by marsjc on 2017/04/17.
 */
public class ExchangeHandler extends Handler{

    ServerListManager serverlist;

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

        ExchangeCommand cmd = (ExchangeCommand)command;
        cmd.getServerList();
        serverlist.updateServerList(cmd.getServerList());

        obj.put(OptionField.response.getValue(), OptionField.success.getValue());
        String msg = obj.toString();

        // TODO send response

    }

}
