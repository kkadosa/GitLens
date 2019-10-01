package hu.bme.mit.platform.data.impl;

import hu.bme.mit.platform.data.Database;
import hu.bme.mit.platform.data.Repository;

import java.util.List;

public class OrientDatabase implements Database {

    @Override
    public Repository getRepository(String uriString) {
        return null;
    }

    @Override
    public List<String> getExtantPlugins() {
        return null;
    }
}
