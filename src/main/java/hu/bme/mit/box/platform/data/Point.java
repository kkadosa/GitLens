package hu.bme.mit.box.platform.data;

import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

public class Point {
    public String name;
    public String hash;
    public JsonObject contents = new JsonObject();
    public Set<Line> incomingLines = new HashSet<>();
    public Set<Line> outgoingLines = new HashSet<>();
}
