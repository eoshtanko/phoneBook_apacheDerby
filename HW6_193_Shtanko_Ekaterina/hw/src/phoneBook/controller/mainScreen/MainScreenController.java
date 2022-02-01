package phoneBook.controller.mainScreen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import phoneBook.MainApp;
import phoneBook.controller.workWithContact.WorkWithContactController;
import phoneBook.controller.workWithFiles.FileExportController;
import phoneBook.controller.workWithFiles.FileImportController;
import phoneBook.model.Contact;
import phoneBook.controller.tools.DBStorage;

import java.util.Objects;


public class MainScreenController {

    @FXML
    TableView<Contact> table;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    MenuItem editMenuItem;
    @FXML
    MenuItem deleteMenuItem;

    /**
     * Индекс выбранного контакта в списке контактов. Доступ - package private. Это позволяет
     * обращаться к полю исключительно из пакета работы с главным экраном.
     */
    static int indexOfSelectedContactInContacts;

    /**
     * Список контактов. Доступ - package private. Это позволяет
     * обращаться к полю исключительно из пакета работы с главным экраном.
     */
    @FXML
    static ObservableList<Contact> contacts;

    public static ObservableList<Contact> getContacts() {
        return contacts;
    }

    public static void setContacts(ObservableList<Contact> val) {
        contacts = val;
    }

    /**
     * Строка поиска, через которое осуществляется поиск конатктов.
     */
    @FXML
    private TextField searchTextField;

    /**
     * Обращается к сохраненной при предыдущей сессии использования приложения
     * информации о состоянии списка контактов, заполняет ими таблицу.
     * "Необходимо и обязательно чтобы данные телефонной книги не терялись
     * при закрытии приложения. Это значит, что закрытие приложения не должно
     * терять уже введенные и сохраненные данные."
     * <p>
     * А также, устанавливает листнер к таблиуе, чтобы кнопки активации и удаления
     * были активны только при выбранном контакте.
     */
    @FXML
    private void initialize() {
        // активируем кнопки только при вбранном контакте
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null) {
                editButton.setDisable(false);
                deleteButton.setDisable(false);
                editMenuItem.setDisable(false);
                deleteMenuItem.setDisable(false);
            } else {
                editButton.setDisable(true);
                deleteButton.setDisable(true);
                editMenuItem.setDisable(true);
                deleteMenuItem.setDisable(true);
            }
        });
        // имя БД
        String dbName = "phoneBookDB";
        var savedContacts = DBStorage.getContactsCurrentState(dbName);
        contacts = FXCollections.observableArrayList(savedContacts);
        table.setItems(contacts);
    }

    /**
     * Открывает модальное окно добавления контакта.
     */
    @FXML
    private void addContactButtonAction() {
        var controller = WorkWithContactController.getController("Добавить контакт");
        controller.setActionWithContact(MainScreenLogic::addContact);
        controller.getWorkWithContactStage().showAndWait();
    }

    /**
     * Открывает модальное окно редактирования контакта.
     */
    @FXML
    private void editContactButtonAction() {
        int indexOfContactInTable = table.getSelectionModel().getSelectedIndex();
        indexOfSelectedContactInContacts = contacts.indexOf(table.getItems().get(indexOfContactInTable));

        var controller = WorkWithContactController.getController("Редактировать пользователя");
        controller.setActionWithContact(MainScreenLogic::editContact);
        controller.setCurrentContactState(table.getItems().get(indexOfContactInTable));
        controller.getWorkWithContactStage().showAndWait();
    }

    /**
     * Вызывает метод, ответственный за удаление.
     */
    @FXML
    private void deleteContactButtonAction() {
        int indexOfContactInTable = table.getSelectionModel().getSelectedIndex();
        indexOfSelectedContactInContacts = contacts.indexOf(table.getItems().get(indexOfContactInTable));
        MainScreenLogic.deleteContact();
    }

    /**
     * Действие кнопки "Выход". Выполняется выход из приложения.
     */
    @FXML
    private void exitButtonAction() {
        ((Stage) table.getScene().getWindow()).close();
    }

    /**
     * Открывает модальное окно импорта.
     */
    @FXML
    private void importFileButtonAction() {
        var controller = FileImportController.getController();
        Objects.requireNonNull(controller).getFileStage().showAndWait();
    }

    /**
     * Открывает модальное окно экспорта.
     */
    @FXML
    private void exportFileButtonAction() {
        var controller = FileExportController.getController();
        Objects.requireNonNull(controller).getFileStage().showAndWait();
    }

    /**
     * Действие кнопки "О щедевре".
     * Производит открытие окна,
     * отображающего данные о том, кто разработал данный шедевр.
     */
    @FXML
    private void showInfoButtonAction() {
        showAlertWindow(Alert.AlertType.INFORMATION,
                "О щедевере",
                "Разработчик: Штанько Екатерина БПИ193\nПростите!");
    }

    /**
     * Вызвает метод, ответственный за осуществление поиска.
     */
    @FXML
    private void searchButtonAction() {
        String searchText = searchTextField.getText();
        table.setItems(MainScreenLogic.search(searchText));
    }

    /**
     * Формирует и раскрывает окно типа Alert.
     *
     * @param type    тип окна
     * @param header  заголовок
     * @param content содержание
     */
    public static void showAlertWindow(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.initOwner(MainApp.getPrimaryStage());
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Формирует и раскрывает модальное окно.
     *
     * @param loader загрузчик интерфейса из fxml
     * @param title  заголовок
     * @return stage
     */
    public static Stage showModalWindow(FXMLLoader loader, String title) {
        try {
            Stage modalStage = new Stage();
            modalStage.setResizable(false);
            if (title != null) {
                modalStage.setTitle(title);
            }
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(MainApp.getPrimaryStage());
            modalStage.setScene(new Scene(loader.load()));
            return modalStage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}