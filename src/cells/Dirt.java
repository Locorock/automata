package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class Dirt extends Cell implements Solid {
    public Dirt(String type, Enviro enviro, String[] args) {
        super (type, enviro);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
