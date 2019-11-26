package cells;

import base.Enviro;
import baseCells.Tree;
import critters.Critter;

public class TaigaTree extends Tree {
    public TaigaTree(String type, Enviro enviro) {
        super (type, enviro, 0.5, true);
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
