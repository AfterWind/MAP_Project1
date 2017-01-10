package afterwind.lab1.validator;

import afterwind.lab1.exception.ValidationException;

public interface IValidator<T> {

    /**
     * Valideaza o entitate
     * @param e entitatea validata
     * @throws ValidationException daca entitatea nu este valida
     */
    void validate(T e) throws ValidationException;

}
