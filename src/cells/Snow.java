package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class Snow extends Cell implements Solid {
    private String[] args;

    public Snow(String type, Enviro enviro, String[] args) {
        super (type, enviro);
        this.args = args;
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
