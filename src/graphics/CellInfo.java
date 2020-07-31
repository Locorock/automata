package graphics;

import base.Cell;

import javax.swing.*;
import java.util.ArrayList;

public class CellInfo extends JPanel implements InfoPanel {
    JLabel type, coo;
    ArrayList<JLabel> foods;
    Cell cell;

    public CellInfo(Cell cell) {
        this.cell = cell;
        type = new JLabel (cell.getType ());
        coo = new JLabel (cell.getAbsX () + " - " + cell.getAbsY ());
        this.add (type);
        this.add (coo);
        foods = new ArrayList<> ();
        refresh ();
    }

    public void refresh() {
        if (cell.getFoods () != null && cell.getFoods ().getFoodTypes () != null) {
            for (int i = 0; i < cell.getFoods ().getFoodTypes ().size (); i++) {
                int id = cell.getFoods ().getFoodTypes ().get (i);
                boolean found = false;
                for (JLabel jl : foods) {
                    if (jl.getName ().equals (id + "")) {
                        found = true;
                        jl.setText (cell.getFoods ().toString (i));
                    }
                }
                if (found == false) {
                    JLabel food = new JLabel (cell.getFoods ().toString (i));
                    food.setName (id + "");
                    foods.add (food);
                    this.add (food);
                }
            }
        }
    }
}
