package CommonLibs.Commands;

/**
 * Created by apple on 17/04/2017.
 */
public class FetchCommand extends ResourceCommand{

    public FetchCommand() {
        this.commonType = CommandType.FETCH;
    }

    @Override
    public String toJSON() {
        return super.toJSON();
    }
}
