package CommonLibs.Communication;

import CommonLibs.Setting.Setting;
import EZShare_Server.Dispatcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

/**
 * Created by apple on 16/04/2017.
 */

public class Communicator {

    private Setting setting;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private Dispatcher recallDispatcher;

    public Communicator(Setting setting) {
        this.setting = setting;
    }

    public void establishConnection(Socket socket) {
        this.socket = socket;
        try {
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void acceptConnection() {
//        ServerSocketFactory factory = ServerSocketFactory.getDefault();
//        try(ServerSocket server = factory.createServerSocket(setting.getPort())){
//            System.out.println("Waiting for client connection..");
//
//            // Wait for connections.
//            while(true){
//                Socket client = server.accept();
////                counter++;
////                System.out.println("Client "+counter+": Applying for connection!");
//
//                // Start a new thread for a connection
//                recallDispatcher.start();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void bindRecallDispatcher(Dispatcher dispatcher) {
//        this.recallDispatcher = dispatcher;
//        this.recallDispatcher.bindCommunicator(this);
//    }

    public int connectToServer() {

        // TODO how about just write:
//        return connectToServer(setting.getHost(),setting.getPort());

        try {
            //new socket
            this.socket = new Socket(setting.getHost(), setting.getPort());
            //create data input and output stream
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());

            return 1;
        } catch (IOException e) {
            e.printStackTrace();

            return 0;
        }
    }

    public int connectToServer(String host, int port){
        try {
            //new socket
            this.socket = new Socket(host, port);
            //create data input and output stream
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());

            return 1;
        } catch (IOException e) {
            e.printStackTrace();

            return 0;
        }
    }

    public void writeData(String data) {
        try {
            this.output.writeUTF(data);
            this.output.flush();
            System.out.println("Send:");
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readData() {
        try {
            String data = this.input.readUTF();
            System.out.println("Receive:");
            System.out.println(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int readableData() {
        try {
            return this.input.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}

//{"command": "QUERY","relay": true,"resourceTemplate": {"name": "","tags": [],"description": "","uri": "","channel": "","owner": "","ezserver": null}}