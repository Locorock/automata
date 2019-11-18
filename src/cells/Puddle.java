package cells;

import base.Appearance;
import base.Cell;
import base.Enviro;
import baseCells.LowWater;
import enumLists.CellList;

import java.util.Random;

public class Puddle extends LowWater {
    Cell previous;
    static final double speedMod = 0.5;
    static final Appearance code = new Appearance ("", "", "", "", "");
    public Puddle(String type, Enviro enviro, Cell previous) {
        super (type, enviro, code, speedMod);
        this.previous = previous;
    }

    @Override
    public void tick() {
        super.tick ();
        Random r = enviro.getR ();
        if ((r.nextInt () * enviro.getTemperature ()) % 1000 == 0 || CellList.valueOf (previous.getType ()).isPermeable () && r.nextInt () % 10 == 0) {
            enviro.replaceWith (this, previous);
        }
    }
}
