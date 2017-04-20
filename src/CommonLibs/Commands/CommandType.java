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
    EXCHANGE("EXCHANGE"),;

    private String value;

    public String getValue() {
        return value;
    }

    CommandType(String value){
        this.value = value;
    }

}
