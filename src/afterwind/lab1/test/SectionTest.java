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
    public void repositoryTest() {
        Repository<Section> repo = new Repository<>(Section.class);
        Section s1 = new Section(5, "Info", 100);
        repo.add(s1);
        Assert.assertEquals("Failed to save a section to repository", s1, repo.get(5));
        Section s2 = new Section(6, "Mate", 20);
        repo.add(s2);
        Assert.assertEquals("Failed to save section to repository", s2, repo.get(6));
        Assert.assertEquals("Failed to get proper size of the repository", 2, repo.getSize());
        repo.remove(s1);
        Assert.assertEquals("Failed to remove section from repository", null, repo.get(5));
        Assert.assertEquals("Failed to remove proper section from repository", s2, repo.get(6));
    }

    @Test
    public void controllerTest() {
        SectionController controller = new SectionController();
        Section c1 = new Section(5, "Info", 100);
        controller.add(c1);
        Assert.assertEquals("Failed to save a section to repository through controller", c1, controller.get(5));
        Section c2 = new Section(6, "Mate", 20);
        controller.add(c2);
        Assert.assertEquals("Failed to save a section to repository through controller", c2, controller.get(6));
        Assert.assertEquals("Failed to get proper amount of candidates", 2, controller.getSize());
        int nextId = controller.getNextId();
        Assert.assertFalse("Failed to get new unique id", nextId == 10 || nextId == 11);
        controller.updateSection(c1, "Romana", 200);
        Assert.assertEquals("Failed to update section", "Romana", c1.getName());
        Assert.assertEquals("Failed to update section", 200, c1.getNrLoc());
        controller.remove(5);
        Assert.assertEquals("Failed to remove a section from repository through controller", null, controller.get(5));
        Assert.assertEquals("Failed to remove a section from repository through controller", c2, controller.get(6));
        controller.remove(c2);
        Assert.assertEquals("Failed to remove a section from repository through controller", null, controller.get(6));
    }
}
