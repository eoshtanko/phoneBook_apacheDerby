package phoneBook;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import phoneBook.controller.tools.DBStorage;

public class MainApp extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } finally {
            DBStorage.shutDownDB();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setTitle("Записная книжка");
        Scene scene = new Scene(FXMLLoader.load(MainApp.class.getResource("view/mainScreen.fxml")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
