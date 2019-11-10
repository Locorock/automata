package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class Snow extends Cell implements Solid {
    public Snow(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
