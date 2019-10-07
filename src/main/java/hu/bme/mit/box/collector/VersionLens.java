package hu.bme.mit.box.collector;

import java.util.concurrent.CompletableFuture;

public interface VersionLens {
    void get(RepositoryLens repositoryLens, String PLACEHOLDER, CompletableFuture<String> out);
    void put(RepositoryLens repositoryLens, String PLACEHOLDER, CompletableFuture<String> out);
}
