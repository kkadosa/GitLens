package hu.bme.mit.platform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Curiosity extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Curiosity");
        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        TabPane banner = new TabPane();
        pane.setTop(banner);

        addFileTab(banner);
        //TODO other base features;



        stage.show();
    }

    private void loadAllPlugins() {
        try {
            String content = Files.readString(Paths.get("plugins.json"));
            JSONArray plugins = new JSONArray(content);
            Map<String, String> f = new HashMap<>();
            for(int i = 0; i < plugins.length(); ++i){
                JSONObject plugin = (JSONObject) plugins.get(i);
                String className = plugin.getString("class");
                String path = plugin.getString("path");
                f.put(className, path);
            }
            for(String k : f.keySet()){
                String v = f.get(k);

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void addFileTab(TabPane banner) {
        Tab fileTab = new Tab();
        fileTab.setText("File");
        fileTab.setClosable(false);
        banner.getTabs().add(fileTab);
        HBox fileMenus = new HBox();
        fileTab.setContent(fileMenus);
        Button addPluginButton = new Button("Add Plugin");
        fileMenus.getChildren().add(addPluginButton);
    }

    @Override
    public void stop() throws Exception {
        //TODO save plugin status
    }
}
