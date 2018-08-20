package core.event;
import core.obj.Point;

import java.io.Serializable;

public class GameEvent implements Serializable {
    public enum Status{
        EAT_FOOD,DIE,MOVE
    }
    public Point pos;
    public Status status;
    public String strEventMes;
    public int iSnakePlayer;
    public GameEvent(){}
    public GameEvent(Status _Status,int _iSnakePlayer)
    {
        status=_Status;
        iSnakePlayer=_iSnakePlayer;
    }
    public GameEvent(Status _Status,int _iSnakePlayer,Point _pos)
    {
        status=_Status;
        iSnakePlayer=_iSnakePlayer;
        pos=_pos;
    }
}
