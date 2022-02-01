package phoneBook.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void getPhoneTest() {
        Contact contact = new Contact();
        String mobilePhone = "+79174817465";
        contact.setMobilePhone(mobilePhone);
        contact.setHomePhone("");
        assertEquals(mobilePhone, contact.getPhone());

        contact = new Contact();
        String homePhone = "89898989";
        contact.setHomePhone(homePhone);
        contact.setMobilePhone("");
        assertEquals(homePhone, contact.getPhone());

        contact.setMobilePhone(mobilePhone);
        assertEquals(mobilePhone + "/" + homePhone, contact.getPhone());
    }

    @Test
    void testEquals() {
        Contact contact1 = new Contact();
        contact1.setName("aaa");
        contact1.setSurname("bbb");
        contact1.setPatronymic("ccc");
        contact1.setMobilePhone("01111");

        Contact contact2 = new Contact();
        contact2.setName("aaa");
        contact2.setSurname("bbb");
        contact2.setPatronymic("ccc");
        contact2.setMobilePhone("02222");

        Contact contact3 = new Contact();
        contact3.setName("aaa");
        contact3.setSurname("cccc");
        contact3.setPatronymic("bbb");
        contact3.setMobilePhone("01111");

        assertEquals(contact1, contact2);
        assertNotEquals(contact1, contact3);
    }
}