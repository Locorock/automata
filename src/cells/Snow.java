package cells;

import base.Cell;
import base.Critter;
import base.Enviro;
import baseCells.Solid;

public class Snow extends Cell implements Solid {
    public Snow(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.7);
    }
}
