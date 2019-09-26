package hu.bme.mit.platform.db;

import java.util.List;

public interface Database {

    Repository getRepository(String uriString);
    List<String> getExtantPlugins();
}
