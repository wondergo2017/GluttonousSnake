package gameui.client;

import com.sun.jdi.IntegerType;
import gameui.GameUIConstants;
import gameui.component.GameButton;
import gameui.test.GameUI;

import javax.swing.*;
import java.awt.*;

public class GameShowPanel extends JPanel {
    //Image imageBG=new ImageIcon("res/window/steam.jpg").getImage();
    static Image imageScores=GameUIConstants.IMAGE_SCORES;
    static Image imageLives=GameUIConstants.IMAGE_LIVES;
    static Image imageSpeed=GameUIConstants.IMAGE_SPEED;
    static Image imagePlayer=GameUIConstants.IMAGE_PLAYER;
    static Image imageSpeedUp=GameUIConstants.IMAGE_SPEED_UP;
    static Image imageSpeedDown=GameUIConstants.IMAGE_SPEED_DOWN;
    static String [] strlabels={"Scores","Lives","Speed","Player"};
    class GameShowButton extends JButton{
        Image image=null;
        Color color=null;
        GameShowButton(Color _color,Dimension dimension) {
            color=_color;
            setPreferredSize(dimension);
            init();
        }
        GameShowButton(Image _image,Dimension dimension)
        {
            image=_image;
            setPreferredSize(dimension);
            init();
        }

        private void init()
        {
            setOpaque(false);
            setFocusable(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setEnabled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(image!=null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }else{
                g.setColor(color);
                g.fillRect(0,0,getWidth(),getHeight());
            }
        }
    }
    class GameGroupPanel extends JPanel{
        GameGroupPanel()
        {
            init();
        }
        GameGroupPanel(JButton jButton,JLabel jLabel)
        {
            init();
            add(jButton);
            add(jLabel);
        }
        GameGroupPanel(JButton jButton,JButton jButton2)
        {
            init();
            add(jButton);
            add(jButton2);
        }
        void init()
        {
            setLayout(new FlowLayout());
            setOpaque(false);
            setBorder(null);
        }
    }
    JButton gbtnPlayerImply=new GameShowButton(imagePlayer,GameUIConstants.dGbtnPrefer);
    JButton gbtnPlayer;
    JButton gbtnScores=new GameShowButton(imageScores,GameUIConstants.dGbtnPrefer);
    JButton gbtnLives=new GameShowButton(imageLives,GameUIConstants.dGbtnPrefer);
    JButton gbtnSpeed=new GameShowButton(imageSpeed,GameUIConstants.dGbtnPrefer);
    public JButton gbtnSpeedUp=new GameButton(imageSpeedUp,GameUIConstants.dGbtnPrefer);
    public JButton gbtnSpeedDown=new GameButton(imageSpeedDown,GameUIConstants.dGbtnPrefer);
    JLabel labels[];
    public int getSpeed()
    {
        return Integer.parseInt(labels[2].getText());
    }
    public GameShowPanel(int Player)
    {
        setBorder(null);
        setOpaque(false);
        setLayout(new FlowLayout());
        labels=new JLabel[strlabels.length];
        for (int i = 0; i < labels.length; i++) {

            labels[i]=new JLabel("0");
            labels[i].setOpaque(false);
            labels[i].setBorder(null);
            labels[i].setPreferredSize(GameUIConstants.dGbtnPrefer);
            Font fontNew=new Font("微软雅黑",Font.BOLD,GameUIConstants.getGbtnPreFontSize());
            labels[i].setFont(fontNew);
            labels[i].setForeground(Color.WHITE);
        }

        gbtnPlayer=new GameShowButton(GameUIConstants.snakeColors[Player],GameUIConstants.dGbtnPrefer);

        add(new GameGroupPanel(gbtnPlayerImply,gbtnPlayer));

        add(new GameGroupPanel(gbtnScores,labels[0]));

        add(new GameGroupPanel(gbtnLives,labels[1]));

        add(new GameGroupPanel(gbtnSpeed,labels[2]));

        add(new GameGroupPanel(gbtnSpeedUp,gbtnSpeedDown));

    }
    void update(String str,int iData)
    {
        if(str.equals("Scores")) labels[0].setText(String .valueOf(iData));
        if(str.equals("Lives")) labels[1].setText(String.valueOf(iData));
        if(str.equals("Speed")) labels[2].setText(String.valueOf(iData));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.drawImage(imageBG,0,0,getWidth(),getHeight(),null);
        g.setColor(new Color(63, 63, 63));
        g.fillRect(0,0,getWidth(),getHeight());
    }
}
