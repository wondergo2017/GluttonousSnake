package core;

import core.event.GameEvent;
import core.event.GameEvents;
import core.exception.NoMoreHoleException;
import core.exception.NoMoreSpaceException;
import core.exception.WrongPlaceException;
import core.obj.Point;
import core.obj.SnakeNode;

import java.util.Vector;

public class GameCore {
    //battleground
    public BattleGround bg;
    public int getM(){return bg.getM();}
    public int getN(){return bg.getN();}
    //snakes
    public Vector<Snake> snakes=new Vector<Snake>();
    public int getNextDir(int i) { return snakes.elementAt(i).getNextDir(); }
    public void setNextDir(int nextDir,int i) {
        snakes.elementAt(i).setNextDir(nextDir);
    }
    public int getBestScores()
    {
        int maxmum=Integer.MIN_VALUE;
        for (Snake snake : snakes) {
            if(snake.getScore()>maxmum)
            {
                maxmum=snake.getScore();
            }
        }
        return maxmum;
    }

    //game status
    private boolean isEnd=false;
    private int winPlayer=-1;
    public boolean isEnd() {
        return isEnd;
    }
    public int getWinPlayer() {
        return winPlayer;
    }
    /**
     * Food fresh counter, if it reaches the FOOD_FRESH_VALVE ,then put more foods
     */
    public int foodFreshCnt=0;
    //constructor
    public GameCore(int _M,int _N)
    {
        bg=new BattleGround(_M,_N);
    }

    //init
    public void start()
    {
        try {
            bg.putRanLineWalls(GameCoreConstants.INIT_WALL_NUM,GameCoreConstants.INIT_WALL_LEN);
            bg.putRanHoles(GameCoreConstants.INIT_HOLE_NUM);
            bg.putRanFoods(GameCoreConstants.INIT_FOOD_NUM);
            //born from holes
            snakes.add(Snake.CreateFromHole(GameCoreConstants.SNAKE_INIT_LEN,bg,GameCoreConstants.SNAKE_PLAYER1));
            snakes.add(Snake.CreateFromHole(GameCoreConstants.SNAKE_INIT_LEN,bg,GameCoreConstants.SNAKE_PLAYER2));
        }catch (NoMoreSpaceException e)
        {
            System.out.println(e);
            //todo
        }catch (NoMoreHoleException e)
        {
            System.out.println(e);
            //todo
        }
    }

    //run
    public GameEvents actOnePeriod()
    {
        {//food
            if (bg.getFoodCnt() == 0) {
                foodFreshCnt++;
            }
            if(foodFreshCnt>= GameCoreConstants.FOOD_FRESH_VALVE)
            {
                foodFreshCnt=0;
                try{bg.putRanFoods(GameCoreConstants.FOOD_ONE_TIME); }catch (NoMoreSpaceException e){
                    System.out.println(e);
                }
            }
        }
        {//move
            GameEvents gameEvents=new GameEvents();
            try {
                for (Snake snake : snakes) {
                    gameEvents.add(snake.move());
                }
            }catch (NoMoreHoleException expNMH)
            {
                //TODO
                System.out.println(expNMH);
            }catch (WrongPlaceException expWP)
            {
                for (int i = 0; i < snakes.size(); i++) {
                    if(expWP.getObj()==snakes.elementAt(i))
                    {
                        SnakeNode nHead=snakes.elementAt(i).getHead();
                        gameEvents.add(new GameEvent(GameEvent.Status.DIE,i,new Point(nHead.getI(),nHead.getJ())));
                        snakes.elementAt(i).die();
                        if(snakes.elementAt(i).getLives()<=0)
                        {
                            isEnd=true;
                            //todo
                            winPlayer=1^i;
                        }
                        try {
                            Snake snakeNew = Snake.CreateFromHole(2, bg, i);
                            snakeNew.setLives(snakes.elementAt(i).getLives()-1);
                            snakeNew.setScore(snakes.elementAt(i).getScore());
                            snakes.setElementAt(snakeNew,i);
                        }catch (NoMoreHoleException eNMH)
                        {
                            System.out.println(eNMH);
                            //todo
                        }
                    }
                }
            }
            return gameEvents;
        }
    }



}
