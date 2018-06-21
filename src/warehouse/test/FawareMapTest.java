package warehouse.test;

import static org.junit.Assert.*;
import java.util.TreeMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import warehouse.dao.DAO;
import warehouse.individual.FawareMap;

public class FawareMapTest {

	private static final String MAPA_MAP = "mapa.map";
	FawareMap<String, String> mapa;

	@Before
	public void setUp() throws Exception {
		mapa = new FawareMap<>(new TreeMap<>(), MAPA_MAP);
	}

	@Test
	public void test() {
		String[] clave = {"1", "2", "3"};
		String[] valor = { "Uno", "Dos", "Tres" };
		
		for (int i = 0; i < clave.length; i++) {
			assertTrue(mapa.grabar(clave[i], valor[i]));
			assertFalse(mapa.grabar(clave[i], valor[i]));
		}

		assertEquals(valor[0], mapa.obtener(clave[0]));
	}

	@After
	public void tearDown() throws Exception {
		new DAO<>().borrarArchivo(MAPA_MAP);
	}

}
