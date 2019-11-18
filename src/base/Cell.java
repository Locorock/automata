package base;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public abstract class Cell {
    public String type;
    public Enviro enviro;
    protected Appearance code;
    protected double speedMod;
    protected Random r;
    protected int x, y;

    public Cell(String type, Enviro enviro, Appearance code, double speedMod) {
        this.type = type;
        this.enviro = enviro;
        this.r = enviro.getR ();
        this.code = code;
        this.speedMod = speedMod;
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

    public Appearance getCode() {
        return code;
    }

    public void setCode(Appearance code) {
        this.code = code;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getSpeedMod() {
        return speedMod;
    }

    public void setSpeedMod(double speedMod) {
        this.speedMod = speedMod;
    }

    public abstract void tick();
}
