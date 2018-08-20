package core.exception;

import core.obj.Obj;

/**
 * goto wrongplace, like out of the border or uncrossable things
 */
public class WrongPlaceException extends Exception{
    private Obj obj;
    public WrongPlaceException(Obj _obj,String Mes)
    {
        super(Mes);
        obj=_obj;
    }

    public Obj getObj() {
        return obj;
    }
}
