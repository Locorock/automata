package graphics;

import base.Cell;

import javax.swing.*;

public class PopupMenu extends JPopupMenu {
    public Cell c;
    JMenuItem critterInfo;

    public PopupMenu(MainGUI gui, Cell c) {
        this.c = c;
        critterInfo = new JMenuItem ("Critter Info");
        critterInfo.setName ("critterInfo");
        critterInfo.addActionListener (gui);
        add (critterInfo);
    }
}
