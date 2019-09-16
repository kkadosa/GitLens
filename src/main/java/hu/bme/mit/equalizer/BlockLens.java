package hu.bme.mit.equalizer;

import java.util.concurrent.CompletableFuture;

public interface BlockLens {
    void get(VersionLens versionLens, Block from, CompletableFuture<String> out);
    void put(VersionLens versionLens, Block parent, CompletableFuture<String> out);
}