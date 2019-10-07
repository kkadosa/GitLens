package hu.bme.mit.box.gitlens;

import hu.bme.mit.box.collector.RepositoryLens;
import hu.bme.mit.box.platform.Platform;
import hu.bme.mit.box.platform.data.Repository;
import io.vertx.core.json.JsonObject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class GitRepositoryLens implements RepositoryLens {
    @Override
    public void get(Repository repository, JsonObject payload, CompletableFuture<String> out) {
        Path root = Platform.localData.getFolder("GitRepos");
        AtomicReference<String> primaryProject = new AtomicReference<>();
        repository.outgoingLines.forEach(line -> {
            if(line.name.equals("repository") && line.order.equals(0)){
                primaryProject.set(line.to.name);
            }
        });
        Path gitDir = root.resolve(repository.user).resolve(primaryProject.get()).resolve(".git");
        try {
            if (!Files.exists(gitDir)) {
                clone(gitDir, repository);
            }
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            org.eclipse.jgit.lib.Repository repo = builder.setGitDir(gitDir.toFile()).setMustExist(true).build();
            Git git = new Git(repo);
            git.fetch().call();
            repo.getRefDatabase();

        } catch (IOException | GitAPIException e) {
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
