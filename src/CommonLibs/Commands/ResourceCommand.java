package CommonLibs.Commands;

import CommonLibs.CliManager;
import CommonLibs.Resource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
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
        if (cli.hasOption("name")) {
            String name = cli.getOptionValue("name");
            if (null != name) {
                resource.setName(name);
            }
        }
        if (cli.hasOption("description")) {
            String description = cli.getOptionValue("description");
            if (null != description) {
                resource.setDescription(description);
            }
        }
        if (cli.hasOption("tags")) {
            String tagString = cli.getOptionValue("tags");
            if (null != tagString) {
                String[] tags = tagString.split(",");
                resource.setTags(new ArrayList<>(Arrays.asList(tags)));
            }
        }
        if (cli.hasOption("uri")) {
            String uri = cli.getOptionValue("uri");
            if (null != uri) {
                resource.setUri(uri);
            }
        }
        if (cli.hasOption("channel")) {
            String channel = cli.getOptionValue("channel");
            if (null != channel) {
                resource.setDescription(channel);
            }
        }
        if (cli.hasOption("owner")) {
            String owner = cli.getOptionValue("owner");
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
        obj.put("name", resource.getName());
        obj.put("description", resource.getDescription());

        JSONArray arr = new JSONArray();
        for (String tag : resource.getTags()) {
            arr.put(tag);
        }
        obj.put("tags", arr);

        obj.put("uri", resource.getUri());
        obj.put("channel", resource.getChannel());
        obj.put("owner", resource.getOwner());
        obj.put("ezserver", resource.getEzserver());

        return obj;
    }
}
