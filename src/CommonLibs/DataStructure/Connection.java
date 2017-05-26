package CommonLibs.DataStructure;

import CommonLibs.Communication.Communicator;
import EZShare_Server.ServerSetting;

import javax.naming.Name;

/**
 * Created by Anson Chen on 2017/5/27.
 */
public class Connection implements Runnable {

    private IPAddress address;
    private Communicator communicator;

    public Connection(IPAddress address){
        this.address = address;
        this.communicator = new Communicator(ServerSetting.sharedSetting());
        communicator.connectToServer(address.hostname,address.port);
    }

    public Connection(String ip, int port){
        this(new IPAddress(ip,port));
    }

    public void run(){

    }

}
