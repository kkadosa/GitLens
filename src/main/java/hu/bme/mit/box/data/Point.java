package hu.bme.mit.box.data;

import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

public class Point {
    public String name;
    public String hash;
    public final JsonObject contents = new JsonObject();
    public final Set<String> incomingLines = new HashSet<>();
    public final Set<String> outgoingLines = new HashSet<>();
}
