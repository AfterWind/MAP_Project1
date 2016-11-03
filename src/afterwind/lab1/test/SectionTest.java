package afterwind.lab1.test;

import afterwind.lab1.controller.OptionController;
import afterwind.lab1.controller.SectionController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.ISerializer;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.validator.IValidator;
import afterwind.lab1.validator.SectionValidator;
import org.junit.Assert;
import org.junit.Test;

public class SectionTest {
    @Test
    public void entityTest() {
        Section c = new Section(5, "Info", 100);
        Assert.assertEquals("Failed to get proper ID", 5, (int) c.getId());
        Assert.assertEquals("Failed to get proper name", "Info", c.getName());
        Assert.assertEquals("Failed to get proper nrLoc", 100, c.getNrLoc());
        c.setName("Mate");
        c.setNrLoc(50);
        Assert.assertEquals("Failed to set proper name", "Mate", c.getName());
        Assert.assertEquals("Failed to set proper nrLoc", 50, c.getNrLoc());
        Assert.assertNotNull("Failed to convert to string", c.toString());
    }

    @Test
    public void controllerTest() throws ValidationException {
        SectionController controller = new SectionController();
        Section c1 = new Section(0, "Mate", 12);
        controller.add(c1);
        Assert.assertTrue("Failed to add section", controller.contains(0));
        controller.updateSection(c1, "Romana", 200);
        Assert.assertEquals("Failed to update section", "Romana", c1.getName());
        Assert.assertEquals("Failed to update section", 200, c1.getNrLoc());
        Assert.assertEquals("Failed to update section", 0, (int) c1.getId());


        Assert.assertNotNull("Failed to convert controller to string", controller.toString());
    }

    @Test
    public void controllerMostOccupiedTest() throws ValidationException {
        SectionController controller = new SectionController();
        Section s1 = new Section(0, "Mate", 12);
        Section s2 = new Section(1, "Info", 300);
        Section s3 = new Section(2, "Geografie", 10);
        Section s4 = new Section(3, "Romana", 100);
        Section s5 = new Section(4, "Istorie", 300);
        controller.add(s1);
        controller.add(s2);
        controller.add(s3);
        controller.add(s4);
        controller.add(s5);

        Candidate c1 = new Candidate(0, "Andrei", "000", "Address");
        Candidate c2 = new Candidate(1, "Adi", "111", "Address");
        Candidate c3 = new Candidate(2, "Victor", "123", "Address");
        OptionController optionController = new OptionController();
        Option o1 = new Option(0, s1, c1);
        Option o2 = new Option(1, s2, c1);
        Option o3 = new Option(2, s2, c2);
        Option o4 = new Option(3, s3, c2);
        Option o5 = new Option(4, s1, c2);
        Option o6 = new Option(5, s1, c3);
        optionController.add(o1);
        optionController.add(o2);
        optionController.add(o3);
        optionController.add(o4);
        optionController.add(o5);
        optionController.add(o6);

        IRepository<Section, Integer> result = controller.getMostOccupiedSections(optionController.getRepo(), 2);
        Assert.assertTrue("Failed to get top most occupied section", result.contains(s1.getId()));
        Assert.assertTrue("Failed to get second most occupied section", result.contains(s2.getId()));
    }

    @Test
    public void filterTest() throws ValidationException {
        SectionController controller = new SectionController();
        Section c1 = new Section(1, "Info", 100);
        Section c2 = new Section(2, "Mate", 100);
        Section c3 = new Section(3, "Romana", 50);
        Section c4 = new Section(4, "Marketing", 40);
        controller.add(c1);
        controller.add(c2);
        controller.add(c3);
        controller.add(c4);

        IRepository<Section, Integer> filter1 = controller.filterByName("Info");
        Assert.assertTrue(filter1.get(1) == c1);
        IRepository<Section, Integer> filter2 = controller.filterByNrLoc(99, true);
        Assert.assertTrue(filter2.get(3) == c3);
        Assert.assertTrue(filter2.get(4) == c4);
        IRepository<Section, Integer> filter3 = controller.filterByNrLoc(99, false);
        Assert.assertTrue(filter3.get(1) == c1);
        Assert.assertTrue(filter3.get(2) == c2);
    }

    @Test
    public void serializerTest() {
        ISerializer<Section> serializer = new Section.Serializer();
        Section c = new Section(1231, "Info", 100);
        String cs = serializer.serialize(c);
        Assert.assertNotNull("Failed to serialize a section");
        Assert.assertTrue("Failed to serialize id of section", cs.contains("1231"));
    }

    @Test
    public void deserializerTest() {
        ISerializer<Section> serializer = new Section.Serializer();
        String cs = "1|Info|100";
        Section c = serializer.deserialize(cs);
        Assert.assertNotNull("Failed to deserialize section", c);
        Assert.assertEquals("Failed to deserialize id of section", 1, (int) c.getId());
        Assert.assertEquals("Failed to deserialize name of section", "Info", c.getName());
        Assert.assertEquals("Failed to deserialize telephone of section", 100, c.getNrLoc());
    }

    @Test
    public void validatorValidTest() throws ValidationException {
        IValidator<Section> validator = new SectionValidator();
        Section valid = new Section(1, "Informatica", 100);
        validator.validate(valid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidIDTest() throws ValidationException {
        IValidator<Section> validator = new SectionValidator();
        Section invalid = new Section(-1, "Informatica", 100);
        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidNameTest() throws ValidationException {
        IValidator<Section> validator = new SectionValidator();
        Section invalid = new Section(1, "", 100);
        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void validatorInvalidLocTest() throws ValidationException {
        IValidator<Section> validator = new SectionValidator();
        Section invalid = new Section(1, "Informatica", -50);
        validator.validate(invalid);
    }
}