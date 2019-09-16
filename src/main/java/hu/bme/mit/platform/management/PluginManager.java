package hu.bme.mit.platform.management;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.Attributes;

public class PluginManager {

    private static final Path plugins = Paths.get("plugins.json");
    //Map<PluginProxy, Set<PluginProxy>> dependencies = new HashMap<>();
    private Set<PluginProxy> proxies = new HashSet<>();

    public void loadExtantPlugins() {
        try {
            if (Files.exists(plugins)) {
                String content = Files.readString(plugins);
                JSONArray plugins = new JSONArray(content);
                List<URL> urls = new ArrayList<>();
                for (int i = 0; i < plugins.length(); ++i) {
                    JSONObject plugin = (JSONObject) plugins.get(i);
                    PluginProxy proxy = new PluginProxy();
                    proxy.className = plugin.getString("class");
                    proxy.url = new URL(plugin.getString("baseURL"));
                    urls.add(proxy.url);
                    proxies.add(proxy);

                    URL jarURL = new URL("jar", "", proxy.url.toString() + "!/");
                    JarURLConnection juc = (JarURLConnection) jarURL.openConnection();
                    Attributes attrs = juc.getAttributes();

                }
                URLClassLoader classLoader = new URLClassLoader((URL[]) urls.toArray());
                for(PluginProxy proxy : proxies) {

                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static class PluginProxy {
        String className;
        URL url;
        Set<PluginProxy> dependencies = new HashSet<>();
        Set<PluginProxy> dependents = new HashSet<>();

        @Override
        public boolean equals(Object obj) {
            if(obj == this){
                return true;
            } else if (!(obj instanceof PluginProxy)){
                return false;
            } else {
                PluginProxy other = (PluginProxy) obj;
                return this.className.equals(other.className);
            }
        }

        @Override
        public int hashCode() {
            return className.hashCode();
        }
    }
}
