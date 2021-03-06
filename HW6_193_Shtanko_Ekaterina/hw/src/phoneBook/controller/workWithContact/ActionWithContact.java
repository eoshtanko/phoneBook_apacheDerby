package phoneBook.controller.workWithContact;

import phoneBook.model.Contact;

/**
 * Окна добавления контака и редактирования контакта практически не отличаются, кроме
 * логики действия при нажатии кнопики "Сохранить".
 * Поэтому, действие, производимое при нажатии кнопки, устаанвливается
 * через интерфейс.
 */
public interface ActionWithContact {
    /**
     * Либо логика добавления нового контакта, либо
     * замещения старого контакта новым, отредактированным.
     *
     * @param contact только что созданный контакт
     *                /контакт после редактирования
     */
    void doSomeAction(Contact contact);
}
