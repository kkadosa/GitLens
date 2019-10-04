package hu.bme.mit.gitlens;

import hu.bme.mit.collector.RepositoryLens;
import hu.bme.mit.platform.Platform;
import hu.bme.mit.platform.data.Repository;
import io.vertx.core.json.JsonObject;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class GitRepositoryLens implements RepositoryLens {
    @Override
    public void get(Repository repository, JsonObject payload, CompletableFuture<String> out) {
        Path root = Platform.localData.getFolder("GitRepos");
        Path gitDir = root.resolve(repository.user).resolve(repository.project).resolve(".git");
        org.eclipse.jgit.lib.Repository repo;
        try {
            if (!Files.exists(gitDir)) {
                clone(gitDir, repository);
            }
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            repo = builder.setGitDir(gitDir.toFile()).setMustExist(true).build();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            out.completeExceptionally(e);
        }
    }

    private void clone(Path gitDir, Repository repository) {
    }

    @Override
    public void put(Repository repo, JsonObject payload, CompletableFuture<String> out) {

    }
}
