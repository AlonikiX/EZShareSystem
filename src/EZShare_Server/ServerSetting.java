package EZShare_Server;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.Setting.Setting;

/**
 * Created by Anson Chen on 2017/4/24.
 */
public class ServerSetting extends Setting {
    private static ServerSetting setting;
    private String secret;
    private String advertisedHostName;
    private int connectionIntervalLimit;
    private int exchangeInterval;

    private ServerSetting() {
        this.secret = "";
        this.advertisedHostName = "EZServer";
        this.connectionIntervalLimit = 5000;
        this.exchangeInterval = 10000;
    }

    public static ServerSetting sharedSetting(){
        if (null == setting) {
            setting = new ServerSetting();
        }
        return setting;
    }

    @Override
    public void initSetting(CliManager cli) {
        super.initSetting(cli);

        if (cli.hasOption(OptionField.secret.getValue())) {
            String secret = cli.getOptionValue(OptionField.secret.getValue());
            if (null != secret) {
                this.secret = secret;
            }
        }
        if (cli.hasOption(OptionField.advertisedhostname.getValue())) {
            String advertisedHostName = cli.getOptionValue(OptionField.advertisedhostname.getValue());
            if (null != advertisedHostName) {
                this.advertisedHostName = advertisedHostName;
            }
        }
        if (cli.hasOption(OptionField.connectionintervallimit.getValue())) {
            String connectionIntervalLimit = cli.getOptionValue(OptionField.connectionintervallimit.getValue());
            if (null != connectionIntervalLimit) {
                this.connectionIntervalLimit = Integer.parseInt(connectionIntervalLimit);
            }
        }
        if (cli.hasOption(OptionField.exchangeinterval.getValue())) {
            String exchangeInterval = cli.getOptionValue(OptionField.exchangeinterval.getValue());
            if (null != exchangeInterval) {
                this.exchangeInterval = Integer.parseInt(exchangeInterval);
            }
        }
    }

    public String getSecret(){
        return secret;
    }

    public String getAdvertisedHostName() {
        return advertisedHostName;
    }

    public int getConnectionIntervalLimit() {
        return connectionIntervalLimit;
    }

    public int getExchangeInterval() {
        return exchangeInterval;
    }
}
