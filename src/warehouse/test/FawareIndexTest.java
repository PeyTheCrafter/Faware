package warehouse.test;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import warehouse.index.FawareIndex;
import warehouse.String;

public class FawareIndexTest {

	private static final String CLIENTES_DAT = new String("datos.dat");
	private static final String INDICE_DAT = new String("indice.dat");
	private FawareIndex<String, java.lang.String> instancia;

	@Before
	public void setUp() {
		instancia = new FawareIndex<>(INDICE_DAT.getString(), CLIENTES_DAT.getString());
	}

	@Test
	public void testObtener() {
		String[] numerosEnteros = {new String("1"), new String("2"), new String("3")};
		java.lang.String[] numerosCaracteres = { "Uno", "Dos", "Tres" };
		
		for (int i = 0; i < numerosCaracteres.length; i++) {
			instancia.grabar(numerosEnteros[i], numerosCaracteres[i]);
		}
		
		for (int i = 0; i < numerosCaracteres.length; i++) {
			assertEquals(numerosEnteros[i].getString(), instancia.obtener(numerosCaracteres[i]).getString());
		}
		
		assertNull(instancia.obtener("Cuatro"));
	}

	@Test
	public void testGrabar() {
		String[] numerosEnteros = {new String("1"), new String("2"), new String("3")};
		java.lang.String[] numerosCaracteres = {"Uno", "Dos", "Tres"};
		
		for (int i = 0; i < numerosCaracteres.length; i++) {
			assertTrue(instancia.grabar(numerosEnteros[i], numerosCaracteres[i]));
		}
		
		for (int j = 0; j < numerosCaracteres.length; j++) {
			assertEquals(numerosEnteros[j].getString(), instancia.obtener(numerosCaracteres[j]).getString());
		}
	}

	@Test
	public void testborrar() {
		String[] numerosEnteros = {new String("1"), new String("2"), new String("3")};
		java.lang.String[] numerosCaracteres = {"Uno", "Dos", "Tres"};
		
		for (int i = 0; i < numerosCaracteres.length; i++) {
			assertTrue(instancia.grabar(numerosEnteros[i], numerosCaracteres[i]));
		}
		
		instancia.borrar("Uno");
		assertNotNull(instancia.obtener("Dos"));
	}

	@After
	public void tearDown() {
		File file = new File(INDICE_DAT.getString());
		file.delete();
		file = new File(CLIENTES_DAT.getString());
		file.delete();
	}
}
