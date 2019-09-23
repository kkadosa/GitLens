package hu.bme.mit.platform;

import hu.bme.mit.platform.plugin.PluginManager;

public class Platform {

    public static PluginManager pluginManager = new PluginManager();

    public void start() {
        //TODO base features;

        pluginManager.loadExtantPlugins();
    }

    public static void main(String[] args) {
        new Platform().start();
    }
}
