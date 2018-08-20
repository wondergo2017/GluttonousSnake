package gameui.client;

import gameui.GameUIConstants;
import gameui.component.GameButton;
import gameui.component.GameMusicButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFuncPanel extends JPanel {
    public GameMusicButton gameMusicButton;

   static Image imagePause=GameUIConstants.IMAGE_PAUSE;
    static Image imageContinue=GameUIConstants.IMAGE_CONTINUE;
    static Image imageHelp=GameUIConstants.IMAGE_HELP;
    static Image imageChat=GameUIConstants.IMAGE_CHAT;
    class  GamePauseButton extends GameButton {
        boolean isPause=false;
        public void setPause()
        {
            isPause=true;
            setImage(imagePause);
        }
        public void setContinue()
        {
            isPause=false;
            setImage(imageContinue);
        }
        public GamePauseButton()
        {
            super(imageContinue,GameUIConstants.dGbtnPrefer);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(isPause)
                    {
                        setContinue();
                    }else{
                        setPause();
                    }
                    repaint();
                }
            });
        }

    }
    public GamePauseButton gamePauseButton=new GamePauseButton();
    class GameHelpButton extends GameButton{
        boolean isHelp=false;

        public boolean isHelp() {
            return isHelp;
        }
        public GameHelpButton()
        {
            super(imageHelp,GameUIConstants.dGbtnPrefer);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isHelp^=true;
                }
            });
        }
    }
    class GameChatButton extends GameButton{
        boolean isChat=false;
        public boolean isChat()
        {
            return isChat;
        }
        GameChatButton()
        {
            super(imageChat,GameUIConstants.dGbtnPrefer);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isChat^=true;
                }
            });
        }
    }
    public GameHelpButton gameHelpButton=new GameHelpButton();
    public GameChatButton gameChatButton=new GameChatButton();
    public GameFuncPanel(GameMusicButton _gameMusicButton)
    {
        gameMusicButton=_gameMusicButton;
        setLayout(new FlowLayout());
        setOpaque(false);
        add(gameMusicButton);
        add(gamePauseButton);
        add(gameHelpButton);
        add(gameChatButton);
    }
}
