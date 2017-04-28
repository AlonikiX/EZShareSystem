package EZShare_Server.Handler;

import CommonLibs.Commands.Command;
import CommonLibs.Commands.CommandType;
import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.ResourceListManager;
import EZShare_Server.Server;
import EZShare_Server.ServerSetting;

/**
 * Created by marsjc on 2017/04/17.
 */
public abstract class Handler {
    // which command is being handled
    protected Command command;
    protected Communicator communicator;
    protected ResourceListManager resourceListManager;

    public Handler(Command cmd){
        this.command = cmd;
        this.resourceListManager = ResourceListManager.shareResourceListManager();
    }

    public void bindCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public static boolean isFile(String s){
        if (s.length()<5) return false;
        if (s.charAt(0) == 'f'
                && s.charAt(1) == 'i'
                && s.charAt(2) == 'l'
                && s.charAt(3) == 'e'
                && s.charAt(4) == ':') return true;
        return false;
    }

    public abstract void handle();

//    public abstract boolean checkValid();

    public static Handler handlerFactory(Command cmd){
        switch (cmd.getCommandType().getValue()){
            case "PUBLISH":
                return new PublishHandler(cmd);
            case "REMOVE":
                return new RemoveHandler(cmd);
            case "SHARE":
                return new ShareHandler(cmd);
            case "QUERY":
                return new QueryHandler(cmd);
            case "FETCH":
                return new FetchHandler(cmd);
            case "EXCHANGE":
                return new ExchangeHandler(cmd);
        }
        return new UndefinedHandler(cmd);
    }

//    protected boolean isDebug(){
//        return ServerSetting.sharedSetting().isDebugModel();
//    }

    protected void printLog(String msg){
        String prefix = ServerSetting.sharedSetting().getTime() +
                " - [EZShare.Server.sendMessage] - [FINE] - SENT:";
        String suffix = "\nTarget Client: " +
                communicator.getClientAddress() + ":" + communicator.getClientPort();
        if (ServerSetting.sharedSetting().isDebugModel()){
            System.out.println(prefix + msg + suffix);
        } else {
            System.out.println("SENT:" + msg);
        }
    }

}
