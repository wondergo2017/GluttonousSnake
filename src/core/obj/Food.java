package core.obj;

public class Food extends Obj {
    /**
     * to distinguish different foods
     * more features may be added
     * it is used for UI now;
     */
   private int differindex=0;
    public Food(int i, int j){ super(i,j);type="Food"; crossable=true;}

    public int getDifferindex() {
        return differindex;
    }
    public void setDifferindex(int differindex) {
        this.differindex = differindex;
    }
}
