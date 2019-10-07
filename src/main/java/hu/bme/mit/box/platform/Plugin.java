package hu.bme.mit.box.platform;

import java.util.Set;

public interface Plugin {
    void load(Set<String> expectedCollaborators);
    void unload();
}