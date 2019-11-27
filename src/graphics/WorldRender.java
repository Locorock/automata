package graphics;

import base.Cell;
import base.Enviro;
import base.World;
import baseCells.Food;
import baseCells.FreshWater;
import cells.SaltWater;
import critters.Critter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class WorldRender extends JPanel implements MouseMotionListener, MouseListener {
    private Enviro enviro;
    private int width;
    private float wUnit, hUnit;
    private World w;
    private JFrame f;
    private boolean init = false;
    private int xSelected;
    private int ySelected;
    private Rectangle2D select;

    public WorldRender(World w, JFrame f) {
        this.w = w;
        this.f = f;
        this.setSize (new Dimension (800, 800));
        this.wUnit = (float) this.getWidth () / (float) w.getFullWidth ();
        this.hUnit = (float) this.getHeight () / (float) w.getFullHeight ();
        if (wUnit < hUnit) {
            hUnit = wUnit;
        } else {
            wUnit = hUnit;
        }
        this.addMouseListener (this);
        this.addMouseMotionListener (this);
    }

    public void update(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent (graphics);
        Graphics2D g = (Graphics2D) graphics;
        if (!init) {
            g.setColor (Color.white);
            g.fillRect (0, 0, this.getWidth (), this.getHeight ());
            init = true;
        }
        this.wUnit = (float) this.getWidth () / (float) w.getFullWidth ();
        this.hUnit = (float) this.getHeight () / (float) w.getFullHeight ();
        if (wUnit < hUnit) {
            hUnit = wUnit;
        } else {
            wUnit = hUnit;
        }
        int prevx = 0;
        int prevy = 0;
        for (int i = 0; i < w.getFullHeight (); i++) {
            for (int j = 0; j < w.getFullWidth (); j++) {
                Cell current = w.getAbsCell (j, i);
                if (current.getUpdate () == false) {
                    //continue;
                }
                Color c = null;
                /*
                switch (current.getType ()) {
                    case "RiverWater": {
                        c = Color.decode ("#0077E0");
                        break;
                    }
                    case "BerryBush": {
                        c = Color.decode ("#CF4977");
                        break;
                    }
                    case "Bush": {
                        c = Color.decode ("#0CFF00");
                        break;
                    }
                    case "FruitTree": {
                        c = Color.red;
                        break;
                    }
                    case "TokenTree": {
                        c = Color.decode ("#4AB004");
                        break;
                    }
                    case "Cactus": {
                        c = Color.decode ("#0E4B00");
                        break;
                    }
                    case "Dirt": {
                        c = Color.decode ("#974A1C");
                        break;
                    }
                    case "LowGrass": {
                        c = Color.green;
                        break;
                    }
                    case "HighGrass": {
                        c = Color.yellow;
                        break;
                    }
                    case "Ice": {
                        c = Color.decode ("#7DCB8C");
                        break;
                    }
                    case "Lichen": {
                        c = Color.decode ("#165D24");
                        break;
                    }
                    case "RockySoil": {
                        c = Color.gray;
                        break;
                    }
                    case "LowWater": {
                        c = Color.blue;
                        break;
                    }
                    case "Puddle": {
                        c = Color.cyan;
                        break;
                    }
                    case "Snow": {
                        c = Color.white;
                        break;
                    }
                    case "Sand": {
                        c = Color.decode ("#FBFF81");
                        break;
                    }
                    case "SaltWater": {
                        c = Color.decode ("#5087FF");
                        break;
                    }
                    default: {
                        c = Color.lightGray;
                    }
                }
                */
                c = Color.black;
                if (current instanceof Food) {
                    c = getGreyscale ((int) Math.round (((Food) current).getFoodAmount (0)) * 8);
                }
                if (current instanceof FreshWater || current instanceof SaltWater) {
                    c = Color.blue;
                }
                for (Critter critter : new Vector<Critter> (w.getCritters ())) {
                    if (critter.getAbsx () == j && critter.getAbsy () == i) {
                        c = Color.red;
                    }
                }
                g.setColor (c);
                Rectangle2D r = new Rectangle (prevx, prevy, Math.round (wUnit), Math.round (hUnit));
                g.fill (r);
                prevx += Math.round (wUnit);
            }
            prevx = 0;
            prevy += Math.round (hUnit);
        }
        if (select != null) {
            g.setColor (Color.yellow);
            g.draw (select);
        }

    }

    public Color getGreyscale(int num) {
        float h = Color.RGBtoHSB (0, num, 0, null)[0];
        float s = Color.RGBtoHSB (0, num, 0, null)[1];
        float b = Color.RGBtoHSB (0, num, 0, null)[2];
        return Color.getHSBColor (h, s, b);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        xSelected = mouseEvent.getX ();
        ySelected = mouseEvent.getY ();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        Rectangle2D selection = new Rectangle (xSelected, ySelected, mouseEvent.getX (), mouseEvent.getY ());
        //EVALUATE CRITTERS IN SELECTED AREA
        System.out.println (selection.getBounds2D ());
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        select = new Rectangle2D.Double (xSelected, ySelected, -xSelected + mouseEvent.getX (), -ySelected + mouseEvent.getY ());
        repaint ();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}