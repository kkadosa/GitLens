package hu.bme.mit.gitlens;

import hu.bme.mit.equalizer.RepositoryLens;
import hu.bme.mit.platform.db.Repository;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.CompletableFuture;

public class GitRepositoryLens implements RepositoryLens {
    @Override
    public void get(Repository repo, JsonObject payload, CompletableFuture<String> out) {

    }

    @Override
    public void put(Repository repo, JsonObject payload, CompletableFuture<String> out) {

    }
}
