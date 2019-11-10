package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class Ice extends Cell implements Solid {
    public Ice(String type, Enviro enviro, String[] args) {
        super (type, enviro);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
