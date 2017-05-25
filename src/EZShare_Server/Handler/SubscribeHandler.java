package EZShare_Server.Handler;

import CommonLibs.Commands.Command;

/**
 * Created by Anson Chen on 2017/5/25.
 */
public class SubscribeHandler extends Handler {

    int hits = 0;

    public SubscribeHandler(Command cmd){
        super(cmd);
    }

    public void handle() {

        // validate query



        // if relay() create new threads with relay off




        // loop listening to new resource




        // close phase : print hits


    }


    public void newResource(){

        hits ++;
        // send new resource

    }

}
