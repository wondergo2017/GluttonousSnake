package gameui.client;

import gameui.component.GameComposePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameChatPanel extends JPanel {
    JLabel labelChat=new JLabel("聊天室");
    JTextArea jTextArea=new JTextArea();
    public JButton btnSend=new JButton("发送");
    JTextArea jTextSend=new JTextArea("发送消息");

    public void addChatStr(String strChat)
    {
        jTextArea.setText(jTextArea.getText()+strChat+"\n");
    }
    public GameChatPanel()
    {
        setFocusable(false);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        labelChat.setFocusable(false);
        //labelChat.setBorder(null);
        //labelChat.setOpaque(false);
        add(new GameComposePanel(labelChat));


        //jTextArea.setOpaque(false);
        //jTextArea.setEnabled(false);
        jTextArea.setFocusable(false);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(false);
        jTextArea.setRows(20);
        JScrollPane jScrollPane=new JScrollPane(jTextArea);
        add(jScrollPane);



        jTextSend.setLineWrap(true);
        jTextSend.setWrapStyleWord(true);
        jTextSend.setRows(20);
        add(new JScrollPane( jTextSend));

        //btnSend.setBorder(null);
        btnSend.setFocusable(false);
        btnSend.setContentAreaFilled(false);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
            }
        });
        //jButton.setOpaque(false);
        add(new GameComposePanel(btnSend));


    }
}
