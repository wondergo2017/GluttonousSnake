package gameui.event;

public class GameMoveEvent extends GameEvent {
    public int snakePlayer;
    public int nextDir;
    public GameMoveEvent(int _snakePlayer,int _nextDir)
    {
        snakePlayer=_snakePlayer;
        nextDir=_nextDir;
    }
}
