package core.obj;

public class SnakeNode extends Obj {
    private int snakeCode=0;
    public SnakeNode(int i, int j, int _snakeCode){ super(i,j);type="SnakeNode";crossable=false;snakeCode=_snakeCode;};
    private Hole inHole=null;
    private Hole outHole=null;

    public boolean isInHole()
    {
        return inHole!=null;
    }
    public Hole getInHole() {
        return inHole;
    }

    public void setInHole(Hole inHole) {
        this.inHole = inHole;
    }

    public Hole getOutHole() {
        return outHole;
    }

    public void setOutHole(Hole outHole) {
        this.outHole = outHole;
    }

    public int getSnakeCode() {
        return snakeCode;
    }
}
