package hu.bme.mit.box.plugin;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PluginDescriptor {
    final String className;
    URL url;
    boolean isLoaded = false;
    final Set<PluginDescriptor> dependencies = new HashSet<>();
    final Set<PluginDescriptor> dependents = new HashSet<>();
    final Set<PluginDescriptor> collaborators = new HashSet<>();

    public PluginDescriptor(String className) {
        this.className = className;
    }

    public boolean isLoadable() {
        boolean out = true;
        Iterator<PluginDescriptor> iterator = dependencies.iterator();
        while (iterator.hasNext() && out) {
            out = iterator.next().isLoaded;
        }
        return out;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(PluginDescriptor.class.isAssignableFrom(obj.getClass()))) {
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
