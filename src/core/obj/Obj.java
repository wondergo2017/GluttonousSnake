package core.obj;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Obj implements Serializable  {
    //attributes
    protected String type;
    protected boolean crossable;
    private int i,j;
    public String getType(){ return type; }
    public boolean isCrossable(){ return crossable; }
    //func
    public Obj(int _i,int _j){
        i=_i;
        j=_j;
    }
    public Obj(){}
    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }
    public void setJ(int j) {
        this.j = j;
    }
    //debug
    public void debug()
    {
        System.out.print("I:"+getI()+" J:"+getJ()+" ");
    }
}

