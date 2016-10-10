package afterwind.lab1.test;

import afterwind.lab1.controller.SectionController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.Repository;
import org.junit.Assert;
import org.junit.Test;

public class GenericTests {

    @Test
    public void repositoryTest() {
        Repository<Candidate> repo = new Repository<>(Candidate.class);
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        repo.add(c1);
        Assert.assertEquals("Failed to save an entity to repository" ,c1, repo.get(10));
        Candidate c2 = new Candidate(11, "Victor", "000111222", "Kappa");
        repo.add(c2);
        Assert.assertEquals("Failed to save an entity to repository", c2, repo.get(11));
        Assert.assertEquals("Failed to get proper size of the repository", 2, repo.getSize());
        repo.remove(c1);
        Assert.assertEquals("Failed to remove an entity from repository", null, repo.get(10));
        Assert.assertEquals("Failed to remove proper entity from repository", c2, repo.get(11));
    }

    @Test
    public void controllerTest() {
        SectionController controller = new SectionController();
        Section c1 = new Section(5, "Info", 100);
        controller.add(c1);
        Assert.assertEquals("Failed to save an entity to repository through controller", c1, controller.get(5));
        Section c2 = new Section(6, "Mate", 20);
        controller.add(c2);
        Assert.assertEquals("Failed to save an entity to repository through controller", c2, controller.get(6));
        Assert.assertEquals("Failed to get proper amount of entities", 2, controller.getSize());

        int nextId = controller.getNextId();
        Assert.assertFalse("Failed to get new unique id", nextId == 10 || nextId == 11);
        Assert.assertNotNull("Failed to get the vector of entities", controller.getData());

        controller.remove(5);
        Assert.assertEquals("Failed to remove an entity from repository through controller", null, controller.get(5));
        Assert.assertEquals("Failed to remove an entity from repository through controller", c2, controller.get(6));
        controller.remove(c2);
        Assert.assertEquals("Failed to remove an entity from repository through controller", null, controller.get(6));
    }
}