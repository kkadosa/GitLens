package hu.bme.mit.gitlens;

import hu.bme.mit.collector.Collector;
import hu.bme.mit.platform.Plugin;

import java.util.Set;

class GitLens implements Plugin {

    @Override
    public void load(Set<String> expectedCollaborators) {
        Collector.lensManager.add(new GitRepositoryLens());
    }

    @Override
    public void unload() {

    }
}