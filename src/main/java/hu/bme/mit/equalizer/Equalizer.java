package hu.bme.mit.equalizer;

import hu.bme.mit.platform.Plugin;

public class Equalizer implements Plugin {

    @Override
    public void load() {
        //TODO database button, client button, "update eagerly", "mostly one way", "level"
    }

    @Override
    public void unload() {
        //TODO unknown
    }

    @Override
    public boolean isGui() {
        return true;
    }
}