package hu.bme.mit.box.gitlens;

import hu.bme.mit.box.collector.RepositoryLens;
import hu.bme.mit.box.Platform;
import hu.bme.mit.box.data.Repository;
import io.vertx.core.json.JsonObject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class GitSingleRepositoryLens implements RepositoryLens {
    @Override
    public void get(Repository repository, JsonObject payload, CompletableFuture<String> out) {
        try {
            Path root = Platform.localData.getFolder("GitRepos");
            Path localGitDir = root.resolve(repository.user).resolve(".git"); //TODO project split
            Platform.database.fillProjectTables();
//        AtomicReference<String> primaryProject = new AtomicReference<>();
//        repository.outgoingLines.parallelStream().forEach(line -> {
//            if(line.name.equals("repository") && line.order.equals(0)){
//                primaryProject.set(line.to.name);
//            }
//        });
//        try {
//            if (!Files.exists(gitDir)) {
//                clone(gitDir, repository);
//            }
//            FileRepositoryBuilder builder = new FileRepositoryBuilder();
//            org.eclipse.jgit.lib.Repository repo = builder.setGitDir(gitDir.toFile()).setMustExist(true).build();
//            RefDatabase refs = repo.getRefDatabase();
//            RevWalk walk = new RevWalk(repo);
//            refs.getRefs().parallelStream().forEach(ref -> {
//                try {
//                    walk.markStart(walk.parseCommit(ref.getObjectId()));
//                } catch (IOException e) {
//                    e.printStackTrace(); //missing its own branch, very likely...
//                }
//            });
//            walk.sort(RevSort.REVERSE);
//
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
