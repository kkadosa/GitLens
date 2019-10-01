package hu.bme.mit.gitlens;

import hu.bme.mit.collector.RepositoryLens;
import hu.bme.mit.platform.data.Repository;
import io.vertx.core.json.JsonObject;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.util.concurrent.CompletableFuture;

public class GitRepositoryLens implements RepositoryLens {
    @Override
    public void get(Repository repo, JsonObject payload, CompletableFuture<String> out) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
    }

    @Override
    public void put(Repository repo, JsonObject payload, CompletableFuture<String> out) {

    }
}
