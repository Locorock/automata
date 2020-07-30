package graphics;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class WorldMenuBar extends JMenuBar {
    private final JMenu overlays;
    private final JRadioButtonMenuItem biomeView;
    private final JRadioButtonMenuItem humidityView;
    private final JRadioButtonMenuItem popView;
    private final JRadioButtonMenuItem heightView;
    private final JCheckBoxMenuItem allView;
    private final MainGUI gui;

    public WorldMenuBar(MainGUI gui) {
        this.gui = gui;
        overlays = new JMenu ("Overlays");
        overlays.setMnemonic (KeyEvent.VK_O);
        this.add (overlays);

        overlays.addSeparator ();

        ButtonGroup group = new ButtonGroup ();

        biomeView = new JRadioButtonMenuItem ("Biome View");
        biomeView.setName ("biomeView");
        biomeView.setSelected (true);
        biomeView.setMnemonic (KeyEvent.VK_B);
        group.add (biomeView);
        overlays.add (biomeView);
        biomeView.addActionListener (gui);

        popView = new JRadioButtonMenuItem ("Population View");
        popView.setName ("popView");
        popView.setMnemonic (KeyEvent.VK_P);
        group.add (popView);
        overlays.add (popView);
        popView.addActionListener (gui);

        humidityView = new JRadioButtonMenuItem ("Humidity View");
        humidityView.setName ("humView");
        humidityView.setMnemonic (KeyEvent.VK_U);
        group.add (humidityView);
        overlays.add (humidityView);
        humidityView.addActionListener (gui);

        heightView = new JRadioButtonMenuItem ("Height View");
        heightView.setName ("heightView");
        heightView.setMnemonic (KeyEvent.VK_H);
        group.add (heightView);
        overlays.add (heightView);
        heightView.addActionListener (gui);

        overlays.addSeparator ();

        allView = new JCheckBoxMenuItem ("Critter View");
        allView.setName ("allView");
        allView.setMnemonic (KeyEvent.VK_A);
        overlays.add (allView);
        allView.addActionListener (gui);
    }
}
