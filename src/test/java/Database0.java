import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Database0 {
    public static void main(String[] args) throws IOException {
        File dir = new File("C:/neo4jDB/db");
        Files.createDirectories(dir.toPath());
        GraphDatabaseService graph = new GraphDatabaseFactory().newEmbeddedDatabase(dir);
        Runtime.getRuntime().addShutdownHook(new Thread(graph::shutdown));
        try (Transaction t = graph.beginTx()) {
            Node a = graph.createNode(() -> "Person");
            a.setProperty("name", "Bob");
            Node b = graph.createNode(() -> "Person");
            b.setProperty("name", "Greg");
            Relationship r = a.createRelationshipTo(b, () -> "Henge");
            r.setProperty("Wonp", "65777");
        }
        try (Transaction t = graph.beginTx()) {
            ResourceIterator<Node> n = graph.findNodes(() -> "Person");
            while (n.hasNext()) {
                Node d = n.next();
                System.out.println(d.getLabels().iterator().next());
            }
        }
    }
}
