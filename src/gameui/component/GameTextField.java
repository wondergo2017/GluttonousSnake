package gameui.component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GameTextField extends JPanel
{
    JLabel labelDepic=new JLabel();
    JTextField textField=new JTextField();
    public GameTextField(String depic, String strDefault){
        setOpaque(false);
        labelDepic.setText(depic);
        labelDepic.setBorder(null);
        labelDepic.setOpaque(false);
        Border blackline = BorderFactory.createLineBorder(Color.white);
        Border border=BorderFactory.createTitledBorder(blackline, depic);
        ((TitledBorder) border).setTitleColor(Color.white);
        textField.setForeground(Color.white);
        textField.setBorder(border);
        textField.setOpaque(false);
        textField.setPreferredSize(new Dimension(60,40));
        //textField.setAlignmentX(CENTER_ALIGNMENT);
        textField.setText(strDefault);
        setLayout(new FlowLayout());
       // add(labelDepic);
        add(textField);
    }
    public String getText()
    {
        return textField.getText();
    }
}