package graphics;

import base.Critter;
import base.GenCode;
import base.GeneLibrary;
import enumLists.CellList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PopulationRenderer extends JPanel {
    ArrayList<JProgressBar> jbars = new ArrayList<> ();

    public PopulationRenderer(ArrayList<Critter> critters) {
        int size = GeneLibrary.getIndex ().size () + CellList.values ().length * 2 - 2;
        this.setLayout (new GridLayout (size / 3, 3));
        for (String name : GeneLibrary.getIndex ().keySet ()) {
            int[] index = GeneLibrary.searchIndex (name);
            if (index[2] != 3) {
                JProgressBar jp = new JProgressBar (SwingConstants.HORIZONTAL);
                jp.setPreferredSize (new Dimension (600, 50));

                jp.setStringPainted (true);
                jp.setString (name);
                int avg = 0;
                for (Critter c : critters) {
                    if (index[2] == 0) {
                        jp.setMaximum (index[1]);
                        avg += c.getCode ().getGene (name).cardinality ();
                    }
                    if (index[2] == 1) {
                        jp.setMaximum (index[1]);
                        avg += c.getCode ().getGene (name).cardinality ();
                    }
                    if (index[2] == 2) {
                        jp.setMaximum (256);
                        avg += GenCode.convert (c.getCode ().getGene (name));
                    }
                }
                avg /= critters.size ();
                jp.setValue (avg);
                jbars.add (jp);
                this.add (jp);
            } else {
                for (int i = 0; i < CellList.values ().length; i++) {
                    JProgressBar jp = new JProgressBar (SwingConstants.HORIZONTAL);
                    jp.setPreferredSize (new Dimension (600, 50));
                    jp.setMaximum (256);
                    jp.setStringPainted (true);
                    jp.setString (CellList.values ()[i].name ());
                    int avg = 0;
                    for (Critter c : critters) {
                        avg += GenCode.convert (c.getCode ().getGene (name).get (i * 8, i * 8 + 8));
                    }
                    avg /= critters.size ();
                    jp.setValue (avg);
                    jbars.add (jp);
                    this.add (jp);
                }
            }
        }
        JProgressBar jp = new JProgressBar (SwingConstants.HORIZONTAL);
        jp.setPreferredSize (new Dimension (600, 50));
        jp.setMaximum (100);
        jp.setStringPainted (true);
        jp.setString ("Hunger");
        jp.setValue ((int) critters.get (0).getHunger ());
        jbars.add (jp);
        this.add (jp);
        jp = new JProgressBar (SwingConstants.HORIZONTAL);
        jp.setPreferredSize (new Dimension (600, 50));
        jp.setMaximum (100);
        jp.setStringPainted (true);
        jp.setString ("Thirst");
        jp.setValue ((int) critters.get (0).getThirst ());
        jbars.add (jp);
        this.add (jp);
    }
}