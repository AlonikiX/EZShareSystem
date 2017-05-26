package CommonLibs.Communication;

import CommonLibs.DataStructure.IPAddress;
import CommonLibs.Setting.SecurityMode;
import CommonLibs.Setting.Setting;
import EZShare_Server.Dispatcher;
import EZShare_Server.ServerSetting;
import org.json.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by apple on 16/04/2017.
 */

public class Communicator {

    private Setting setting;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private SecurityMode securityMode;

    public Communicator(Setting setting) {
        this.setting = setting;
        this.securityMode = SecurityMode.inSecure;
    }

    public void setSecureMode(SecurityMode secureMode){
        this.securityMode = secureMode;
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

    public boolean isLocalHost(IPAddress ipAddress) {
        if (ServerSetting.sharedSetting().getPort() == ipAddress.port) {
            if (ServerSetting.sharedSetting().getHosts().contains(ipAddress.hostname)) {
                return true;
            }
        }
        return false;
    }

    public String getClientAddress() {
        return this.socket.getInetAddress().getHostAddress();
    }

    public int getClientPort() {
        return this.socket.getPort();
    }

    public boolean connectToServer() {
        if (SecurityMode.inSecure == this.securityMode){
            //new socket
            this.socket = new Socket();
            try {
                this.socket.connect(new InetSocketAddress(
                        setting.getHost(),setting.getPort()), setting.getTimeout());
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Connect timed out\n\n\n\n\n\n\n\n");
                return false;
            }
        }else {
            //Create SSL socket and connect it to the remote server
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try {
                this.socket = sslsocketfactory.createSocket(setting.getHost(),setting.getPort());
//                this.socket.connect(new InetSocketAddress(
//                        setting.getHost(), setting.getSecurePort()), setting.getTimeout());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connect timed out");

                return false;
            }
        }

        //create data input and output stream
        try {
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

            return true;

    }

    public boolean connectToServer(String host, int port){
        if (SecurityMode.inSecure == this.securityMode){
            this.socket = new Socket();
            try {
                //new socket
                this.socket.connect(new InetSocketAddress(host, port), setting.getTimeout());
            } catch (Exception e) {
//            e.printStackTrace();
                System.out.println("Connect timed out");

                return false;
            }
        }else {
            //Create SSL socket and connect it to the remote server
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try {
                this.socket = sslsocketfactory.createSocket(host,port);
//                this.socket.connect(new InetSocketAddress(host, port), setting.getTimeout());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connect timed out");

                return false;
            }
        }

        //create data input and output stream
        try {
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public void distory(){
        try {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
            this.socket = new Socket();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("socket close error!");
        }
    }

    public void writeData(String data) {
        try {
            this.output.writeUTF(data);
            this.output.flush();
//            System.out.println("Send:");
//            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readData() {
        try {
            String data = this.input.readUTF();
//            System.out.println("Receive:");
//            System.out.println(data);
            return data;
        } catch (EOFException e){
            e.printStackTrace();
            System.out.print("EOF ERROR!!!!!!\n\n\n");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("IO ERROR!!!!!\n\n\n");
            return null;
        }
    }

    public int readableData() {
        return 1;

//        try {
//            return this.input.available();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return -1;
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

//        System.out.println("Downloading "+fileName+" of size "+fileSizeRemaining);
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
//            System.out.println("File received!");
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

    public Socket getSocket(){
        return this.socket;
    }

}

//{"command": "QUERY","relay": true,"resourceTemplate": {"name": "","tags": [],"description": "","uri": "","channel": "","owner": "","ezserver": null}}