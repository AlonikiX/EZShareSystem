package EZShare_Client.Processor;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.SubscribeCommand;
import CommonLibs.Communication.IPFetcher;
import EZShare_Client.ClientSetting;
import EZShare_Server.Dispatcher;
import org.json.JSONObject;

/**
 * Created by Anson Chen on 2017/5/27.
 */
public class SubscribeProcessor extends Processor{

    public SubscribeProcessor(Command command) {
        super(command);
        printConnectionLog("subscribing from ");
    }

    @Override
    public void process() {
        if (true == communicator.connectToServer()) {
            String id = IPFetcher.getPublicIpAddress(communicator.getSocket());
            ((SubscribeCommand)command).setId(id);
            String msg = command.toJSON();
            communicator.writeData(msg);
            if (ClientSetting.sharedSetting().isDebugModel()){
                String prefix = ClientSetting.sharedSetting().getTime() +
                        " - [EZShare.Client.sendMessage] - [FINE] - SENT:";
                System.out.println(prefix + msg);
            } else {
                System.out.println("SENT:" + msg);
            }
        } else {
            System.out.println(ClientSetting.sharedSetting().getTime() +
                    " - [EZShare.Client.Connection] - [Failed]");
        }

        while (true) {
            if (0 < communicator.readableData()) {
                String data = communicator.readData();
                printReceiveLog(data);
                JSONObject object = new JSONObject(data);
                if (object.has(OptionField.resultSize.getValue())) {
                    break;
                }
                if (object.has(OptionField.errorMessage.getValue())) {
                    break;
                }
            }
        }
    }

}
