package gameui.test;

import core.*;
import core.event.GameEvent;
import core.event.GameEvents;
import core.obj.*;
import core.obj.Point;
import gameui.keyboard.GameKeyBoard;
import gameui.keyboard.GameKeyBoardURDL;
import gameui.keyboard.GameKeyBoardWDSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class GameUI extends JFrame {
    class GameUIPanel extends JPanel
    {
        private GameCore gameCore;
        int M,N;
        GameUIPanel(int _M,int _N)
        {
            //core
            M=_M;
            N=_N;
            //this.setSize(300,300);
            gameCore=new GameCore(M,N);
            gameCore.start();
        }
        private int blockW;
        private int blockH;
        private void fillBlock(Color c,int i,int j,Graphics g)
        {
            g.setColor(c);
            g.fillRect((int)((j+0.5) * blockW), (int)((i+0.5) * blockH), blockW, blockH);
        }
        @Override
        public void paintComponent(Graphics g)
        {
            //super.paint(g);
            if(gameCore.isEnd())
            {
                g.drawString("DIE!!!!!!!!",100,100);
                return;
            }
            int h=getHeight();
            int w=getWidth();
            blockH=h/M;
            blockW=w/N;
            g.setColor(Color.black);
            g.fillRect(0,0,w,h);
            for (Wall wall : gameCore.bg.wallVec) {
                fillBlock(Color.orange,wall.getI(),wall.getJ(),g);
            }
            for (Food food : gameCore.bg.foodVec) {
                fillBlock(Color.RED,food.getI(),food.getJ(),g);
            }
            for (Hole hole : gameCore.bg.holeVec) {
                if(!hole.isTaken()) {
                    fillBlock(Color.WHITE, hole.getI(), hole.getJ(), g);
                }else{
                    fillBlock(Color.magenta,hole.getI(),hole.getJ(),g);
                }
                int dirHole= hole.getDir();
                Point pNew= gameCore.bg.getNextPoint(hole.getI(),hole.getJ(),dirHole);
                int iTmp=(int)((pNew.getI()+0.8) * blockH);
                int jTmp=(int)((pNew.getJ()+0.8) * blockW);
                g.drawLine((int)((hole.getJ()+0.8) * blockW),(int)((hole.getI()+0.8) * blockH),jTmp,iTmp);
            }
            Color snakeColors[]={Color.GREEN,Color.BLUE};
            for (Snake snake : gameCore.snakes) {
                for (int i = 0; i < snake.bodyvec.size(); i++) {
                    SnakeNode snakeNode=snake.bodyvec.elementAt(i);
                    if (!snakeNode.isInHole()) {
                        fillBlock(snakeColors[snake.getSnakePlayer()],snakeNode.getI(),snakeNode.getJ(),g);
                        g.setColor(Color.WHITE);
                        g.drawString(Integer.toString(snake.getSnakePlayer()), (int) ((snakeNode.getJ() + 0.8) * blockW), (int) ((snakeNode.getI() + 0.8) * blockH));
                    }
                }
            }

        }
    }
    class GameFuncPanel extends JPanel{
    }
    class GameShowPanel extends JPanel{
        Map<String,Integer> mapLabel=new HashMap<String,Integer>();
        Label labelShow[];
        GameShowPanel()
        {
            mapLabel.put("Score",0);
            mapLabel.put("Lives",1);
            mapLabel.put("Level",2);
            labelShow=new Label[mapLabel.size()];
            for (Map.Entry<String, Integer> entry : mapLabel.entrySet()) {
                labelShow[entry.getValue()]=new Label(entry.getKey()+":"+0);
            }
            setLayout(new FlowLayout());
            for (Label label : labelShow) {
                add(label);
            }
        }
        void update(String str,int iData)
        {
            labelShow[mapLabel.get(str)].setText(str+":"+iData);
        }
    }
    private Timer timePainter;
    private GameUIPanel gameUIPanel;
    private GameShowPanel gameShowPanel;
    private boolean isPause=false;
    private void GameKeyBoardReact(int snakePlayer, GameKeyBoard gkb, int iKey)
    {
        if(gkb.keyToFunc(iKey)!=-1)
        {
            if(gkb.keyToFunc(iKey)==4) {
                if (isPause) {
                    isPause = false;
                    timePainter.start();
                } else {
                    isPause = true;
                    timePainter.stop();
                }
            }
        }
        if(gkb.keyToWDSA(iKey)!=-1)
        {
            gameUIPanel.gameCore.setNextDir(gkb.keyToWDSA(iKey),snakePlayer);
            return;
        }
    }
    public  GameUI(int _M,int _N)
    {
        //UI
        setSize(600,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameUIPanel=new GameUIPanel(_M,_N);
        add("Center",gameUIPanel);
        gameShowPanel=new GameShowPanel();
        add("South",gameShowPanel);

        GameKeyBoard gkbs[]={new GameKeyBoardURDL(),new GameKeyBoardWDSA()};
        addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {
            }
            @Override public void keyPressed(KeyEvent e) {
                int iKey=e.getKeyCode();
                GameKeyBoardReact(0,gkbs[0],iKey);
                GameKeyBoardReact(1,gkbs[1],iKey);
            }
            @Override public void keyReleased(KeyEvent e) {;}
        });
        ActionListener actionTimer=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameEvents gameEvents= gameUIPanel.gameCore.actOnePeriod();
                for (GameEvent gameEvent : gameEvents) {
                    if(gameEvent.status==GameEvent.Status.EAT_FOOD&&gameEvent.iSnakePlayer==0)
                    {
                        gameShowPanel.update("Score",gameUIPanel.gameCore.snakes.elementAt(0).getScore());
                    }
                }
                if (gameUIPanel.gameCore.isEnd())
                {
                    try{Thread.sleep(1000);}catch (Exception ex){
                        System.out.println(ex);
                    }
                    timePainter.stop();
                }
                repaint();
            }
        };
        timePainter=new Timer(100,actionTimer);
        timePainter.start();
        setVisible(true);
    }

}
