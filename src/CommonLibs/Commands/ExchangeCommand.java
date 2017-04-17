package CommonLibs.Commands;

import java.util.Set;

/**
 * Created by apple on 17/04/2017.
 */
public class ExchangeCommand extends Command {
    private Set<String> serverList;

    public Set<String> getServerList() {
        return serverList;
    }

    public void setServerList(Set<String> serverList) {
        this.serverList = serverList;
    }

    public ExchangeCommand() {
        this.commonType = CommandType.EXCHANGE;
    }

    @Override
    public String toJSON() {
        return super.toJSON();
    }
}
