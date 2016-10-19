package afterwind.lab1.test;

import afterwind.lab1.controller.CandidateController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.repository.IRepository;
import org.junit.Assert;
import org.junit.Test;

public class CandidateTest {

    @Test
    public void entityTest() {
        Candidate c = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Assert.assertEquals("Failed to get proper ID", 10, (int) c.getId());
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
    public void controllerTest() {
        CandidateController controller = new CandidateController();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        controller.updateCandidate(c1, "Andrei", "111", "idk");
        Assert.assertEquals("Failed to update candidate", "Andrei", c1.getName());
        Assert.assertEquals("Failed to update candidate", "111", c1.getTel());
        Assert.assertEquals("Failed to update candidate", "idk", c1.getAddress());
        Assert.assertEquals("Failed to update candidate", 10, (int) c1.getId());
    }

    @Test
    public void filterTest() {
        CandidateController controller = new CandidateController();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Candidate c2 = new Candidate(11, "Andrei", "111", "IDK");
        Candidate c3 = new Candidate(12, "Vlad", "222", "Task");
        Candidate c4 = new Candidate(13, "Vlad", "222", "Task");
        controller.add(c1);
        controller.add(c2);
        controller.add(c3);
        controller.add(c4);

        IRepository<Candidate, Integer> filter1 = controller.filterByName("Sergiu");
        Assert.assertTrue(filter1.get(10) == c1);
        IRepository<Candidate, Integer> filter2 = controller.filterByTelephone("111");
        Assert.assertTrue(filter2.get(11) == c2);
        IRepository<Candidate, Integer> filter3 = controller.filterByAddress("Task");
        Assert.assertTrue(filter3.get(12) == c3);
        Assert.assertTrue(filter3.get(13) == c4);
    }
}
