package afterwind.lab1.test;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.controller.CandidateController;
import afterwind.lab1.repository.Repository;
import org.junit.Assert;
import org.junit.Test;

public class CandidateTest {

    @Test
    public void entityTest() {
        Candidate c = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Assert.assertEquals("Failed to get proper ID", 10, c.getId());
        Assert.assertEquals("Failed to get proper name", "Sergiu", c.getName());
        Assert.assertEquals("Failed to get proper telephone number", "000111222", c.getTel());
        Assert.assertEquals("Failed to get proper address", "Kappa", c.getAddress());
        c.setName("Victor");
        c.setTel("00000000");
        c.setAddress("Dunarii");
        Assert.assertEquals("Failed to set proper name", "Victor", c.getName());
        Assert.assertEquals("Failed to set proper telephone number", "00000000", c.getTel());
        Assert.assertEquals("Failed to set proper address", "Dunarii", c.getAddress());
    }

    @Test
    public void repositoryTest() {
        Repository<Candidate> repo = new Repository<>(Candidate.class);
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        repo.add(c1);
        Assert.assertEquals("Failed to save a candidate to repository" ,c1, repo.get(10));
        Candidate c2 = new Candidate(11, "Victor", "000111222", "Kappa");
        repo.add(c2);
        Assert.assertEquals("Failed to save candidate to repository", c2, repo.get(11));
        Assert.assertEquals("Failed to get proper size of the repository", 2, repo.getSize());
        repo.remove(c1);
        Assert.assertEquals("Failed to remove candidate from repository", null, repo.get(10));
        Assert.assertEquals("Failed to remove proper candidate from repository", c2, repo.get(11));
    }

    @Test
    public void controllerTest() {
        CandidateController controller = new CandidateController();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        controller.add(c1);
        Assert.assertEquals("Failed to save a candidate to repository through controller", c1, controller.get(10));
        Candidate c2 = new Candidate(11, "Victor", "000111222", "Kappa");
        controller.add(c2);
        Assert.assertEquals("Failed to save a candidate to repository through controller", c2, controller.get(11));
        Assert.assertEquals("Failed to get proper amount of candidates", 2, controller.getSize());
        int nextId = controller.getNextId();
        Assert.assertFalse("Failed to get new unique id", nextId == 10 || nextId == 11);
        controller.updateCandidate(c1, "Andrei", "111", "idk");
        Assert.assertEquals("Failed to update candidate", "Andrei", c1.getName());
        Assert.assertEquals("Failed to update candidate", "111", c1.getTel());
        Assert.assertEquals("Failed to update candidate", "idk", c1.getAddress());
        controller.remove(10);
        Assert.assertEquals("Failed to remove a candidate from repository through controller", null, controller.get(10));
        Assert.assertEquals("Failed to remove a candidate from repository through controller", c2, controller.get(11));
        controller.remove(c2);
        Assert.assertEquals("Failed to remove a candidate from repository through controller", null, controller.get(11));
    }
}
