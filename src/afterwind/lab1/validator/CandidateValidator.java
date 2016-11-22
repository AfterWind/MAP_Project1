package afterwind.lab1.validator;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;

public class CandidateValidator implements IValidator<Candidate> {

    @Override
    public void validate(Candidate e) throws ValidationException {
        String message = "";
        if (e.getId() < 0) {
            message += "ID invalid!\n";
        }
        if (e.getName().equals("")) {
            message += "Nume invalid!\n";
        }
        if (e.getTelephone().equals("")) {
            message += "Telefon invalid!\n";
        }
        if (e.getAddress().equals("")) {
            message += "Adresa invalida!\n";
        }
        for (Character c : e.getTelephone().toCharArray()) {
            if (!(c >= '0' && c <= '9')) {
                message += "Telefon invalid!\n";
                break;
            }
        }
        if (!message.equals("")) {
            throw new ValidationException(message.substring(0, message.length() - 1));
        }
    }
}
