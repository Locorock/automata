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

    public CritterRender(ArrayList<Critter> critters) {
        selection = new JComboBox (new Vector (critters));
        selection.addItemListener (this);
        this.add (selection);
        info = new JLabel ();
        this.add (info);
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
        info += "Size: " + null + "<br>";
        info += "Last Action: " + c.getActions ().getFirst () + "<br>";
        info += "</html>";
        this.info.setText (info);
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
