package gameui.event;

public class GameChatEvent extends GameEvent {
    String strChat;

    public String getStrChat() {
        return strChat;
    }

    public GameChatEvent(String _strChat)
    {
        strChat=_strChat;
    }
}
