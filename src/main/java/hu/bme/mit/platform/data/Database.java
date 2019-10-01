package hu.bme.mit.platform.data;

import java.util.List;

public interface Database {

    Repository getRepository(String uriString);
    List<String> getExtantPlugins();
}
