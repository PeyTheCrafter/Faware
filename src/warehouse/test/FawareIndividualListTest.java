package warehouse.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import warehouse.dao.DAO;
import warehouse.individual.FawareIndividualList;

public class FawareIndividualListTest {

	private static final String LISTA_LIST = "lista.list";
	String string = "String";
	FawareIndividualList<String> instancia;

	@Before
	public void setUp() throws Exception {
		// Para probar asercion
		try {
			instancia = new FawareIndividualList<>(null, LISTA_LIST);
			assertNull(instancia.obtener(0));
			fail();
		} catch (AssertionError e) {
			// Si falla, todo correcto
		}
		instancia = new FawareIndividualList<>(new ArrayList<>(), LISTA_LIST);
	}

	@Test
	public void testObtener() {
		assertNull(instancia.obtener(0));
		assertTrue(instancia.grabar(string));
		assertNotNull(instancia.obtener(0));
		assertNull(instancia.obtener(1));
	}

	@Test
	public void testGrabar() {
		assertTrue(instancia.grabar(string));
		assertTrue(instancia.grabar(string));
		assertEquals(instancia.obtener(0), instancia.obtener(1));
		try {
			// No puede grabar un objeto null
			assertTrue(instancia.grabar(null));
			fail();
		} catch (AssertionError e) {
		}
	}

	@After
	public void tearDown() throws Exception {
		new DAO<>().borrarArchivo(LISTA_LIST);
	}

}
