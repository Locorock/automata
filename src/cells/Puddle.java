package cells;

import base.Cell;
import base.Enviro;
import enumLists.CellList;

import java.util.Random;

public class Puddle extends LowWater {
    String previous;

    public Puddle(String type, Enviro enviro, String previous) {
        super (type, enviro);
        previous = previous;
    }

    @Override
    public void tick() {
        super.tick ();
        Random r = enviro.getR ();
        if ((r.nextInt () * enviro.getTemperature ()) % 1000 == 0 || CellList.valueOf (previous).isPermeable () && r.nextInt () % 100 == 0) {
            enviro.replaceWith (this, Cell.makeCell (previous, enviro));
        }
    }
}
