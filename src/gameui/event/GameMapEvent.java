package gameui.event;

import core.obj.Food;
import core.obj.Hole;
import core.Snake;
import core.obj.Wall;

import java.util.Vector;

public class GameMapEvent extends GameEvent{
    public Vector<Wall> wallVec;
    public Vector<Hole> holeVec;
    public Vector<Food> foodVec;
    public Vector<Snake> snakes;
   public GameMapEvent(Vector<Wall> _wallVec,Vector<Hole> _holeVec,
           Vector<Food> _foodVec,
           Vector<Snake> _snakes)
    {
        wallVec=_wallVec;
        holeVec=_holeVec;
        foodVec=_foodVec;
        snakes=_snakes;
    }

    public void show()
    {
        System.out.println("wall"+wallVec.size());
        System.out.println("hole"+holeVec.size());
        System.out.println("food"+foodVec.size());
        System.out.println("snake"+snakes.size());
    }
}
