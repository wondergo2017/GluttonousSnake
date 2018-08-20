package core;

public class GameCoreConstants {
    //dirs
    public static final int UP=0;
    public static final int RIGHT=1;
    public static final int DOWN=2;
    public static final int LEFT=3;
    public static int DIRS[]={UP,RIGHT,DOWN,LEFT};
    public static int DirJ[]={0,1,0,-1};
    public static int DirI[]={-1,0,1,0};
    public static int toDir(int i,int j){
        for (int k = 0; k < 4; k++) {
            if(DirJ[k]==j&&DirI[k]==i) return k;
        }
        System.err.println("toDir Unconcerned!");
        return  -1;}

    public static int getRanDir()
    {
        return (int)(Math.random()*4);
    }

    //obj
    /**
     * num of distinguished foods, see more in food:differIndex
     */
    public static int FOODNUM=5;
    //simple func

    public static int getRanInt(int i){return (int )(Math.random()*i);}

    //battleground
    /**
     * Waiting time for freshing foods
     */
    public final static int FOOD_FRESH_VALVE =20;
    /**
     * count of foods put at a time
     */
    public final static int FOOD_ONE_TIME=2;
    public final static int INIT_FOOD_NUM=2;
    public final static int INIT_HOLE_NUM=10;
    public final static int INIT_WALL_NUM=5;
    public final static int INIT_WALL_LEN=5;

    //snake
    public final static int SNAKE_INIT_LEN=10;
    public final static int SNAKE_PLAYER1=0;
    public final static int SNAKE_PLAYER2=1;
    public final static int SNAKE_INIT_LIVES=5;
    /**
     * Time need for spending in hole
     */
    public static int VALVE_WAIT=20;


}

