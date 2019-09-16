package hu.bme.mit.equalizer;

import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

public interface RepositoryLens {
    void get(URI uri, JSONObject payload, CompletableFuture<String> out);
    void put(URI uri, JSONObject payload, CompletableFuture<String> out);
}
