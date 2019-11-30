package cells;

import base.Cell;
import base.Critter;
import base.Enviro;
import baseCells.Solid;

public class BirthingBoughs extends Cell implements Solid {
    int cooldown = 2;
    int elapsed = 0;

    public BirthingBoughs(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void tick() {
        if (enviro.getWorld ().getCritters ().size () < 30 && elapsed == 0) {
            elapsed = cooldown;
            enviro.getWorld ().getCritters ().add (new Critter ("Boughy", enviro.getWorld (), this.absX, this.absY));
        }
        if (elapsed > 0) {
            elapsed--;
        }
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.8);
    }
}
