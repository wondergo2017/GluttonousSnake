package gameui.client;

import core.*;
import core.obj.Food;
import core.obj.Hole;
import core.obj.SnakeNode;
import core.obj.Wall;
import gameui.GameUIConstants;
import core.obj.Point;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GameUIPanel extends JPanel {
    public Vector<Wall> wallVec=new Vector<Wall>();
    public Vector<Hole> holeVec=new Vector<Hole>();
    public Vector<Food> foodVec=new Vector<Food>();
    public Vector<Snake> snakes=new Vector<Snake>();

    int player;
    int M,N;
    public boolean isEnd=false;
    public String strEnd="";
    public boolean isPause=false;
    Image imageFood[]=new Image[GameCoreConstants.FOODNUM];
    Image imageGrassBlock;
    Image imageHole=new ImageIcon("res/obj/hole4.png").getImage();
    Image imageHoleTaken=new ImageIcon("res/obj/hole3.png").getImage();
    Image imageWall=new ImageIcon("res/obj/hole5.png").getImage();
    Image imagePause=new ImageIcon("res/window/Gamepause.png").getImage();

    CardLayout cardLayout= new CardLayout();

    //for animation
    Vector<Point> eatFoodVec=new Vector<>();
    Vector<Point> dieVec=new Vector<>();
    public GameUIPanel(int _M, int _N)
    {
        M=_M;
        N=_N;
        for (int i = 0; i < imageFood.length; i++) {
            imageFood[i]=new ImageIcon("res/obj/food"+i+".png").getImage();
        }
        imageGrassBlock = new ImageIcon("res/grass/grass8.jpg").getImage();
        setLayout(cardLayout);
        setBorder(null);
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
    }
    private int blockW;
    private int blockH;

    private void fillCircle(Color c, int i, int j, Graphics g,double ratio)
    {
        g.setColor(c);
        g.fillOval((int)((j-ratio) * blockW), (int)((i-ratio) * blockH), (int)((1+ratio*2)*blockW), (int)((1+ratio*2)*blockH));
    }
    private void fillBlock(Color c, int i, int j, Graphics g)
    {
        g.setColor(c);
        g.fillRect(getGameJ(j),getGameI(i), getGameJ(j+1)-getGameJ(j),getGameI(i+1)-getGameI(i));
    }
    public int getGameI(int i)
    {
        if(i==M) return getHeight();
        return (i*blockH);
    }
    public int getGameJ(int j)
    {
        if(j==N) return getWidth();
        return (j*blockW);
    }

    private void fillImage(Image image,int i,int j,Graphics g)
    {
        g.drawImage(image,getGameJ(j),getGameI(i), getGameJ(j+1)-getGameJ(j),getGameI(i+1)-getGameI(i),null);
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int h=getHeight();
        int w=getWidth();
        blockH=h/M;
        blockW=w/N;

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                fillImage(imageGrassBlock,i,j,g);
            }
        }
        for (Wall wall : wallVec) {
            fillImage(imageWall,wall.getI(),wall.getJ(),g);
        }
        for (Food food : foodVec) {
            fillImage(imageFood[food.getDifferindex()],food.getI(),food.getJ(),g);
        }
        for (Hole hole : holeVec) {
            if(!hole.isTaken()) {
                fillImage(imageHole,hole.getI(),hole.getJ(),g);
            }else{
                fillImage(imageHoleTaken,hole.getI(),hole.getJ(),g);
            }
        }

        for (Snake snake : snakes) {
            for (int i = 0; i < snake.bodyvec.size(); i++) {
                SnakeNode snakeNode=snake.bodyvec.elementAt(i);
                if (!snakeNode.isInHole()) {
                    fillCircle(GameUIConstants.snakeColors[snake.getSnakePlayer()],snakeNode.getI(),snakeNode.getJ(),g,0.25*(snake.getLength()-i)/(snake.getLength()));
                   }
            }
        }
        //for animation
        g.setFont(new Font("微软雅黑",Font.BOLD,30));
        g.setColor(Color.magenta);
        for (Point point : eatFoodVec) {
            g.drawString("+1",getGameJ(point.getJ()),getGameI(point.getI()));
        }
        eatFoodVec.clear();

        for (Point point : dieVec) {
            g.drawString("die!",getGameJ(point.getJ()),getGameI(point.getI()));
        }
        dieVec.clear();

        if(isPause)
        {
            g.drawImage(imagePause,0,0,getWidth(),getHeight(),null);
        }
    }

}
