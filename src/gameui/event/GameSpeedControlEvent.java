package gameui.event;

public class GameSpeedControlEvent extends GameEvent {
    boolean isUp;
    public boolean isUp() {
        return isUp;
    }
    public GameSpeedControlEvent(boolean _isUp)
    {
        isUp=_isUp;
    }
}
