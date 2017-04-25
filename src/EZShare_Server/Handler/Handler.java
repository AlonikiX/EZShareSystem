package EZShare_Server.Handler;

import CommonLibs.Commands.Command;
import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.ResourceListManager;

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


}
