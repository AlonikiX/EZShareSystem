package CommonLibs.Communication;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class IPFetcher {
    public static String getPublicIpAddress(Socket socket) {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            String ip = in.readLine(); //you get the IP as a String
            ip += socket.getLocalSocketAddress().toString();
            return ip;
        } catch (Exception e) {
            return null;
        }
    }
}