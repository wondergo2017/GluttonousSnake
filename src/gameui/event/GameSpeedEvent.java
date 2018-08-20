package gameui.event;

public class GameSpeedEvent extends GameEvent {
    int speed;

    public int getSpeed() {
        return speed;
    }

    public GameSpeedEvent(int _speed)
    {
        speed=_speed;
    }
}
