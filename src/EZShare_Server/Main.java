package EZShare_Server;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.Commands.Command;
import CommonLibs.Communication.Communicator;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by apple on 17/04/2017.
 */
public class Main {
    private Dispatcher dispatcher;

    public static void main(String[] args) {
        //initial command line options
        CliManager cliManager = new CliManager();
        cliManager.initOptions(args);

        //initial server setting
        ServerSetting.sharedSetting().initSetting(cliManager);

        //run server

        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try(ServerSocket server = factory.createServerSocket(ServerSetting.sharedSetting().getPort())){
            System.out.println("Waiting for client connection..");

            // Wait for connections.
            while(true){
                Communicator communicator = new Communicator(ServerSetting.sharedSetting());
                communicator.establishConnection(server.accept());

                Dispatcher dispatcher = new Dispatcher();
                dispatcher.bindCommunicator(communicator);
                dispatcher.start();

//                counter++;
//                System.out.println("Client "+counter+": Applying for connection!");

                // Start a new thread for a connection
//                recallDispatcher.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
