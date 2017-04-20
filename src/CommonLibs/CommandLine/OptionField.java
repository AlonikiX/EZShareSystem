package CommonLibs.CommandLine;

/**
 * Created by apple on 20/04/2017.
 */
public enum OptionField {
    //settings
    debug("debug"),
    host("host"),
    port("port"),
    //commands
    query("query"),
    publish("publish"),
    remove("remove"),
    share("share"),
    fetch("fetch"),
    exchange("exchange"),
    //command fields
    name("name"),
    description("description"),
    uri("uri"),
    tags("tags"),
    owner("owner"),
    channel("channel"),
    secret("secret"),
    servers("servers"),

    //server options
    advertisedhostname("advertisedhostname"),
    connectionintervallimit("connectionintervallimit"),
    exchangeinterval("exchangeinterval"),

    //json
    command("command"),
    ezserver("ezserver"),
    resource("resource"),
    resourceTemplate("resourceTemplate"),
    ;


    private String value;

    public String getValue() {
        return value;
    }

    OptionField(String value){
        this.value = value;
    }
}
