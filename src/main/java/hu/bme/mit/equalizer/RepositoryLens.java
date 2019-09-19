package hu.bme.mit.equalizer;

import hu.bme.mit.equalizer.db.Repository;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.CompletableFuture;

public interface RepositoryLens {
    void get(Repository repo, JsonObject payload, CompletableFuture<String> out);
    void put(Repository repo, JsonObject payload, CompletableFuture<String> out);
}