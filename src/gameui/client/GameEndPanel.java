package gameui.client;

import gameui.GameUIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GameEndPanel extends JPanel {

    Image imageLose=GameUIConstants.IMAGE_END_LOSE_BG;
    Image imageWin=GameUIConstants.IMAGE_END_WIN_BG;
    int player;
    String strEnd;
    int scores;

    private Vector<Integer> ranks;
    private boolean isLose()
    {
        return strEnd.equals("You Lose!\n");
    }
    public GameEndPanel(String _strEnd, int _Scores, int _player, Vector<Integer> _ranks){
        player=_player;
        strEnd=_strEnd;
        scores=_Scores;
        ranks=_ranks;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(strEnd.equals("You Lose!\n"))
        {
            g.drawImage(imageLose,0,0,getWidth(),getHeight(),null);
        }else{
            g.drawImage(imageWin,0,0,getWidth(),getHeight(),null);
        }


        int wordw=getWidth()/20;
        int wordh=getHeight()/20;
        int orgx=(getWidth()-10*wordw)/2;
        int orgy=4*wordh;
        Color cBg=new Color(20,20,20,120);
        g.setColor(cBg);
        g.fillRect(orgx-wordw,orgy-wordh,wordw*10,wordh*(6+2*(ranks.size()>=5?5:ranks.size())));


        g.setColor(Color.WHITE);
        g.setFont(new Font("微软雅黑",Font.PLAIN,wordw));
        g.drawString(strEnd,orgx,orgy);
        g.drawString("Scores:"+scores,orgx,orgy+wordh);
        g.drawString("龍虎榜",orgx+3*wordw,orgy+3*wordh);

        for (int i = 0; i < ranks.size(); i++) {
            if(i< GameUIConstants.RANK_NUM)
            g.drawString("第"+(i+1)+"名:"+ranks.elementAt(i),orgx,orgy+(5+2*i+1)*wordh);
        }
        return;
    }
}
