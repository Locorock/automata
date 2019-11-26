package cells;

import base.Enviro;
import baseCells.Tree;
import critters.Critter;

public class ForestTree extends Tree {
    public ForestTree(String type, Enviro enviro) {
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
