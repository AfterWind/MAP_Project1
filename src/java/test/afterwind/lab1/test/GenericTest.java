package afterwind.lab1.test;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.*;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.SectionValidator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GenericTest {

    @Test
    public void repositoryTest() throws ValidationException {
        IRepository<Candidate, Integer> repo = new FileRepository<>(new CandidateValidator(), new Candidate.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/candidates_test.txt");
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
        repo.remove(c2);
        Assert.assertNotNull("Failed to convert repository to string", repo.toString());
    }

    @Test
    public void repositoryNumeroDosTest() throws ValidationException {
        IRepository<Candidate, Integer> repo = new FileRepositoryNumeroDos<>(new CandidateValidator(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/candidates_test2.txt");
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
        repo.remove(c2);
        Assert.assertNotNull("Failed to convert repository to string", repo.toString());
    }

    @Test
    public void controllerTest() throws ValidationException {
        Repository<Section, Integer> repo = new Repository<>(new SectionValidator());
        SectionService controller = new SectionService(repo);
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

        List<Section> sections = new ArrayList<>();
        sections.add(c1);
        sections.add(c2);
        sections = controller.sort(sections, (s1, s2) -> s1.getNrLoc() - s2.getNrLoc());
        Assert.assertEquals("Failed to sort list", sections.get(0), c2);
        Assert.assertEquals("Failed to sort list", sections.get(1), c1);

        controller.remove(5);
        Assert.assertEquals("Failed to remove an entity from repository through controller", null, controller.get(5));
        Assert.assertEquals("Failed to remove an entity from repository through controller", c2, controller.get(6));
        controller.remove(c2);
        Assert.assertEquals("Failed to remove an entity from repository through controller", null, controller.get(6));
    }

    @Test
    public void paginatedRepositoryTest() {
        PaginatedRepository<Section, Integer> repo = new PaginatedRepository<>(new SectionValidator());
        for (int i = 0; i < 101; i++) {
            try {
                repo.add(new Section(i, "Num" + i, i * 3));
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        }
        Assert.assertTrue("Adding entities to a paginated repository does not work", repo.contains(100));
        Assert.assertTrue("Adding entities to a paginated repository does not work", repo.contains(50));
        Assert.assertTrue("Adding entities to a paginated repository does not work", repo.contains(30));

        repo.sortBy((t1, t2) -> t2.getNrLoc() - t1.getNrLoc());
        Assert.assertEquals("Sorting does not work", 100, (long) repo.getData().get(0).getId());
        Assert.assertEquals("Sorting does not work", 0, (long) repo.getData().get(100).getId());
        Assert.assertEquals("Sorting does not work", 50, (long) repo.getData().get(50).getId());

        repo.remove(repo.get(15));
        repo.remove(repo.get(60));
        repo.remove(repo.get(100));
        repo.remove(repo.get(30));
        Assert.assertFalse("Removing entities from a paginated repository does not work", repo.contains(15));
        Assert.assertFalse("Removing entities from a paginated repository does not work", repo.contains(100));
        Assert.assertFalse("Removing entities from a paginated repository does not work", repo.contains(60));
        Assert.assertFalse("Removing entities from a paginated repository does not work", repo.contains(30));

    }
}
