package EZShare_Client;

import CommonLibs.CliManager;

/**
 * Created by apple on 20/04/2017.
 */
public class ClientSetting {
    private static ClientSetting clientSetting;
    private String host;
    private int port;
    private boolean isDebugModel;

    private ClientSetting() {
        this.host = "sunrise.cis.unimelb.edu.au";
        this.port = 3780;
        this.isDebugModel = false;
    }

    public static ClientSetting sharedClientSetting(){
        if (null == clientSetting) {
            clientSetting = new ClientSetting();
        }
        return clientSetting;
    }

    public void initClientSetting(CliManager cli) {
        if (cli.hasOption("host")) {
            String hostString = cli.getOptionValue("host");
            if (null != hostString) {
                this.host = hostString;
            }
        }
        if (cli.hasOption("port")) {
            String portString = cli.getOptionValue("port");
            if (null != portString) {
                this.port = Integer.parseInt(portString);
            }
        }
        if (cli.hasOption("debug")) {
            this.isDebugModel = true;
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isDebugModel() {
        return isDebugModel;
    }
}
