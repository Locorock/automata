package graphics;

import base.Cell;
import base.Enviro;
import base.World;
import baseCells.Food;
import baseCells.FreshWater;
import cells.RiverWater;
import critters.Critter;
import critters.GenCode;
import enumLists.CellList;
import enumLists.GeneIds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class AdvancedWorldRenderer extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private Enviro enviro;
    private int width;
    private int wUnit, hUnit;
    private double zoom = 1;
    private World w;
    private JFrame f;
    private boolean init = false;
    private int selectionOriginX = 0;
    private int selectionOriginY = 0;
    private int selectionEndX = 0;
    private int selectionEndY = 0;
    private Rectangle2D selection;
    private int oldPosX = 0;
    private int oldPosY = 0;
    private int cameraPosX = 0;
    private int cameraPosY = 0;
    private int dragStartX = 0;
    private int dragStartY = 0;
    private double renderThreshold = 5;
    public int totalAmount = 0;
    public double lastCycleTime = 0;
    private int count = 0;
    private AffineTransform translate;
    private AffineTransform scale;

    public AdvancedWorldRenderer(World w, JFrame f) {
        this.w = w;
        this.f = f;
        this.wUnit = 32;
        this.addMouseListener (this);
        this.addMouseMotionListener (this);
        this.addMouseWheelListener (this);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent (graphics);
        Graphics2D g = (Graphics2D) graphics;

        translate = AffineTransform.getTranslateInstance (translateOnScale (cameraPosX - dragStartX + oldPosX, true), translateOnScale (cameraPosY - dragStartY + oldPosY, false));
        scale = AffineTransform.getScaleInstance (zoom, zoom);
        g.transform (scale);
        g.transform (translate);
        g.setColor (Color.decode ("#0077E0"));
        g.fill (g.getClipBounds ());

        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c = null;
                switch (currentEnviro.getBiome ()) {
                    case "Ocean": {
                        c = Color.decode ("#0077E0");
                        break;
                    }
                    case "Forest": {
                        c = Color.decode ("#0CFF00");
                        break;
                    }
                    case "Plains": {
                        c = Color.decode ("#4AB004");
                        break;
                    }
                    case "Taiga": {
                        c = Color.decode ("#0E4B00");
                        break;
                    }
                    case "Tundra": {
                        c = Color.decode ("#974A1C");
                        break;
                    }
                    case "Jungle": {
                        c = Color.green;
                        break;
                    }
                    case "Savanna": {
                        c = Color.yellow;
                        break;
                    }
                    case "Arctic": {
                        c = Color.decode ("#7DCB8C");
                        break;
                    }
                    case "PrimSoup": {
                        c = Color.decode ("#A44A4A");
                        break;
                    }
                    case "Desert": {
                        c = Color.decode ("#5087FF");
                        break;
                    }
                    case "Wetland": {
                        c = Color.decode ("#42B260");
                        break;
                    }
                    case "Steppe": {
                        c = Color.decode ("#A7D87A");
                        break;
                    }
                    default: {
                        c = Color.lightGray;
                    }
                }
                g.setColor (c);

                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
                    g.fill (r);
                    g.setColor (Color.decode ("#0077E0"));
                    if (currentEnviro.isRiver () && zoom <= renderThreshold) {
                        Cell[][] grid = currentEnviro.getGrid ();
                        for (Cell[] row : grid) {
                            for (Cell cell : row) {
                                if (cell instanceof RiverWater) {
                                    r = new Rectangle (2 * cell.getAbsX (), 2 * cell.getAbsY (), Math.round (2), Math.round (2));
                                    g.fill (r);
                                }
                            }
                        }
                    }
                    if (currentEnviro.getBiome () != "Ocean" && zoom > renderThreshold) {
                        Cell[][] grid = currentEnviro.getGrid ();
                        for (Cell[] row : grid) {
                            for (Cell cell : row) {
                                g.setColor (Color.black);
                                if (cell instanceof Food) {
                                    g.setColor (Color.green);
                                }
                                if (cell instanceof FreshWater) {
                                    g.setColor (Color.blue);
                                }
                                r = new Rectangle (2 * cell.getAbsX (), 2 * cell.getAbsY (), 2, 2);
                                g.fill (r);
                            }
                        }
                    }
                }

            });
        });
        ((Vector<Critter>) w.getCritters ().clone ()).forEach (current -> {
            Rectangle2D r = new Rectangle (2 * current.getAbsx (), 2 * current.getAbsy (), 2, 2);
            g.setColor (Color.red);
            g.fill (r);
        });
        g.drawRect (selectionOriginX, selectionOriginY, selectionEndX - selectionOriginX, selectionEndY - selectionOriginY);
        g.drawString (String.valueOf (count), selectionOriginX, selectionOriginY);
        g.drawString (String.valueOf (totalAmount), (int) g.getClipBounds ().getX () + 120, (int) g.getClipBounds ().getY () + 40);
        g.drawString (String.valueOf (lastCycleTime), (int) g.getClipBounds ().getX () + 40, (int) g.getClipBounds ().getY () + 40);
    }

    public Color getGreyscale(int num) {
        float h = Color.RGBtoHSB (0, num, 0, null)[0];
        float s = Color.RGBtoHSB (0, num, 0, null)[1];
        float b = Color.RGBtoHSB (0, num, 0, null)[2];
        return Color.getHSBColor (h, s, b);
    }

    public int translateOnScale(int coordinate, boolean horizontal) {
        if (horizontal) {
            return (int) ((coordinate - (this.getWidth () - (this.getWidth () / zoom)) / 2));
        } else {
            return (int) ((coordinate - (this.getHeight () - (this.getHeight () / zoom)) / 2));
        }
    }

    public boolean isOnScreen(Rectangle2D rect, Graphics2D g) {
        if (g.getClipBounds ().contains (new Point ((int) rect.getX (), (int) rect.getY ()))) {
            return true;
        }
        if (g.getClipBounds ().contains (new Point ((int) (rect.getX () + rect.getWidth ()), (int) rect.getY ()))) {
            return true;
        }
        if (g.getClipBounds ().contains (new Point ((int) rect.getX (), (int) (rect.getY () + rect.getHeight ())))) {
            return true;
        }
        return g.getClipBounds ().contains (new Point ((int) (rect.getX () + rect.getWidth ()), (int) (rect.getY () + rect.getHeight ())));
    }

    public int getAbsoluteCoordinates(int coo, boolean horizontal) {
        if (horizontal) {
            return (int) (((-this.getWidth () / 2 + coo) / zoom) - oldPosX) + this.getWidth () / 2;
        } else {
            return (int) (((-this.getHeight () / 2 + coo) / zoom) - oldPosY) + this.getHeight () / 2;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            selectionOriginX = getAbsoluteCoordinates (mouseEvent.getX (), true);
            selectionOriginY = getAbsoluteCoordinates (mouseEvent.getY (), false);
            selectionEndX = selectionOriginX;
            selectionEndY = selectionOriginY;
            repaint ();
        } else {
            if (SwingUtilities.isMiddleMouseButton (mouseEvent)) {
                if (!w.getT ().isInterrupted ()) {
                    w.getT ().run ();
                } else {
                    w.getT ().interrupt ();
                }
            }
            dragStartX = mouseEvent.getX ();
            dragStartY = mouseEvent.getY ();
            cameraPosX = dragStartX;
            cameraPosY = dragStartY;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            selectionEndX = getAbsoluteCoordinates (mouseEvent.getX (), true);
            selectionEndY = getAbsoluteCoordinates (mouseEvent.getY (), false);
            count = 0;
            int[] traits = new int[32];
            Arrays.fill (traits, 0);
            for (Critter current : w.getCritters ()) {
                if (current.getAbsx () * wUnit / 16 < selectionEndX && current.getAbsy () * wUnit / 16 < selectionEndY && current.getAbsx () * wUnit / 16 > selectionOriginX && current.getAbsy () * wUnit / 16 > selectionOriginY) {
                    count++;
                    GenCode gc = current.getCode ();
                    for (int i = 0; i < 9; i++) {
                        traits[i] += gc.getGene (GeneIds.values ()[i].name ()).cardinality ();
                    }
                    for (int i = 0; i < CellList.values ().length - 1; i++) {
                        traits[i + 9] += gc.getGene ("PropensionCluster").get (i * 8, i * 8 + 8).cardinality ();
                    }
                }
            }
            for (int i = 0; i < traits.length; i++) {
                traits[i] /= count;
            }
            JFrame jf = new JFrame ();
            jf.setSize (600, 30 * 32);
            jf.setVisible (true);
            jf.add (new PopulationRenderer (traits));

        } else {
            oldPosX = oldPosX + cameraPosX - dragStartX;
            oldPosY = oldPosY + cameraPosY - dragStartY;
            cameraPosX = 0;
            cameraPosY = 0;
            dragStartX = 0;
            dragStartY = 0;
        }
        repaint ();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            selectionEndX = getAbsoluteCoordinates (mouseEvent.getX (), true);
            selectionEndY = getAbsoluteCoordinates (mouseEvent.getY (), false);
        } else {
            cameraPosX = mouseEvent.getX ();
            cameraPosY = mouseEvent.getY ();
        }
        repaint ();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        zoom -= (double) mouseWheelEvent.getWheelRotation () / 5;
        if (zoom < 1) {
            zoom = 1;
        }
        renderThreshold = 5 + (this.getWidth () * this.getHeight ()) / 520000;
        repaint ();
    }
}