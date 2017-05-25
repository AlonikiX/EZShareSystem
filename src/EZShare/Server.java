package EZShare;
import CommonLibs.CommandLine.CliManager;
import CommonLibs.Communication.Communicator;
import CommonLibs.DataStructure.ServerListManager;
import EZShare_Server.*;
import org.apache.commons.cli.ParseException;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by apple on 17/04/2017.
 */
public class Server {

    private static final String ROLE  = "SERVER";
    private Dispatcher dispatcher;

    public static void main(String[] args) {
        //initial command line options
        CliManager cliManager = new CliManager(ROLE);
        try {
            cliManager.initOptions(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //initial server setting
        ServerSetting setting = ServerSetting.sharedSetting();
        setting.initSetting(cliManager);

        // print server setting information
        String log = setting.getTime() + " - [EZShare.Server.main] " +
                "- [INFO] Starting the EZShare Server";
        System.out.println(log);
        log = setting.getTime() + " - [EZShare.ServerSetting] " +
                "- [INFO] - using secret: " + setting.getSecret();
        System.out.println(log);
        log = setting.getTime() + " - [EZShare.ServerSetting] " +
                "- [INFO] - using advertised hostname: " + setting.getAdvertisedHostName();
        System.out.println(log);
        log = setting.getTime() + " - [EZShare.ServerSetting] " +
                "- [INFO] - bound to por " + setting.getPort();
        System.out.println(log);
        log = setting.getTime() + " - [EZShare.ServerSetting] " +
                "- [INFO] - setting debug " + ((setting.isDebugModel())?"on":"off");
        System.out.println(log);

        NormalSocketListener normalSocketListener = new NormalSocketListener();
        normalSocketListener.start();

        SecureSocketListener secureSocketListener = new SecureSocketListener(args[1],args[2]);
        secureSocketListener.start();

    }
}
