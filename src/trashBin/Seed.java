package trashBin;

import base.Critter;
import base.Enviro;
import baseCells.Living;
import baseCells.Solid;

public class Seed extends Living implements Solid {
    public Seed(String type, Enviro enviro) {
        super (type, enviro, -1);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
