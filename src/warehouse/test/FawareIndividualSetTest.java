package warehouse.test;

import static org.junit.Assert.*;
import java.util.TreeSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import warehouse.dao.DAO;
import warehouse.individual.FawareIndividualSet;

public class FawareIndividualSetTest {

	private static final String CONJUNTO_SET = "conjunto.set";
	private String string = "string";
	FawareIndividualSet<String> instancia;

	@Before
	public void setUp() throws Exception {
		// Para probar asercion
		try {
			instancia = new FawareIndividualSet<>(null, CONJUNTO_SET);
			assertNull(instancia.first());
			fail();
		} catch (AssertionError e) {
			// Si falla, todo correcto
		}
		instancia = new FawareIndividualSet<>(new TreeSet<>(), CONJUNTO_SET);
	}

	@Test
	public void testObtener() {
		assertNull(instancia.obtener(0));
		assertNull(instancia.first());
		assertTrue(instancia.grabar(string));
		assertNotNull(instancia.first());
		assertEquals(instancia.first(), instancia.last());
		assertEquals(string, instancia.obtener(0));
	}

	@Test
	public void testGrabar() {
		assertTrue(instancia.grabar(string));
		assertFalse(instancia.grabar(string));
		assertEquals(instancia.first(), instancia.last());
		assertNull(instancia.obtener(1));
		try {
			// No puede grabar un objeto null
			assertTrue(instancia.grabar(null));
			fail();
		} catch (AssertionError e) {
		}
	}

	@After
	public void tearDown() throws Exception {
		new DAO<>().borrarArchivo(CONJUNTO_SET);
	}

}
