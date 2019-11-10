package cells;

import base.Cell;
import base.Enviro;

public class Snow extends Cell {
    private String[] args;

    public Snow(String type, Enviro enviro, String[] args) {
        super (type, enviro);
        this.args = args;
    }
}
