package hu.bme.mit.equalizer;

import hu.bme.mit.platform.Plugin;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class Equalizer extends AbstractVerticle implements Plugin {

    public static Router masterRouter;

    @Override
    public void load() {
        //TODO database button, client button, "update eagerly", "mostly one way", "level"
        Vertx.vertx().deployVerticle(this);
    }

    @Override
    public void unload() {
        //TODO unknown
    }

    @Override
    public boolean isGui() {
        return true;
    }

    @Override
    public void start() {
        Vertx vertx = Vertx.vertx();
        Router masterRouter = Router.router(vertx);
        masterRouter.get().handler(this::get);
        masterRouter.put().handler(this::put);
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(masterRouter).listen();
    }

    private void get(RoutingContext routingContext) {
        
    }

    private void put(RoutingContext routingContext) {
    }
}