package hu.bme.mit.platform.plugin;

import hu.bme.mit.platform.Plugin;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.jar.Attributes;

public class PluginManager {

    private static final Path PLUGIN_PATH = Paths.get("plugins.json");
    private Map<String, PluginDescriptor> plugins = new HashMap<>();
    private URLClassLoader classLoader;

    private Map<PluginDescriptor, Set<PluginDescriptor>> loadMap = Collections.synchronizedMap(new HashMap<>());
    private Set<PluginDescriptor> currentlyLoading = Collections.synchronizedSet(new HashSet<>());

    public void loadExtantPlugins() {
        try {
            if (Files.exists(PLUGIN_PATH)) {
                JsonArray pluginArray = new JsonArray(Files.readString(PLUGIN_PATH));
                List<URL> urls = new ArrayList<>();
                Map<String, List<String>> dependencyMap = new HashMap<>();
                for (int i = 0; i < pluginArray.size(); ++i) {
                    PluginDescriptor plugin = new PluginDescriptor();
                    plugin.url = new URL(pluginArray.getString(i));
                    urls.add(plugin.url);

                    URL jarURL = new URL("jar", "", plugin.url.toString() + "!/");
                    JarURLConnection jarUrlConnection = (JarURLConnection) jarURL.openConnection();
                    Attributes attributes = jarUrlConnection.getAttributes();
                    JsonObject context = new JsonObject(attributes.getValue(Keys.CONTEXT));
                    //TODO decide upon attributes
                    plugin.className = context.getString(Keys.PLUGIN_CLASS);
                    plugins.put(plugin.className, plugin);
                    JsonArray dependencies = context.getJsonArray(Keys.DEPENDENCIES);
                    List<String> list = new ArrayList<>();
                    for (int j = 0; j < dependencies.size(); ++j) {
                        String dependency = dependencies.getString(j);
                        list.add(dependency);
                    }
                    if (!list.isEmpty()) {
                        dependencyMap.put(plugin.className, list);
                    }
                }
                classLoader = new URLClassLoader((URL[]) urls.toArray());
                for (PluginDescriptor dependent : plugins.values()) {
                    for (String dependency : dependencyMap.get(dependent.className)) {
                        if (dependency != null) {
                            PluginDescriptor depended = plugins.get(dependency);
                            depended.dependents.add(dependent);
                            dependent.dependencies.add(depended);
                        }
                    }
                }

                for (PluginDescriptor plugin : plugins.values()) {
                    loadMap.put(plugin, new HashSet<>(plugin.dependencies));
                }
                for (PluginDescriptor plugin : plugins.values()) {
                    if (loadMap.get(plugin).isEmpty()) {
                        currentlyLoading.add(plugin);
                        ForkJoinPool.commonPool().submit(new ActualLoader(plugin));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void done(PluginDescriptor pluginDescriptor) {
    }


    private class ActualLoader extends RecursiveAction {

        PluginDescriptor pluginDescriptor;

        ActualLoader(PluginDescriptor PluginDescriptor) {
            pluginDescriptor = PluginDescriptor;
        }

        @Override
        protected void compute() {
            try {
                Class<?> pluginClass = classLoader.loadClass(pluginDescriptor.className);
                Plugin plugin = (Plugin) pluginClass.getConstructor().newInstance();
                plugin.load();
                currentlyLoading.remove(pluginDescriptor);
                loadMap.remove(pluginDescriptor);
                for (PluginDescriptor plug : loadMap.keySet()) {
                    Set<PluginDescriptor> set = loadMap.get(plug);
                    set.remove(pluginDescriptor);
                    if (set.isEmpty()) {
                        currentlyLoading.add(plug);
                        ForkJoinPool.commonPool().submit(new ActualLoader(plug));
                    }
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                //TODO user viewable exception handling
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static class Keys {
        static final String CONTEXT = "Plugin-Context";
        static final String PLUGIN_CLASS = "Plugin-Class";
        static final String DEPENDENCIES = "Dependencies";
    }
}
