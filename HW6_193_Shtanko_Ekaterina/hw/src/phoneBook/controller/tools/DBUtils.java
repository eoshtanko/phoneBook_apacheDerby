package phoneBook.controller.tools;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DBUtils {

    /**
     * Метод, проверяющий, существует ли таблицв и готова ли она к работе.
     * @param conTst подключение к базе данных
     * @return true - таблица существует и готова к работе,
     * false - таблица не была создана
     * @throws SQLException - ошибка в работе с БД
     */
     static boolean checkForTable(Statement s) throws SQLException {
        try {
            s.execute("UPDATE CONTACTS SET NAME = 'AA', SURNAME = 'AA', PATRONYMIC = 'AA'," +
                    " MOBILEPHONE = '111', HOMEPHONE = '111', ADDRESS = 'AA', " +
                    "BIRTHDAY = NULL, NOTES = 'AA' WHERE 1=3");
        } catch (SQLException sqle) {
            String theError = (sqle).getSQLState();
            if (theError.equals("42X05"))   // Таблицы не существует
            {
                return false;
            } else if (theError.equals("42X14") || theError.equals("42821")) {
                System.out.println("checkForTable: Неверное определение таблицы." +
                        " Drop таблицу CONTACTS и перезапустите программу.");
                throw sqle;
            } else {
                System.out.println("checkForTable: Ошибка!");
                throw sqle;
            }
        }
        return true;
    }

    /**
     * Метод, конвертирующий Date в LocalDate.
     * @param dateToConvert дата в формате Date
     * @return дата в формате LocalDate
     */
    static LocalDate convertToLocalDate(Date dateToConvert) {
        if(dateToConvert != null) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
        }
        return null;
    }

    /**
     * Метод, конвертирующий LocalDate в Date.
     * @param dateToConvert дата в формате LocalDate
     * @return дата в формате Date
     */
    static Date convertToDate(LocalDate dateToConvert) {
        if(dateToConvert != null) {
            return java.sql.Date.valueOf(dateToConvert);
        }
        return null;
    }
}
