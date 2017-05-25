package EZShare_Server;

import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.ServerListManager;
import CommonLibs.Setting.SecurityMode;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by apple on 22/05/2017.
 */
public class NormalSocketListener extends Thread {
    @Override
    public void run() {
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
                dispatcher.setSecurityMode(SecurityMode.inSecure);
                dispatcher.bindCommunicator(communicator);
                dispatcher.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
