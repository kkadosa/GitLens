package hu.bme.mit.platform.db;

import java.util.List;

public class OrangoDatabase implements Database {

    @Override
    public Repository getRepository(String uriString) {
        return null;
    }

    @Override
    public List<String> getExtantPlugins() {
        return null;
    }
}
