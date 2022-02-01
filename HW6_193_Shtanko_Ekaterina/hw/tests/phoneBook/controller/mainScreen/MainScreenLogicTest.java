package phoneBook.controller.mainScreen;

import javafx.collections.FXCollections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import phoneBook.controller.tools.DBStorage;
import phoneBook.model.Contact;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Здесь присутствуют тесты для основной логики работы с программой:
 * добавление, редактирование, удаление и поиск клнтактов.
 * <p>
 * Поскольку, тесты для данного класса полностью покрывают и логику класса "DBStorage",
 * куда вынесены методы работы с БД, я приняла решение не создавать для него отдельного
 * тестового класса.
 */
class MainScreenLogicTest {

    private static Contact contact;

    @BeforeAll
    @Test
    static void prepareForTest() {
        String dbName = "TestDB";
        var savedContacts = DBStorage.getContactsCurrentState(dbName);
        MainScreenController.contacts = FXCollections.observableArrayList(savedContacts);

        contact = new Contact();
        contact.setName("Test");
        contact.setSurname("Testovich");
        contact.setPatronymic("");
        contact.setMobilePhone("+79176574637");
    }

    @Test
    void mainLogicTest() {
        doAddContact();
        doEditContact();
        doSearch();
        doDeleteContact();
    }

    void doAddContact() {
        assertEquals(0, MainScreenController.contacts.size());

        assertDoesNotThrow(() -> MainScreenLogic.addContact(contact));

        assertEquals(1, MainScreenController.contacts.size());

        List<Contact> contacts = assertDoesNotThrow(DBStorage::getContacts);
        assertEquals(1, contacts.size());

        assertEquals(contact.getId(), contacts.get(0).getId());
        assertEquals(contact.getName(), contacts.get(0).getName());
        assertEquals(contact.getSurname(), contacts.get(0).getSurname());
        assertEquals(contact.getMobilePhone(), contacts.get(0).getMobilePhone());

        // попытка добавить контакт с уже сущ. ФИО
        assertThrows(IllegalArgumentException.class, () -> MainScreenLogic.addContact(contact));
        // попытка добавить контакт с уже сущ. ФИО только в БД
        assertThrows(SQLException.class, () -> DBStorage.addContact(contact));
    }

    void doEditContact() {
        String testName = "AAA";
        String testPhone = "+71111111111";

        Contact newContact = new Contact();
        newContact.setId(contact.getId());
        newContact.setSurname(contact.getSurname());
        newContact.setMobilePhone(testPhone);
        newContact.setName(testName);
        newContact.setPatronymic("");

        MainScreenController.indexOfSelectedContactInContacts = 0;

        assertDoesNotThrow(() -> MainScreenLogic.editContact(newContact));

        List<Contact> contacts = assertDoesNotThrow(DBStorage::getContacts);

        assertEquals(testName, contacts.get(0).getName());
        assertEquals(contact.getSurname(), contacts.get(0).getSurname());
        assertEquals(testPhone, contacts.get(0).getMobilePhone());
        assertEquals(contact.getId(), contacts.get(0).getId());
        assertEquals(MainScreenController.contacts.get(0), newContact);

        // Если полученный в результате исправления контакт уже существует

        assertDoesNotThrow(() -> MainScreenLogic.addContact(contact));

        Contact newContact2 = new Contact();
        newContact2.setId(contact.getId());
        newContact2.setSurname(contact.getSurname());
        newContact2.setMobilePhone(testPhone);
        newContact2.setName(testName);
        newContact2.setPatronymic("");

        MainScreenController.indexOfSelectedContactInContacts = 1;

        assertThrows(SQLException.class, () -> DBStorage.editContact(newContact2));
        assertThrows(Throwable.class, () -> MainScreenLogic.editContact(newContact2));

        assertEquals(MainScreenController.contacts.get(0), newContact);
        assertEquals(MainScreenController.contacts.get(1), contact);
    }

    void doDeleteContact() {
        assertEquals(5, MainScreenController.contacts.size());
        List<Contact> contacts = assertDoesNotThrow(DBStorage::getContacts);
        assertEquals(5, contacts.size());

        MainScreenController.indexOfSelectedContactInContacts = 0;
        MainScreenLogic.deleteContact();
        MainScreenLogic.deleteContact();
        MainScreenLogic.deleteContact();
        MainScreenLogic.deleteContact();
        MainScreenLogic.deleteContact();

        assertEquals(0, MainScreenController.contacts.size());
    }

    void doSearch() {
        Contact contact1 = new Contact();
        contact1.setName("rrr");
        contact1.setSurname("aab");
        contact1.setPatronymic("ccc");
        contact1.setMobilePhone("01");

        Contact contact2 = new Contact();
        contact2.setName("ppp");
        contact2.setSurname("abc");
        contact2.setPatronymic("ppp");
        contact2.setMobilePhone("02");

        Contact contact3 = new Contact();
        contact3.setName("aaa");
        contact3.setSurname("Ргрг");
        contact3.setPatronymic("СССР");
        contact3.setMobilePhone("03");

        MainScreenLogic.addContact(contact1);
        MainScreenLogic.addContact(contact2);
        MainScreenLogic.addContact(contact3);

        List<Contact> contactsAfter = MainScreenLogic.search("abc");
        assertEquals(1, contactsAfter.size());
        assertEquals("02", contactsAfter.get(0).getMobilePhone());

        contactsAfter = MainScreenLogic.search("СССР");
        assertEquals(1, contactsAfter.size());
        assertEquals("03", contactsAfter.get(0).getMobilePhone());

        contactsAfter = MainScreenLogic.search("rrr");
        assertEquals(1, contactsAfter.size());
        assertEquals("01", contactsAfter.get(0).getMobilePhone());

        contactsAfter = MainScreenLogic.search("a");
        assertEquals(4, contactsAfter.size());

        contactsAfter = MainScreenLogic.search("");
        assertEquals(5, contactsAfter.size());
    }

    @AfterAll
    @Test
    static void shutDownDBTest() {
        assertTrue(DBStorage.shutDownDB());
    }
}