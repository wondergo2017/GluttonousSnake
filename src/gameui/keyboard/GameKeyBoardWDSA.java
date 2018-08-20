package gameui.keyboard;

import java.awt.event.KeyEvent;

public class GameKeyBoardWDSA implements GameKeyBoard{
    @Override
    public int keyToWDSA(int c) {
        if(c=='w'||c=='W') return K_W;
        if(c=='d'||c=='D') return K_D;
        if(c=='s'||c=='S') return K_S;
        if(c=='a'||c=='A') return K_A;
        if(c==KeyEvent.VK_UP) return K_W;
        if(c==KeyEvent.VK_RIGHT) return K_D;
        if(c==KeyEvent.VK_DOWN) return K_S;
        if(c==KeyEvent.VK_LEFT) return K_A;
        return K_NONE;
    }
    @Override
    public int keyToFunc(int chKey) {
        if(chKey=='P'||chKey=='p') return PAUSE;
        if(chKey=='+'||chKey=='=') return SPEEDUP;
        if(chKey=='-'||chKey=='_') return SPEEDDOWN;
        if(chKey=='c'||chKey=='C') return CHAT;
        if(chKey=='m'||chKey=='M') return MUSIC;
        if(chKey=='h'||chKey=='H') return HELP;

        return K_NONE;
    }
}
