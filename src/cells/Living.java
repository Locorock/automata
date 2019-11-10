package cells;

import base.Cell;
import base.Enviro;

public class Living extends Cell {
    protected boolean alive;

    public Living(String type, Enviro enviro, String[] args) {
        super (type, enviro);
    }

}
