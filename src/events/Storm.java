package events;

import base.Enviro;

public class Storm extends Precipitation {
    public Storm(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    @Override
    public void apply(int str, Enviro enviro) {
        super.apply (str, enviro);
        enviro.setLightningStr (enviro.getLightningStr () + str);
    }

    @Override
    public void remove(int str, Enviro enviro) {
        super.remove (str, enviro);
        enviro.setLightningStr (enviro.getLightningStr () - str);
    }
}