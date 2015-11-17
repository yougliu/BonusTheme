package themerom.bonus.com.themerom.entity;

/**
 * Created by bonus on 11/17/15.
 * Class name ${type_name}
 */
public class MsgEvent<T> {
    private Object msg;

    public MsgEvent(Object msg){
        this.msg = msg;
    }

    public Object getMsg(){
        return msg;
    }

}
