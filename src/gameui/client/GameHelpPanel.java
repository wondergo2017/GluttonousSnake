package gameui.client;

import javax.swing.*;
import java.awt.*;

public class GameHelpPanel extends JPanel {
    static Image imageHelp=new ImageIcon("res/window/helppanel.png").getImage();
    public GameHelpPanel(){
        setOpaque(false);
        setFocusable(false);
        setBorder(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageHelp,0,0,getWidth(),getHeight(),null);
    }
}
