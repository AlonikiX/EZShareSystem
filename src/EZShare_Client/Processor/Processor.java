package EZShare_Client.Processor;

import CommonLibs.Commands.Command;
import CommonLibs.Communication.Communicator;
import CommonLibs.Exception.UndefinedCommandException;
import EZShare_Client.ClientSetting;

/**
 * Created by apple on 24/04/2017.
 */
public abstract class Processor {
    protected Command command;
    protected Communicator communicator;

    protected Processor(Command command){
        this.command = command;
        this.communicator = new Communicator(ClientSetting.sharedSetting());
    }

    public static Processor processorFactory(Command command) throws UndefinedCommandException{

        if (command == null) throw new UndefinedCommandException();

        switch (command.getCommandType() ) {
            case PUBLISH:
                return new PublishProcessor(command);
            case QUERY:
                return new QueryProcessor(command);
            case REMOVE:
                return new RemoveProcessor(command);
            case SHARE:
                return new ShareProcessor(command);
            case FETCH:
                return new FetchProcessor(command);
            case EXCHANGE:
                return new ExchangeProcessor(command);
        } throw new UndefinedCommandException();
    }

    public void process() {
        if (true == communicator.connectToServer()) {
            communicator.writeData(command.toJSON());
        }else {
                System.out.println("System Information: Connection failed");
        }
    }

//    protected void printLog(String msg){
//        String prefix = "[EZShare.Client.receiveMessage] - [FINE] - RECEIVED:";
//        String suffix = "\nFrom Server: " +
//                communicator.getClientAddress() + ":" + communicator.getClientPort();
//        if (ServerSetting.sharedSetting().isDebugModel()){
//            System.out.println(prefix + msg + suffix);
//        } else {
//            System.out.println("SENT:" + msg);
//        }
//    }
}