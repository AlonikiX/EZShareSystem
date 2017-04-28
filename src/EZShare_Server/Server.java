package EZShare_Server;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.ServerListManager;
import org.apache.commons.cli.ParseException;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * Created by apple on 17/04/2017.
 */
public class Server {
    private Dispatcher dispatcher;

    public static void main(String[] args) {
        //initial command line options
        CliManager cliManager = new CliManager();
        try {
            cliManager.initOptions(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //initial server setting
        ServerSetting.sharedSetting().initSetting(cliManager);

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
                if (/*limitedIPAddressListManager.limitConnection(host)*/false) {
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
