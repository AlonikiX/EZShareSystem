package EZShare_Client.Processor;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import org.json.JSONObject;

/**
 * Created by apple on 24/04/2017.
 */
public class QueryProcessor extends Processor {
    public QueryProcessor(Command command) {
        super(command);
        printConnectionLog("querying ");
    }

    @Override
    public void process() {
        super.process();
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
