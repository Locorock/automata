package cells;

import base.Cell;
import base.Critter;
import base.Enviro;
import baseCells.Solid;

public class Dirt extends Cell implements Solid {
    public Dirt(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 1);
    }
}
