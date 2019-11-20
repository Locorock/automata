package cells;

import base.Cell;
import base.Enviro;
import baseCells.LowWater;
import enumLists.CellList;

import java.util.Random;

public class Puddle extends LowWater {
    Cell previous;
    static final double speedMod = 0.5;
    public Puddle(String type, Enviro enviro, Cell previous) {
        super (type, enviro, speedMod);
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
