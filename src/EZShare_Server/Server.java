package EZShare_Server;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.ServerListManager;
import org.apache.commons.cli.ParseException;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by apple on 17/04/2017.
 */
public class Server {

    private static final String ROLE  = "SERVER";
    private Dispatcher dispatcher;


    public static void main(String[] args) {
        //initial command line options
        CliManager cliManager = new CliManager(ROLE);
        try {
            cliManager.initOptions(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //initial server setting
        ServerSetting setting = ServerSetting.sharedSetting();
        setting.initSetting(cliManager);

        // print server setting information
        String log = setting.getTime() + " - [EZShare.Server.main] " +
                "- [INFO] Starting the EZShare Server";
        System.out.println(log);
        log = setting.getTime() + " - [EZShare.ServerSetting] " +
                "- [INFO] - using secret: " + setting.getSecret();
        System.out.println(log);
        log = setting.getTime() + " - [EZShare.ServerSetting] " +
                "- [INFO] - using advertised hostname: " + setting.getAdvertisedHostName();
        System.out.println(log);
        log = setting.getTime() + " - [EZShare.ServerSetting] " +
                "- [INFO] - bound to por " + setting.getPort();
        System.out.println(log);



        //initial server list manager
        ServerListManager serverListManager = ServerListManager.sharedServerListManager();

        //initial limited client list manager
        LimitedIPAddressListManager limitedIPAddressListManager = LimitedIPAddressListManager.shareClientListManager();

        //run server
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try(ServerSocket serverSocket = factory.createServerSocket(ServerSetting.sharedSetting().getPort())){
            System.out.println("Waiting for client connection..");
            // Wait for connections.
            while(true){
                //set communicator
                Communicator communicator = new Communicator(ServerSetting.sharedSetting());
                communicator.establishConnection(serverSocket.accept());

                String host = communicator.getClientAddress();
                System.out.println("Server Information:");
                System.out.println("connected with IP address:" + host);
                if (limitedIPAddressListManager.limitConnection(host)) {
                    System.out.println("Server Information:");
                    System.out.println("connection too frequent");
                    limitedIPAddressListManager.addIntervalLimitedIPAddress(host);
                    continue;
                }
                limitedIPAddressListManager.addIntervalLimitedIPAddress(host);

                //new dispatching thread
                Dispatcher dispatcher = new Dispatcher();
                dispatcher.bindCommunicator(communicator);
                dispatcher.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
