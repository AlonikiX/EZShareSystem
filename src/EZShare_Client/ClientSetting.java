package EZShare_Client;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.Setting.SecurityMode;
import CommonLibs.Setting.Setting;

/**
 * Created by apple on 20/04/2017.
 */
public class ClientSetting extends Setting {
    private static ClientSetting setting;
    protected SecurityMode securityMode;

    private ClientSetting() {
        super();
        this.securityMode = SecurityMode.inSecure;
    }

    public static ClientSetting sharedSetting(){
        if (null == setting) {
            setting = new ClientSetting();
        }
        return setting;
    }

    @Override
    public void initSetting(CliManager cli) {
        super.initSetting(cli);
        if (cli.hasOption(OptionField.secure.getValue())) {
            this.securityMode = SecurityMode.secure;
        }
    }

    public SecurityMode getSecurityMode() {
        return securityMode;
    }

    public void setSecurityMode(SecurityMode securityMode) {
        this.securityMode = securityMode;
    }
}
