package CommonLibs;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
/**
 * Created by apple on 16/04/2017.
 */
public class CliManager {
    private static final String ROLE = "CLIENT";

    private CommandLine cmd;

    public void initOptions(String[] args) {
        Options options = new Options();
        if (ROLE == "CLIENT") {
            //settings
            options.addOption("debug",false, "debug model");
            options.addOption("host",true, "connect to host");
            options.addOption("port",true, "connect to port");
            //commands
            options.addOption("query",false, "query command");
            options.addOption("publish",false, "publish command");
            options.addOption("remove",false, "remove command");
            options.addOption("share",false, "share command");
            options.addOption("fetch",false, "fetch shared file");
            options.addOption("exchange",false, "exchange server list");
            //command fields
            options.addOption("name",true, "name of the resource");
            options.addOption("description",true, "describe the client");
            options.addOption("uri",true, "uri of the resource");
            options.addOption("tags",true, "uri of the resource");
            options.addOption("owner",true, "owner ot the resource");
            options.addOption("channel",true,"channel");
            options.addOption("secret",true, "secret of the server");
            options.addOption("servers",true, "server list");
        }else if (ROLE == "SERVER") {
            options.addOption("debug",true, "print debug information");
            options.addOption("port",true,"server port, an integer");
            options.addOption("secret",true, "secret of the server");
            options.addOption("advertisedhostname",true, "advertised hostname");
            options.addOption("connectionintervallimit",true, "connection interval limit in seconds");
            options.addOption("exchangeinterval",true, "exchange interval in seconds");
        }

        //parse options to command line
        CommandLineParser parser = new DefaultParser();
        this.cmd = null;
        try{
            this.cmd = parser.parse(options,args);
        } catch (ParseException e){
            e.printStackTrace();
        }
//        name = cmd.getOptionValue("name");
//
//        if(cmd.hasOption("email")){
//            email = cmd.getOptionValue("email");
//        } else {
//            email = "The user does not provide email address";
//        }
//
//        System.out.println("student name: "+ name+" email:"+email);
    }

    public boolean hasOption(String option) {
        return this.cmd.hasOption(option);
    }

    public String getOptionValue(String option) {
        if (this.cmd.hasOption(option)) {
            return this.cmd.getOptionValue(option);
        }
        return null;
    }
}
