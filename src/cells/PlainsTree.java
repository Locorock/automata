package cells;

import base.Critter;
import base.Enviro;
import baseCells.Tree;

public class PlainsTree extends Tree {
    public PlainsTree(String type, Enviro enviro) {
        super (type, enviro, 1, true);
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
