package core;

import core.event.GameEvent;
import core.exception.NoMoreHoleException;
import core.exception.NoMoreSpaceException;
import core.exception.WrongPlaceException;
import core.obj.*;

import java.util.Vector;

public class Snake extends Obj {
    //battleground
    transient BattleGround battleGround=null;

    //snakecode belonging
    private int snakeCode=0;
    private static int snakeCoder=0;
    private int snakePlayer=-1;
    private int lives=GameCoreConstants.SNAKE_INIT_LIVES;

    //status
    private boolean isDead=false;
    private int nowDir=0;
    private int nextDir=0;
    /**
     * time already spent in hole
     */
    private int cntWait=0;
    private int score=0;
    public int getNowDir(){return nowDir;}
    public int getNextDir() {
        return nextDir;
    }
    public void setNextDir(int nextDir) {
        this.nextDir = nextDir;
    }
    public boolean isDead() {        return isDead; }
    public int getScore() {        return score; }
    public void setScore(int i){score=i;}
    public int getSnakeCode(){        return snakeCode; }
    public int getSnakePlayer() {return snakePlayer;    }
    public void setSnakePlayer(int snakePlayer) { this.snakePlayer = snakePlayer; }

    //body
    public Vector<SnakeNode> bodyvec=new Vector<SnakeNode>();
    public SnakeNode getTail() { return bodyvec.elementAt(bodyvec.size()-1);}
    public SnakeNode getHead(){return bodyvec.elementAt(0);}
    public int getLength(){return bodyvec.size();}

    //constructor
    private Snake(int _snakePlayer) {
        snakeCoder++;
        snakeCode=snakeCoder;
        snakePlayer=_snakePlayer;
    }
    public static Snake CreateFromBlank(int initLen,BattleGround _bg,int _snakePlayer)//dir 初始头的方向，
    {
        //1 得到完整一块线段存放蛇的初始身体
        //2 在蛇身上将所得到的身体片段加入
        //3 在地图上进行注册
        Snake snakeNew=new Snake(_snakePlayer);
        Vector<Point> pVec=new Vector<Point>();
        try{_bg.getRanBlankLine(initLen,pVec);}catch (NoMoreSpaceException e){
            System.out.println(e);
        }
        for (Point point : pVec) {
            snakeNew.bodyvec.add(new SnakeNode(point.getI(),point.getJ(),snakeNew.snakeCode));
        }
        //register map;
        snakeNew.battleGround=_bg;
        for (SnakeNode snakeNode : snakeNew.bodyvec) {
            snakeNew.battleGround.setMap(snakeNode.getI(),snakeNode.getJ(),snakeNode);
        }
        return snakeNew;
    }
    public static Snake CreateFromHole(int initLen,BattleGround _bg,int _snakePlayer) throws NoMoreHoleException {
        Snake snakeNew=new Snake(_snakePlayer);
        snakeNew.battleGround=_bg;
        for (int i = 0; i < initLen; i++) {
            snakeNew.bodyvec.add(new SnakeNode(0,0,snakeNew.snakeCode));
        }
        Hole holeRan=_bg.getRanHole();
        for (SnakeNode snakeNode : snakeNew.bodyvec) {
            snakeNode.setInHole(holeRan);
        }
        return snakeNew;
    }

    //move
    private void pushBodyForward(int i)
    {
        SnakeNode tmp= bodyvec.elementAt(i-1);
        SnakeNode bodyNow=bodyvec.elementAt(i);
        bodyNow.setI(tmp.getI());
        bodyNow.setJ(tmp.getJ());
        bodyNow.setInHole(tmp.getInHole());
        bodyNow.setOutHole(tmp.getOutHole());
    }
    private void _move() throws NoMoreHoleException,WrongPlaceException
    {
        //1 如果尾巴在洞内，并且马上要出洞了，那么取消占领，并且清除所有标记
        //2 如果尾巴马上就要进入洞了，那么对应洞口取消占领
        //3 整体移动
        //4 头如果在洞内，如果要出洞，那么占领，取消标记，
        //5 如果头马上就要进入洞了，那么对应洞口开始占领,并且标记洞

        //1
        if(getTail().isInHole()&&bodyvec.elementAt(bodyvec.size()-2).isInHole()==false)
        {
            bodyvec.elementAt(bodyvec.size()-2).getOutHole().setTaken(false);
            bodyvec.elementAt(bodyvec.size()-2).getOutHole().clearSnakeCode();
//            for (SnakeNode snakeNode : bodyvec) {
//                snakeNode.setOutHole(null);
//            }
        }
        //2
        if(getTail().isInHole()==false&&bodyvec.elementAt(bodyvec.size()-2).isInHole()==true) {
            bodyvec.elementAt(bodyvec.size() - 2).getInHole().setTaken(false);
            bodyvec.elementAt(bodyvec.size() - 2).getInHole().clearSnakeCode();
        }
        //3
        //3.0
        SnakeNode nodeHead=getHead();
        Hole nextHole=battleGround.getRanHole();
        if(cntWait>= GameCoreConstants.VALVE_WAIT)
        {
            System.out.println("go out of hole");

            nowDir=nextHole.getDir();
            nextDir=nowDir;
            Point pNext2=battleGround.getNextPoint(nextHole.getI(),nextHole.getJ(),nowDir);
            if(!battleGround.canWalkOnMap(pNext2.getI(),pNext2.getJ())&&nextHole.getSnakeCode()!=snakeCode)throw new WrongPlaceException(this,"_move:can not walk");
            if(battleGround.getMap(pNext2.getI(),pNext2.getJ()).getType().equals("Food"))
            {
                int nextI=pNext2.getI();
                int nextJ=pNext2.getJ();
                score++;
                SnakeNode snHeadBef=getHead();
                battleGround.eatFood(nextI,nextJ);
                bodyvec.add(0,new SnakeNode(nextI,nextJ,snakeCode));
                getHead().setInHole(null);
                getHead().setOutHole(nextHole);
                cntWait=0;
                nextHole.setTaken(true);
                nextHole.setSnakeCode(snakeCode);
                battleGround.setMap(nextI,nextJ,bodyvec.elementAt(0));
                //return new GameEvent(GameEvent.Status.EAT_FOOD,snakePlayer);
                return;
            }
        }
        for (int i = bodyvec.size() - 1; i >= 1; i--) {
            pushBodyForward(i);
        }
        //4
        nodeHead=getHead();
        if(cntWait>= GameCoreConstants.VALVE_WAIT)
        {
            System.out.println("go out of hole");
            nowDir=nextHole.getDir();
            nextDir=nowDir;
            Point pNext2=battleGround.getNextPoint(nextHole.getI(),nextHole.getJ(),nowDir);
            if(!battleGround.canWalkOnMap(pNext2.getI(),pNext2.getJ())&&nextHole.getSnakeCode()!=snakeCode)throw new WrongPlaceException(this,"_move:can not walk");
            nodeHead.setI(pNext2.getI());
            nodeHead.setJ(pNext2.getJ());
            cntWait=0;
            nodeHead.setInHole(null);
            nodeHead.setOutHole(nextHole);
            nextHole.setTaken(true);
            nextHole.setSnakeCode(snakeCode);
            return;
        }
        if(nodeHead.isInHole())
        {
            cntWait++;
            return ;
        }
        //5
        Point pNext3=battleGround.getNextPoint(nodeHead.getI(),nodeHead.getJ(),nowDir);
        int nextI=pNext3.getI();
        int nextJ=pNext3.getJ();
        if(!battleGround.canWalkOnMap(nextI,nextJ))throw new WrongPlaceException(this,"_move: can not walk");
        if(battleGround.getMap(nextI,nextJ).getType()=="Hole")
        {
            System.out.println("in hole");
            Hole hole=(Hole)battleGround.getMap(nextI,nextJ);
            nodeHead.setInHole(hole);
            hole.setTaken(true);
            hole.setSnakeCode(snakeCode);
            return;
        }
        nodeHead.setI(nextI);
        nodeHead.setJ(nextJ);
    }
    public GameEvent move()throws WrongPlaceException,NoMoreHoleException
    {
        //1 调整方向
        //2 判断下一个位置是否合法（包括地形和），否则抛出异常
        //3 如果是食物，头部添加一个单位
        //4 否则移动
        if(((nextDir&1)^(nowDir&1))!=0) {
            nowDir=nextDir;
        }
        SnakeNode nodeHead1=getHead();
        Point pNext=battleGround.getNextPoint(nodeHead1.getI(),nodeHead1.getJ(),nowDir);
        int nextI= pNext.getI();
        int nextJ= pNext.getJ();
        if(!battleGround.isInBorder(nextI,nextJ)){
            throw new WrongPlaceException(this,"move out of border");
        }
        else{
            if(!battleGround.canWalkOnMap(nextI,nextJ))
            {
                if(battleGround.getMap(nextI,nextJ).getType()=="Hole"&&((Hole)battleGround.getMap(nextI,nextJ)).getSnakeCode()!=snakeCode)
                {
                    throw new WrongPlaceException(this,"move to others' hole");
                }
            }
        }
        Obj nextObj=battleGround.getMap(nextI,nextJ);
        //register map;
        if(nextObj.getType().equals("Food"))
        {
            //头部长一个，并且在地图上更新
            score++;
            SnakeNode snHeadBef=getHead();
            battleGround.eatFood(nextI,nextJ);
            bodyvec.add(0,new SnakeNode(nextI,nextJ,snakeCode));
            getHead().setInHole(snHeadBef.getInHole());
            getHead().setOutHole(snHeadBef.getOutHole());
            battleGround.setMap(nextI,nextJ,bodyvec.elementAt(0));
            return new GameEvent(GameEvent.Status.EAT_FOOD,snakePlayer,new Point(nextI,nextJ));
        }
        //1 地图上尾巴去掉
        //2 整体移动
        battleGround.setMap(getTail().getI(), getTail().getJ(), new Blank(getTail().getI(), getTail().getJ()));
        _move();
        //register map;
        for (SnakeNode snakeNode : bodyvec) {
            if(!snakeNode.isInHole()) {
                battleGround.setMap(snakeNode.getI(), snakeNode.getJ(), snakeNode);
            }
        }
       // System.out.println("move");
        return new GameEvent(GameEvent.Status.MOVE,snakePlayer);
    }

    //die
    private void remove()
    {
        //1 将所有不在洞中的蛇身体抹去
        //2 将所有蛇占领的洞清除
        for (SnakeNode snakeNode : bodyvec) {
            int i=snakeNode.getI();
            int j=snakeNode.getJ();
            if(!snakeNode.isInHole()) {
                battleGround.setMap(i, j, new Blank(i, j));
            }
            else{
                snakeNode.getInHole().setTaken(false);
                snakeNode.getInHole().clearSnakeCode();
            }
            if(snakeNode.getOutHole()!=null)
            {
                snakeNode.getOutHole().setTaken(false);
                snakeNode.getOutHole().clearSnakeCode(); }

        }
        //triger something
        System.err.println("remove Should Triger Something!");
    }
    public void die()
    {
        remove();
        isDead=true;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
