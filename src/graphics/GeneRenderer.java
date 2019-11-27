package graphics;

import critters.Critter;
import critters.GenCode;
import enumLists.CellList;
import enumLists.GeneIds;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GeneRenderer extends JPanel {
    ArrayList<JProgressBar> jbars = new ArrayList<> ();

    public GeneRenderer() {
        this.setLayout (new GridLayout (1, 32));
        for (int i = 0; i < 32; i++) {
            JProgressBar jp = new JProgressBar (SwingConstants.VERTICAL);
            jp.setPreferredSize (new Dimension (20, 600));
            jp.setMaximum (8);
            jbars.add (jp);
            this.add (jp);

        }
    }

    public void render(Critter c) {
        GenCode gc = c.getCode ();
        for (int i = 0; i < 9; i++) {
            jbars.get (i).setValue (gc.getGene (GeneIds.values ()[i].name ()).cardinality ());
        }
        for (int i = 0; i < CellList.values ().length - 1; i++) {
            jbars.get (i + 9).setValue (gc.getGene ("PropensionCluster").get (i * 8, i * 8 + 8).cardinality ());
        }
        this.repaint ();
    }


}
