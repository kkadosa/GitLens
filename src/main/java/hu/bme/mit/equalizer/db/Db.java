package hu.bme.mit.equalizer.db;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;

public class Db {
    //TODO database management

    ArangoDB arango;
    ArangoDatabase db;

    public Db() {
        arango = new ArangoDB.Builder().user("root").password("root").build();
        db = arango.db("Eq");
    }

    public Repository getRepository(String uri) {
        Repository t = new Repository();
        t.isValid = true;
        return t;
    }
}
