package phoneBook.controller.workWithContact;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkWithContactLogicTest {

    @Test
    void isValidNameOrSurnameTest() {
        String validNameMinSize = "Ян";
        String validLongName = "Барнаби Мармадюк Алоизий Бенджи Кобвеб" +
                " Дартаньян Эгберт Феликс Гаспар Гумберт Игнатий" +
                " Джейден Каспер Лерой Максимилиан Недди Объяхулу " +
                "Пепин Кьюллиам Розенкранц Секстон Тедди Апвуд Виватма" +
                " Уэйленд Ксилон Ярдли Закари Усански";
        String emptyName = "";
        String blancName = "       ";
        String tooShortName = "Z";
        String notValidNullName = null;
        assertTrue(WorkWithContactLogic.isValidNameOrSurname(validNameMinSize));
        assertTrue(WorkWithContactLogic.isValidNameOrSurname(validLongName));
        assertFalse(WorkWithContactLogic.isValidNameOrSurname(emptyName));
        assertFalse(WorkWithContactLogic.isValidNameOrSurname(blancName));
        assertFalse(WorkWithContactLogic.isValidNameOrSurname(tooShortName));
        assertFalse(WorkWithContactLogic.isValidNameOrSurname(notValidNullName));
    }

    @Test
    void isValidPhone() {
        String validShortPhone = "02";
        String validShortPhoneWithPlus = "+79174817493";
        String maxLongPhone = "+828282828282";
        String beleberdaPhone = "belebrda";
        String tooLongPhone = "2020202020202";
        String wrongPhoneFormat = "++9292";
        String tooShortPhone = "+0";
        assertTrue(WorkWithContactLogic.isValidPhone(validShortPhone));
        assertTrue(WorkWithContactLogic.isValidPhone(validShortPhoneWithPlus));
        assertTrue(WorkWithContactLogic.isValidPhone(maxLongPhone));
        assertFalse(WorkWithContactLogic.isValidPhone(beleberdaPhone));
        assertFalse(WorkWithContactLogic.isValidPhone(tooLongPhone));
        assertFalse(WorkWithContactLogic.isValidPhone(wrongPhoneFormat));
        assertFalse(WorkWithContactLogic.isValidPhone(tooShortPhone));
    }

    @Test
    void isValidBirthday() {
        LocalDate correctDateMax = LocalDate.now().minusDays(1);
        assertTrue(WorkWithContactLogic.isValidBirthday(correctDateMax));

        LocalDate correctDateMin = LocalDate.now().minusDays(150);
        assertTrue(WorkWithContactLogic.isValidBirthday(correctDateMin));

        LocalDate wrongTooMaxDate = LocalDate.MAX;
        assertFalse(WorkWithContactLogic.isValidBirthday(wrongTooMaxDate));

        LocalDate wrongTooMinDate = LocalDate.MIN;
        assertFalse(WorkWithContactLogic.isValidBirthday(wrongTooMinDate));

        LocalDate wrongDateNow = LocalDate.now();
        assertFalse(WorkWithContactLogic.isValidBirthday(wrongDateNow));

        LocalDate nullDate = null;
        assertFalse(WorkWithContactLogic.isValidBirthday(nullDate));
    }
}