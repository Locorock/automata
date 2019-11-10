package graphics;

import base.Enviro;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class TestRender extends JPanel {
    private ArrayList<ArrayList<Enviro>> grid;
    private int height, width;
    private float wUnit = 0, hUnit;

    public TestRender(ArrayList<ArrayList<Enviro>> grid) {
        this.grid = grid;
        this.setSize (new Dimension (800, 800));
        this.height = grid.size ();
        this.width = grid.get (0).size ();
        this.wUnit = (float) ((this.getWidth ()) / width) / 2;
        this.hUnit = wUnit;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent (graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setColor (Color.white);
        g.fillRect (0, 0, this.getWidth (), this.getHeight ());
        this.wUnit = (float) ((this.getWidth ()) / width) / 2;
        this.hUnit = (float) ((this.getHeight ()) / height) / 2;
        if (wUnit < hUnit) {
            hUnit = wUnit;
        } else {
            wUnit = hUnit;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < grid.get (i).size (); j++) {
                int width = grid.get (i).size ();
                if (grid.get (i).get (j) != null) {
                    g.setColor (Color.getHSBColor ((float) (grid.get (i).get (j).getTemperature () + 180) / 120, 1, 1)); //180-->360
                } else {
                    g.setColor (Color.white);
                }
                Rectangle2D r = new Rectangle (Math.round ((j * wUnit)), Math.round ((i * hUnit)), Math.round (wUnit), Math.round (hUnit));
                g.fill (r);
                if (grid.get (i).get (j) != null) {
                    g.setColor (Color.getHSBColor ((float) (grid.get (i).get (j).getHumidity () + 180) / 120, 1, 1)); //180-->360
                } else {
                    g.setColor (Color.white);
                }
                Rectangle2D r2 = new Rectangle (Math.round ((this.getWidth () / 2) + (j * wUnit)), Math.round ((i * hUnit)), Math.round (wUnit), Math.round (hUnit));
                g.fill (r2);
                if (grid.get (i).get (j) != null) {
                    g.setColor (Color.getHSBColor ((float) (grid.get (i).get (j).getAltitude () + 150) / 120, 1, 1)); //180-->360
                } else {
                    g.setColor (Color.white);
                }
                Rectangle2D r3 = new Rectangle (Math.round ((j * wUnit)), Math.round ((this.getHeight () / 2) + (i * hUnit)), Math.round (wUnit), Math.round (hUnit));

                g.fill (r3);
                if (grid.get (i).get (j) != null) {
                    if (grid.get (i).get (j).isRiver ()) {
                        g.setColor (Color.cyan);
                    } else {
                        g.setColor (Color.green);
                    }
                } else {
                    g.setColor (Color.white);
                }
                Rectangle2D r4 = new Rectangle (Math.round ((this.getWidth () / 2) + (j * wUnit)), Math.round ((this.getHeight () / 2) + (i * hUnit)), Math.round (wUnit), Math.round (hUnit));
                g.fill (r4);
                g.setColor (Color.black);
                g.drawLine (0, this.getHeight () / 2, this.getWidth (), this.getHeight () / 2);
                g.drawLine (this.getWidth () / 2, 0, this.getWidth () / 2, this.getHeight ());
            }
        }
        System.out.println (this.getHeight ());
        System.out.println (((double) this.getHeight () / this.height) * this.height);
    }
}
