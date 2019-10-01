package hu.bme.mit.collector;

import hu.bme.mit.platform.data.Repository;

import java.util.HashMap;
import java.util.Map;

public class LensManager {

    Map<String, RepositoryLens> repoLensesByClassname = new HashMap<>();

    //TODO factory stuff
    public RepositoryLens get(Repository repo) {
        return null;
    }

    public void add(RepositoryLens lens){
        repoLensesByClassname.put(lens.getClass().getCanonicalName(), lens);
    }
}
