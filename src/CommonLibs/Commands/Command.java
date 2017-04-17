package CommonLibs.Commands;

import CommonLibs.Commands.CommandType;

/**
 * Created by apple on 17/04/2017.
 */
public class Command {
    protected CommandType commonType;

    public String toJSON(){
        return this.commonType.toString();
    }
}
