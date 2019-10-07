package hu.bme.mit.box.collector;

import hu.bme.mit.box.data.Repository;

import java.util.HashMap;
import java.util.Map;

public class LensManager {

    final Map<String, RepositoryLens> repoLensesByClassname = new HashMap<>();

    //TODO factory stuff
    public RepositoryLens get(Repository repo) {
        return null;
    }

    public void add(RepositoryLens lens){
        repoLensesByClassname.put(lens.getClass().getCanonicalName(), lens);
    }
}
