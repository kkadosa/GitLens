package hu.bme.mit.box.collector;

import hu.bme.mit.box.platform.data.Repository;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.CompletableFuture;

public interface RepositoryLens {
    void get(Repository repo, JsonObject payload, CompletableFuture<String> out);
    void put(Repository repo, JsonObject payload, CompletableFuture<String> out);
}