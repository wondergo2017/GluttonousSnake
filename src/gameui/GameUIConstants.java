package gameui;

import javax.swing.*;
import java.awt.*;

public class GameUIConstants {
    public static final int RANK_NUM=5;
    static public Color snakeColors[]={Color.GREEN,Color.ORANGE};
    static public Dimension dGbtnPrefer=new Dimension(25,30);
    static public int getGbtnPreFontSize()
    {
        int fontsize=Integer.min((int)GameUIConstants.dGbtnPrefer.getWidth(),(int)GameUIConstants.dGbtnPrefer.getHeight());
        return fontsize*8/10;
    }

    //gamecore
    //size of the chessboard
    public static final int GAME_M_INIT=25;
    public static final int GAME_N_INIT=25;
    public static final int UI_WIDTH=590;
    public static final int UI_HEIGHT=613;
    //network
    public static final int PORT_INIT=6666;
    //Move Speed of the snake
    /**
     * milisec
     */
    public static final int SPEED_TIME=1000;
    public static final int SPEED_MIN=1;
    public static final int SPEED_MAX=20;
    public static final int SPEED_INIT=10;
    //Image Res
    static final public Image IMAGE_START_BG =new ImageIcon("res/snake/snake6.jpg").getImage();
    public static final Image IMAGE_START_INTRO=new ImageIcon("res/intro/start.png").getImage();
    public static final Image IMAGE_END_LOSE_BG=new ImageIcon("res/snake/snake12.jpg").getImage();
    public static final Image IMAGE_END_WIN_BG=new ImageIcon("res/snake/snake11.jpg").getImage();
    public static final Image IMAGE_PAUSE=new ImageIcon("res/window/pause.png").getImage();
    public static final Image IMAGE_CONTINUE=new ImageIcon("res/window/continue.png").getImage();
    public static final Image IMAGE_HELP=new ImageIcon("res/window/help.png").getImage();
    public static final Image IMAGE_CHAT=new ImageIcon("res/window/chat.png").getImage();
    public static final Image IMAGE_MUSIC=new ImageIcon("res/window/music.png").getImage();
    public static final Image IMAGE_STOP_MUSIC=new ImageIcon("res/window/stopmusic.png").getImage();
    public static final Image IMAGE_OPEN_MUSIC=new ImageIcon("res/window/openmusic.png").getImage();
    public static Image IMAGE_SCORES=new ImageIcon("res/window/scores.png").getImage();
    public static Image IMAGE_LIVES =new ImageIcon("res/window/heart.png").getImage();
    public static Image IMAGE_SPEED =new ImageIcon("res/window/speed.png").getImage();
    public static Image IMAGE_PLAYER =new ImageIcon("res/window/player.png").getImage();
    public static Image IMAGE_SPEED_UP =new ImageIcon("res/window/speedup.png").getImage();
    public static Image IMAGE_SPEED_DOWN =new ImageIcon("res/window/speeddown.png").getImage();
    public static final StrData DEFAULT_MUSIC_URL=new StrData("file:res/sound/Daughtry.mp3");
//    static Image imageGrass=new ImageIcon("res/grass/grass1.png").getImage();
//    static Image imageHole=new ImageIcon("res/obj/hole4.png").getImage();
//    static Image imageHoleTaken=new ImageIcon("res/obj/hole3.png").getImage();
//    static Image imageWall=new ImageIcon("res/obj/hole5.png").getImage();
//    static Image imagePause=new ImageIcon("res/window/Gamepause.png").getImage();

}
