package chongding.congxun.chongding.util;

public class UserEvent {

    int level;
    String content;
    public UserEvent(int id,String name) {
        this.level= id;
        this.content= name;
    }
    public int getId() {
        return level;
    }
    public String getName() {
        return content;
    }


}