package core;

import core.exception.NoMoreHoleException;
import core.exception.NoMoreSpaceException;
import core.obj.*;

import java.util.Vector;

public class BattleGround {
    //map
    private Obj map[][]=null;
    private int M,N;
    public int getM() {
        return M;
    }
    public int getN() {
        return N;
    }
    public boolean isInBorder(int i,int j)
    {
        return i>=0&&i<M&&j>=0&&j<N;
    }
    public boolean isTakenOnMap(int i,int j) { return !(map[i][j].getType()=="Blank"); }
    public boolean canWalkOnMap(int i,int j)
    {
        if(!isInBorder(i,j)) return false;
        Obj obj=getMap(i, j);
        return obj.isCrossable();

    }
    public BattleGround(int _M,int _N)//all will be init as blank
    {
        M=_M;
        N=_N;
        map=new Obj[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                map[i][j]=new Blank(i,j);
            }
        }
    }
    public  void setMap(int i,int j,Obj obj)
    {
        map[i][j]=obj;
    }
    public Obj getMap(int i,int j) { return map[i][j]; }
    /**
     * 不考虑洞的穿梭效应
     * @param i
     * @param j
     * @return
     */
    public Point getNextPoint(int i, int j, int dir)
    {
        Point pNext=new Point(i+GameCoreConstants.DirI[dir],j+GameCoreConstants.DirJ[dir]);
        if(pNext.getI()<0) pNext.setI(M-1);
        if(pNext.getJ()<0) pNext.setJ(N-1);
        if(pNext.getI()>=M) pNext.setI(0);
        if(pNext.getJ()>=N) pNext.setJ(0);
        return pNext;
    }

    //obj
    public Vector<Wall> wallVec=new Vector<Wall>();
    public Vector<Hole> holeVec=new Vector<Hole>();
    public Vector<Food> foodVec=new Vector<Food>();
    public int getFoodCnt()
    {
        return foodVec.size();
    }
    public void eatFood(int i,int j)
    {
        int index=0;
        for (int k = 0; k < foodVec.size(); k++) {
            Food foodTmp=foodVec.elementAt(k);
            if(foodTmp.getI()==i&&foodTmp.getJ()==j)
            {
                index=k;
                break;
            }
        }
        foodVec.remove(index);
    }

    //request space
    /**
     * @return the blank
     * @throws NoMoreSpaceException
     */
    public Point getRanBlank() throws NoMoreSpaceException
    {
        //1 collect all the blank
        //2 getRand in the vec
        //3 if no more blank throw the NoMoreSpaceExcetion
        Vector<Point> pVec=new Vector<Point>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if(map[i][j].getType()=="Blank")
                {
                    pVec.add(new Point(i,j));
                }
            }
        }
        if(pVec.size()<=0){
            throw new NoMoreSpaceException(getClass().toString());
        }
        int iRan=GameCoreConstants.getRanInt(pVec.size());
        return pVec.elementAt(iRan);
    }
    public Point getRanBlankWithNoNeighbour()throws NoMoreSpaceException
    {
        Vector<Point> points=getAllBlanksWithNoNeighbour();
        if(points.isEmpty())
        {
            throw  new NoMoreSpaceException(getClass().toString()) ;
        }
        return points.elementAt(GameCoreConstants.getRanInt(points.size()));
    }
    private Vector<Point>  getAllBlanksWithNoNeighbour()
    {
        boolean bitmap[][]=new boolean[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if(isTakenOnMap(i,j))
                {
                    bitmap[i][j]=true;
                    for (int dir : GameCoreConstants.DIRS) {
                        Point pNext=getNextPoint(i,j,dir);
                        bitmap[pNext.getI()][pNext.getJ()]=true;
                    }
                }
            }
        }
        Vector<Point> points=new Vector<>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if(bitmap[i][j]==false)
                {
                    points.add(new Point(i,j));
                }
            }
        }
        return points;
    }
    private Vector<Point> getRanBlankWithNoNeighbour(int cnt)throws NoMoreSpaceException
    {
        Vector<Point> points=getAllBlanksWithNoNeighbour();
        if(points.size()<cnt)
        {
            throw  new NoMoreSpaceException(getClass().toString());
        }
        //hack
        Vector<Point> pointRes=new Vector<>();
        for (int i = 0; i < cnt; i++) {
            int iPointToAdd=GameCoreConstants.getRanInt(points.size());
            pointRes.add(points.elementAt(iPointToAdd));
            points.removeElementAt(iPointToAdd);
        }
        return pointRes;
    }
    /**
     * @param cnt as the linelength
     * @param pVec for return, it will be cleared first.
     * @throws NoMoreSpaceException
     */
    public void getRanBlankLine(int cnt,Vector<Point> pVec) throws NoMoreSpaceException
    {
        //1. in 2 directions(horizontally and vertically),put all continuous blocks into the vec(larger than cnt)
        //2. random get the choice
        pVec.clear();
        boolean isFound=false;
        Vector<Vector<Point> > pLines=new Vector<>();
        pLines.addAll(getRanBlankLineInDir(cnt,GameCoreConstants.RIGHT));
        pLines.addAll(getRanBlankLineInDir(cnt,GameCoreConstants.DOWN));
        if(pLines.isEmpty())
        {
            throw new NoMoreSpaceException(getClass().toString());
        }
        else{
            Vector<Point> points= pLines.elementAt(GameCoreConstants.getRanInt(pLines.size()));
            //get the specific length.
            int index=GameCoreConstants.getRanInt(points.size()-cnt);
            for (int i = 0; i < cnt; i++) {
                pVec.add(points.elementAt(i+index));
            }
        }
    }
    private Vector<Vector<Point> > getRanBlankLineInDir(int cnt,int dir)
    {
        boolean bitmap[][]=new boolean[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if(isTakenOnMap(i,j))
                {
                    bitmap[i][j]=true;
                }
            }
        }
        Vector<Vector<Point> >pLines=new Vector<>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if(bitmap[i][j]==false)
                {
                    //getoneline
                    int iTmp=i;
                    int jTmp=j;
                    Vector<Point> points=new Vector<>();
                    while(isInBorder(iTmp,jTmp)&&bitmap[iTmp][jTmp]==false)
                    {
                        points.add(new Point(iTmp,jTmp));
                        bitmap[iTmp][jTmp]=true;
                        iTmp+=GameCoreConstants.DirI[dir];
                        jTmp+=GameCoreConstants.DirJ[dir];
                    }
                    if(points.size()>=cnt)
                    {
                        pLines.add(points);
                    }
                }
            }
        }
        return pLines;
    }
    //customization
    public void putWall(Wall wall)
    {
        wallVec.add(wall);
        setMap(wall.getI(),wall.getJ(),wall);
    }
    public void putRanSingleWalls(int cnt) throws NoMoreSpaceException
    {
        for (int i = 0; i < cnt; i++) {
            Point pTmp=getRanBlankWithNoNeighbour();
            putWall(new Wall(pTmp.getI(),pTmp.getJ()));
        }
    }
    /**
     * @param cnt as How many linewalls
     * @param length as how long is each linewall
     * @throws NoMoreSpaceException
     */
    public void putRanLineWalls(int cnt,int length)throws NoMoreSpaceException
    {
        for (int i = 0; i < cnt; i++) {
            Vector<Point> pVec=new Vector<Point>();
            getRanBlankLine(length,pVec);
            for (Point point : pVec) {
                putWall(new Wall(point.getI(),point.getJ()));
            }
        }
    }
    public void putFood(Food food)
    {
        food.setDifferindex(GameCoreConstants.getRanInt(GameCoreConstants.FOODNUM));
        foodVec.add(food);
        setMap(food.getI(),food.getJ(),food);
    }
    public void putRanFoods(int cnt)throws NoMoreSpaceException
    {
        Vector<Point> points=getRanBlankWithNoNeighbour(cnt);
        for (Point point : points) {
            putFood(new Food(point.getI(),point.getJ()));
        }
    }
    public void putHole(Hole hole)
    {
        holeVec.add(hole);
        setMap(hole.getI(),hole.getJ(),hole);
    }
    public void putRanHoles(int cnt)throws NoMoreSpaceException
    {
        Vector<Point> points=getRanBlankWithNoNeighbour(cnt);
        for (Point point : points) {
            putHole(new Hole(point.getI(),point.getJ()));
        }
    }
    public Hole getRanHole() throws NoMoreHoleException
    {
        Vector<Hole> tmpVec=new Vector<Hole>();
        for (Hole hole : holeVec) {
            if(!hole.isTaken())
            {
                tmpVec.add(hole);
            }
        }
        if(holeVec.isEmpty())
        {
            throw new NoMoreHoleException(getClass().toString());
        }
        return (tmpVec.elementAt(GameCoreConstants.getRanInt(tmpVec.size()-1)));
    }

}
