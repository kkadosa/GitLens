package hu.bme.mit.platform;

import java.util.Set;

public interface Plugin {
    void load(Set<String> expectedCollaborators);
    void unload();
}