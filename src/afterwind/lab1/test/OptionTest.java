package afterwind.lab1.test;

import afterwind.lab1.controller.OptionController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import org.junit.Assert;
import org.junit.Test;

public class OptionTest {

    @Test
    public void entityTest() {
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Candidate c2 = new Candidate(11, "Andrei", "000111222", "Kappa");
        Section s1 = new Section(5, "Info", 100);
        Section s2 = new Section(3, "Matematica", 100);

        Option o = new Option(0, s1, c2);
        Assert.assertEquals("Failed to get proper ID", 0, o.getId());
        Assert.assertEquals("Failed to get proper section", s1, o.getSection());
        Assert.assertEquals("Failed to get proper candidate", c2, o.getCandidate());
        o.setCandidate(c1);
        o.setSection(s2);
        Assert.assertEquals("Failed to set section", s2, o.getSection());
        Assert.assertEquals("Failed to set candidate", c1, o.getCandidate());
    }

    @Test
    public void controllerTest() {
        OptionController controller = new OptionController();
        Candidate c1 = new Candidate(10, "Sergiu", "000111222", "Kappa");
        Candidate c2 = new Candidate(11, "Andrei", "000111222", "Kappa");
        Section s1 = new Section(5, "Info", 100);
        Section s2 = new Section(3, "Matematica", 100);

        Option o = new Option(0, s1, c2);
        controller.updateOption(o, c1, s2);
        Assert.assertEquals("Failed to update option", c1, o.getCandidate());
        Assert.assertEquals("Failed to update option", s2, o.getSection());
        Assert.assertEquals("Failed to update option", 0, o.getId());
    }

}
