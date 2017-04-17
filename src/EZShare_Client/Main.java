package EZShare_Client;


public class Main {
    private static String name = "";
    private static String email = "";

    public static void main(String[] args) {
        //initial command line options
        CliManager cliManager = new CliManager();
        cliManager.initOptions(args);

        //connect to the server and start communication
        EZShareClient client = new EZShareClient();
        client.connectToServer();
        client.writeData("{\"command\": \"QUERY\",\"relay\": false,\"resourceTemplate\": {\"name\": \"Mar\",\"tags\": [],\"description\": \"\",\"uri\": \"\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}");
        client.readData();
    }
}
