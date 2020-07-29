package graphics;

import base.Critter;
import base.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainGUI implements KeyListener, ActionListener, WindowListener {
    private final World w;
    private final ArrayList<CritterRender> openRenders;
    public AdvancedWorldRenderer panel;
    public WorldMenuBar menu;
    private CountDownLatch latch;
    private JFrame frame;

    public MainGUI(World w) {
        this.w = w;
        openRenders = new ArrayList<CritterRender> ();
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

    public void update(CountDownLatch latch) {
        this.latch = latch;
        panel.following.clear ();
        panel.repaint ();
        for (int i = 0; i < openRenders.size (); i++) {
            CritterRender cr = openRenders.get (i);
            cr.refresh (panel.following);
            System.out.println ("FRESHO");
        }
        latch.countDown ();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JComponent comp = (JComponent) actionEvent.getSource ();
        String name = comp.getName ();
        System.out.println (name);
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
                    CritterRender cr = new CritterRender (found);
                    f.setContentPane (cr);
                    openRenders.add (cr);
                    f.setVisible (true);
                }

            }
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
            CritterRender cr = (CritterRender) ((JFrame) windowEvent.getSource ()).getContentPane ();
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

