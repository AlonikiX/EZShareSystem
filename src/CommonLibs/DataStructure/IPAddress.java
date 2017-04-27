package CommonLibs.DataStructure;

/**
 * Created by apple on 25/04/2017.
 */
public class IPAddress {
    public String hostname;
    public int port;

    public IPAddress(String data) {
        String[] serverString = data.split(":");
        this.hostname = serverString[0];
        this.port = Integer.parseInt(serverString[1]);
    }

    public IPAddress(String hostName, int port) {
        this.hostname = hostName;
        this.port = port;
    }

    @Override
    public String toString() {
        return hostname + ":" + port;
    }

}
