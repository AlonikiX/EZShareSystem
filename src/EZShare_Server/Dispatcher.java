package EZShare_Server;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.*;
import CommonLibs.Communication.Communicator;
import EZShare_Server.Handler.Handler;
import org.json.JSONObject;

/**
 * Created by apple on 25/04/2017.
 */
public class Dispatcher extends Thread {
    private Communicator communicator;

    public void bindCommunicator(Communicator communicator){
        this.communicator = communicator;
    }

    @Override
    public void run() {
        while (true) {
            if (0 < communicator.readableData()) {
                //receive data
                String data = communicator.readData();
                System.out.println("RECEIVE:");
                System.out.println(data);
                //dispatch
                Command command = Command.commandFactory(data);
                Handler handler = Handler.handlerFactory(command);
                handler.bindCommunicator(this.communicator);
                //handle
                handler.handle();

                System.out.println("System Information:");
                System.out.println("Finish handling");
                break;
            }
        }
    }
}
