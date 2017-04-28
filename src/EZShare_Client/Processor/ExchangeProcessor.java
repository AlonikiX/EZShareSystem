package EZShare_Client.Processor;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import org.json.JSONObject;

/**
 * Created by apple on 24/04/2017.
 */
public class ExchangeProcessor extends Processor {
    public ExchangeProcessor(Command command) {
        super(command);
        printConnectionLog("adding exchange list to ");
    }

    @Override
    public void process() {
        super.process();
        while (true) {
            if (0 < communicator.readableData()) {
                String data = communicator.readData();
                JSONObject object = new JSONObject(data);
                printReceiveLog(data);
                if (object.has(OptionField.response.getValue())) {
                    String response = object.getString(OptionField.response.getValue());
                    if (0 == response.compareTo(OptionField.success.getValue())) {
                        break;
                    }
                }
                if (object.has(OptionField.errorMessage.getValue())) {
                    break;
                }
            }
        }
    }
}
