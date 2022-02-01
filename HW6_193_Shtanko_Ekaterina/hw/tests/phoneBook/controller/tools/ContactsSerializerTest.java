package phoneBook.controller.tools;

import org.junit.jupiter.api.Test;
import phoneBook.model.Contact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactsSerializerTest {

    @Test
    void serializeAndDeserializeTest() {
        Contact contact = new Contact();
        contact.setName("Екатерина");
        contact.setSurname("Штанько");
        contact.setPatronymic("Олеговна");
        contact.setMobilePhone("909090");

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);

        String fileName = "test.txt";

        assertDoesNotThrow(() -> ContactsSerializer.serialize(contacts, new File(fileName)));

        File file = new File(fileName);
        assertTrue(file.exists());
        List<Contact> contactsAfter = ContactsSerializer.deserialize(file);

        assertEquals(contacts.size(), contactsAfter.size());
        assertEquals(contacts.get(0), contactsAfter.get(0));
        assertArrayEquals(contacts.toArray(), contactsAfter.toArray());

        file = new File("beleberda");
        assertNull(ContactsSerializer.deserialize(file));
    }
}