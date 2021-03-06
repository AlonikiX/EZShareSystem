package EZShare_Client.Processor;

import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import org.json.JSONObject;

/**
 * Created by apple on 24/04/2017.
 */
public class FetchProcessor extends Processor {
    public FetchProcessor(Command command) {
        super(command);
        printConnectionLog("fetching from ");
    }

    @Override
    public void process() {
        super.process();
        while (true) {
            if (0 < communicator.readableData()) {
                String data = communicator.readData();
                JSONObject object = new JSONObject(data);
                printReceiveLog(data);
                if (object.has(OptionField.resourceSize.getValue())) {
                    long fileSize = object.getLong(OptionField.resourceSize.getValue());
                    String uri = object.getString(OptionField.uri.getValue());
                    String fileName = object.getString(OptionField.name.getValue());
                    fileName += uri.substring(uri.lastIndexOf("."));

                    communicator.downloadFile(fileSize, fileName);
                }
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
