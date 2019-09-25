package hu.bme.mit.platform.plugin;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class PluginDescriptor {
    String className;
    URL url;
    Set<PluginDescriptor> dependencies = new HashSet<>();
    Set<PluginDescriptor> dependents = new HashSet<>();
    Set<PluginDescriptor> collaborators = new HashSet<>();

    public PluginDescriptor(String className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof PluginDescriptor)) {
            return false;
        } else {
            PluginDescriptor other = (PluginDescriptor) obj;
            return this.className.equals(other.className);
        }
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }
}
