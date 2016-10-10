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
    public void controllerTest() {
        CandidateController controller = new CandidateController();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        controller.updateCandidate(c1, "Andrei", "111", "idk");
        Assert.assertEquals("Failed to update candidate", "Andrei", c1.getName());
        Assert.assertEquals("Failed to update candidate", "111", c1.getTel());
        Assert.assertEquals("Failed to update candidate", "idk", c1.getAddress());
        Assert.assertEquals("Failed to update candidate", 10, c1.getId());
    }
}
