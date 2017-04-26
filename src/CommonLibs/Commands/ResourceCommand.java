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

        if (!commandObj.has(OptionField.resource.getValue())){
            resource = null;
            return;
        }

        JSONObject obj = commandObj.getJSONObject(OptionField.resource.getValue());
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
        //initial
        initResourceCommand();
        //parse to resource
        if (cli.hasOption(OptionField.name.getValue())) {
            String name = cli.getOptionValue(OptionField.name.getValue());
            if (null != name) {
                resource.setName(name.trim());
            }
        }
        if (cli.hasOption(OptionField.description.getValue())) {
            String description = cli.getOptionValue(OptionField.description.getValue());
            if (null != description) {
                resource.setDescription(description.trim());
            }
        }
        if (cli.hasOption(OptionField.tags.getValue())) {
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
        }
        if (cli.hasOption(OptionField.uri.getValue())) {
            String uri = cli.getOptionValue(OptionField.uri.getValue());
            if (null != uri) {
                resource.setUri(uri.trim());
            }
        }
        if (cli.hasOption(OptionField.channel.getValue())) {
            String channel = cli.getOptionValue(OptionField.channel.getValue());
            if (null != channel) {
                resource.setChannel(channel.trim());
            }
        }
        if (cli.hasOption(OptionField.owner.getValue())) {
            String owner = cli.getOptionValue(OptionField.owner.getValue());
            if (null != owner) {
                resource.setOwner(owner.trim());
            }
        }
    }


    /**
     * @description parse the resource to a json object
     * @return a json object for the resource
     */
    protected JSONObject toResourceJSONObject() {
        JSONObject obj = new JSONObject();
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


    public Resource getResource(){
        return resource;
    }
}
