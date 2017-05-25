package EZShare_Server;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.*;
import CommonLibs.Communication.Communicator;
import CommonLibs.Setting.SecurityMode;
import EZShare_Server.Handler.Handler;
import org.json.JSONObject;

/**
 * Created by apple on 25/04/2017.
 */
public class Dispatcher extends Thread {
    private Communicator communicator;
    private SecurityMode securityMode;

    public Dispatcher(){
        this.securityMode = SecurityMode.inSecure;
    }

    public void bindCommunicator(Communicator communicator){
        this.communicator = communicator;
    }

    public void setSecurityMode(SecurityMode securityMode) {
        this.securityMode = securityMode;
    }

    @Override
    public void run() {
        while (true) {
            if (0 < communicator.readableData()) {
                //receive data
                String data = communicator.readData();
                if (ServerSetting.sharedSetting().isDebugModel()){
                    String prefix = ServerSetting.sharedSetting().getTime() +
                            "[EZShare.Server.receiveMessage] - [FINE] - RECEIVED:";
                    String suffix = "\nFrom Client: " +
                            communicator.getClientAddress() + ":" + communicator.getClientPort();
                    System.out.println(prefix + data + suffix);
                } else {
                    System.out.println("RECEIVED:" + data);
                }
                //dispatch
                Command command = Command.commandFactory(data);
                //use different handler factory base on the secure mode
                Handler handler;
                if (SecurityMode.inSecure == this.securityMode) {
                    handler = Handler.handlerFactory(command);
                }else {
                    handler = Handler.secureHandlerFactory(command);
                }

                handler.bindCommunicator(this.communicator);
                //handle
                handler.handle();

                if (ServerSetting.sharedSetting().isDebugModel()){
                    System.out.println(ServerSetting.sharedSetting().getTime() +
                            "[EZShare.Server.FinishHandleRequest] From Client: "+
                            communicator.getClientAddress() + ":" + communicator.getClientPort());
                }
                break;
            }
        }
    }
}
