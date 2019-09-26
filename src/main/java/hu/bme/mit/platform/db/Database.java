package hu.bme.mit.platform.db;

public interface Database {

    Repository getRepository(String uriString);
}
