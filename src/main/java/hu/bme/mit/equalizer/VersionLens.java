package hu.bme.mit.equalizer;

import java.util.concurrent.CompletableFuture;

public interface VersionLens {
    void get(RepositoryLens repositoryLens, String PLACEHOLDER, CompletableFuture<String> out);
    void put(RepositoryLens repositoryLens, String PLACEHOLDER, CompletableFuture<String> out);
}
