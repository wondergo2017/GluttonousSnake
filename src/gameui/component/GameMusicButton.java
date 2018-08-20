package gameui.component;

import gameui.GameUIConstants;
import gameui.StrData;
import gameui.component.GameButton;
import javafx.scene.media.AudioClip;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * for start or pause the music
 */
public class GameMusicButton extends GameButton {
    static Image imageMusic=GameUIConstants.IMAGE_MUSIC;
    static Image imageStopMusic=GameUIConstants.IMAGE_STOP_MUSIC;
    boolean isMusic=false;
    public boolean isFirst=true;
    public StrData strMusicUrl=GameUIConstants.DEFAULT_MUSIC_URL;
    public AudioClip audioClip;
    public GameMusicButton()
    {
        super(imageStopMusic,GameUIConstants.dGbtnPrefer);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isMusic^=true;
                if(isMusic){
                    setImage(imageMusic);
                    if(isFirst)
                    {
                        isFirst^=true;
                        audioClip=new AudioClip(strMusicUrl.strData);
                        audioClip.setCycleCount(AudioClip.INDEFINITE);
                        audioClip.play();
                    }
                    else {
                        audioClip.play();
                    }
                }else{
                    setImage(imageStopMusic);
                    audioClip.stop();
                }
                repaint();
            }
        });
    }
}
