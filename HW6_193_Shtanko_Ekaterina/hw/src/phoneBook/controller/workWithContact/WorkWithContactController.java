package phoneBook.controller.workWithContact;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import phoneBook.MainApp;
import phoneBook.controller.mainScreen.MainScreenController;
import phoneBook.model.Contact;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WorkWithContactController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField patronymicTextField;
    @FXML
    private TextField mobilePhoneTextField;
    @FXML
    private TextField homePhoneTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private DatePicker birthdayDatePicker;

    private int id;

    final private static String ERROR_MESS = "Некорректные данные!";

    private Stage workWithContactStage;

    private ActionWithContact actionWithContact;

    public Stage getWorkWithContactStage() {
        return workWithContactStage;
    }

    public void setWorkWithContactStage(Stage workWithContactStage) {
        this.workWithContactStage = workWithContactStage;
    }

    /**
     * Окна добавления контака и редактирования контакта практически не отличаются, кроме
     * логики действия при нажатии кнопики "Сохранить".
     * Поэтому, действие, производимое при нажатии кнопки, устаанвливается
     * через интерфейс.
     *
     * @param actionWithContact действие при нажатии кнопки "Сохранить"
     */
    public void setActionWithContact(ActionWithContact actionWithContact) {
        this.actionWithContact = actionWithContact;
    }

    /**
     * Действие при нажатии на кнопку "Сохранить".
     * В завимимости от установленного значения, выполняет сохранение либо
     * новосозданного контакта, либо отредактированного.
     */
    @FXML
    private void saveButtonAction() {
        if (validateName() & validateSurname() & validatePhone() & validateDate()) {
            try {
                actionWithContact.doSomeAction(getCurrentContactState());
            } catch (Exception e) {
                MainScreenController.showAlertWindow(Alert.AlertType.WARNING,
                        "Ошибка! Контакт с таким именем уже содержится в справочнике.",
                        "Необходимо, чтобы среди контактов не было таких, чьи имена совпадают.");
            }
            workWithContactStage.close();
        }
    }

    /**
     * Действие при нажатии на кнопку "Отмена".
     * Закрытие модального окна без сохранения изменений.
     */
    @FXML
    private void cancelButtonAction() {
        workWithContactStage.close();
    }

    /**
     * Получения контроллера.
     *
     * @param title заголовок(Редактировать/Добавить контакт)
     * @return контроллер
     */
    public static WorkWithContactController getController(String title) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/workWithContact.fxml"));
        var stage = MainScreenController.showModalWindow(loader, title);
        WorkWithContactController controller = loader.getController();
        controller.setWorkWithContactStage(stage);
        return controller;
    }

    /**
     * Возвращает текущее состояние полей контакта.
     *
     * @return Новый/отредатированный контакт.
     */
    private Contact getCurrentContactState() {
        Contact contact = new Contact();
        contact.setId(id);
        contact.setName(nameTextField.getText());
        contact.setSurname(surnameTextField.getText());
        contact.setPatronymic(patronymicTextField.getText());
        contact.setMobilePhone(mobilePhoneTextField.getText());
        contact.setHomePhone(homePhoneTextField.getText());
        contact.setAddress(addressTextField.getText());
        contact.setBirthday(birthdayDatePicker.getValue());
        contact.setNotes(noteTextArea.getText());
        return contact;
    }

    /**
     * Устанавливает полям соответствующие значения контакта.
     *
     * @param contact выбранный для редактирования контакт
     */
    public void setCurrentContactState(Contact contact) {
        id = contact.getId();
        nameTextField.setText(contact.getName());
        surnameTextField.setText(contact.getSurname());
        patronymicTextField.setText(contact.getPatronymic());
        mobilePhoneTextField.setText(contact.getMobilePhone());
        homePhoneTextField.setText(contact.getHomePhone());
        addressTextField.setText(contact.getAddress());
        birthdayDatePicker.setValue(contact.getBirthday());
        noteTextArea.setText(contact.getNotes());
    }

    /**
     * UI-состовляющая валидации имени.
     * Имя обязательно должно быть введено.
     *
     * @return true, если имя валидено, иначе - false
     */
    private boolean validateName() {
        String name = nameTextField.getText();
        if (WorkWithContactLogic.isValidNameOrSurname(name)) {
            removeErrorState(nameTextField);
            return true;
        }
        setErrorState(nameTextField);
        MainScreenController.showAlertWindow(Alert.AlertType.ERROR, ERROR_MESS, "Введите корректное имя.");
        return false;
    }

    /**
     * UI-состовляющая валидации фамилии.
     * Фамилия обязательно должна быть введена.
     *
     * @return true, если фамилия валидена, иначе - false
     */
    private boolean validateSurname() {
        String surname = surnameTextField.getText();
        if (WorkWithContactLogic.isValidNameOrSurname(surname)) {
            removeErrorState(surnameTextField);
            return true;
        }
        setErrorState(surnameTextField);
        MainScreenController.showAlertWindow(Alert.AlertType.ERROR, ERROR_MESS,
                "Введите корректную фамилию.");
        return false;
    }

    /**
     * UI-состовляющая валидации телефонов.
     * Должен быть введен хотя бы один номер.
     *
     * @return true, если телефоны валидены, иначе - false
     */
    private boolean validatePhone() {
        boolean result = true;
        String mobilePhoneText = mobilePhoneTextField.getText();
        String homePhoneText = homePhoneTextField.getText();
        String errorMess = "Номер телефона может начинаться с '+' и содержать от 2 до 12 цифр.";
        if (mobilePhoneText.isBlank() && homePhoneText.isBlank()) {
            MainScreenController.showAlertWindow(Alert.AlertType.ERROR, ERROR_MESS,
                    "Должна быть указана иформация хотя бы об одном номере телефона.");
            setErrorState(mobilePhoneTextField);
            return false;
        }
        if (!mobilePhoneText.isBlank()) {
            result = validateMobilePhone(mobilePhoneText, errorMess);
        }
        if (!homePhoneText.isBlank()) {
            result &= validateHomePhone(homePhoneText, errorMess);
        }
        return result;
    }

    /**
     * UI-составляющая валидации мобильного телефона.
     *
     * @return true, если телефон валиден, иначе - false
     */
    private boolean validateMobilePhone(String mobilePhoneText, String errorMess) {
        if (WorkWithContactLogic.isValidPhone(mobilePhoneText)) {
            removeErrorState(mobilePhoneTextField);
            return true;
        }
        MainScreenController.showAlertWindow(Alert.AlertType.ERROR, ERROR_MESS, errorMess);
        setErrorState(mobilePhoneTextField);
        return false;
    }

    /**
     * UI-составляющая валидации домашнего телефона.
     *
     * @return true, если телефон валиден, иначе - false
     */
    private boolean validateHomePhone(String homePhoneText, String errorMess) {
        if (WorkWithContactLogic.isValidPhone(homePhoneText)) {
            removeErrorState(homePhoneTextField);
            return true;
        }
        MainScreenController.showAlertWindow(Alert.AlertType.ERROR, ERROR_MESS, errorMess);
        setErrorState(homePhoneTextField);
        return false;
    }

    /**
     * UI-составляющая валидации даты.
     *
     * @return true, если дата валидна, иначе - false
     */
    private boolean validateDate() {
        String stringData = birthdayDatePicker.getEditor().getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate date = LocalDate.parse(stringData, formatter);
            birthdayDatePicker.setValue(date);
            if (WorkWithContactLogic.isValidBirthday(date)) {
                removeErrorState(birthdayDatePicker);
                return true;
            }
        } catch (Exception e) {
            if (stringData.isBlank()) {
                birthdayDatePicker.setValue(null);
                return true;
            }
        }
        setErrorState(birthdayDatePicker);
        MainScreenController.showAlertWindow(Alert.AlertType.ERROR, ERROR_MESS,
                "Введите корректную дату рождения\n(Формат: dd.MM.yyyy)");
        return false;
    }

    /**
     * Добавляет красную рамку, чтобы обозначить некорректные данные.
     *
     * @param control объект, к которому нужно добавить красную рамку.
     */
    private void setErrorState(Control control) {
        control.setStyle("-fx-border-color: #8B0000");
    }

    /**
     * Убирает красную рамку, если данные более не признаются
     * некорректными.
     *
     * @param control объект, с которого нужно снять красную рамку.
     */
    private void removeErrorState(Control control) {
        control.setStyle("");
    }
}
