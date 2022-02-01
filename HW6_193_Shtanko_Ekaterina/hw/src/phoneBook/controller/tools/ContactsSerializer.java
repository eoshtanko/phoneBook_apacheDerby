package phoneBook.controller.tools;

import phoneBook.model.Contact;

import java.io.*;
import java.util.List;

public class ContactsSerializer {

    /**
     * Сериализация
     *
     * @param contacts контакты для сериализации
     * @param file     файл, в который будет осуществленная запись
     * @throws IOException при ошибке в работе с файлами.
     */
    static public void serialize(List<Contact> contacts, File file) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(contacts);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IOException();
        }
    }

    /**
     * Десериализация
     *
     * @param file файл, из которого будут взяты данные
     * @return лист с десериализованными контактами или null в случае
     * неудачи
     */
    static public List<Contact> deserialize(File file) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Contact>) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
