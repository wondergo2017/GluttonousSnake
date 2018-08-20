package gameui.event;

public class GameStartGameEvent extends GameEvent{
    public int M,N;
    public int player;
   public GameStartGameEvent(int _M,int _N,int _player)
    {
        M=_M;
        N=_N;
        player=_player;
    }
}
