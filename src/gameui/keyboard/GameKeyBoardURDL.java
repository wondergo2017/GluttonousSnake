package gameui.keyboard;

import java.awt.event.KeyEvent;

public class GameKeyBoardURDL implements GameKeyBoard{

    @Override
    public int keyToWDSA(int iKey) {
        if(iKey==KeyEvent.VK_UP) return K_W;
        if(iKey==KeyEvent.VK_RIGHT) return K_D;
        if(iKey==KeyEvent.VK_DOWN) return K_S;
        if(iKey==KeyEvent.VK_LEFT) return K_A;
        return K_NONE;
    }

    @Override
    public int keyToFunc(int chKey) {
        //if(chKey=='/'||chKey=='?') return PAUSE;
        if(chKey=='P'||chKey=='p') return PAUSE;
        if(chKey=='+'||chKey=='=') return SPEEDUP;
        if(chKey=='-'||chKey=='_') return SPEEDDOWN;
        if(chKey=='c'||chKey=='C') return CHAT;
        if(chKey=='m'||chKey=='M') return MUSIC;
        if(chKey=='h'||chKey=='H') return HELP;
        return K_NONE;
    }
}
