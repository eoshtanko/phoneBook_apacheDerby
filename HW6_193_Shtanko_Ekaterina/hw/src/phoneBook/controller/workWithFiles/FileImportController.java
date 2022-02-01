package phoneBook.controller.workWithFiles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import phoneBook.MainApp;
import phoneBook.controller.mainScreen.MainScreenController;
import phoneBook.controller.tools.ContactsSerializer;
import phoneBook.controller.tools.DBStorage;
import phoneBook.model.Contact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileImportController extends WorkWithFileController {

    /**
     * Список контактов, которые не были добавленны после импорта, так как
     * уже содержались в телефонной книге.
     */
    private final List<Contact> conflictContacts = new ArrayList<>();

    /**
     * Подтверждение импорта.
     * Вызывает метод, ответственный за логику импорта.
     */
    @Override
    @FXML
    void okButtonAction() {
        File file = new File(pathTextField.getText());
        try {
            importFile(file);
            if (!conflictContacts.isEmpty()) {
                showConflictsAfterImport(conflictContacts);
            }
        } catch (Exception e) {
            MainScreenController.showAlertWindow(Alert.AlertType.ERROR,
                    "Ошибка импорта файла.",
                    "При попытке импортровать контакты что-то пошло не так.");
        }
        fileStage.close();
    }

    /**
     * Открывает окно для выбора файла.
     */
    @Override
    @FXML
    void findPathButtonAction() {
        File file = fileChooser.showOpenDialog(getFileStage().getScene().getWindow());
        if (file != null) {
            pathTextField.setText(file.getPath());
        }
    }

    /**
     * Метода, реализующий логику импорта.
     * Производит проверку, не содержится ли уже контакт в списке,
     * и только в случае, если такого контакта еще нет - добавляет.
     * Если контакт уже существет - добавляет в список с коллизиями.
     *
     * @param file файл, из которого производится попытка импорта
     * @throws NullPointerException если deserialize вернул null
     */
    private void importFile(File file) throws NullPointerException {
        List<Contact> contactsFromFile = ContactsSerializer.deserialize(file);
        if (contactsFromFile != null) {
            for (var contact : contactsFromFile) {
                if (!MainScreenController.getContacts().contains(contact)) {
                    try {
                        DBStorage.addContact(contact);
                        MainScreenController.getContacts().add(contact);
                    } catch (Throwable ex) {
                        System.out.println("Ошибка при добавлении контакта в БД");
                    }
                } else {
                    conflictContacts.add(contact);
                }
            }
        }
    }

    /**
     * @return контроллер
     */
    public static FileImportController getController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/import.fxml"));
        var stage = MainScreenController.showModalWindow(loader, null);
        FileImportController controller = loader.getController();
        controller.setFileStage(stage);
        return controller;
    }

    /**
     * При попытки импортировать файл с уже имеющимися контаками, сообщает о
     * коллизиях.
     *
     * @param conflictContacts контакты, которые не были добавленны
     */
    private void showConflictsAfterImport(List<Contact> conflictContacts) {
        StringBuilder conflictContactsNames = new StringBuilder();
        for (var contact : conflictContacts) {
            conflictContactsNames.append(contact.getName()).append(" ").append(contact.getSurname()).append(" ").append(contact.getPhone()).append("\n");
        }
        MainScreenController.showAlertWindow(Alert.AlertType.INFORMATION,
                "Попытка добавить контакты, которые уже содержаться в телефонной книге.",
                "Следующие контакты не были добавлены:\n" + conflictContactsNames);
    }
}
