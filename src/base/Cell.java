package base;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public abstract class Cell {
    public String type;
    public Enviro enviro;
    protected boolean solid;
    protected Random r;

    public Cell(String type, Enviro enviro) {
        this.type = type;
        this.enviro = enviro;
        this.r = enviro.getR ();
    }

    public static Cell makeCell(String cellName, Enviro enviro) {
        Cell instance = null;
        try {
            instance = (Cell) Class.forName ("cells." + cellName).getDeclaredConstructor (new Class[]{String.class, Enviro.class}).newInstance (cellName, enviro);
        } catch (InstantiationException e) {
            e.printStackTrace ();
        } catch (IllegalAccessException e) {
            e.printStackTrace ();
        } catch (InvocationTargetException e) {
            e.printStackTrace ();
        } catch (NoSuchMethodException e) {
            e.printStackTrace ();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }
        return instance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Enviro getEnviro() {
        return enviro;
    }

    public void setEnviro(Enviro enviro) {
        this.enviro = enviro;
    }

    public abstract void tick();
}
