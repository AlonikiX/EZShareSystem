package EZShare_Server.Handler;

import CommonLibs.Commands.Command;
import CommonLibs.Setting.SecurityMode;

/**
 * Created by Anson Chen on 2017/5/27.
 */
public class SecureSubscribeHandler extends SubscribeHandler{
    public SecureSubscribeHandler(Command cmd){
        super(cmd);
        this.securityMode = SecurityMode.secure;
    }
}
