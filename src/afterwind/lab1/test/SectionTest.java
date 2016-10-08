package afterwind.lab1.test;

import afterwind.lab1.entity.Section;
import afterwind.lab1.controller.SectionController;
import afterwind.lab1.repository.SectionRepository;
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
    public void repositoryTest() {
        SectionRepository repo = new SectionRepository();
        Section s1 = new Section(5, "Info", 100);
        repo.add(s1);
        Assert.assertEquals("Failed to save a section to repository", s1, repo.get(5));
        Section s2 = new Section(6, "Mate", 20);
        repo.add(s2);
        Assert.assertEquals("Failed to save section to repository", s2, repo.get(6));
        repo.remove(s1);
        Assert.assertEquals("Failed to remove section from repository", null, repo.get(5));
        Assert.assertEquals("Failed to remove proper section from repository", s2, repo.get(6));
    }

    @Test
    public void controllerTest() {
        SectionController controller = new SectionController();
        Section c1 = new Section(5, "Info", 100);
        controller.addSection(c1);
        Assert.assertEquals("Failed to save a section to repository through controller", c1, controller.getSection(5));
        Section c2 = new Section(6, "Mate", 20);
        controller.addSection(c2);
        Assert.assertEquals("Failed to save a section to repository through controller", c2, controller.getSection(6));

        controller.removeSection(5);
        Assert.assertEquals("Failed to remove a section from repository through controller", null, controller.getSection(5));
        Assert.assertEquals("Failed to remove a section from repository through controller", c2, controller.getSection(6));
        controller.removeSection(c2);
        Assert.assertEquals("Failed to remove a section from repository through controller", null, controller.getSection(6));
    }
}
