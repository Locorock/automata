package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class Sand extends Cell implements Solid {
    public Sand(String type, Enviro enviro, String[] args) {
        super (type, enviro);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
