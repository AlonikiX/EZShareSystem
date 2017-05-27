package EZShare;
import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.Commands.Command;
import CommonLibs.Exception.UndefinedCommandException;
import CommonLibs.Setting.SecurityMode;
import EZShare_Client.ClientSetting;
import EZShare_Client.Processor.Processor;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

public class Client {
    private static final String ROLE  = "CLIENT";
    public static void main(String[] args) {
        String trust = "rootAC.jks";
        String key = "clientKS.jks";
        //Location of the Java keystore file containing the collection of
        //certificates trusted by this application (trust store).
        System.setProperty("javax.net.ssl.trustStore", "Certificates/"+trust);
//        System.setProperty("javax.net.ssl.trustStore", trust);
        System.setProperty("javax.net.ssl.trustStorePassword", "penis123");

        //Specify the keystore details (this can be specified as VM arguments as well)
        //the keystore file contains an application's own certificate and private key
        System.setProperty("javax.net.ssl.keyStore","Certificates/"+key);
//        System.setProperty("javax.net.ssl.keyStore",key);
        //Password to access the private key from the keystore file
        System.setProperty("javax.net.ssl.keyStorePassword","penis123");

        try{
            //initial command line options
            CliManager cliManager = new CliManager(ROLE);
            cliManager.initOptions(args);

            //initial client setting
            ClientSetting.sharedSetting().initSetting(cliManager);

            String log = ClientSetting.sharedSetting().getTime() +
                    " - [EZShare.Client.main] - [INFO]- setting debug " +
                    ((ClientSetting.sharedSetting().isDebugModel())?"on":"off");

            //parse command
            Command command = Command.commandFactory(cliManager);

//        Communicator client = new Communicator();
            Processor processor = Processor.processorFactory(command);
            processor.setSecurityMode(ClientSetting.sharedSetting().getSecurityMode());
            processor.process();
            //connect to the server and start communication
//        client.connectToServer();
//        client.writeData(command.toJSON());
//        client.readData();
        } catch (UndefinedCommandException uce){
            System.out.println("Received:\n");
            System.out.println(uce.errorMessage());
        } catch (ParseException e){
            JSONObject obj = new JSONObject();
            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
            obj.put(OptionField.errorMessage.getValue(),"invalid command");
            System.out.println(obj.toString());
        }
    }
}
