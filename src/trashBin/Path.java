package trashBin;

import base.Cell;

import java.util.ArrayList;

public class Path {
    double cost;
    ArrayList<Cell> path = new ArrayList<> ();

    public Path(Path oldPath, Cell newCell, double cost) {
        this.path = (ArrayList<Cell>) oldPath.getPath ().clone ();
        this.path.add (newCell);
        this.cost = oldPath.cost + cost;
    }

    public Path(Cell start) {
        path.add (start);
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public ArrayList<Cell> getPath() {
        return path;
    }

    public void setPath(ArrayList<Cell> path) {
        this.path = path;
    }

    public Cell getLast() {
        return path.get (path.size () - 1);
    }

    public boolean contains(Cell c) {
        for (Cell cell : path) {
            if (cell == c) {
                return true;
            }
        }
        return false;
    }
}
