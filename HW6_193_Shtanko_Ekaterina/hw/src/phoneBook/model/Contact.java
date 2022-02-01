package phoneBook.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Контакт - основная сущность приложения.
 */
public class Contact implements Serializable {

    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String mobilePhone;
    private String homePhone;
    private String address;
    private LocalDate birthday;
    private String notes;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }


    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    /**
     * Формирует общую информацию о номерах
     * телофонов в формате "мобильный номер/ домашний номер".
     *
     * @return информация о внесенных телефонах
     * в формате "мобильный номер/ домашний номер".
     */
    public String getPhone() {
        if (!mobilePhone.equals("") && !homePhone.equals("")) {
            return mobilePhone + "/" + homePhone;
        }
        if (!mobilePhone.equals("")) {
            return mobilePhone;
        }
        return homePhone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /*
    Уникальным идентификатором Контакта является комбинация полей Имя, Фамилия и Отчество.
    */
    @Override
    public int hashCode() {
        return Objects.hash(name, surname, patronymic);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contact person = (Contact) o;
        return Objects.equals(name, person.name) && Objects.equals(surname, person.surname)
                && Objects.equals(patronymic, person.patronymic);
    }
}
