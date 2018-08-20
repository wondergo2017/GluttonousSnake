package core.exception;

import core.obj.Obj;

/**
 * happens when requiring more space on the BattleGround, but there is no more
 */
public class NoMoreSpaceException extends Exception{
     public NoMoreSpaceException(String Mes)
    {
        super(Mes);
    }
}

