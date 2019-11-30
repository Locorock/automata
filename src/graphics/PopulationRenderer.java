package graphics;

import base.Critter;
import enumLists.CellList;
import enumLists.GeneIds;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PopulationRenderer extends JPanel {
    ArrayList<JProgressBar> jbars = new ArrayList<> ();
    int[] traits;

    public PopulationRenderer(int[] traits, ArrayList<Critter> critters) {
        this.setLayout (new GridLayout (36, 1));
        this.setSize (600, 50 * 36);
        JLabel desc = new JLabel (critters.get (0).getActions ().toString ());
        this.add (desc);
        JScrollPane scrollPane = new JScrollPane (desc);
        scrollPane.setSize (600, 5);
        this.add (scrollPane);
        this.traits = traits;
        for (int i = 0; i < 35; i++) {
            JProgressBar jp = new JProgressBar (SwingConstants.HORIZONTAL);
            jp.setPreferredSize (new Dimension (600, 50));
            jp.setMaximum (256);
            if (i < 9) {
                jp.setString (GeneIds.values ()[i].name ());
                if (GeneIds.values ()[i].name ().equals ("DietType")) {
                    jp.setMaximum (6);
                }
                if (i < 3) {
                    jp.setMaximum (16);
                }
            } else {
                if (i < 32) {
                    jp.setString (CellList.values ()[i - 9].name ());
                } else {
                    if (i == 32) {
                        jp.setMaximum (100);
                        jp.setString ("Hunger");
                    }
                    if (i == 33) {
                        jp.setMaximum (100);
                        jp.setString ("Thirst");
                    }
                    if (i == 34) {
                        jp.setMaximum (1);
                        jp.setString ("Gender");
                    }
                }
            }
            jp.setStringPainted (true);
            jbars.add (jp);
            this.add (jp);
            this.render ();
        }
    }

    public void render() {
        for (int i = 0; i < jbars.size (); i++) {
            jbars.get (i).setValue (traits[i]);
        }
        this.repaint ();
    }
}