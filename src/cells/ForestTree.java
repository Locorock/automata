package cells;

import base.Critter;
import base.Enviro;
import baseCells.Tree;

public class ForestTree extends Tree {
    public ForestTree(String type, Enviro enviro) {
        super (type, enviro, 1.5, true);
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
