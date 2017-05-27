package EZShare_Client.Processor;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Commands.SubscribeCommand;
import CommonLibs.Commands.UnsubscribeCommand;
import CommonLibs.Communication.IPFetcher;
import EZShare_Client.ClientSetting;
import EZShare_Server.Dispatcher;
import org.json.JSONObject;

import java.util.Scanner;

/**
 * Created by Anson Chen on 2017/5/27.
 */
public class SubscribeProcessor extends Processor{

    String id = "";
    boolean listen = true;

    public SubscribeProcessor(Command command) {
        super(command);
        printConnectionLog("subscribing from ");
    }

    @Override
    public void process() {
        if (true == communicator.connectToServer()) {
            id = IPFetcher.getPublicIpAddress(communicator.getSocket());
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

        Thread thread = new Thread(){
            public void run(){
                // listen to "enter"
                Scanner input = new Scanner(System.in);
                String enter = input.nextLine();
                UnsubscribeCommand cmd = ((SubscribeCommand)command).cancleCommand();
                String unsub = cmd.toJSON();
                while (listen){
                    if (enter == ""){
                        communicator.writeData(unsub);
                        break;
                    }
                }
            }
        };
        thread.start();

        while (true) {
            if (0 < communicator.readableData()) {
                String data = communicator.readData();
                printReceiveLog(data);
                JSONObject object = new JSONObject(data);
                if (object.has(OptionField.resultSize.getValue())) {
                    listen = false;
                    break;
                }
                if (object.has(OptionField.errorMessage.getValue())) {
                    listen = false;
                    break;
                }
            }
        }
    }

}
