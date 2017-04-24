package EZShare_Server;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import EZShare_Client.ClientSetting;

/**
 * Created by Anson Chen on 2017/4/24.
 */
public class ServerSetting {
    private static ServerSetting serverSetting;
    private String host;
    private int port;
    private String secret;
    private boolean isDebugModel;

    private ServerSetting() {
        this.host = "sunrise.cis.unimelb.edu.au";
        this.port = 3781;
        this.isDebugModel = false;
        this.secret = "";
    }

    public static ServerSetting sharedServerSetting(){
        if (null == serverSetting) {
            serverSetting = new ServerSetting();
        }
        return serverSetting;
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
        if (cli.hasOption(OptionField.secret.getValue())) {
            String portString = cli.getOptionValue(OptionField.secret.getValue());
            if (null != portString) {
                this.secret = portString;
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

    public String getSecret(){
        return secret;
    }

    public boolean isDebugModel() {
        return isDebugModel;
    }

}
