package CommonLibs.Setting;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apple on 25/04/2017.
 */
public abstract class Setting {
    protected String host;
    protected int port;
    protected int securePort;
    protected int timeout;
    protected boolean isDebugModel;
    protected SimpleDateFormat dateFormat;

    protected Setting() {
        this.host = "sunrise.cis.unimelb.edu.au";
        this.port = 3781;
        this.securePort = 40001;
        this.timeout = 12000;
        this.isDebugModel = false;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
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

    public void setHost(String host) {
        this.host = host;
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

    public String getTime(){
        String currentTime = dateFormat.format(new Date());
        return currentTime;
    }

    public int getSecurePort(){
        return this.securePort;
    }

    public void setSecurePort(int sport){
        this.securePort = sport;
    }
}
