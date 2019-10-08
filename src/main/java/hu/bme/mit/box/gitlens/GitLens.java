package hu.bme.mit.box.gitlens;

import hu.bme.mit.box.collector.Collector;
import hu.bme.mit.box.Plugin;

import java.util.Set;

class GitLens implements Plugin {

    @Override
    public void load(Set<String> expectedCollaborators) {
        Collector.lensManager.add(new GitSingleRepositoryLens());
    }

    @Override
    public void unload() {

    }
}