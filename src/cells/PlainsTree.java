package cells;

import base.Enviro;
import baseCells.Tree;
import critters.Critter;

public class PlainsTree extends Tree {
    public PlainsTree(String type, Enviro enviro) {
        super (type, enviro, 0.8, true);
    }

    @Override
    public void tick() {
        if (alive) {
            super.tick ();
        }
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }
}
