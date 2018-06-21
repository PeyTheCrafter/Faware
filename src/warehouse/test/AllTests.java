package warehouse.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

// Activar -ea
@RunWith(Suite.class)
@SuiteClasses({ FawareIndexTest.class, FawareIndividualListTest.class, FawareIndividualSetTest.class,
		FawareMapTest.class, FawarePathDestinationTest.class, FawareMappedPathTest.class })
public class AllTests {
}
