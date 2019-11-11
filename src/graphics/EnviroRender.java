package graphics;

import base.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnviroRender extends JPanel {
    public Cell[][] grid;
    private int width;
    private float wUnit, hUnit;

    public EnviroRender(Cell[][] grid) {
        this.grid = grid;
        this.setSize (new Dimension (800, 800));
        this.width = grid.length;
        this.wUnit = (float) ((this.getWidth ()) / width);
        this.hUnit = (float) ((this.getHeight ()) / width);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent (graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setColor (Color.white);
        g.fillRect (0, 0, this.getWidth (), this.getHeight ());
        this.wUnit = (float) ((this.getWidth ()) / width);
        this.hUnit = (float) ((this.getHeight ()) / width);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                Color c = null;
                switch (grid[j][i].getType ()) {
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
                    default: {
                        c = Color.lightGray;
                    }
                }
                g.setColor (c);
                Rectangle2D r = new Rectangle (Math.round ((j * wUnit)), Math.round ((i * hUnit)), Math.round (wUnit), Math.round (hUnit));
                g.fill (r);
                g.setColor (Color.black);
                g.drawLine (0, this.getHeight () / 2, this.getWidth (), this.getHeight () / 2);
                g.drawLine (this.getWidth () / 2, 0, this.getWidth () / 2, this.getHeight ());
            }
        }
    }
}