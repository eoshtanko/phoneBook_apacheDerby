package phoneBook.controller.workWithFiles;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public abstract class WorkWithFileController {

    Stage fileStage;

    void setFileStage(Stage fileStage) {
        this.fileStage = fileStage;
    }

    public Stage getFileStage() {
        return fileStage;
    }

    /**
     * Текстовое поле для пути к файлу/директории.
     */
    @FXML
    TextField pathTextField;

    /**
     * Подтверждение импорта/экспорта.
     */
    @FXML
    abstract void okButtonAction();

    /**
     * Закрытие модального окна работы с файлами.
     */
    @FXML
    void cancelButtonAction() {
        fileStage.close();
    }

    /**
     * Открытие окна для поиска файла/диретории.
     */
    @FXML
    abstract void findPathButtonAction();

    FileChooser fileChooser = new FileChooser();
}
