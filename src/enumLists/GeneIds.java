package enumLists;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public enum GeneIds {
    AppearanceRecognition (16),
    AppearanceCluster (16),
    AppearanceTolerance (16),
    MateRate (8),
    WaterEff (8),
    FoodEff (8),
    BaseSpeed (8),
    Height (8),
    DietType (8),
    PropensionCluster (getClasses ("cells").length * 8);



    private final int offset, size;
    int off = 0;

    GeneIds(int size) {
        this.offset = off;
        this.size = size;
        off += size;

    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }

    private static Class[] getClasses(String packageName) {

        ClassLoader classLoader = Thread.currentThread ().getContextClassLoader ();

        assert classLoader != null;

        String path = packageName.replace ('.', '/');

        Enumeration resources = null;
        try {
            resources = classLoader.getResources (path);
        } catch (IOException e) {
            e.printStackTrace ();
        }

        List dirs = new ArrayList ();

        while (resources.hasMoreElements ()) {

            URL resource = (URL) resources.nextElement ();

            dirs.add (new File (resource.getFile ()));

        }

        ArrayList classes = new ArrayList ();

        for (Object directory : dirs) {

            try {
                classes.addAll (findClasses ((File) directory, packageName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }

        }

        return (Class[]) classes.toArray (new Class[classes.size ()]);
    }

    private static List findClasses(File directory, String packageName) throws ClassNotFoundException {

        List classes = new ArrayList ();

        if (!directory.exists ()) {

            return classes;

        }

        File[] files = directory.listFiles ();

        for (File file : files) {

            if (file.isDirectory ()) {

                assert !file.getName ().contains (".");

                classes.addAll (findClasses (file, packageName + "." + file.getName ()));

            } else if (file.getName ().endsWith (".class")) {

                classes.add (Class.forName (packageName + '.' + file.getName ().substring (0, file.getName ().length () - 6)));

            }

        }

        return classes;

    }
}
