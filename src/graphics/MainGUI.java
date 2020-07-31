package graphics;

import base.Cell;
import base.Critter;
import base.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainGUI implements KeyListener, ActionListener, WindowListener {
    private final World w;
    public ArrayList<InfoPanel> openRenders;
    public AdvancedWorldRenderer panel;
    public WorldMenuBar menu;
    private CountDownLatch latch;
    private JFrame frame;
    public int lastCycleTime;

    public MainGUI(World w) {
        this.w = w;
        openRenders = new ArrayList<InfoPanel> ();
        javax.swing.SwingUtilities.invokeLater (new Runnable () {
            public void run() {
                createAndShowGUI ();
            }
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame ();
        frame.setSize (new Dimension (w.getFullWidth () * 2, w.getFullHeight () * 2));
        frame.setVisible (true);
        menu = new WorldMenuBar (this);
        panel = new AdvancedWorldRenderer (w, frame);
        frame.setContentPane (panel);
        frame.setJMenuBar (menu);
        panel.repaint ();
        frame.addKeyListener (this);
    }

    public static String getFixedString(int original, int length) {
        String originalS = original + "";
        String result = "";
        if (originalS.length () > length) {
            return originalS + "";
        }
        for (int i = 0; i < 8 - originalS.length (); i++) {
            result += " ";
        }
        result += originalS;
        return result;
    }

    public void update(CountDownLatch latch) {
        this.latch = latch;
        panel.following.clear ();
        panel.repaint ();
        for (int i = 0; i < openRenders.size (); i++) {
            InfoPanel cr = openRenders.get (i);
            cr.refresh ();
        }

        menu.pop.setText (getFixedString (w.getCritters ().size (), 5));
        menu.fDeaths.setText (getFixedString (Critter.fDeaths, 4));
        menu.tDeaths.setText (getFixedString (Critter.tDeaths, 4));
        menu.aDeaths.setText (getFixedString (Critter.aDeaths, 4));
        menu.kDeaths.setText (getFixedString (Critter.kDeaths, 4));
        menu.ticks.setText (getFixedString (lastCycleTime, 4));
        latch.countDown ();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JComponent comp = (JComponent) actionEvent.getSource ();
        String name = comp.getName ();
        switch (name) {
            case "biomeView": {
                panel.setBiomeView ();
                break;
            }
            case "popView": {
                panel.setPopView ();
                break;
            }
            case "humView": {
                panel.setHumView ();
                break;
            }
            case "allView": {
                panel.switchAllView ();
                break;
            }
            case "heightView": {
                panel.setHeightView ();
                break;
            }
            case "critterInfo": {
                Cell c = ((PopupMenu) comp.getParent ()).c;
                CellInfo ci = new CellInfo (c);
                JFrame f = new JFrame ();
                f.addWindowListener (this);
                f.setSize (300, 400);
                f.setContentPane (ci);
                openRenders.add (ci);
                f.setVisible (true);
            }
        }
        panel.repaint ();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode ();
        System.out.println (key);
        if (key == KeyEvent.VK_C) {
            if (panel.getCellSelected () != null) {
                ArrayList<Critter> found = new ArrayList<Critter> ();
                for (Critter c : panel.getCellSelected ().enviro.getCritters ()) {
                    if (c.getAbsx () == panel.getCellSelected ().getAbsX () && c.getAbsy () == panel.getCellSelected ().getAbsY ()) {
                        found.add (c);
                    }
                }
                if (!found.isEmpty ()) {
                    JFrame f = new JFrame ();
                    f.addWindowListener (this);
                    f.setSize (300, 400);
                    CritterInfo cr = new CritterInfo (found, panel);
                    f.setContentPane (cr);
                    openRenders.add (cr);
                    f.setVisible (true);
                }

            }
        }
        if (key == KeyEvent.VK_G) {
            JFrame f = new JFrame ();
            f.addWindowListener (this);
            f.setSize (300, 400);
            Graph cr = new Graph ("PopDelta", w);
            f.setContentPane (cr);
            openRenders.add (cr);
            f.setVisible (true);
        }
        if (key == KeyEvent.VK_F) {
            w.getT ().loop = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            w.getT ().running = !w.getT ().running;
        }
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        System.out.println ("CLOSED");
        if (windowEvent.getSource () != this) {
            JPanel cr = (JPanel) ((JFrame) windowEvent.getSource ()).getContentPane ();
            openRenders.remove (cr);
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }


    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
}

