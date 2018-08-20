package gameui.event;

import java.util.Vector;

public class GameOverEvent extends GameEvent {
    public int winPlayer;
    public Vector<Integer> ranks;
    public GameOverEvent(int _winPlayer,Vector<Integer> _ranks)
    {
        winPlayer=_winPlayer;
        ranks=_ranks;
    }
}
