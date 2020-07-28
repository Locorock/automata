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
    private double zoom = 2;
    private final JFrame f;
    private final boolean init = false;
    private int hUnit;
    private boolean biomeView = false;
    private int selectionOriginX = 0;
    private int selectionOriginY = 0;
    private int selectionEndX = 0;
    private int selectionEndY = 0;
    private Rectangle2D selection;
    private int oldPosX;
    private int oldPosY;
    private boolean toInit = true;
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
    public boolean spheed = false;
    private boolean showAll = false;
    private boolean humidityView = false;
    private AffineTransform transform = null;

    public AdvancedWorldRenderer(World w, JFrame f) {
        this.w = w;
        this.f = f;
        this.wUnit = Enviro.width;
        this.addMouseListener (this);
        this.addMouseMotionListener (this);
        this.addMouseWheelListener (this);
        System.out.println (oldPosX + " - " + oldPosY);
        repaint ();
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
        if (toInit) {
            oldPosX = this.getWidth () / 4;
            oldPosY = this.getHeight () / 4;
            toInit = false;
        }
        Graphics2D g = (Graphics2D) graphics;
        System.out.println (oldPosX);
        System.out.println (oldPosY);
        translate = AffineTransform.getTranslateInstance (translateOnScale (cameraPosX - dragStartX + oldPosX, true), translateOnScale (cameraPosY - dragStartY + oldPosY, false));
        scale = AffineTransform.getScaleInstance (zoom, zoom);
        scale.concatenate (translate);
        transform = scale;
        g.setTransform (transform);

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
                    if (humidityView) {
                        c = getGreyscale (currentEnviro.getHumidity () * 2.5);
                    } else {
                        c = getGreyscale (currentEnviro.getCritters ().size () * 30);
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
                                    r = new Rectangle (cell.getAbsX (), cell.getAbsY (), 1, 1);
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
                                    if (cell.equals (cellSelected)) {
                                        g.setColor (Color.magenta);
                                    }
                                    r = new Rectangle (cell.getAbsX (), cell.getAbsY (), 1, 1);
                                    g.fill (r);
                                }
                            }
                        }
                        if (!showAll) {
                            currentEnviro.getCritters ().forEach (critter -> {
                                if (critter.getSize () >= 0) {
                                    Rectangle2D re = new Rectangle (critter.getAbsx (), critter.getAbsy (), 1, 1);
                                    g.setColor (getRedScale (critter.getSize () * 100));
                                    g.fill (re);
                                }
                            });
                        }
                    }
                    if (showAll) {
                        currentEnviro.getCritters ().forEach (critter -> {
                            if (critter.getSize () >= 0) {
                                Rectangle2D re = new Rectangle (critter.getAbsx (), critter.getAbsy (), 1, 1);
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

    public Point getAbsolutePoint(Point p) {
        try {
            p.x = p.x - (int) (zoom / 2); //REMOVE HALF OF THE ZOOM VALUE TO FIX IMPRECISIONS IN THE REVERSED TRANSFORMATION
            p.y = p.y - (int) (zoom / 2);
            Point np = new Point ();
            transform.inverseTransform (p, np);
            System.out.println (np.toString ());
            return np;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (SwingUtilities.isRightMouseButton (mouseEvent) && mouseEvent.isControlDown ()) {
            Point p = new Point (mouseEvent.getX (), mouseEvent.getY ());
            System.out.println (p.toString ());
            p = getAbsolutePoint (p);
            System.out.println (p.toString ());
            cellSelected = w.getAbsCell (p.x, p.y);
            System.out.println (cellSelected);
        }
        if (SwingUtilities.isRightMouseButton (mouseEvent) && mouseEvent.isShiftDown ()) {
            biomeView = !biomeView;
        }
        if (SwingUtilities.isRightMouseButton (mouseEvent) && mouseEvent.isShiftDown () && mouseEvent.isAltDown ()) {
            spheed = !spheed;
        }
        if (SwingUtilities.isRightMouseButton (mouseEvent) && mouseEvent.isAltDown () && mouseEvent.isControlDown ()) {
            showAll = !showAll;
        }
        if (SwingUtilities.isRightMouseButton (mouseEvent) && mouseEvent.isAltGraphDown ()) {
            humidityView = !humidityView;
        }
    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            selectionOriginX = getAbsolutePoint (mouseEvent.getPoint ()).x;
            selectionOriginY = getAbsolutePoint (mouseEvent.getPoint ()).y;
            selectionEndX = selectionOriginX;
            selectionEndY = selectionOriginY;
            count = 0;
            for (Critter current : (TreeSet<Critter>) w.getCritters ().clone ()) {
                if (current.getAbsx () < selectionEndX && current.getAbsy () < selectionEndY && current.getAbsx () > selectionOriginX && current.getAbsy () > selectionOriginY) {
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
            selectionEndX = getAbsolutePoint (mouseEvent.getPoint ()).x;
            selectionEndY = getAbsolutePoint (mouseEvent.getPoint ()).y;
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
            selectionEndX = getAbsolutePoint (mouseEvent.getPoint ()).x;
            selectionEndY = getAbsolutePoint (mouseEvent.getPoint ()).y;
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
        zoom -= (double) mouseWheelEvent.getWheelRotation () / 2;
        if (zoom < 1) {
            zoom = 1;
        }
        renderThreshold = 5 + (this.getWidth () * this.getHeight ()) / 520000;
        repaint ();
    }
}