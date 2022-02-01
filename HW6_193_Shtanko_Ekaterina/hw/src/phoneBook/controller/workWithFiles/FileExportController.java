package phoneBook.controller.workWithFiles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import phoneBook.MainApp;
import phoneBook.controller.mainScreen.MainScreenController;
import phoneBook.controller.tools.ContactsSerializer;

import java.io.File;
import java.util.List;

public class FileExportController extends WorkWithFileController {

    /**
     * Подтверждение экспорта.
     */
    @Override
    @FXML
    void okButtonAction() {
        File file = new File(pathTextField.getText());
        try {
            ContactsSerializer.serialize(List.copyOf(MainScreenController.getContacts()), file);
        } catch (Exception e) {
            MainScreenController.showAlertWindow(Alert.AlertType.ERROR, "Ошибка при экспорте!",
                    "При экспорте что-то пошло не так.");
        }
        fileStage.close();
    }

    /**
     * Открытие окна для поиска дериктории для экспорта, и
     * установак выбранного пути в pathTextField
     */
    @Override
    @FXML
    void findPathButtonAction() {
        File path = fileChooser.showSaveDialog(MainApp.getPrimaryStage());
        if (path != null) {
            path = new File(path.getPath());
            pathTextField.setText(path.getPath());
        }
    }

    /**
     * @return контроллер
     */
    public static FileExportController getController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/export.fxml"));
        var stage = MainScreenController.showModalWindow(loader, null);
        FileExportController controller = loader.getController();
        controller.setFileStage(stage);
        return controller;
    }
}
