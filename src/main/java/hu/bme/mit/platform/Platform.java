package hu.bme.mit.platform;

import hu.bme.mit.platform.db.Database;
import hu.bme.mit.platform.db.OrangoDatabase;
import hu.bme.mit.platform.plugin.PluginManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;

public class Platform extends AbstractVerticle {

    public static PluginManager pluginManager = new PluginManager();
    public static Vertx vertx;
    public static Database database;

    public Platform(){
        int cores = CpuCoreSensor.availableProcessors();
        VertxOptions op = new VertxOptions();
        op.setWorkerPoolSize(2*cores); //rethinking architecture; might be unnecessary
        vertx = Vertx.vertx(op);
        vertx.deployVerticle(this);
    }

    @Override
    public void start() {
        //base features;

        database = new OrangoDatabase();
        pluginManager.loadExtantPlugins();
    }

    public static void main(String[] args) {
        new Platform();
    }
}
