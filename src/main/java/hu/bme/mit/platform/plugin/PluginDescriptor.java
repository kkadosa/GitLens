package hu.bme.mit.platform.plugin;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class PluginDescriptor {
    String name;
    URL url;
    Set<PluginDescriptor> dependencies = new HashSet<>();
    Set<PluginDescriptor> dependents = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof PluginDescriptor)) {
            return false;
        } else {
            PluginDescriptor other = (PluginDescriptor) obj;
            return this.name.equals(other.name);
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
