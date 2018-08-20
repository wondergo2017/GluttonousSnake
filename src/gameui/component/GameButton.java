package gameui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * when being hovered,the icon will be larger and be scoped on
 */
public  class GameButton extends JButton
{
    public Dimension sizeOrgPrefer;
    private Image image;
    public int index=0;
    public void setImage(Image _image){image=_image;}
    public GameButton(Image _image,Dimension _sizeOrgPrefer){
        image=_image;
        sizeOrgPrefer=_sizeOrgPrefer;
        setPreferredSize(new Dimension((int)(sizeOrgPrefer.getWidth()/1.2),(int)(sizeOrgPrefer.getHeight()/1.2)));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusable(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(!isEnabled()) return;
                setPreferredSize(new Dimension((int)(sizeOrgPrefer.getWidth()),(int)(sizeOrgPrefer.getHeight())));
                revalidate();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(!isEnabled()) return;
                setPreferredSize(new Dimension((int)(sizeOrgPrefer.getWidth()/1.2),(int)(sizeOrgPrefer.getHeight()/1.2)));
                revalidate();
                repaint();
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}