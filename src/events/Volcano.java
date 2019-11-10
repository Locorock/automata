package events;

import base.Enviro;
import base.Event;

public class Volcano extends Event {
    public Volcano(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    @Override
    public void apply(int str, Enviro enviro) {
        enviro.setEruptionS (enviro.getEruptionS () + str);
    }

    @Override
    public void remove(int str, Enviro enviro) {
        enviro.setEruptionS (enviro.getEruptionS () - str);

    }
}