package gameui.component;

import javax.swing.*;
import java.awt.*;

/**
 * transparent panel to wrap a component
 */
public class GameComposePanel extends JPanel
    {
        public GameComposePanel(JComponent jComponent){
            init();
            add(jComponent);
        }
        void init()
        {
            setOpaque(false);
            setLayout(new FlowLayout());
            setFocusable(false);
        }

}
