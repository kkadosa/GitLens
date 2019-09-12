package hu.bme.mit.platform;

import javafx.stage.Stage;

public interface Plugin {
    void load(Stage stage);
    void unload();
}