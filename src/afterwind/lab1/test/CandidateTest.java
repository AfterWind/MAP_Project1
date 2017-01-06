package afterwind.lab1.test;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.ISerializer;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.IValidator;
import org.junit.Assert;
import org.junit.Test;

public class CandidateTest {

    @Test
    public void entityTest() {
        Candidate c = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Assert.assertEquals("Failed to get proper ID", 10, (int) c.getId());
        Assert.assertEquals("Failed to get proper name", "Sergiu", c.getName());
        Assert.assertEquals("Failed to get proper telephone number", "000111222", c.getTelephone());
        Assert.assertEquals("Failed to get proper address", "Kappa", c.getAddress());
        c.setName("Victor");
        c.setTelephone("00000000");
        c.setAddress("Dunarii");
        Assert.assertEquals("Failed to set proper name", "Victor", c.getName());
        Assert.assertEquals("Failed to set proper telephone number", "00000000", c.getTelephone());
        Assert.assertEquals("Failed to set proper address", "Dunarii", c.getAddress());
        Assert.assertNotNull("Failed to convert to string", c.toString());
    }

    @Test
    public void controllerTest() {
        CandidateService controller = new CandidateService();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        controller.updateCandidate(c1, "Andrei", "111", "idk");
        Assert.assertEquals("Failed to update candidate", "Andrei", c1.getName());
        Assert.assertEquals("Failed to update candidate", "111", c1.getTelephone());
        Assert.assertEquals("Failed to update candidate", "idk", c1.getAddress());
        Assert.assertEquals("Failed to update candidate", 10, (int) c1.getId());
        Assert.assertNotNull("Failed to convert controller to string", controller.toString());
    }

    @Test
    public void filterTest() throws ValidationException {
        CandidateService controller = new CandidateService();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Candidate c2 = new Candidate(11, "Andrei", "111", "IDK");
        Candidate c3 = new Candidate(12, "Vlad", "222", "Task");
        Candidate c4 = new Candidate(13, "Vlad", "222", "Task");
        controller.add(c1);
        controller.add(c2);
        controller.add(c3);
        controller.add(c4);

        IRepository<Candidate, Integer> filter1 = controller.filterByName("Sergiu");
        Assert.assertTrue(filter1.get(10) == c1);
        IRepository<Candidate, Integer> filter2 = controller.filterByTelephone("111");
        Assert.assertTrue(filter2.get(11) == c2);
        IRepository<Candidate, Integer> filter3 = controller.filterByAddress("Task");
        Assert.assertTrue(filter3.get(12) == c3);
        Assert.assertTrue(filter3.get(13) == c4);
    }

    @Test
    public void serializerTest() {
        ISerializer<Candidate> serializer = new Candidate.Serializer();
        Candidate c = new Candidate(1231, "Adi", "012", "Asdf");
        String cs = serializer.serialize(c);
        Assert.assertNotNull("Failed to serialize a candidate");
        Assert.assertTrue("Failed to serialize id of candidate", cs.contains("1231"));
    }

    @Test
    public void deserializerTest() {
        ISerializer<Candidate> serializer = new Candidate.Serializer();
        String cs = "1|Adi|012|Asdf";
        Candidate c = serializer.deserialize(cs);
        Assert.assertNotNull("Failed to desearialize candidate", c);
        Assert.assertEquals("Failed to deserialize id of candidate", 1, (int) c.getId());
        Assert.assertEquals("Failed to deserialize name of candidate", "Adi", c.getName());
        Assert.assertEquals("Failed to deserialize telephone of candidate", "012", c.getTelephone());
        Assert.assertEquals("Failed to deserialize address of candidate", "Asdf", c.getAddress());
    }

    @Test
    public void validatorValidTest() throws ValidationException {
        IValidator<Candidate> validator = new CandidateValidator();
        Candidate valid = new Candidate(1, "Andrei", "000111", "Address");
        validator.validate(valid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidIDTest() throws ValidationException {
        IValidator<Candidate> validator = new CandidateValidator();
        Candidate invalid = new Candidate(-1, "Andrei", "000111", "Address");
        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidNameTest() throws ValidationException {
        IValidator<Candidate> validator = new CandidateValidator();
        Candidate invalid = new Candidate(1, "", "000111", "Address");
        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidTelephoneTest() throws ValidationException {
        IValidator<Candidate> validator = new CandidateValidator();
        Candidate invalid = new Candidate(1, "Andrei", "abcd", "Address");
        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidAddressTest() throws ValidationException {
        IValidator<Candidate> validator = new CandidateValidator();
        Candidate invalid = new Candidate(1, "Andrei", "000111", "");
        validator.validate(invalid);
    }
}
