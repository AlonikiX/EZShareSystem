package EZShare_Server.Handler;

import CommonLibs.Commands.Command;
import CommonLibs.Setting.SecurityMode;

/**
 * Created by apple on 25/05/2017.
 */
public class SecureExchangeHandler extends ExchangeHandler {
    public SecureExchangeHandler(Command cmd){
        super(cmd);
        this.securityMode = SecurityMode.secure;
    }
}
