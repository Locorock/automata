package cells;

import base.Enviro;
import enumLists.CellList;

import java.util.Random;

public class Puddle extends LowWater {
    String previous;

    public Puddle(String type, Enviro enviro, String[] args) {
        super (type, enviro, args);
        previous = args[0];
    }

    @Override
    public void tick() {
        super.tick ();
        Random r = enviro.getR ();
        if ((r.nextInt () * enviro.getTemperature ()) % 1000 == 0 || CellList.valueOf (previous).isPermeable () && r.nextInt () % 100 == 0) {
            enviro.replaceWith (this, Enviro.makeCell (previous, enviro, null));
        }
    }
}
