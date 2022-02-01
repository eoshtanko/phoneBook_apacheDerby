package phoneBook.controller.tools;

import phoneBook.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 * Класс, ответственный за работу с БД.
 */
public class DBStorage {
    // подключение к базе данных
    private static Connection conn;
    private static final String TABLE_NAME = "CONTACTS";
    private static Statement statement;

    /**
     * Метод для считывания ранее введенных и сохраненных данных.
     * Он подключается к БД и либо считывает контакты из таблицы,
     * либо создает новую таблицу.
     *
     * @return сохраненные при прошлой сессии использования программы
     * контакты или пустой список,если таковых не оказалось
     */
    public static List<Contact> getContactsCurrentState(String dbName) {
        // Derby connection URL
        String connectionURL = "jdbc:derby:" + dbName + ";create=true";
        String createString =
                "CREATE TABLE " + TABLE_NAME
                        + " (ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                        + " NAME VARCHAR(255) NOT NULL,"
                        + " SURNAME VARCHAR(255) NOT NULL,"
                        + " PATRONYMIC VARCHAR(255),"
                        + " MOBILEPHONE VARCHAR(255),"
                        + " HOMEPHONE VARCHAR(255),"
                        + " ADDRESS VARCHAR(255),"
                        + " BIRTHDAY VARCHAR(255),"
                        + " NOTES VARCHAR(255),"
                        + " CONSTRAINT unique_contact UNIQUE (NAME, SURNAME, PATRONYMIC))";
        try {
            conn = DriverManager.getConnection(connectionURL);
            System.out.println("Подключение к БД " + dbName);
            statement = conn.createStatement();
            if (!DBUtils.checkForTable(statement)) {
                // если БД еще не была создана - создаем ее.
                System.out.println("Создание таблицы " + TABLE_NAME);
                statement.execute(createString);
                System.out.println("Таблица создана успешно.");
                return new ArrayList<>();
            }
            System.out.println("Таблица уже существует. Идет считывание данных...");
            return getContacts();
        } catch (Throwable e) {
            System.out.println("Произошла ошибка!");
            e.printStackTrace(System.out);
        }
        return new ArrayList<>();
    }

    /**
     * Считывание данных из уже существующей таблицы.
     *
     * @return список котнактов из уже существующей таблицы
     * @throws SQLException - ошибка в работе с БД
     */
    public static List<Contact> getContacts() throws SQLException {
        List<Contact> contactsList = new ArrayList<>();
        ResultSet contacts = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
        while (contacts.next()) {
            Contact contact = new Contact();
            contact.setId(contacts.getInt(1));
            contact.setName(contacts.getString(2));
            contact.setSurname(contacts.getString(3));
            contact.setPatronymic(contacts.getString(4));
            contact.setMobilePhone(contacts.getString(5));
            contact.setHomePhone(contacts.getString(6));
            contact.setAddress(contacts.getString(7));
            contact.setBirthday(DBUtils.convertToLocalDate(contacts.getDate(8)));
            contact.setNotes(contacts.getString(9));
            contactsList.add(contact);
        }
        contacts.close();
        System.out.println("Считывание данных завершилось успешно.");
        return contactsList;
    }

    /**
     * Добавление контакта в БД.
     *
     * @param contact добавляемый контакт
     * @throws SQLException - ошибка в работе с БД
     */
    public static void addContact(Contact contact) throws SQLException {
        PreparedStatement psInsert = conn.prepareStatement(
                "INSERT INTO " + TABLE_NAME
                        + " (NAME, SURNAME, PATRONYMIC, MOBILEPHONE, HOMEPHONE, ADDRESS, BIRTHDAY, NOTES)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        insertContact(contact, psInsert, false);
        psInsert.executeUpdate();
        psInsert.close();
        setId(contact);
        System.out.println("Добавление прошло успешно.");
    }

    /**
     * Удаление контакта из БД.
     *
     * @param contact удаляемый контакт
     * @throws SQLException - ошибка в работе с БД
     */
    public static void deleteContact(Contact contact) throws SQLException {
        String deleteString = "DELETE FROM " + TABLE_NAME + " WHERE ID = (?)";
        PreparedStatement psDelete = conn.prepareStatement(deleteString);
        psDelete.setInt(1, contact.getId());
        psDelete.execute();
        psDelete.close();
        System.out.println("Удаление прошло успешно.");
    }

    /**
     * Редактирование контатка в БД.
     *
     * @param contact редактируемый контатк
     * @throws SQLException - ошибка в работе с БД
     */
    public static void editContact(Contact contact) throws SQLException {
        PreparedStatement psEdit = conn.prepareStatement(
                "UPDATE " + TABLE_NAME + " SET NAME = ?, SURNAME = ?, PATRONYMIC = ?," +
                        " MOBILEPHONE = ?, HOMEPHONE = ?, ADDRESS = ?, BIRTHDAY = ?," +
                        " NOTES = ? WHERE ID = ?");
        insertContact(contact, psEdit, true);
        psEdit.executeUpdate();
        psEdit.close();
        System.out.println("Редактирование прошло успешно.");
    }

    /**
     * Вспомогательный метод вставки данных контакта в заготовленное выражение.
     *
     * @param contact           контатк, данные которого вставляются
     * @param preparedStatement заготовленное выраже
     * @throws SQLException - ошибка в работе с БД
     */
    private static void insertContact(Contact contact, PreparedStatement preparedStatement, boolean setId)
            throws SQLException {
        preparedStatement.setString(1, contact.getName());
        preparedStatement.setString(2, contact.getSurname());
        preparedStatement.setString(3, contact.getPatronymic());
        preparedStatement.setString(4, contact.getMobilePhone());
        preparedStatement.setString(5, contact.getHomePhone());
        preparedStatement.setString(6, contact.getAddress());
        preparedStatement.setDate(7, DBUtils.convertToDate(contact.getBirthday()));
        preparedStatement.setString(8, contact.getNotes());
        if (setId) {
            preparedStatement.setInt(9, contact.getId());
        }
    }

    /**
     * Устанавливает контакту сгенерированное id.
     *
     * @param contact контакт для устаноки id
     * @throws SQLException - ошибка в работе с БД
     */
    private static void setId(Contact contact) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT ID FROM " + TABLE_NAME
                        + " WHERE NAME = (?) AND SURNAME = (?) AND PATRONYMIC = (?)");
        preparedStatement.setString(1, contact.getName());
        preparedStatement.setString(2, contact.getSurname());
        preparedStatement.setString(3, contact.getPatronymic());
        ResultSet id = preparedStatement.executeQuery();
        id.next();
        contact.setId(id.getInt("ID"));
        id.close();
        preparedStatement.close();
    }

    /**
     * Метод, ответственный за завершение работы с БД.
     */
    public static boolean shutDownDB() {
        try {
            statement.close();
            conn.close();
            boolean gotSQLExc = false;
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                if (se.getSQLState().equals("XJ015")) {
                    gotSQLExc = true;
                }
            }
            if (!gotSQLExc) {
                System.out.println("БД не завершила работу корректно!!");
                return false;
            } else {
                System.out.println("БД завершила работу корректно.");
                return true;
            }
        } catch (SQLException throwables) {
            System.out.println("Ошибка при завершении работы !!.");
            return false;
        }
    }
}
