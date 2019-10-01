package hu.bme.mit.equalizer;

import hu.bme.mit.platform.Platform;
import hu.bme.mit.platform.data.Repository;
import hu.bme.mit.platform.Plugin;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Equalizer extends AbstractVerticle implements Plugin {

    public static Router masterRouter;
    public static LensManager lensManager;

    @Override
    public void load(Set<String> expectedCollaborators) {
        vertx.deployVerticle(this);
    }

    @Override
    public void unload() {
        //TODO unknown
    }

    @Override
    public void start() {
        Vertx vertx = Vertx.vertx();
        masterRouter = Router.router(vertx);
        lensManager = new LensManager();
        masterRouter.get().handler(this::get);
        masterRouter.put().handler(this::put);
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(masterRouter).listen();
    }

    private void get(RoutingContext routingContext) {
        JsonObject payload =  routingContext.getBodyAsJson();
        String uriString = payload.getString("uri");
        Repository repo = Platform.database.getRepository(uriString);
        HttpServerResponse response = routingContext.response();
        if(repo.isValid){
            response.setStatusCode(200);
            response.end();
            RepositoryLens lens = lensManager.get(repo);
            CompletableFuture<String> future = new CompletableFuture<>();
            ForkJoinPool.commonPool().submit(new LensWrapper(lens, repo, payload, true, future));
        } else {
            response.setStatusCode(410);
            response.end();
        }
    }

    private void put(RoutingContext routingContext) {

    }

    private static class LensWrapper extends RecursiveAction {
        private Repository repo;
        private JsonObject payload;
        boolean get;
        RepositoryLens lens;
        CompletableFuture<String> future;

        private LensWrapper(RepositoryLens lens, Repository repository, JsonObject payload, boolean get, CompletableFuture<String> future){
            this.repo = repository;
            this.payload = payload;
            this.get = get;
            this.lens = lens;
            this.future = future;
        }

        @Override
        protected void compute() {
            if(get){
                lens.get(repo, payload, future);
            } else {
                lens.put(repo, payload, future);
            }
        }
    }
}