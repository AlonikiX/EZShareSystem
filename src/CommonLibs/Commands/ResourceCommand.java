package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.DataStructure.Resource;
import org.apache.commons.cli.Options;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by apple on 17/04/2017.
 */
public abstract class ResourceCommand extends Command {
    protected Resource resource;


    /**
     * @description initial the resource
     */
    /* ***********************************************************************
     * notes for code optimization later:
     * don't have to initiate, because if resource is not recorded, then use resource = null,
     * otherwise, resource = new Resource(),
     * then don't have to set initial value, check each option field one by one:
     * IF option_defined
     * THEN set defined value
     * ELSE set default value
     * NOTICE, however, do initialize tags as an empty arraylist
     */
    private void initResourceCommand() {
        this.resource = new Resource();
        resource.setName("");
        resource.setDescription("");
        resource.setTags(new ArrayList<String>());
        resource.setUri("");
        resource.setChannel("");
        resource.setOwner("");
        resource.setEzserver(null);
    }

    protected void toResource(JSONObject commandObj) {
        JSONObject obj;
        if (!commandObj.has(OptionField.resource.getValue())){
            if (!commandObj.has(OptionField.resourceTemplate.getValue())){
                resource = null;
                return;
            }else {
                obj = commandObj.getJSONObject(OptionField.resourceTemplate.getValue());
            }
        }else {
            obj = commandObj.getJSONObject(OptionField.resource.getValue());
        }

        initResourceCommand();

        if (obj.has(OptionField.name.getValue())) {
            String name = obj.getString(OptionField.name.getValue());
                if (null != name) {
                    resource.setName(name);
                }
        }
        if (obj.has(OptionField.description.getValue())) {
            String description = obj.getString(OptionField.description.getValue());
            if (null != description) {
                resource.setDescription(description);
            }
        }
        if (obj.has(OptionField.tags.getValue())) {
            JSONArray arr = obj.getJSONArray(OptionField.tags.getValue());
            for (int i = 0; i < arr.length(); i++) {
                resource.getTags().add(arr.getString(i).toLowerCase());
            }
        }
        if (obj.has(OptionField.uri.getValue())) {
            String uri = obj.getString(OptionField.uri.getValue());
            if (null != uri) {
                resource.setUri(uri);
            }
        }
        if (obj.has(OptionField.channel.getValue())) {
            String channel = obj.getString(OptionField.channel.getValue());
            if (null != channel) {
                resource.setChannel(channel);
            }
        }
        if (obj.has(OptionField.owner.getValue())) {
            String owner = obj.getString(OptionField.owner.getValue());
            if (null != owner) {
                resource.setOwner(owner);
            }
        }
        if (obj.has(OptionField.ezserver.getValue())) {
            String ezserver = obj.getString(OptionField.ezserver.getValue());
            if (null != ezserver) {
                resource.setEzserver(ezserver);
            }
        }
    }

    /**
     * @description parse cli to resource
     * @param cli, an instance of CliManager
     */
    protected void toResource(CliManager cli) {



        System.out.println("Resource gained via CLI");





        //initial
        initResourceCommand();
        boolean resourceDefined = false;
        //parse to resource
        if (cli.hasOption(OptionField.name.getValue())) {

            System.out.println("Option has a name field");

            String name = cli.getOptionValue(OptionField.name.getValue());
            if (null != name) {
                resource.setName(name.trim());
            }
            resourceDefined = true;
        }
        if (cli.hasOption(OptionField.description.getValue())) {

            System.out.println("Option has a desc field");


            String description = cli.getOptionValue(OptionField.description.getValue());
            if (null != description) {
                resource.setDescription(description.trim());
            }
            resourceDefined = true;
        }
        if (cli.hasOption(OptionField.tags.getValue())) {

            System.out.println("Option has a tags field");


            String tagString = cli.getOptionValue(OptionField.tags.getValue());
            if (null != tagString) {
                String[] tags = tagString.split(",");
                ArrayList<String> tageList = new ArrayList<String>(Arrays.asList(tags));
                String tmp = "";
                for (int i=0; i<tageList.size(); i++){
                    tmp = tageList.get(i).toLowerCase();
                    tageList.set(i,tmp);
                }
                resource.setTags(tageList);
            }
            resourceDefined = true;
        }
        if (cli.hasOption(OptionField.uri.getValue())) {


            System.out.println("Option has a uri field");



            String uri = cli.getOptionValue(OptionField.uri.getValue());
            if (null != uri) {
                resource.setUri(uri.trim());
            }
            resourceDefined = true;
        }
        if (cli.hasOption(OptionField.channel.getValue())) {


            System.out.println("Option has a channel field");



            String channel = cli.getOptionValue(OptionField.channel.getValue());
            if (null != channel) {
                resource.setChannel(channel.trim());
            }
            resourceDefined = true;
        }
        if (cli.hasOption(OptionField.owner.getValue())) {



            System.out.println("Option has an owner field");



            String owner = cli.getOptionValue(OptionField.owner.getValue());
            if (null != owner) {
                resource.setOwner(owner.trim());
            }
            resourceDefined = true;
        }



        System.out.println("Resource is " + ((resourceDefined)?"":"not ") + "defined");

        if(!resourceDefined) resource = null;
    }


    /**
     * @description parse the resource to a json object
     * @return a json object for the resource
     */
    protected JSONObject toResourceJSONObject() {
        JSONObject obj = new JSONObject();

        if (resource != null){

            // Opt tips: we don;t need the if resource != null, because this method is never called in that case
            obj.put(OptionField.name.getValue(), resource.getName());
            obj.put(OptionField.description.getValue(), resource.getDescription());

            JSONArray arr = new JSONArray();
            for (String tag : resource.getTags()) {
                arr.put(tag);
            }
            obj.put(OptionField.tags.getValue(), arr);

            obj.put(OptionField.uri.getValue(), resource.getUri());
            obj.put(OptionField.channel.getValue(), resource.getChannel());
            obj.put(OptionField.owner.getValue(), resource.getOwner());
            obj.put(OptionField.ezserver.getValue(), resource.getEzserver());
            return obj;

        }

        return obj;

    }

    public Resource getResource(){
        return resource;
    }
}
