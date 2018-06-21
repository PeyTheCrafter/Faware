package warehouse.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import warehouse.dao.DAO;
import warehouse.path.FawareMappedPath;

public class FawareMappedPathTest {

	private static final String PATH = "data";
	private static final String EXTENSION = "dat";
	private static final String PATH_MAPA = "map.map";
	private FawareMappedPath<String, String, String> instancia;

	@Before
	public void setUp() throws Exception {
		instancia = new FawareMappedPath<>(EXTENSION, PATH, PATH_MAPA);
	}

	@Test
	public void test() {
		assertNull(instancia.obtener("Uno"));

		String[] numerosEnteros = { "1", "2", "3" };
		String[] numerosCaracteres = { "Uno", "Dos", "Tres" };
		
		for (int i = 0; i < numerosCaracteres.length; i++) {
			assertTrue(instancia.grabar(numerosEnteros[i], numerosCaracteres[i], numerosCaracteres[i]));
			assertFalse(instancia.grabar(numerosEnteros[i], numerosCaracteres[i], numerosCaracteres[i]));
		}
		
		for (int i = 0; i < numerosCaracteres.length; i++) {
			assertEquals(numerosEnteros[i], instancia.obtener(numerosCaracteres[i]));
		}
		
		assertNull(instancia.obtener("Cuatro"));
	}

	@After
	public void tearDown() throws Exception {
		assertTrue(new DAO<>().borrarArchivo("./" + PATH + "/" + PATH_MAPA));
		assertTrue(new DAO<>().borrarArchivo("./" + PATH + "/Uno." + EXTENSION));
		assertTrue(new DAO<>().borrarArchivo("./" + PATH + "/Dos." + EXTENSION));
		assertTrue(new DAO<>().borrarArchivo("./" + PATH + "/Tres." + EXTENSION));
	}

}
