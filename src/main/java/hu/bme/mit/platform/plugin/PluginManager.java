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
                Map<String, PluginDescriptor> tempPlugins = new HashMap<>();
                List<URL> urls = new ArrayList<>();
                //Map<String, List<String>> dependencyMap = new HashMap<>();
                for (int i = 0; i < pluginArray.size(); ++i) {
                    try {
                        URL url = new URL(pluginArray.getString(i));
                        URL jarURL = new URL("jar", "", url.toString() + "!/");
                        JarURLConnection jarUrlConnection = (JarURLConnection) jarURL.openConnection();
                        Attributes attributes = jarUrlConnection.getAttributes();
                        JsonArray contexts = new JsonArray(attributes.getValue(Keys.CONTEXTS));
                        for (int j = 0; j < contexts.size(); ++j) {
                            JsonObject context = contexts.getJsonObject(j);
                            String className = context.getString(Keys.PLUGIN_CLASS);
                            PluginDescriptor plugin;
                            if (tempPlugins.containsKey(className)) {
                                plugin = tempPlugins.remove(className);
                            } else {
                                plugin = new PluginDescriptor(className);
                            }
                            plugins.put(plugin.className, plugin);
                            JsonArray dependencies = context.getJsonArray(Keys.DEPENDENCIES);
                            for(int k = 0; k < dependencies.size(); ++k){
                                String dName = dependencies.getString(k);
                                PluginDescriptor otherPlugin = plugins.get(dName);
                                if(otherPlugin == null){
                                    otherPlugin = tempPlugins.computeIfAbsent(dName, PluginDescriptor::new);
                                }
                                plugin.dependencies.add(otherPlugin);
                                otherPlugin.dependents.add(otherPlugin);
                            }
                            JsonArray collabs = context.getJsonArray(Keys.COLLABS);
                            for(int k = 0; k < collabs.size(); ++k){
                                String dName = collabs.getString(k);
                                PluginDescriptor otherPlugin = plugins.get(dName);
                                if(otherPlugin == null){
                                    otherPlugin = tempPlugins.computeIfAbsent(dName, PluginDescriptor::new);
                                }
                                plugin.collaborators.add(otherPlugin);
                            }

                        }
                        urls.add(url);
                    } catch (IOException e) { //Problems with the jar
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (
                IOException e) { //Problems with the file
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
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
        static final String CONTEXTS = "Plugin-Contexts";
        static final String PLUGIN_CLASS = "Plugin-Class";
        static final String DEPENDENCIES = "Dependencies";
        static final String COLLABS = "Collaborators";
    }
}
