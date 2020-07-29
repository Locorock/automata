package graphics;

import base.Cell;
import base.Critter;
import base.Enviro;
import base.World;
import baseCells.FreshWater;
import cells.RiverWater;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MacroOverlays {
    private static final int wUnit = Enviro.width;

    public static void call(Graphics g, World w, String code) {
        AdvancedWorldRenderer panel = w.gui.panel;
        switch (code) {
            case "allView":
                allView ((Graphics2D) g, w, panel);
                break;
            case "popView":
                popView ((Graphics2D) g, w, panel);
                break;
            case "biomeView":
                biomeView ((Graphics2D) g, w, panel);
                break;
            case "humView":
                humidityView ((Graphics2D) g, w, panel);
                break;
            case "microView":
                microView ((Graphics2D) g, w, panel);
        }
    }

    public static void microView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        g.setColor (Color.decode ("#0077E0"));
        g.fill (g.getClipBounds ());
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
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
                                if (cell.equals (panel.getCellSelected ())) {
                                    g.setColor (Color.magenta);
                                }
                                r = new Rectangle (cell.getAbsX (), cell.getAbsY (), 1, 1);
                                g.fill (r);
                            }
                        }
                    }
                    ((ArrayList<Critter>) currentEnviro.getCritters ().clone ()).forEach (critter -> {
                        if (critter.getSize () >= 0) {
                            Rectangle2D re = new Rectangle (critter.getAbsx (), critter.getAbsy (), 1, 1);
                            g.setColor (getRedScale (critter.getSize () * 100, Color.black));
                            if (w.gui.panel.following.contains (critter)) {
                                g.setColor (Color.yellow);
                            }
                            g.fill (re);
                        }
                    });
                }
            });
        });
    }

    public static void allView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                ((ArrayList<Critter>) currentEnviro.getCritters ().clone ()).forEach (critter -> {
                    if (critter.getSize () >= 0) {
                        Rectangle2D re = new Rectangle (critter.getAbsx (), critter.getAbsy (), 1, 1);
                        g.setColor (getRedScale (critter.getSize () * 100, Color.black));
                        if (w.gui.panel.following.contains (critter)) {
                            g.setColor (Color.yellow);
                        }
                        g.fill (re);
                    }
                });
            });
        });
    }

    public static void popView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        g.setColor (Color.black);
        g.fill (g.getClipBounds ());
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c;
                if (currentEnviro.getBiome () == "Ocean") {
                    c = Color.darkGray;
                } else {
                    c = Color.gray;
                }
                if (currentEnviro.getCritters ().size () > 1) {
                    c = getRedScale (currentEnviro.getCritters ().size (), c);
                }
                g.setColor (c);
                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
                    g.fill (r);
                    g.setColor (Color.decode ("#0077E0"));
                    if (currentEnviro.isRiver () && panel.getZoom () <= panel.getRenderTreshold ()) {
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
                }
            });
        });
    }

    public static void biomeView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        g.setColor (Color.decode ("#0077E0"));
        g.fill (g.getClipBounds ());
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c;
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
                    if (currentEnviro.isRiver () && panel.getZoom () <= panel.getRenderTreshold ()) {
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
                }
            });
        });
    }

    public static void humidityView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c = getGreyscale (currentEnviro.getHumidity () * 2.5);
                g.setColor (c);
                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
                    g.fill (r);
                    g.setColor (Color.decode ("#0077E0"));
                    if (currentEnviro.isRiver () && panel.getZoom () <= panel.getRenderTreshold ()) {
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
                }
            });
        });
    }

    public static Color getRedScale(double num, Color c) {
        float[] color = Color.RGBtoHSB ((int) (c.getRed () + num * 10), c.getGreen (), c.getBlue (), null);
        return Color.getHSBColor (color[0], color[1], (float) (color[2] - num / 30.0));
    }

    public static Color getGreyscale(double num) {
        float[] color = Color.RGBtoHSB (0, (int) num, 0, null);
        return Color.getHSBColor (color[0], color[1], color[2]);
    }

    public static boolean isOnScreen(Rectangle2D rect, Graphics2D g) {
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
}
