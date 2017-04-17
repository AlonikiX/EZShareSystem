package CommonLibs.Commands;

/**
 * Created by apple on 17/04/2017.
 */
public class ShareCommand extends ResourceCommand {
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public ShareCommand() {
        this.commonType = CommandType.SHARE;
    }

    @Override
    public String toJSON() {
        return super.toJSON();
    }
}

