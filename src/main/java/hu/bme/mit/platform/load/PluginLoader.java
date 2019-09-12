package hu.bme.mit.platform.load;

import org.json.JSONObject;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;

public class PluginLoader extends URLClassLoader {

    URL url;

    public PluginLoader(URL url) {
        super(new URL[]{url});
        this.url = url;
    }

    public JSONObject getPluginContext() throws IOException {
        URL u = new URL("jar", "", url + "!/");
        JarURLConnection uc = (JarURLConnection) u.openConnection();
        Attributes attr = uc.getAttributes();
        String t = attr.getValue("plugin_context");
        return new JSONObject(t);
    }
}
