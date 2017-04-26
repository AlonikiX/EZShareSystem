package CommonLibs.Setting;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;

/**
 * Created by apple on 25/04/2017.
 */
public abstract class Setting {
    protected String host;
    protected int port;
    protected int timeout;
    protected boolean isDebugModel;

    protected Setting() {
        this.host = "sunrise.cis.unimelb.edu.au";
        this.port = 3781;
        this.timeout = 10000;
        this.isDebugModel = false;
    }

    public void initSetting(CliManager cli) {
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
        if (cli.hasOption(OptionField.timeout.getValue())) {
            String timeout = cli.getOptionValue(OptionField.timeout.getValue());
            if (null != timeout) {
                this.timeout = Integer.parseInt(timeout);
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

    public int getTimeout() {
        return timeout;
    }
}
