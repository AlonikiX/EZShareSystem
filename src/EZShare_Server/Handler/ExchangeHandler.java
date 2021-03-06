package EZShare_Server.Handler;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.ExchangeCommand;
import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.IPAddress;
import CommonLibs.DataStructure.Resource;
import CommonLibs.DataStructure.ServerListManager;
import EZShare_Server.ServerSetting;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by marsjc on 2017/04/17.
 */
public class ExchangeHandler extends Handler{

    ServerListManager serverlist;

    private static final String serverMissingOrInvalidError = "missing or invalid server list";
    private static final String serverInvalidError = "one or more servers is/are invalid/inaccessible";

    public ExchangeHandler(Command cmd){
        super(cmd);
        serverlist = ServerListManager.sharedServerListManager();
    }

    /**
     * attempt to add the servers listed in the commands to the server list
     */
    public void handle(){

        JSONObject obj = new JSONObject();

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

        // validate the servers in the given list
        boolean allValid = true;
        for (IPAddress ipAddress:serverList){
            if (!validServer(ipAddress)){
                allValid = false;
                break;
            }
        }
        if (!allValid){
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),serverInvalidError);
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

    /**
     * validate an ip address
     * @param ip the ip address to validate
     * @return true, if the ip address is valid
     *          false, otherwise
     */
    boolean validServer(IPAddress ip) {
        try {
            //new socket
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip.hostname,ip.port), 12000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
