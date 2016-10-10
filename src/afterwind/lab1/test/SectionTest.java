package afterwind.lab1.test;

import afterwind.lab1.entity.Section;
import afterwind.lab1.controller.SectionController;
import afterwind.lab1.repository.Repository;
import org.junit.Assert;
import org.junit.Test;

public class SectionTest {
    @Test
    public void entityTest() {
        Section c = new Section(5, "Info", 100);
        Assert.assertEquals("Failed to get proper ID", 5, c.getId());
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
        Assert.assertEquals("Failed to update section", 0, c1.getId());
    }
}