package warehouse.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import warehouse.dao.DAO;
import warehouse.path.FawarePathDestination;

public class FawarePathDestinationTest {

	FawarePathDestination<String> instancia;

	@Before
	public void setUp() throws Exception {
		instancia = new FawarePathDestination<>("data", "num");
	}

	@Test
	public void test() {
		String[] numeros = { "Uno", "Dos", "Tres" };

		for (int i = 0; i < numeros.length; i++) {
			assertTrue(instancia.grabar("numeros", numeros[i], numeros[i]));
		}

		for (int i = 0; i < numeros.length; i++) {
			assertEquals(numeros[i], instancia.obtener("numeros", numeros[i]));
		}

		assertNotEquals(numeros[0], instancia.obtener("numeros", numeros[1]));
		assertNull(instancia.obtener("numeros", "Cuatro"));
	}

	@After
	public void tearDown() throws Exception {
		new DAO<>().borrarArchivo("data/numeros/Uno.num");
		new DAO<>().borrarArchivo("data/numeros/Dos.num");
		new DAO<>().borrarArchivo("data/numeros/Tres.num");
	}

}
