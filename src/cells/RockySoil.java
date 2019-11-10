package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class RockySoil extends Cell implements Solid {

    public RockySoil(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
