package CommonLibs.DataStructure;

import CommonLibs.Setting.SecurityMode;

import java.util.ArrayList;

/**
 * Created by apple on 27/05/2017.
 */
public interface ISubscriber {
    public void pull(SecurityMode securityMode, IPAddress address);
}
