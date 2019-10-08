package hu.bme.mit.box.data;

import io.vertx.core.Verticle;

import java.util.List;

public interface Database extends Verticle {

    Repository getRepository(String uriString);
    List<String> getExtantPlugins();
    List<String> fillProjectTables();
}
