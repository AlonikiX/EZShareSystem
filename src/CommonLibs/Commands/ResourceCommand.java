package CommonLibs.Commands;

import CommonLibs.CommandLine.CliManager;
import CommonLibs.CommandLine.OptionField;
import CommonLibs.DataStructure.Resource;
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
                resource.setName(name);
            }
        }
        if (cli.hasOption(OptionField.description.getValue())) {
            String description = cli.getOptionValue(OptionField.description.getValue());
            if (null != description) {
                resource.setDescription(description);
            }
        }
        if (cli.hasOption(OptionField.tags.getValue())) {
            String tagString = cli.getOptionValue(OptionField.tags.getValue());
            if (null != tagString) {
                String[] tags = tagString.split(",");
                resource.setTags(new ArrayList<>(Arrays.asList(tags)));
            }
        }
        if (cli.hasOption(OptionField.uri.getValue())) {
            String uri = cli.getOptionValue(OptionField.uri.getValue());
            if (null != uri) {
                resource.setUri(uri);
            }
        }
        if (cli.hasOption(OptionField.channel.getValue())) {
            String channel = cli.getOptionValue(OptionField.channel.getValue());
            if (null != channel) {
                resource.setDescription(channel);
            }
        }
        if (cli.hasOption(OptionField.owner.getValue())) {
            String owner = cli.getOptionValue(OptionField.owner.getValue());
            if (null != owner) {
                resource.setDescription(owner);
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
}
