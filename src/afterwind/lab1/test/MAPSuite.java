package afterwind.lab1.test;

import org.junit.runner.RunWith;

@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({
        SectionTest.class,
        CandidateTest.class,
        OptionTest.class,
        GenericTests.class
})
public class MAPSuite {
}
