package CommonLibs.DataStructure;

/**
 * Created by apple on 25/04/2017.
 */
public class IPAddress {
    public String host;
    public int port;

    public IPAddress(String data) {
        String[] serverString = data.split(":");
        this.host = serverString[0];
        this.port = Integer.parseInt(serverString[1]);
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }

}
