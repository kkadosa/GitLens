package hu.bme.mit.box;

import java.util.Set;

public interface Plugin {
    void load(Set<String> expectedCollaborators);
    void unload();
}