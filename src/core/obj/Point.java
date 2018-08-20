package core.obj;

import java.io.Serializable;

public class Point implements Comparable,Serializable
{
    private int i,j;
    public Point(){}
    public Point(int _i,int _j){
        i=_i;
        j=_j;
    }
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

    @Override
    public int compareTo(Object o) {
        Point tmp=(Point)o;
        if(tmp.i==this.i||tmp.j==this.j) return 0;
        return (this.i<tmp.i)||((this.i==tmp.i)&&(this.j<tmp.j))?-1:1;
    }
}
