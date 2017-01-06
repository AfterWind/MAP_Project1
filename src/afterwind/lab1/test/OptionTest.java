package afterwind.lab1.test;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.validator.IValidator;
import afterwind.lab1.validator.OptionValidator;
import org.junit.Assert;
import org.junit.Test;

public class OptionTest {

    @Test
    public void entityTest() {
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Candidate c2 = new Candidate(11, "Andrei", "000111222", "Kappa");
        Section s1 = new Section(5, "Info", 100);
        Section s2 = new Section(3, "Matematica", 100);

        Option o = new Option(0, s1, c2);
        Assert.assertEquals("Failed to get proper ID", 0, (int) o.getId());
        Assert.assertEquals("Failed to get proper section", s1, o.getSection());
        Assert.assertEquals("Failed to get proper candidate", c2, o.getCandidate());
        o.setCandidate(c1);
        o.setSection(s2);
        Option o2 = new Option(3, s1, c2);
        Option o3 = new Option(0, s2, c1);
        Assert.assertEquals("Failed to set section", s2, o.getSection());
        Assert.assertEquals("Failed to set candidate", c1, o.getCandidate());
        Assert.assertNotNull("Failed to convert to string", o.toString());
        Assert.assertEquals("Failed to check equality of 2 options with same id", o, o3);
        Assert.assertNotEquals("Failed to check inequality of 2 options with same id", o, o2);
    }

    @Test
    public void controllerTest() {
        OptionService controller = new OptionService();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Candidate c2 = new Candidate(11, "Andrei", "000111222", "Kappa");
        Section s1 = new Section(5, "Info", 100);
        Section s2 = new Section(3, "Matematica", 100);

        Option o = new Option(0, s1, c2);
        controller.updateOption(o, c1, s2);
        Assert.assertEquals("Failed to update option", c1, o.getCandidate());
        Assert.assertEquals("Failed to update option", s2, o.getSection());
        Assert.assertEquals("Failed to update option", 0, (int) o.getId());
        Assert.assertNotNull("Failed to convert controller to string", controller.toString());
    }

    @Test
    public void validatorValidTest() throws ValidationException {
        IValidator<Option> validator = new OptionValidator();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Section s1 = new Section(5, "Info", 100);
        Option valid = new Option(0, s1, c1);
        validator.validate(valid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidIDTest() throws ValidationException {
        IValidator<Option> validator = new OptionValidator();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Section s1 = new Section(5, "Info", 100);
        Option invalid = new Option(-2, s1, c1);
        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidCandidateTest() throws ValidationException {
        IValidator<Option> validator = new OptionValidator();
        Section s1 = new Section(5, "Info", 100);
        Option invalid = new Option(0, s1, null);
        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidSectionTest() throws ValidationException {
        IValidator<Option> validator = new OptionValidator();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Option invalid = new Option(0, null, c1);
        validator.validate(invalid);
    }
}
