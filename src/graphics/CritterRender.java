package graphics;

import base.Critter;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Vector;

public class CritterRender extends JPanel implements ItemListener, WindowStateListener {
    private final JLabel info;
    private final JComboBox selection;
    private final JLabel actions;
    private final JScrollPane scroll;

    public CritterRender(ArrayList<Critter> critters) {
        BoxLayout layout = new BoxLayout (this, BoxLayout.Y_AXIS);
        this.setLayout (layout);
        selection = new JComboBox (new Vector (critters));
        selection.addItemListener (this);
        this.add (selection);
        info = new JLabel ();
        this.add (info);
        actions = new JLabel ();
        scroll = new JScrollPane (actions);
        this.add (scroll);
        showInfo (critters.get (0));
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        Critter c = (Critter) itemEvent.getItem ();

    }

    public void showInfo(Critter c) {
        String info = "<html>" + c.getName () + "<br>";
        info += "Hunger: " + (float) c.getHunger () + " / " + c.getMaxHunger () + "<br>";
        info += "Thirst: " + (float) c.getThirst () + " / " + c.getMaxThirst () + "<br>";
        info += "Size: " + c.getSize () + "<br>";
        info += "DietType: " + c.getDietType () + "<br>";
        info += "BaseSpeed: " + c.getBaseSpeed () + "<br>";
        info += "Position: " + c.getAbsx () + " - " + c.getAbsy () + "<br>";
        info += "Last Action: " + c.getActions ().get (0) + "<br>";
        info += "Path: " + c.getPath () + "<br>";
        info += "</html>";
        this.info.setText (info);
        actions.setText ("<html>");
        for (int i = 0; i < c.getActions ().size (); i++) {
            actions.setText (actions.getText () + c.getActions ().get (i) + "<br>");
        }
        actions.setText (actions.getText () + "</html>");
    }

    public void refresh(ArrayList<Critter> following) {
        following.add ((Critter) selection.getSelectedItem ());
        showInfo ((Critter) selection.getSelectedItem ());
    }

    @Override
    public void windowStateChanged(WindowEvent windowEvent) {  //WONKY STUFF
        if (windowEvent.getNewState () == WindowEvent.WINDOW_CLOSED) {
            this.setVisible (false);
        }
    }
}
