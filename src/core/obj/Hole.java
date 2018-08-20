package core.obj;

public class Hole extends Obj {
    public Hole(int i, int j) { super(i,j);type="Hole"; crossable=true; }
    //dir
    private int dir=0;
    public void setDir(int _dir){dir=_dir   ;}
    public int getDir() {
        return dir;
    }
   //status
    private int snakeCode=-1;
    private boolean isTaken=false;
    public boolean isTaken() {
        return isTaken;
    }
    public void setTaken(boolean taken) {
        isTaken = taken;
        if (!taken) crossable = true;
        else crossable=false;

    }

    public int getSnakeCode() {
        return snakeCode;
    }

    public void setSnakeCode(int snakeCode) {
        this.snakeCode = snakeCode;
    }
    public void clearSnakeCode()
    {
        snakeCode=-1;
    }

}
