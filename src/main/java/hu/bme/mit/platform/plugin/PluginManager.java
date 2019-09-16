package hu.bme.mit.platform.plugin;

import hu.bme.mit.platform.Plugin;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
                String content = Files.readString(PLUGIN_PATH);
                JSONArray pluginArray = new JSONArray(content);
                List<URL> urls = new ArrayList<>();
                Map<String, List<String>> dependencyMap = new HashMap<>();
                for (int i = 0; i < pluginArray.length(); ++i) {
                    PluginDescriptor plugin = new PluginDescriptor();
                    plugin.url = new URL(pluginArray.getString(i));
                    urls.add(plugin.url);

                    URL jarURL = new URL("jar", "", plugin.url.toString() + "!/");
                    JarURLConnection juc = (JarURLConnection) jarURL.openConnection();
                    Attributes attributes = juc.getAttributes();
                    JSONObject context = new JSONObject(attributes.getValue(Keys.CONTEXT));
                    //TODO decide upon attributes
                    plugin.name = context.getString(Keys.PLUGIN_CLASS);
                    plugins.put(plugin.name, plugin);
                    JSONArray dependencies = context.getJSONArray(Keys.DEPENDENCIES);
                    List<String> list = new ArrayList<>();
                    for (int j = 0; j < dependencies.length(); ++j) {
                        String dependency = dependencies.getString(j);
                        list.add(dependency);
                    }
                    if (!list.isEmpty()) {
                        dependencyMap.put(plugin.name, list);
                    }
                }
                classLoader = new URLClassLoader((URL[]) urls.toArray());
                for (PluginDescriptor dependent : plugins.values()) {
                    for (String dependency : dependencyMap.get(dependent.name)) {
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
                        CompletableFuture.runAsync(new ActualLoader(plugin));
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


    private class ActualLoader implements Runnable {

        PluginDescriptor pluginDescriptor;

        ActualLoader(PluginDescriptor PluginDescriptor) {
            pluginDescriptor = PluginDescriptor;
        }

        @Override
        public void run() {
            try {
                Class<?> pluginClass = classLoader.loadClass(pluginDescriptor.name);
                Plugin plugin = (Plugin) pluginClass.getConstructor().newInstance();
                if (plugin.isGui()) {
                    CompletableFuture<String> f = new CompletableFuture<>();
                    GuiPlugin guiPlugin = new GuiPlugin(plugin, f);
                    f.get();
                } else {
                    plugin.load();
                }
                currentlyLoading.remove(pluginDescriptor);
                loadMap.remove(pluginDescriptor);
                for (PluginDescriptor plug : loadMap.keySet()) {
                    Set<PluginDescriptor> set = loadMap.get(plug);
                    set.remove(pluginDescriptor);
                    if (set.isEmpty()) {
                        currentlyLoading.add(plug);
                        CompletableFuture.runAsync(new ActualLoader(plug));
                    }
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | InterruptedException | ExecutionException e) {
                //TODO user viewable exception handling
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static class GuiPlugin implements Runnable {

        Plugin plugin;
        CompletableFuture<String> future;

        GuiPlugin(Plugin Plugin, CompletableFuture<String> Future) {
            plugin = Plugin;
            future = Future;
        }

        @Override
        public void run() {
            Platform.runLater(plugin::load);
            future.complete("Probably will be useful later.");
        }
    }

    private static class Keys {
        static final String CONTEXT = "Plugin-Context";
        static final String PLUGIN_CLASS = "Plugin-Class";
        static final String DEPENDENCIES = "Dependencies";
    }
}
