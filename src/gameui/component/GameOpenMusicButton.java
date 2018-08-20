package gameui.component;

import gameui.GameUIConstants;
import gameui.StrData;
import gameui.component.GameButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

/**
 * for finding music
 */
public class GameOpenMusicButton extends GameButton {
    static Image imageOpenMusic=GameUIConstants.IMAGE_OPEN_MUSIC;
    StrData strMusicUrl;
    public GameOpenMusicButton(StrData _strMusicUrl)
    {
        super(imageOpenMusic,new Dimension(40,40));
        strMusicUrl=_strMusicUrl;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fileDialog=new FileDialog(new JFrame(),"选择音乐",FileDialog.LOAD);
                fileDialog.setVisible(true);
                try {
                    String filepath = fileDialog.getDirectory() + fileDialog.getFile();
                    strMusicUrl.strData = new File(filepath).toURI().toURL().toString();
                    System.out.println(strMusicUrl.strData);
                }catch (MalformedURLException e1)
                {
                    System.out.println(e1);
                }
            }
        });
    }

}
