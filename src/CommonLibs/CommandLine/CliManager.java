package CommonLibs.CommandLine;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
            options.addOption(OptionField.debug.getValue(),false, "debug model");
            options.addOption(OptionField.host.getValue(),true, "connect to hostname");
            options.addOption(OptionField.port.getValue(),true, "connect to port");
            //commands
            options.addOption(OptionField.query.getValue(),false, "query command");
            options.addOption(OptionField.publish.getValue(),false, "publish command");
            options.addOption(OptionField.remove.getValue(),false, "remove command");
            options.addOption(OptionField.share.getValue(),false, "share command");
            options.addOption(OptionField.fetch.getValue(),false, "fetch shared file");
            options.addOption(OptionField.exchange.getValue(),false, "exchange server list");
            //command fields
            options.addOption(OptionField.name.getValue(),true, "name of the resource");
            options.addOption(OptionField.description.getValue(),true, "describe the client");
            options.addOption(OptionField.uri.getValue(),true, "uri of the resource");
            options.addOption(OptionField.tags.getValue(),true, "tags of the resource");
            options.addOption(OptionField.owner.getValue(),true, "owner ot the resource");
            options.addOption(OptionField.channel.getValue(),true,"channel");
            options.addOption(OptionField.secret.getValue(),true, "secret of the server");
            options.addOption(OptionField.servers.getValue(),true, "server list");
            options.addOption(OptionField.relay.getValue(), true, "relay");
        }else if (ROLE == "SERVER") {
            options.addOption(OptionField.debug.getValue(),true, "print debug information");
            options.addOption(OptionField.port.getValue(),true,"server port, an integer");
            options.addOption(OptionField.secret.getValue(),true, "secret of the server");
            options.addOption(OptionField.advertisedhostname.getValue(),true, "advertised hostname");
            options.addOption(OptionField.connectionintervallimit.getValue(),true, "connection interval limit in seconds");
            options.addOption(OptionField.exchangeinterval.getValue(),true, "exchange interval in seconds");
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
