package hu.bme.mit.platform;

public interface Plugin {
    void load();
    void unload();
    boolean isGui();
}