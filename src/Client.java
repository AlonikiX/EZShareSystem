//import CommonLibs.CommandLine.CliManager;
//import CommonLibs.CommandLine.OptionField;
//import CommonLibs.Commands.Command;
//import CommonLibs.Exception.UndefinedCommandException;
//import EZShare_Client.ClientSetting;
//import EZShare_Client.Processor.Processor;
//import org.apache.commons.cli.ParseException;
//import org.json.JSONObject;
//
//public class Client {
//    private static final String ROLE  = "CLIENT";
//    public static void main(String[] args) {
//
//        try{
//            //initial command line options
//            CliManager cliManager = new CliManager(ROLE);
//            cliManager.initOptions(args);
//
//            String log = ClientSetting.sharedSetting().getTime() +
//                    " - [EZShare.Client.main] - [INFO]- setting debug " +
//                    ((ClientSetting.sharedSetting().isDebugModel())?"on":"off");
//
//            //parse command
//            Command command = Command.commandFactory(cliManager);
//
//            //initial client setting
//            ClientSetting.sharedSetting().initSetting(cliManager);
////        Communicator client = new Communicator();
//            Processor processor = Processor.processorFactory(command);
//            processor.process();
//            //connect to the server and start communication
////        client.connectToServer();
////        client.writeData(command.toJSON());
////        client.readData();
//        } catch (UndefinedCommandException uce){
//            System.out.println("Received:\n");
//            System.out.println(uce.errorMessage());
//        } catch (ParseException e){
//            JSONObject obj = new JSONObject();
//            obj.put(OptionField.response.getValue(),OptionField.error.getValue());
//            obj.put(OptionField.errorMessage.getValue(),"invalid command");
//            System.out.println(obj.toString());
//        }
//    }
//}
