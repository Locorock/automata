package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class Lava extends Cell implements Fluid {
    public Lava(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {
    }
}
