package EZShare_Server;

import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.ServerListManager;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;

import CommonLibs.Setting.SecurityMode;

/**
 * Created by apple on 22/05/2017.
 */
public class SecureSocketListener extends Thread {
    private String trust;
    private String key;

    public SecureSocketListener(){

    }
    public SecureSocketListener(String trust, String key){
        this.trust = trust;
        this.key = key;
    }
    @Override
    public void run() {
        //Location of the Java keystore file containing the collection of
        //certificates trusted by this application (trust store).
        System.setProperty("javax.net.ssl.trustStore", "Certificates/"+this.trust);
        System.setProperty("javax.net.ssl.trustStorePassword", "penis123");

        //Specify the keystore details (this can be specified as VM arguments as well)
        //the keystore file contains an application's own certificate and private key
        System.setProperty("javax.net.ssl.keyStore", "Certificates/"+this.key);
        //Password to access the private key from the keystore file
        System.setProperty("javax.net.ssl.keyStorePassword","penis123");

        // Enable debugging to view the handshake and communication which happens between the SSLClient and the SSLServer
//        System.setProperty("javax.net.debug","all");

        //initial server list manager
        ServerListManager serverListManager = ServerListManager.sharedServerListManager();

        //initial limited client list manager
        LimitedIPAddressListManager limitedIPAddressListManager = LimitedIPAddressListManager.shareClientListManager();

        //run server
        //Create SSL server socket
        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
                .getDefault();
        try(SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(
                (ServerSetting.sharedSetting().getSecurePort()))){

            //need to authenticate client's certificate
            sslserversocket.setNeedClientAuth(true);

            // Wait for connections.
            System.out.println("Waiting for client connection..");
            while(true){
                //set communicator
                Communicator communicator = new Communicator(ServerSetting.sharedSetting());
                communicator.establishConnection(sslserversocket.accept());

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
                dispatcher.setSecurityMode(SecurityMode.secure);
                dispatcher.bindCommunicator(communicator);
                dispatcher.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
