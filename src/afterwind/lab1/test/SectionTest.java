package afterwind.lab1.test;

import afterwind.lab1.controller.SectionController;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
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
    }

    @Test
    public void controllerTest() {
        SectionController controller = new SectionController();
        Section c1 = new Section(0, "Mate", 12);
        controller.updateSection(c1, "Romana", 200);
        Assert.assertEquals("Failed to update section", "Romana", c1.getName());
        Assert.assertEquals("Failed to update section", 200, c1.getNrLoc());
        Assert.assertEquals("Failed to update section", 0, (int) c1.getId());
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

}