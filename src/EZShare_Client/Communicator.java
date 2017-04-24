package EZShare_Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by apple on 16/04/2017.
 */

public class Communicator {

    private ClientSetting clientSetting;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public Communicator() {
        this.clientSetting = ClientSetting.sharedClientSetting();
    }

    public int connectToServer(){
        try {
            //new socket
            this.socket = new Socket(clientSetting.getHost(), clientSetting.getPort());
            //create data input and output stream
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());

            return  1;
        } catch (IOException e) {
            e.printStackTrace();

            return 0;
        }
//        try(Socket socket = new Socket(localHost, localPort)){
//            // Output and Input Stream
//            this.socket = socket;
//            DataInputStream input = new DataInputStream(socket.
//                    getInputStream());
//            DataOutputStream output = new DataOutputStream(socket.
//                    getOutputStream());
//
//            output.writeUTF("I want to connect!");
//            output.flush();
//
//            while(true){
//                if(input.available() > 0) {
//                    String message = input.readUTF();
//                    System.out.println(message);
//                }
//
//            }
//
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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