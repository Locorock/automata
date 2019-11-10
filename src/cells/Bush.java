package cells;

import base.Cell;
import base.Enviro;

import java.util.Random;

public class Bush extends Cell {
    Random r;

    public Bush(String type, Enviro enviro, String[] args) {
        super (type, enviro);
    }
}
