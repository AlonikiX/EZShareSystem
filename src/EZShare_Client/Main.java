package EZShare_Client;
import CommonLibs.CommandLine.CliManager;
import CommonLibs.Commands.Command;

public class Main {
    private static String name = "";
    private static String email = "";

    public static void main(String[] args) {
        //initial command line options
        CliManager cliManager = new CliManager();
        cliManager.initOptions(args);

        //parse command
        Command command = Command.commandFactory(cliManager);

        //initial client setting
        ClientSetting.sharedClientSetting().initClientSetting(cliManager);
        EZShareClient client = new EZShareClient();
        //connect to the server and start communication
        client.connectToServer();
//        client.writeData("{\"command\": \"QUERY\",\"relay\": false,\"resourceTemplate\": {\"name\": \"Mar\",\"tags\": [],\"description\": \"\",\"uri\": \"\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}");
        client.writeData(command.toJSON());
        client.readData();
    }
}
