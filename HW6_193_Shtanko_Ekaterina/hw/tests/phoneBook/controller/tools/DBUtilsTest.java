package phoneBook.controller.tools;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilsTest {

    @Test
    void convertToLocalDataTest() {
        Date date = null;
        assertNull(DBUtils.convertToLocalDate(date));

        final Date date1 = new Date(1);
        assertDoesNotThrow(() -> (LocalDate) DBUtils.convertToLocalDate(date1));
        assertNotNull(DBUtils.convertToLocalDate(date1));
    }

    @Test
    void convertToDataTest() {
        LocalDate date = null;
        assertNull(DBUtils.convertToDate(date));

        final LocalDate date1 = LocalDate.MAX;
        assertDoesNotThrow(() -> (Date) DBUtils.convertToDate(date1));
        assertNotNull(DBUtils.convertToDate(date1));
    }
}