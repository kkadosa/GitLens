package hu.bme.mit.platform;

import hu.bme.mit.platform.data.Database;
import hu.bme.mit.platform.plugin.PluginManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;

public class Platform extends AbstractVerticle {

    public static PluginManager pluginManager = new PluginManager();
    public static Vertx vertx;
    public static Database database;
    public static LocalData localData;

    public Platform(String url, String username, String password) {
        int cores = CpuCoreSensor.availableProcessors();
        VertxOptions op = new VertxOptions();
        op.setWorkerPoolSize(2*cores); //rethinking architecture; might be unnecessary
        vertx = Vertx.vertx(op);
        vertx.deployVerticle(this);
    }

    @Override
    public void start() {
        //base features;

        localData = new LocalData();
        pluginManager.loadExtantPlugins();
    }

    public static void main(String[] args) {
        String username = args[0];
        String password = args[1];
        String url = null; //TODO default
        if(args.length > 2){
            url = args[2];
        }
        new Platform(url, username, password);
    }
}
