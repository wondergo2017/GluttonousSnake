package gameui.keyboard;

import java.awt.event.KeyEvent;

public interface GameKeyBoard {
    static final int K_NONE=-1;
    static final int K_W=0;
    static final int K_D=1;
    static final int K_S=2;
    static final int K_A=3;
    static final int PAUSE=4;
    static final int SPEEDUP=5;
    static final int SPEEDDOWN=6;
    static final int MUSIC=7;
    static final int HELP=8;
    static final int CHAT=9;
    /**
     * @param iKey
     * @return 0-UP;1-Right;2-Down;3-Left; if not among those,return -1;
     */
    int keyToWDSA(int iKey);

    /**
     * @param iKey
     * @return 4-Pause/Continue;if not among those,return -1;
     */
    int keyToFunc(int iKey);
}

