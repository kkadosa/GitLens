import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import hu.bme.mit.equalizer.db.Repository;

public class Test {
    public static void main(String[] args) {
        ArangoDB arango;
        ArangoDatabase db;
        ArangoGraph graph;
        arango = new ArangoDB.Builder().user("root").password("root").build();
        db = arango.db("Equilazer");
        if(!db.exists()){
            db.create();
        }
        ArangoCollection repositories = db.collection("Repositories");
        if(!repositories.exists()){
            repositories.create();
        }
        Repository r = new Repository();
        r.uri = "teve";
        r.url = "hal";
        r.user = "me";
        repositories.insertDocument(r);
    }
}
