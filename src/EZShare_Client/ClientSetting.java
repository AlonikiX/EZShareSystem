package EZShare_Client;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;

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
        if (cli.hasOption(OptionField.host.getValue())) {
            String hostString = cli.getOptionValue(OptionField.host.getValue());
            if (null != hostString) {
                this.host = hostString;
            }
        }
        if (cli.hasOption(OptionField.port.getValue())) {
            String portString = cli.getOptionValue(OptionField.port.getValue());
            if (null != portString) {
                this.port = Integer.parseInt(portString);
            }
        }
        if (cli.hasOption(OptionField.debug.getValue())) {
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
