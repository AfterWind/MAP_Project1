package afterwind.lab1.validator;

import afterwind.lab1.exception.ValidationException;

public interface IValidator<T> {

    void validate(T e) throws ValidationException;

}
