package CommonLibs.DataStructure;

import CommonLibs.Communication.Communicator;
import CommonLibs.Setting.SecurityMode;
import EZShare_Server.ServerSetting;
import org.json.JSONObject;

import javax.naming.Name;
import java.security.Security;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Anson Chen on 2017/5/27.
 */
public class Connection implements Runnable {

    private IPAddress address;
    private Communicator communicator;
    private SecurityMode securityMode;
    private ReentrantReadWriteLock rwlock;

    public Connection(IPAddress address, boolean secure){
        if (secure){
            this.securityMode = SecurityMode.secure;
        } else {
            this.securityMode = SecurityMode.inSecure;
        }
        this.address = address;
        this.communicator = new Communicator(ServerSetting.sharedSetting());
        communicator.connectToServer(address.hostname,address.port);
        rwlock = new ReentrantReadWriteLock();
    }

    public Connection(String ip, int port, boolean secure){
        this(new IPAddress(ip,port), secure);
    }

    public void run(){

        while(true){

            String data = communicator.readData();
            JSONObject obj = new JSONObject(data);

            // notify problems





        }

    }

    public void writeData(String s){
        rwlock.writeLock().lock();
        communicator.writeData(s);
        rwlock.writeLock().unlock();
    }

    public String readData(){
        rwlock.readLock().lock();
        String s =  communicator.readData();
        rwlock.readLock().unlock();
        return s;
    }

}
