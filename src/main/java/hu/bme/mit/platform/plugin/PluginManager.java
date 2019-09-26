package hu.bme.mit.platform.plugin;

import hu.bme.mit.platform.Plugin;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.FileNotFoundException;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Attributes;
import java.util.stream.Stream;

@SuppressWarnings("SuspiciousMethodCalls")
public class PluginManager {

    //TODO Exception handling

    //TODO store the list of URLS DB
    private static final Path PLUGIN_PATH = Paths.get("plugins.json");
    private Map<String, PluginDescriptor> plugins = new HashMap<>();
    private ClassLoader classLoader;

    public void loadExtantPlugins() {
        try {
            if (Files.exists(PLUGIN_PATH)) {
                JsonArray pluginArray = new JsonArray(Files.readString(PLUGIN_PATH));
                Map<URL, JsonObject> urlToContext = new HashMap<>();
                AtomicBoolean consistent = new AtomicBoolean(true);
                pluginArray.stream().parallel().forEach(o -> {
                    try {
                        URL url = new URL((String) o);
                        URL jarURL = new URL("jar", "", url.toString() + "!/");
                        JarURLConnection jarUrlConnection = (JarURLConnection) jarURL.openConnection();
                        Attributes attributes = jarUrlConnection.getAttributes();
                        JsonArray contexts = new JsonArray(attributes.getValue(Keys.CONTEXTS));
                        contexts.stream().parallel().forEach(p -> urlToContext.put(url, (JsonObject) p));
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                });
                classLoader = new URLClassLoader((URL[]) urlToContext.keySet().toArray());
                urlToContext.keySet().stream().parallel().forEach(url -> {
                    JsonObject context = urlToContext.get(url);
                    String className = context.getString(Keys.PLUGIN_CLASS);
                    PluginDescriptor plugin = new PluginDescriptor(className);
                    plugin.url = url;
                    plugins.put(className, plugin);
                });
                plugins.values().parallelStream().forEach(plugin -> {
                    JsonObject context = urlToContext.get(plugin.url);
                    JsonArray dependencies = context.getJsonArray(Keys.DEPENDENCIES);
                    dependencies.stream().parallel().forEach(o -> {
                        PluginDescriptor depended = plugins.get(o);
                        if (depended != null) {
                            plugin.dependencies.add(depended);
                            depended.dependents.add(plugin);
                        } else {
                            consistent.set(false);
                        }
                    });
                });
                if (consistent.get()) {
                    plugins.values().stream().parallel().forEach(plugin -> {
                        JsonObject context = urlToContext.get(plugin.url);
                        JsonArray coops = context.getJsonArray(Keys.DEPENDENCIES);
                        coops.stream().parallel().forEach(o -> {
                            PluginDescriptor depended = plugins.get(o);
                            if (depended != null) {
                                plugin.collaborators.add(depended);
                            }
                        });
                    });
                    plugins.values().parallelStream().forEach(pluginDescriptor -> ForkJoinPool.commonPool().submit(new ActualLoader(pluginDescriptor)));
                } else {
                    throw new FileNotFoundException("PluginDependencies not Installed");
                }
            }
        } catch (IOException e) { //Problems with the file
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private class ActualLoader extends RecursiveAction{

        PluginDescriptor pluginDescriptor;

        public ActualLoader(PluginDescriptor pluginDescriptor) {
            this.pluginDescriptor = pluginDescriptor;
        }

        @Override
        protected void compute() {
            try {
                if (!pluginDescriptor.isLoaded) {
                    if (pluginDescriptor.isLoadable()) {
                        Class<?> pluginClass = classLoader.loadClass(pluginDescriptor.className);
                        Plugin plugin = (Plugin) pluginClass.getConstructor().newInstance();
                        Set<String> collabs = new HashSet<>();
                        pluginDescriptor.collaborators.parallelStream().forEach(plugDescriptor -> collabs.add(plugDescriptor.className));
                        plugin.load(collabs);
                        pluginDescriptor.isLoaded = true;
                        pluginDescriptor.dependents.parallelStream().forEach(depth -> ForkJoinPool.commonPool().submit(new ActualLoader(depth)));
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
