package EZShare_Client.Processor;

import CommonLibs.Commands.Command;
import CommonLibs.Commands.CommandType;
import CommonLibs.Communication.Communicator;
import CommonLibs.Exception.UndefinedCommandException;
import EZShare_Client.Client;
import EZShare_Client.ClientSetting;
import EZShare_Server.ServerSetting;

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
        CommandType type = command.getCommandType();

        switch (type) {
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
            String msg = command.toJSON();
            communicator.writeData(msg);
            if (ServerSetting.sharedSetting().isDebugModel()){
                String prefix = ClientSetting.sharedSetting().getTime() +
                        " - [EZShare.Client.sendMessage] - [FINE] - SENT:";
                System.out.println(prefix + msg);
            } else {
                System.out.println("SENT:" + msg);
            }


        }else {
            System.out.println(ClientSetting.sharedSetting().getTime() +
                    " - [EZShare.Client.Connection] - [Failed]");
        }
    }

    protected void printConnectionLog(String verb){
        if (ClientSetting.sharedSetting().isDebugModel()){
            String msg = ClientSetting.sharedSetting().getTime() +
                    "[EZShare.Client."+ command.getCommandType().getValue() +"Command] - [FINE] - " + verb +
                    communicator.getClientAddress() + ":" + communicator.getClientPort();
            System.out.println(msg);
        }
    }

    protected void printReceiveLog(String msg){
        String prefix = ClientSetting.sharedSetting().getTime() +
                " - [EZShare.Client.receiveMessage] - [FINE] - RECEIVED:";
        if (ServerSetting.sharedSetting().isDebugModel()){
            System.out.println(prefix + msg);
        } else {
            System.out.println("RECEIVED:" + msg);
        }
    }
}