package hu.bme.mit.equalizer;

import io.vertx.core.json.JsonObject;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

public interface RepositoryLens {
    void get(URI uri, JsonObject payload, CompletableFuture<String> out);
    void put(URI uri, JsonObject payload, CompletableFuture<String> out);
}