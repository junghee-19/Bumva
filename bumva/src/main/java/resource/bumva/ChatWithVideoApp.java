package resource.bumva;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatWithVideoApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
            getClass().getResource("/bumva/MainUI.fxml")
        );
        stage.setScene(new Scene(root));
        stage.setTitle("Chat + Video");
        stage.show();
    }
}