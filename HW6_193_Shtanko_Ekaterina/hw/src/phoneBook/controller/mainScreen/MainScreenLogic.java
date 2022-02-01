package phoneBook.controller.mainScreen;

import javafx.collections.ObservableList;
import phoneBook.controller.tools.DBStorage;
import phoneBook.model.Contact;

public class MainScreenLogic {

    /**
     * Метод, реализующий логику добаления контакта.
     *
     * @param contact контакт для добавления
     * @throws IllegalArgumentException если контакт уже существует в таблице
     */
    static void addContact(Contact contact) throws IllegalArgumentException {
        if (!MainScreenController.getContacts().contains(contact)) {
            try {
                DBStorage.addContact(contact);
                MainScreenController.contacts.add(contact);
            } catch (Throwable ex) {
                System.out.println("Ошибка при добавлении контакта в БД");
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Метод, реализующий логику редактированя контакта.
     *
     * @param contact контакт для редактирования
     * @throws IllegalArgumentException если отредактированный контакт дублирует
     *                                  уже существующий
     */
    static void editContact(Contact contact) throws IllegalArgumentException {
        Contact contactBeforeChange =
                MainScreenController.contacts.get(MainScreenController.indexOfSelectedContactInContacts);
        MainScreenController.contacts.set(MainScreenController.indexOfSelectedContactInContacts, contact);
        int count = 0;
        for (var contactItem : MainScreenController.contacts) {
            if (contact.equals(contactItem)) {
                count++;
                if (count > 1) {
                    MainScreenController.contacts
                            .set(MainScreenController.indexOfSelectedContactInContacts, contactBeforeChange);
                    throw new IllegalArgumentException();
                }
            }
        }
        try {
            DBStorage.editContact(contact);
        } catch (Throwable ex) {
            System.out.println("Ошибка при редактировании контакта в БД");
            MainScreenController.contacts
                    .set(MainScreenController.indexOfSelectedContactInContacts, contactBeforeChange);
        }
    }

    /**
     * Метод, реализующий логику удаления контакта.
     */
    static void deleteContact() {
        try {
            DBStorage.deleteContact(MainScreenController.contacts.get
                    (MainScreenController.indexOfSelectedContactInContacts));
            MainScreenController.contacts.remove(MainScreenController.indexOfSelectedContactInContacts);
        } catch (Throwable ex) {
            System.out.println("Ошибка при удалении контакта из БД");
        }
    }

    /**
     * Метод, реализующий логику поиска.
     * Приводит все к нижнему регистру и проверяет на совпадение переданного
     * значения с началом имени или фамилии или отчества.
     *
     * @param searchText введенное значение для поиска
     * @return лист с контактами, прощедшими фильтрацию
     */
    static ObservableList<Contact> search(String searchText) {
        final String searchTextInLower = searchText.toLowerCase().trim();
        return MainScreenController.contacts.filtered((contact ->
                contact.getName().toLowerCase().startsWith(searchTextInLower) ||
                        contact.getSurname().toLowerCase().startsWith(searchTextInLower) ||
                        contact.getPatronymic().toLowerCase().startsWith(searchTextInLower)));
    }
}
