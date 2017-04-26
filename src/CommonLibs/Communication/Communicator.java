package CommonLibs.Communication;

import CommonLibs.DataStructure.IPAddress;
import CommonLibs.Setting.Setting;
import EZShare_Server.Dispatcher;
import org.json.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

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

    public String getClientAddress() {
        return this.socket.getInetAddress().getHostAddress();
    }


    public boolean connectToServer() {

        // TODO how about just write:
//        return connectToServer(setting.getHost(),setting.getPort());

        try {
            //new socket
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(setting.getHost(),setting.getPort()), setting.getTimeout());
            //create data input and output stream
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());

            return true;
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Connect timed out");
            return false;
        }
    }

    public boolean connectToServer(String host, int port){
        try {
            //new socket
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(setting.getHost(),setting.getPort()), setting.getTimeout());
            //create data input and output stream
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());

            return true;
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Connect timed out");

            return false;
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

    public void downloadFile(long fileSize, String fileName) {
        long fileSizeRemaining = fileSize;

        String filePath = "client_files/" + fileName;

        // Create a RandomAccessFile to read and write the output file.
        RandomAccessFile downloadingFile = null;
        try {
            downloadingFile = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int chunkSize = setChunkSize(fileSizeRemaining);
        // Represents the receiving buffer
        byte[] receiveBuffer = new byte[chunkSize];

        // Variable used to read if there are remaining size left to read.
        int num;

        System.out.println("Downloading "+fileName+" of size "+fileSizeRemaining);
        try {
            while((num=input.read(receiveBuffer))>0){
                // Write the received bytes into the RandomAccessFile
                downloadingFile.write(Arrays.copyOf(receiveBuffer, num));

                // Reduce the file size left to read..
                fileSizeRemaining-=num;

                // Set the chunkSize again
                chunkSize = setChunkSize(fileSizeRemaining);
                receiveBuffer = new byte[chunkSize];

                // If you're done then break
                if(fileSizeRemaining==0){
                    break;
                }
            }
            System.out.println("File received!");
            downloadingFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transmitFile(FileInputStream fin) {
        if(null != fin){
            try {
                // Start sending file
                byte[] sendingBuffer = new byte[1024*1024];
                int num;
                // While there are still bytes to send..
                while((num = fin.read(sendingBuffer)) > 0){
                    System.out.print("Sent:");
                    System.out.println(num);
                    output.write(Arrays.copyOf(sendingBuffer, num));
                }
                fin.close();
                System.out.println("File transmitting complete");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            // Throw an error here..
        }
    }


    public static int setChunkSize(long fileSizeRemaining){
        // Determine the chunkSize
        int chunkSize=1024*1024;

        // If the file size remaining is less than the chunk size
        // then set the chunk size to be equal to the file size.
        if(fileSizeRemaining<chunkSize){
            chunkSize=(int) fileSizeRemaining;
        }

        return chunkSize;
    }
}

//{"command": "QUERY","relay": true,"resourceTemplate": {"name": "","tags": [],"description": "","uri": "","channel": "","owner": "","ezserver": null}}