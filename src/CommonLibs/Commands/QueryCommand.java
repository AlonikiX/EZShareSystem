package CommonLibs.Commands;

/**
 * Created by apple on 17/04/2017.
 */
public class QueryCommand extends ResourceCommand {
    public QueryCommand() {
        this.commonType = CommandType.QUERY;
    }

    @Override
    public String toJSON() {
        //TODO pharse command to json
        return super.toJSON();
    }
}
