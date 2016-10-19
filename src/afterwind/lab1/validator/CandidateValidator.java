package afterwind.lab1.validator;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;

public class CandidateValidator implements IValidator<Candidate> {

    @Override
    public void validate(Candidate e) throws ValidationException {
        String message = "";
        if (e.getName().equals("")) {
            message += "Nume invalid!\n";
        }
        if (e.getTel().equals("")) {
            message += "Telefon invalid!\n";
        }
        if (e.getAddress().equals("")) {
            message += "Adresa invalida!\n";
        }
        if (!message.equals("")) {
            throw new ValidationException(message);
        }
    }
}
