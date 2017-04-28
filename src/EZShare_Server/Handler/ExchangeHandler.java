package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.ExchangeCommand;
import CommonLibs.DataStructure.IPAddress;
import CommonLibs.DataStructure.Resource;
import CommonLibs.DataStructure.ServerListManager;
import EZShare_Server.ServerSetting;
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
        ArrayList<IPAddress> serverList = ((ExchangeCommand)command).getServerList();

        // missing server list
        if (null == serverList
                || serverList.isEmpty()){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),serverMissingOrInvalidError);
            String msg = obj.toString();
            communicator.writeData(msg);
            printLog(msg);
            return;
        }

        //handle successfully
        ExchangeCommand cmd = (ExchangeCommand)command;
        cmd.getServerList();
        serverlist.updateServerList(cmd.getServerList());

        obj.put(OptionField.response.getValue(), OptionField.success.getValue());
        String msg = obj.toString();
        communicator.writeData(msg);
        printLog(msg);

    }

    @Override
    protected void printLog(String msg){
        String prefix = ServerSetting.sharedSetting().getTime() +
                " - [EZShare.Server.sendMessage] - [FINE] - SENT:";
        String suffix = "\nTarget Server: " +
                communicator.getClientAddress() + ":" + communicator.getClientPort();
        if (ServerSetting.sharedSetting().isDebugModel()){
            System.out.println(prefix + msg + suffix);
        } else {
            System.out.println("SENT:" + msg);
        }
    }


}
