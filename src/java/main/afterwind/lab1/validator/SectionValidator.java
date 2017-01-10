package afterwind.lab1.validator;

import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;

public class SectionValidator implements IValidator<Section> {

    @Override
    public void validate(Section e) throws ValidationException {
        String message = "";
        if (e.getId() < 0) {
            message += "ID invalid!\n";
        }
        if (e.getName().equals("")) {
            message += "Nume invalid!\n";
        }
        if (e.getNrLoc() < 0) {
            message += "Numar de locuri invalid!\n";
        }
        if (!message.equals("")) {
            throw new ValidationException(message);
        }
    }
}
