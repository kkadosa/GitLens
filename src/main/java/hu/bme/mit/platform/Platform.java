package hu.bme.mit.platform;

import hu.bme.mit.platform.plugin.PluginManager;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;

public class Platform {

    public static PluginManager pluginManager = new PluginManager();
    public static Vertx vertx;

    public Platform(){
        int cores = CpuCoreSensor.availableProcessors();
        VertxOptions op = new VertxOptions();
        op.setWorkerPoolSize(2*cores); //rethinking architecture; might be unnecessary
        vertx = Vertx.vertx(op);
    }

    public void start() {
        //TODO base features;

        pluginManager.loadExtantPlugins();
    }

    public static void main(String[] args) {
        new Platform().start();
    }
}
