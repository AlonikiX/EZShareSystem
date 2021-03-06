package EZShare_Server;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.DataStructure.HandlerList;
import CommonLibs.DataStructure.IPAddress;
import CommonLibs.Setting.Setting;
import EZShare_Server.Handler.Handler;
import EZShare_Server.Handler.SubscribeHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Anson Chen on 2017/4/24.
 */
public class ServerSetting extends Setting {
    private ArrayList<String> hosts;
    private static ServerSetting setting;
    private String secret;
    private String advertisedHostName;
    private int connectionIntervalLimit;
    private int exchangeInterval;

    private HandlerList relay;
    private HandlerList nonRealy;

    private ServerSetting() {
        this.hosts = new ArrayList<>();
        this.secret = generateSecret();
        try {
            this.advertisedHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            this.advertisedHostName = "EZServer";
        }
        this.connectionIntervalLimit = 1000;
        this.exchangeInterval = 10000;
        this.relay = new HandlerList();
        this.nonRealy = new HandlerList();
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

        try {
            ArrayList<InetAddress> addresses = new ArrayList<>(Arrays.asList(InetAddress.
                    getAllByName("localhost")));
            for (InetAddress address : addresses) {
                hosts.add(address.getHostAddress());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

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

    public ArrayList<String> getHosts() {
        return this.hosts;
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

    public void addRelay (SubscribeHandler handler){
        relay.add(handler);
    }

    public void removeRelay (SubscribeHandler handler){
        relay.remove(handler);
    }

    public void addNonRelay (SubscribeHandler handler){
        nonRealy.add(handler);
    }

    public void removeNonRelay (SubscribeHandler handler){
        nonRealy.remove(handler);
    }

    private String generateSecret(){
        char[] base = "1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
        String result = "";
        Random rdm = new Random();
        for (int i=0; i<20; i++){
            result += base[rdm.nextInt(base.length)];
        }
        return result;
    }



}
