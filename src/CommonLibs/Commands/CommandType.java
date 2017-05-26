package CommonLibs.Commands;

/**
 * Created by apple on 17/04/2017.
 */
public enum CommandType {
    QUERY("QUERY"),
    PUBLISH("PUBLISH"),
    REMOVE("REMOVE"),
    SHARE("SHARE"),
    FETCH("FETCH"),
    EXCHANGE("EXCHANGE"),
    SUBSCRIBE("SUBSCRIBE"),
    UNSUBSCRIBE("UNSUBSCRIBE");

    private String value;

    public String getValue() {
        return value;
    }

    CommandType(String value){
        this.value = value;
    }

    public static CommandType getEnum(String value) {
        switch (value) {
            case "QUERY":
                return QUERY;
            case "REMOVE":
                return REMOVE;
            case "PUBLISH":
                return PUBLISH;
            case "SHARE":
                return SHARE;
            case "FETCH":
                return FETCH;
            case "EXCHANGE":
                return EXCHANGE;
        }
        return null;
    }

}
