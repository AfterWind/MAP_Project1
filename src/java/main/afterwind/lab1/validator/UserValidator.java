package afterwind.lab1.validator;

import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.permission.User;

public class UserValidator implements IValidator<User> {
    @Override
    public void validate(User e) throws ValidationException {
        String message = "";
        if (e.getUsername().equals("")) {
            message += "Username invalid!\n";
        }
        if (e.getPassword().length() < 10) {
            message += "Dati o parola de macar 10 caradtere!\n";
        }
        if (!message.equals("")) {
            throw new ValidationException(message);
        }
    }
}
