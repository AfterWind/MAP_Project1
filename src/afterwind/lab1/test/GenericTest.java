package afterwind.lab1.test;

import afterwind.lab1.controller.SectionController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.SectionValidator;
import org.junit.Assert;
import org.junit.Test;

public class GenericTest {

    @Test
    public void repositoryTest() throws ValidationException {
        IRepository<Candidate, Integer> repo = new Repository<>(new CandidateValidator());
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        repo.add(c1);
        Assert.assertEquals("Failed to save an entity to repository" ,c1, repo.get(10));
        Candidate c2 = new Candidate(11, "Victor", "000111222", "Kappa");
        repo.add(c2);
        Assert.assertEquals("Failed to save an entity to repository", c2, repo.get(11));
        Assert.assertEquals("Failed to get proper size of the repository", 2, repo.getSize());
        Assert.assertTrue("Failed to check existance of entity in repository", repo.contains(10));
        Assert.assertFalse("Failed to check existance of entity in repository", repo.contains(12));
        repo.remove(c1);
        Assert.assertNull("Failed to remove an entity from repository", repo.get(10));
        Assert.assertEquals("Failed to remove proper entity from repository", c2, repo.get(11));
        repo.remove(new Candidate(1, "asd", " asd", "asss"));
        Assert.assertNotNull("Failed to convert repository to string", repo.toString());
    }

    @Test
    public void controllerTest() throws ValidationException {
        Repository<Section, Integer> repo = new Repository<>(new SectionValidator());
        SectionController controller = new SectionController(repo);
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

        Assert.assertTrue("Failed to check existance of entity inside repository through controller", controller.contains(5));
        Assert.assertFalse("Failed to check existance of entity inside repository through controller", controller.contains(4));

        Assert.assertEquals("Failed to get proper repository from controller", repo, controller.getRepo());

        controller.remove(5);
        Assert.assertEquals("Failed to remove an entity from repository through controller", null, controller.get(5));
        Assert.assertEquals("Failed to remove an entity from repository through controller", c2, controller.get(6));
        controller.remove(c2);
        Assert.assertEquals("Failed to remove an entity from repository through controller", null, controller.get(6));
    }
}
