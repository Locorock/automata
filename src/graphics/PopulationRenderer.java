package graphics;

import enumLists.CellList;
import enumLists.GeneIds;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PopulationRenderer extends JPanel {
    ArrayList<JProgressBar> jbars = new ArrayList<> ();
    int[] traits;

    public PopulationRenderer(int[] traits) {
        this.setLayout (new GridLayout (32, 1));
        this.setSize (600, 50 * 32);
        this.traits = traits;
        for (int i = 0; i < 32; i++) {
            JProgressBar jp = new JProgressBar (SwingConstants.HORIZONTAL);
            jp.setPreferredSize (new Dimension (600, 50));
            jp.setMaximum (8);
            if (i < 9) {
                jp.setString (GeneIds.values ()[i].name ());
            } else {
                jp.setString (CellList.values ()[i - 9].name ());
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
            System.out.println (traits[i]);
        }
        this.repaint ();
    }
}