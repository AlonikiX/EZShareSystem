package CommonLibs.CommandLine;

import com.sun.net.httpserver.Authenticator;

/**
 * Created by apple on 20/04/2017.
 */
public enum OptionField {

    //settings
    debug("debug"),
    host("host"),
    port("port"),
    timeout("timeout"),

    //commands
    query("query"),
    publish("publish"),
    remove("remove"),
    share("share"),
    fetch("fetch"),
    exchange("exchange"),
    subscribe("subscribe"),
    unsubscribe("unsubscribe"),

    //resource fields
    name("name"),
    description("description"),
    uri("uri"),
    tags("tags"),
    owner("owner"),
    channel("channel"),
    secret("secret"),
    servers("servers"),
    resourceSize("resourceSize"),

    //result
    resultSize("resultSize"),
    response("response"),
    success("success"),
    error("error"),
    errorMessage("errorMessage"),

    //server options
    advertisedhostname("advertisedhostname"),
    connectionintervallimit("connectionintervallimit"),
    exchangeinterval("exchangeinterval"),

    //json
    command("command"),
    relay("relay"),
    serverList("serverList"),
    ezserver("ezserver"),
    hostname("hostname"),
    resource("resource"),
    resourceTemplate("resourceTemplate"),

    //invalid message
    invalidResource("invalid resource"),
    invalidTemplate("invalid resourceTemplate"),
    missingResource("missing resource"),
    missingTemplate("missing resourceTemplate"),
//    missingResourceTemplate("missingResourceTemplate"),

    // secure socket
    secure("secure"),
    sport("sport"),

    // subscription
    id("id"),



    ;

    private String value;

    public String getValue() {
        return value;
    }

    OptionField(String value){
        this.value = value;
    }
}
