package EZShare_Client;
import CommonLibs.CommandLine.CliManager;
import CommonLibs.Commands.Command;
import EZShare_Client.Processor.Processor;

public class Main {
    public static void main(String[] args) {
        //initial command line options
        CliManager cliManager = new CliManager();
        cliManager.initOptions(args);

        //parse command
        Command command = Command.commandFactory(cliManager);

        //initial client setting
        ClientSetting.sharedClientSetting().initClientSetting(cliManager);
//        Communicator client = new Communicator();
        Processor processor = Processor.processorFactory(command);
        processor.process();
        //connect to the server and start communication
//        client.connectToServer();
//        client.writeData(command.toJSON());
//        client.readData();
    }
}
