package events;

import base.Enviro;
import base.Event;

public class Meteor extends Event {
    public Meteor(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    @Override
    public void apply(int str, Enviro enviro) {

    }

    @Override
    public void remove(int str, Enviro enviro) {

    }
}