package EZShare_Client;

import CommonLibs.Setting.Setting;

/**
 * Created by apple on 20/04/2017.
 */
public class ClientSetting extends Setting {
    private static ClientSetting setting;

    private ClientSetting() {
        super();
    }

    public static ClientSetting sharedSetting(){
        if (null == setting) {
            setting = new ClientSetting();
        }
        return setting;
    }

}
