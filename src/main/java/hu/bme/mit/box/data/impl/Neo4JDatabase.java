package hu.bme.mit.box.data.impl;

import hu.bme.mit.box.data.Database;
import hu.bme.mit.box.data.Repository;
import io.vertx.core.AbstractVerticle;

import java.util.List;

public class Neo4JDatabase extends AbstractVerticle implements Database {

    @Override
    public Repository getRepository(String uriString) {
        return null;
    }

    @Override
    public List<String> getExtantPlugins() {
        return null;
    }
}
