package EZShare_Server;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.Commands.Command;
import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.ServerListManager;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by apple on 17/04/2017.
 */
public class Server {
    private Dispatcher dispatcher;

    public static void main(String[] args) {
        //initial command line options
        CliManager cliManager = new CliManager();
        cliManager.initOptions(args);

        //initial server setting
        ServerSetting.sharedSetting().initSetting(cliManager);

        //initial server list manager
        ServerListManager.sharedServerListManager();

        //run server
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try(ServerSocket server = factory.createServerSocket(ServerSetting.sharedSetting().getPort())){
            System.out.println("Waiting for client connection..");

            // Wait for connections.
            while(true){
                //set communicator
                Communicator communicator = new Communicator(ServerSetting.sharedSetting());
                communicator.establishConnection(server.accept());

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
