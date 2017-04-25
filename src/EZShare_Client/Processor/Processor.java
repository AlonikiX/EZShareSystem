package EZShare_Client.Processor;

import CommonLibs.Commands.Command;
import CommonLibs.Communication.Communicator;
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

    public static Processor processorFactory(Command command) {
        switch (command.getCommandType()) {
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
        }
        return null;
    }

    public void process() {
        communicator.connectToServer();
        communicator.writeData(command.toJSON());
    }
}
