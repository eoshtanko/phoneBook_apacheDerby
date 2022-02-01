package phoneBook.controller.workWithContact;

import java.time.LocalDate;
import java.util.regex.Pattern;

class WorkWithContactLogic {
    /**
     * Логика проверки валидности имени/фамилии.
     * Имя должно иметь два или более символа.
     *
     * @param nameOrSurname имя/фамилия
     * @return true, если имя валидно, иначе - false
     */
    static boolean isValidNameOrSurname(String nameOrSurname) {
        return nameOrSurname != null &&
                !nameOrSurname.isBlank() &&
                nameOrSurname.length() >= 2;
    }

    /**
     * Логика проверки валидности номера телефона.
     * Метод проверяет номер на соответствие следующему правилу:
     * "Номер телефона может начинаться с '+' и содержать от 2 до 12 цифр".
     *
     * @param phone телефон (мобильный или домашний)
     * @return true, если телефон валиден, иначе - false
     */
    static boolean isValidPhone(String phone) {
        // Данный паттерн расшифровывается так:
        // - телефон может(но не обязан) начинаться с '+'.
        // - телефон(вне зависимости от '+' обязан содержать от 2 до 12 цифр.
        // - никаких иных символов не допускается.
        return phone != null && Pattern.compile("^\\+?\\d{2,12}$").matcher(phone).matches();
    }

    /**
     * Логика проверки валидности дня рождения.
     * Проверка попадания во временной интервал(150 лет назад - самая
     * "далекая" дата, нынешняя дата - "ближайшая".
     *
     * @param date день рождения(LocalDate)
     * @return true, если дата валидна, иначе - false
     */
    static boolean isValidBirthday(LocalDate date) {
        return date != null &&
                date.isAfter(LocalDate.now().minusYears(150)) &&
                date.isBefore(LocalDate.now());
    }
}
