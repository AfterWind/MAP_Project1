package afterwind.lab1.validator;

import afterwind.lab1.entity.Option;
import afterwind.lab1.exception.ValidationException;

public class OptionValidator implements IValidator<Option> {

    @Override
    public void validate(Option e) throws ValidationException {
        String message = "";
        if (e.getCandidate() == null) {
            message += "Candidat invalid!\n";
        }
        if (e.getSection() == null) {
            message += "Sectie invalida!\n";
        }
        if (!message.equals("")) {
            throw new ValidationException(message);
        }
    }
}
