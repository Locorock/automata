package graphics;

import base.Cell;
import base.Critter;
import base.Enviro;
import base.World;
import baseCells.FreshWater;
import cells.RiverWater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

public class AdvancedWorldRenderer extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private Enviro enviro;
    private int width;
    private final int wUnit;
    private final World w;
    private double zoom = 1;
    private final JFrame f;
    private final boolean init = false;
    private int hUnit;
    private boolean biomeView = false;
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
    private Cell cellSelected;
    private CountDownLatch latch;
    private AffineTransform translate;
    private AffineTransform scale;

    public AdvancedWorldRenderer(World w, JFrame f) {
        this.w = w;
        this.f = f;
        this.wUnit = Enviro.width * 2;
        this.addMouseListener (this);
        this.addMouseMotionListener (this);
        this.addMouseWheelListener (this);
        validate ();
    }

    public static Color getGreyscale(double num) {
        float[] color = Color.RGBtoHSB (0, (int) num, 0, null);
        return Color.getHSBColor (color[0], color[1], color[2]);
    }

    public static Color getRedScale(double num) {
        float[] color = Color.RGBtoHSB ((int) num, 0, 0, null);
        return Color.getHSBColor (color[0], color[1], color[2]);
    }

    public void update(CountDownLatch latch) {
        this.latch = latch;
        this.repaint ();
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
                if (biomeView) {
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
                } else {
                    c = getGreyscale (currentEnviro.getCritters ().size () * 30);
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
                    if (zoom > renderThreshold) {
                        if (currentEnviro.getBiome () != "Ocean#") { // TEST #
                            Cell[][] grid = currentEnviro.getGrid ();
                            for (Cell[] row : grid) {
                                for (Cell cell : row) {
                                    g.setColor (Color.black);
                                    if (cell instanceof FreshWater) {
                                        g.setColor (Color.blue);
                                    }
                                    if (cell.getFoods () != null) {
                                        int amount = (int) (cell.getFoodAmount (0) * 15);
                                        if (cell.getFoodTypes ().size () > 1) {
                                            amount += (int) (cell.getFoodAmount (1) * 15);
                                        }
                                        g.setColor (getGreyscale (amount));
                                    }
                                    r = new Rectangle (2 * cell.getAbsX (), 2 * cell.getAbsY (), 2, 2);
                                    g.fill (r);
                                }
                            }
                        }
                        currentEnviro.getCritters ().forEach (critter -> {
                            if (critter.getSize () >= 0) {
                                Rectangle2D re = new Rectangle (2 * critter.getAbsx (), 2 * critter.getAbsy (), 2, 2);
                                g.setColor (getRedScale (critter.getSize () * 100));
                                g.fill (re);
                            }
                        });
                    }
                }
            });
        });

        g.setColor (Color.red);
        g.drawRect (selectionOriginX, selectionOriginY, selectionEndX - selectionOriginX, selectionEndY - selectionOriginY);
        g.setFont (g.getFont ().deriveFont ((float) (g.getFont ().getSize () / zoom)));
        g.drawString (String.valueOf (count), selectionOriginX, selectionOriginY);
        g.drawString (String.valueOf (totalAmount), (int) (g.getClipBounds ().getX () + 120 / zoom), (int) (g.getClipBounds ().getY () + 40 / zoom));
        g.drawString (String.valueOf (lastCycleTime), (int) (g.getClipBounds ().getX () + 40 / zoom), (int) (g.getClipBounds ().getY () + 40 / zoom));
        if (cellSelected != null) {
            g.drawString (String.valueOf (cellSelected.type), (int) (g.getClipBounds ().getX () + 40 / zoom), (int) (g.getClipBounds ().getY () + 80 / zoom));
            if (cellSelected.getFoods () != null) {
                g.drawString (cellSelected.getFoodAmount (0) + "", (int) (g.getClipBounds ().getX () + 120 / zoom), (int) (g.getClipBounds ().getY () + 80 / zoom));
                g.drawString (cellSelected.getFoods ().growthRates.get (0) + "", (int) (g.getClipBounds ().getX () + 480 / zoom), (int) (g.getClipBounds ().getY () + 80 / zoom));
            }
        }
        g.drawString (String.valueOf (Critter.fDeaths), (int) (g.getClipBounds ().getX () + 400 / zoom), (int) (g.getClipBounds ().getY () + 40 / zoom));
        g.drawString (String.valueOf (Critter.tDeaths), (int) (g.getClipBounds ().getX () + 440 / zoom), (int) (g.getClipBounds ().getY () + 40 / zoom));
        g.drawString (String.valueOf (Critter.aDeaths), (int) (g.getClipBounds ().getX () + 480 / zoom), (int) (g.getClipBounds ().getY () + 40 / zoom));
        g.drawString (String.valueOf (Critter.kDeaths), (int) (g.getClipBounds ().getX () + 520 / zoom), (int) (g.getClipBounds ().getY () + 40 / zoom));
        if (latch != null) {
            this.latch.countDown ();
        }

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
            return (int) ((((-this.getWidth () / 2.0 + coo) / zoom) - oldPosX) + (this.getWidth () / 2.0));
        } else {
            return (int) ((((-this.getHeight () / 2.0 + coo) / zoom) - oldPosY) + (this.getHeight () / 2.0));
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (SwingUtilities.isRightMouseButton (mouseEvent) && mouseEvent.isControlDown ()) {
            int x = getAbsoluteCoordinates (mouseEvent.getX (), true);
            int y = getAbsoluteCoordinates (mouseEvent.getY (), false);
            System.out.println ("Conversion");
            System.out.println (mouseEvent.getX () + " - " + mouseEvent.getY ());
            System.out.println (x + " - " + y);
            this.cellSelected = w.getAbsCell ((int) Math.round ((x - 1) / 2.0), (int) Math.round ((y - 1) / 2.0));
        }
        if (SwingUtilities.isRightMouseButton (mouseEvent) && mouseEvent.isShiftDown ()) {
            biomeView = !biomeView;
        }
    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            selectionOriginX = getAbsoluteCoordinates (mouseEvent.getX (), true);
            selectionOriginY = getAbsoluteCoordinates (mouseEvent.getY (), false);
            selectionEndX = selectionOriginX;
            selectionEndY = selectionOriginY;
            count = 0;
            for (Critter current : (TreeSet<Critter>) w.getCritters ().clone ()) {
                if (current.getAbsx () * 2 < selectionEndX && current.getAbsy () * 2 < selectionEndY && current.getAbsx () * 2 > selectionOriginX && current.getAbsy () * 2 > selectionOriginY) {
                    count++;
                }
            }
            System.out.println (count);
            repaint ();
        } else {
            if (SwingUtilities.isMiddleMouseButton (mouseEvent)) {
                w.getT ().running = !w.getT ().running;
            }
            dragStartX = mouseEvent.getX ();
            dragStartY = mouseEvent.getY ();
            cameraPosX = dragStartX;
            cameraPosY = dragStartY;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent) && !mouseEvent.isControlDown ()) {
            selectionEndX = getAbsoluteCoordinates (mouseEvent.getX (), true);
            selectionEndY = getAbsoluteCoordinates (mouseEvent.getY (), false);
            count = 0;
            int[] traits = new int[35];
            ArrayList<Critter> critters = new ArrayList<> ();
            Arrays.fill (traits, 0);
            for (Critter current : (TreeSet<Critter>) w.getCritters ().clone ()) {
                if (current.getAbsx () * 2 < selectionEndX && current.getAbsy () * 2 < selectionEndY && current.getAbsx () * 2 > selectionOriginX && current.getAbsy () * 2 > selectionOriginY) {
                    critters.add (current);
                }
            }
            count = critters.size ();
            JFrame jf = new JFrame ();
            jf.setLayout (new GridLayout (2, 1));
            jf.setSize (600, 30 * 32);
            jf.setVisible (true);
            jf.add (new PopulationRenderer (critters));
            JPanel jp = new JPanel ();
            jp.setLayout (new GridLayout (20, 2));
            jp.add (new JLabel (critters.get (0).name));
            ArrayDeque actions = critters.get (0).getActions ().clone ();
            while (!actions.isEmpty ()) {
                JLabel desc = new JLabel ((String) actions.removeFirst ());
                jp.add (desc);
                System.out.println (desc.getText ());
            }
            JLabel desc = new JLabel (String.valueOf (critters.get (0).getSize ()));
            jp.add (desc);
            JLabel desc2 = new JLabel (String.valueOf (critters.get (0).getAggressiveness ()));
            jp.add (desc2);
            jf.add (jp);

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