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
        repo.remove(c1);
        Assert.assertEquals("Failed to remove candidate from repository", null, repo.get(10));
        Assert.assertEquals("Failed to remove proper candidate from repository", c2, repo.get(11));
    }

    @Test
    public void controllerTest() {
        CandidateController controller = new CandidateController();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        controller.addCandidate(c1);
        Assert.assertEquals("Failed to save a candidate to repository through controller", c1, controller.getCandidate(10));
        Candidate c2 = new Candidate(11, "Victor", "000111222", "Kappa");
        controller.addCandidate(c2);
        Assert.assertEquals("Failed to save a candidate to repository through controller", c2, controller.getCandidate(11));

        controller.removeCandidate(10);
        Assert.assertEquals("Failed to remove a candidate from repository through controller", null, controller.getCandidate(10));
        Assert.assertEquals("Failed to remove a candidate from repository through controller", c2, controller.getCandidate(11));
        controller.removeCandidate(c2);
        Assert.assertEquals("Failed to remove a candidate from repository through controller", null, controller.getCandidate(11));
    }
}
