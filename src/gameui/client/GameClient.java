package gameui.client;

import core.event.GameEvent;
import core.event.GameEvents;
import gameui.component.GameButton;
import gameui.component.GameMusicButton;
import gameui.component.GameOpenMusicButton;
import gameui.component.GameTextField;
import gameui.keyboard.GameKeyBoard;
import gameui.keyboard.GameKeyBoardWDSA;
import gameui.event.*;
import core.obj.Point;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import static javax.swing.JOptionPane.*;
import static gameui.GameUIConstants.*;
public class GameClient extends JFrame {
    public static void main(String args[])
    {
        try {
             GameClient gameClient = new GameClient();
        }catch (IOException e)
        {
            System.out.println(e);
        }
    }


    Socket socket;
    GameClientThread gameClientThread;
    GameUIPanel gameUIPanel;
    GameStartPanel gameStartPanel;
    GameShowPanel gameShowPanel;
    GameFuncPanel gameFuncPanel;
    GameEndPanel gameEndPanel;
    GameChatPanel gameChatPanel=new GameChatPanel();
    GameHelpPanel gameHelpPanel=new GameHelpPanel();
    Image icon= IMAGE_START_BG;
    int player;
    public GameClient() throws IOException
    {
        setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameStartPanel=new GameStartPanel();
        add("Center",gameStartPanel);
        setSize(800,800);
        setVisible(true);
        setFocusable(false);
    }
    class GameStartPanel extends JPanel{
       String strAddress;
       int iPort;
       Image imageBG= IMAGE_START_BG;
       GameStartIntroPanel gameStartIntroPanel=new GameStartIntroPanel();
       GameStartEnterPanel gameStartEnterPanel=new GameStartEnterPanel();

       public void clear()
       {
           gameStartEnterPanel.getTimerConnecting().stop();
       }

       @Override
       protected void paintComponent(Graphics g) {
           super.paintComponent(g);
           g.drawImage(imageBG,0,0,getWidth(),getHeight(),null);
       }
       GameStartPanel()
       {
           setSize(500,500);

           setLayout(new GridLayout(3,1));
           add(new JLabel(""));
           add(gameStartIntroPanel);
           add(gameStartEnterPanel);
       }
       class GameStartIntroPanel extends JPanel
       {
           Image imageGreedy=new ImageIcon("res/intro/greedy.png").getImage();
           GameStartIntroPanel(){
                setOpaque(false);
           }
           @Override
           protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               g.drawImage(imageGreedy,0,0,getWidth(),getHeight(),null);
           }
       }
       class GameStartEnterPanel extends JPanel
       {
           Image imageConnecting[]=new Image[4];
           Image imageStart=IMAGE_START_INTRO;
           GameButton btnStart=new GameButton(imageStart,new Dimension(60,40));
           GameButton btnConnecting;
           GameTextField textStrAddress=new GameTextField("IP地址","127.0.0.1");
           GameTextField textStrPort=new GameTextField("端口号","6666");
           Timer timerConnecting;
           GameMusicButton gameMusicButton=new GameMusicButton();
           GameOpenMusicButton gameOpenMusicButton=new GameOpenMusicButton(gameMusicButton.strMusicUrl);
            public Timer getTimerConnecting()
          {
              return timerConnecting;
          }
           GameStartEnterPanel()
           {
               for (int i = 0; i < imageConnecting.length; i++) {
                   imageConnecting[i]=new ImageIcon("res/intro/Connecting"+i+".png").getImage();
               }

               setOpaque(false);
               setLayout(new GridLayout(4,1));

               JPanel btnStartPanel=new JPanel();
               btnStartPanel.setLayout(new FlowLayout());
               btnStartPanel.add(btnStart);
               btnStartPanel.setOpaque(false);
               add(btnStartPanel);

               btnStart.addMouseListener(new MouseAdapter() {
                   @Override
                   public void mouseClicked(MouseEvent e) {
                       try{
                           strAddress=textStrAddress.getText();
                           iPort=Integer.parseInt(textStrPort.getText());
                           socket = new Socket(strAddress, iPort);

                           btnStartPanel.remove(btnStart);
                           btnConnecting=new GameButton(imageConnecting[0],new Dimension(60,40));
                           btnConnecting.setEnabled(false);
                           btnStartPanel.add(btnConnecting);
                           btnStartPanel.revalidate();
                           btnStartPanel.repaint();
                           timerConnecting=new Timer(300,new ActionListener() {
                               @Override
                               public void actionPerformed(ActionEvent e) {
                                   int index = btnConnecting.index;
                                   index++;
                                   if (index >= imageConnecting.length) index = 0;
                                   btnConnecting.index=index;
                                   btnConnecting.setImage(imageConnecting[index]);
                                   btnConnecting.repaint();
                               }
                           });
                           timerConnecting.start();
                           ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                           oos.writeObject(new GameWindowEvent("start"));
                           oos.flush();
                           gameClientThread=new GameClientThread();
                           gameClientThread.start();
                       }catch (UnknownHostException eUH)
                       {
                           showMessageDialog(null, "要不再看一下IP地址对不对？");
                       }
                       catch (NumberFormatException eNF)
                       {
                           showMessageDialog(null, "要不再看一下PORT对不对？");
                       }
                       catch (IOException eIO)
                       {
                           System.out.println(eIO);
                       }
                   }
               });
               add(textStrAddress);
               add(textStrPort);

               JPanel btnMusicPanel=new JPanel();
               btnMusicPanel.setLayout(new FlowLayout());
               btnMusicPanel.add(gameMusicButton);
                btnMusicPanel.add(gameOpenMusicButton);
                gameOpenMusicButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        gameMusicButton.isFirst=true;
                    }
                });
               btnMusicPanel.setOpaque(false);
               add(btnMusicPanel);
           }

       }

   }

    class GameClientThread extends Thread{
        @Override
        public void run() {
            while (true) {
                try {
                    ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    Object objHead=ois.readObject();
                    //1. game start
                    if(objHead.getClass().equals(GameStartGameEvent.class))
                    {
                        //1 remove startpanel, use gameuipanel
                        //2 receive battleground information, and draw
                        //3 add funcpanel and showpanel and according listener
                        //4 add chatpanel

                        GameStartGameEvent windowEvent= (GameStartGameEvent)objHead;
                        //1 remove startpanel, use gameuipanel, and add according listener
                        gameUIPanel=new GameUIPanel(windowEvent.M,windowEvent.N);
                        player=windowEvent.player;
                        gameStartPanel.clear();
                        remove(gameStartPanel);
                        add("Center",gameUIPanel);
                        GameClient.this.setMinimumSize(new Dimension(UI_WIDTH,UI_HEIGHT));

                        setSize(200,200);
                        setResizable(false);
                        GameKeyBoard gkb=new GameKeyBoardWDSA();
                        gameUIPanel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                super.mouseClicked(e);
                                requestFocus();
                                requestFocusInWindow();
                            }
                        });
                        gameUIPanel.setFocusable(true);
                        gameUIPanel. addKeyListener(new KeyAdapter() {
                                @Override
                                public void keyPressed(KeyEvent e) {
                                    int iKey=e.getKeyCode();
                                    System.out.println(iKey);
                                    if(gkb.keyToWDSA(iKey)!=-1)
                                    {
                                        try {
                                            System.out.println("sent move");
                                            ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                                            oos.writeObject(new GameMoveEvent(1,gkb.keyToWDSA(iKey)));
                                            oos.flush();
                                        }catch (IOException eIO)
                                        {
                                            System.out.println(eIO);
                                        }
                                        return;
                                    }
                                }

                            @Override
                            public void keyReleased(KeyEvent e) {
                                super.keyReleased(e);
                                int iKey=e.getKeyCode();
                                if(gkb.keyToFunc(iKey)!=-1)
                                {
                                    switch (gkb.keyToFunc(iKey)) {
                                        case GameKeyBoard.PAUSE:
                                            gameFuncPanel.gamePauseButton.doClick();
                                            break;
                                        case GameKeyBoard.SPEEDUP:
                                            gameShowPanel.gbtnSpeedUp.doClick();
                                            break;
                                        case GameKeyBoard.SPEEDDOWN:
                                            gameShowPanel.gbtnSpeedDown.doClick();
                                            break;
                                        case GameKeyBoard.HELP:
                                            gameFuncPanel.gameHelpButton.doClick();
                                            break;
                                        case GameKeyBoard.MUSIC:
                                            gameFuncPanel.gameMusicButton.doClick();
                                            break;
                                        case GameKeyBoard.CHAT:
                                            gameFuncPanel.gameChatButton.doClick();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        });

                        //2 receive battleground information, and draw
                        GameMapEvent gme=((GameMapEvent)ois.readObject());
                        gameUIPanel.wallVec=gme.wallVec;
                        gameUIPanel.holeVec=gme.holeVec;
                        gameUIPanel.foodVec=gme.foodVec;
                        gameUIPanel.snakes=gme.snakes;
                        gameUIPanel.player=player;

                        //3 add funcpanel and showpanel and according listener
                        gameFuncPanel=new GameFuncPanel(gameStartPanel.gameStartEnterPanel.gameMusicButton);
                        gameShowPanel=new GameShowPanel(player);
                        add("South",gameShowPanel);
                        gameShowPanel.update("Lives",gameUIPanel.snakes.elementAt(player).getLives());
                        gameShowPanel.update("Scores",gameUIPanel.snakes.elementAt(player).getScore());
                        gameShowPanel.add(gameFuncPanel);
                        GameSpeedEvent gse=((GameSpeedEvent)ois.readObject());
                        gameShowPanel.update("Speed",gse.getSpeed());
                        gameFuncPanel.gamePauseButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                    oos.writeObject(new GameWindowEvent("Pause"));
                                    oos.flush();
                                }catch (IOException e1)
                                {
                                    System.out.println(e1);
                                }
                            }
                        });
                        gameShowPanel.gbtnSpeedUp.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                    oos.writeObject(new GameSpeedControlEvent(true));
                                    oos.flush();
                                }catch (IOException e1)
                                {
                                    System.out.println(e1);
                                }
                            }
                        });
                        gameShowPanel.gbtnSpeedDown.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    //if(gameShowPanel.getSpeed()<=GameUIConstants.SPEED_MIN) return;
                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                    oos.writeObject(new GameSpeedControlEvent(false));
                                    oos.flush();
                                }catch (IOException e1)
                                {
                                    System.out.println(e1);
                                }
                            }
                        });
                        gameFuncPanel.gameChatButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(gameFuncPanel.gameChatButton.isChat())
                                {
                                    int deltaw=getWidth()/3;
                                    GameClient.this.setSize(getWidth()+deltaw,getHeight());
                                    gameChatPanel.setPreferredSize(new Dimension(deltaw,getHeight()));
                                    GameClient.this.add("East",gameChatPanel);
                                    gameChatPanel.jTextSend.requestFocus();
                                }
                                else{
                                    GameClient.this.setSize(getWidth()-gameChatPanel.getWidth(),getHeight());
                                    GameClient.this.remove(gameChatPanel);
                                    gameUIPanel.requestFocusInWindow();
                                    gameUIPanel.requestFocus();
                                }
                                revalidate();
                                repaint();
                            }
                        });

                        //4 add chatpanel
                        class klTextArea extends KeyAdapter
                        {
                            boolean isAlt=false;

                            @Override
                            public void keyPressed(KeyEvent e) {
                                if (e.getKeyCode()==KeyEvent.VK_ALT)
                                {
                                    isAlt=true;
                                }
                                if(isAlt&& e.getKeyChar()==KeyEvent.VK_ENTER)
                                {
                                    gameChatPanel.btnSend.doClick();
                                    gameFuncPanel.gameChatButton.doClick();
                                    isAlt=false;
                                }
                                if(!isAlt&&e.getKeyChar()==KeyEvent.VK_ENTER)
                                {
                                    gameChatPanel.btnSend.doClick();
                                }
                            }

                            @Override
                            public void keyReleased(KeyEvent e) {
                                if(e.getKeyCode()==KeyEvent.VK_ALT)
                                {
                                    isAlt=false;
                                }
                            }
                        }
                        gameChatPanel.jTextSend.addKeyListener(new klTextArea());
                        gameChatPanel.btnSend.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    String str=gameChatPanel.jTextSend.getText();
                                    gameChatPanel.jTextSend.setText("");
                                    while(!str.isEmpty()&&str.charAt(0)=='\n') str=str.substring(1);
                                    if(str.equals("")||str.equals("\n")) return;
                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                    oos.writeObject(new GameChatEvent(str));
                                    oos.flush();
                                }catch (IOException e1)
                                {
                                    System.out.println(e1);
                                }
                            }
                        });
                        gameFuncPanel.gameHelpButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(gameFuncPanel.gameHelpButton.isHelp())
                                {
                                    gameUIPanel.add(gameHelpPanel);
                                }
                                else{
                                    gameUIPanel.remove(gameHelpPanel);
                                }
                                revalidate();
                                repaint();
                            }
                        });
                        setTitle("Player:"+player);
                        revalidate();
                        repaint();
                    }

                    //2 adjust speed
                    if(objHead.getClass().equals(GameSpeedEvent.class))
                    {
                        GameSpeedEvent gs=(GameSpeedEvent) objHead;
                        gameShowPanel.update("Speed",gs.getSpeed());
                    }

                    //3 control game progress
                    if(objHead.getClass().equals(GameWindowEvent.class))
                    {
                        GameWindowEvent gwe=(GameWindowEvent)objHead;
                        if((gwe).strDepic.equals("repaint"))
                        {
                            repaint();
                        }
                        if((gwe).strDepic.equals("pause"))
                        {
                            gameUIPanel.isPause=true;
                            gameFuncPanel.gamePauseButton.setPause();
                            repaint();
                        }
                        if((gwe).strDepic.equals("continue"))
                        {
                            gameUIPanel.isPause=false;
                            gameFuncPanel.gamePauseButton.setContinue();
                            repaint();
                        }
                    }

                    //4 refresh battleground
                    if(objHead.getClass().equals(GameMapEvent.class))
                    {
                        GameMapEvent gme=(GameMapEvent) objHead;
                        gameUIPanel.wallVec=gme.wallVec;
                        gameUIPanel.holeVec=gme.holeVec;
                        gameUIPanel.foodVec=gme.foodVec;
                        gameUIPanel.snakes=gme.snakes;
                        gameShowPanel.update("Lives",gameUIPanel.snakes.elementAt(player).getLives());
                        gameShowPanel.update("Scores",gameUIPanel.snakes.elementAt(player).getScore());
                        repaint();
                    }

                    //5 gameoverevent
                    if(objHead.getClass().equals(GameOverEvent.class))
                    {
                        GameOverEvent goe=(GameOverEvent) objHead;
                        String strEnd;
                        if(goe.winPlayer==player)
                        {
                            strEnd="You Win!\n";
                        }
                        else{
                            strEnd="You Lose!\n";
                        }
                        GameClient.this.remove(gameUIPanel);
                        gameEndPanel=new GameEndPanel(strEnd,gameUIPanel.snakes.elementAt(player).getScore(),player,goe.ranks);
                        GameClient.this.add(gameEndPanel);
                        revalidate();
                        repaint();
                        break;
                    }

                    //6 game chatevent
                    if(objHead.getClass().equals(GameChatEvent.class))
                    {
                        GameChatEvent gce=(GameChatEvent)objHead;
                        gameChatPanel.addChatStr(gce.getStrChat());
                    }
                    //7 game events
                    if(objHead.getClass().equals(core.event.GameEvents.class))
                    {
                        GameEvents ges=(GameEvents)objHead;
                        for (GameEvent ge : ges) {
                            if(ge.status==GameEvent.Status.EAT_FOOD)
                            {
                                gameUIPanel.eatFoodVec.add(ge.pos);
                            }
                            if(ge.status==GameEvent.Status.DIE)
                            {
                                gameUIPanel.dieVec.add(ge.pos);
                            }
                        }
                    }

                }catch (IOException eIO)
                {
                    System.out.println(eIO+" in Client io");
                }catch (ClassNotFoundException eCNF)
                {
                    System.out.println(eCNF +"in Client cnf");
                }
            }
        }
    }
}
