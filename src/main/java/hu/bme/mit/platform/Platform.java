package hu.bme.mit.platform;

import hu.bme.mit.platform.management.PluginManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Platform extends Application {

    public static PluginManager pluginManager = new PluginManager();

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

        pluginManager.loadExtantPlugins();

        stage.show();
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
