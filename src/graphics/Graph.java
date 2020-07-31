package graphics;

import base.World;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class Graph extends JPanel implements InfoPanel {
    XYSeries xy;
    XYSeriesCollection ds;
    ChartPanel panel;
    JFreeChart chart;
    World w;
    double[][] series;

    public Graph(String type, World w) {
        this.setPreferredSize (new Dimension (400, 100));
        this.setName (type);
        this.w = w;
        xy = new XYSeries ("turn");
        xy.add (0, 0);
        ds = new XYSeriesCollection ();
        ds.addSeries (xy);
        this.chart = ChartFactory.createXYLineChart (type, "turn", "deltaP", ds);
        this.panel = new ChartPanel (chart);
        this.add (panel);
    }

    public void refresh() {
        //double current = w.getCritters ().size ()-w.getT().lastPop;
        double current = w.getCritters ().size ();
        System.out.println ("Curr: " + current + " da: " + w.getT ().lastPop + " a " + w.getCritters ().size ());
        xy.add (xy.getItemCount (), current);
        panel.repaint ();
    }
}
