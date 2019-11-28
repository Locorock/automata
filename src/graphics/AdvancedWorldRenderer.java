package graphics;

import base.Cell;
import base.Enviro;
import base.World;
import cells.RiverWater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class AdvancedWorldRenderer extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private Enviro enviro;
    private int width;
    private int wUnit, hUnit;
    private double zoom = 1;
    private World w;
    private JFrame f;
    private boolean init = false;
    private int xSelected;
    private int ySelected;
    private Rectangle2D select;
    private int oldPosX = 0;
    private int oldPosY = 0;
    private int cameraPosX = 0;
    private int cameraPosY = 0;
    private int dragStartX = 0;
    private int dragStartY = 0;

    public AdvancedWorldRenderer(World w, JFrame f) {
        this.w = w;
        this.f = f;
        this.setSize (new Dimension (w.getMap ().size (), w.getMap ().get (0).size ()));
        this.wUnit = 32;
        this.addMouseListener (this);
        this.addMouseMotionListener (this);
        this.addMouseWheelListener (this);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent (graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.translate ((cameraPosX - dragStartX + oldPosX), (cameraPosY - dragStartY + oldPosY));
        System.out.println (zoom);
        g.scale (zoom, zoom);
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
                g.fill (r);
                g.setColor (Color.decode ("#0077E0"));
                if (currentEnviro.isRiver ()) {
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
            });
        });
        System.out.println ("Done");
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

    public int translateOnScale(int coordinate, boolean horizontal) {
        if (horizontal) {
            return (int) (coordinate - (this.getWidth () - (this.getWidth () / zoom)) / 2);
        } else {
            return (int) (coordinate - (this.getHeight () - (this.getHeight () / zoom)) / 2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            xSelected = translateOnScale (mouseEvent.getX (), true);
            ySelected = translateOnScale (mouseEvent.getY (), false);
        } else {
            dragStartX = translateOnScale (mouseEvent.getX (), true);
            dragStartY = translateOnScale (mouseEvent.getY (), false);
            cameraPosX = dragStartX;
            cameraPosY = dragStartY;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            Rectangle2D selection = new Rectangle (xSelected, ySelected, translateOnScale (mouseEvent.getX (), true), translateOnScale (mouseEvent.getY (), false));
            //EVALUATE CRITTERS IN SELECTED AREA
            System.out.println (selection.getBounds2D ());
        } else {
            cameraPosX = translateOnScale (mouseEvent.getX (), true);
            cameraPosY = translateOnScale (mouseEvent.getY (), false);
            oldPosX = oldPosX + cameraPosX - dragStartX;
            oldPosY = oldPosY + cameraPosY - dragStartY;
        }
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
            select = new Rectangle2D.Double (xSelected, ySelected, -xSelected + translateOnScale (mouseEvent.getX (), true), -ySelected + translateOnScale (mouseEvent.getY (), false));
        } else {
            cameraPosX = translateOnScale (mouseEvent.getX (), true);
            cameraPosY = translateOnScale (mouseEvent.getY (), false);
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
        cameraPosX = translateOnScale (cameraPosX, true);
        cameraPosY = translateOnScale (cameraPosY, false);
        oldPosX = translateOnScale (oldPosX, true);
        oldPosY = translateOnScale (oldPosY, false);
        dragStartX = translateOnScale (dragStartX, true);
        dragStartY = translateOnScale (dragStartY, false);
        repaint ();
    }
}