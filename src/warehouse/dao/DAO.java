package warehouse.dao;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * DAO (Data Access Object), permite grabar un objeto en un fichero y leerlo del
 * mismo. La grabacion y lectura es secuencial.
 * 
 * @author joseemilio
 *
 * @param <T>
 *            Tipo de objeto.
 */
public class DAO<T> {

	private DAO dao;

	private FileInputStream abrir(String path) {
		FileInputStream flujoR = null;
		File file = new File(path);
		try {
			if (file.exists()) {
				flujoR = new FileInputStream(path);
			}
		} catch (FileNotFoundException e) {
		}
		return flujoR;
	}

	private FileOutputStream abrir(String path, boolean adicion) {
		FileOutputStream flujoW = null;
		File file = new File(path);
		try {
			flujoW = new FileOutputStream(file, adicion);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return flujoW;

	}

	private boolean cerrarFlujo(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Recupera el primer objeto almacenado en el fichero especificado.
	 * 
	 * @param path
	 *            Ruta hasta el fichero.
	 * @return Retorna el objeto almacenado en la posicion 0 del fichero
	 *         especificado.
	 */
	public T leer(String path) {
		return leer(path, 0);
	}

	/**
	 * Recupera el objeto en la posicion especificada del fichero especificado.
	 * 
	 * @param path
	 *            Ruta hasta el fichero.
	 * @param posicion
	 *            Posicion del objeto en el fichero. Empieza desde la posicion 0.
	 * @return Retorna el objeto almacenado en la posicion indicada del fichero
	 *         especificado o NULL en caso de no ser encontrado.
	 */
	public T leer(String path, int posicion) {
		assert path != null && posicion >= 0;
		T t = null;
		FileInputStream flujoR = abrir(path);
		if (flujoR != null) {
			try {
				ObjectInputStream adaptador = new ObjectInputStream(flujoR);
				for (int i = 0; i <= posicion; i++) {
					t = (T) adaptador.readObject();
				}
			} catch (IOException | ClassNotFoundException e) {
				// e.printStackTrace();
				t = null;
			}
			cerrarFlujo(flujoR);
		}
		return t;
	}

	/**
	 * Graba un objeto en el fichero especificado. Por defecto se especifica que el
	 * objeto a grabar es el primero del fichero.
	 * 
	 * @param path
	 *            Ruta hasta el fichero. Por ejemplo: Partiendo desde la raiz del
	 *            proyecto, "data/fichero".
	 * @param t
	 *            Objeto a grabar.
	 * @return Retorna TRUE si se ha grabado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean grabar(String path, T t) {
		return grabar(path, t, false);
	}

	/**
	 * Graba un objeto en el fichero especificado.
	 * 
	 * @param path
	 *            Ruta hasta el fichero. Por ejemplo: Partiendo desde la raiz del
	 *            proyecto, "data/fichero".
	 * @param t
	 *            Objeto a grabar.
	 * @param adicion
	 *            TRUE si ya existen mas objetos en el fichero o FALSE en caso de
	 *            que el objeto sea el primero en grabarse.
	 * @return Retorna TRUE en caso de haberse grabado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean grabar(String path, T t, boolean adicion) {
		assert path != null && t != null;

		File file = new File(path);
		boolean existe = file.exists() && adicion;
		FileOutputStream flujoW = abrir(path, adicion);

		try {
			ObjectOutputStream adaptador = null;
			if (existe) {
				adaptador = new MyObjectOutputStream(flujoW);
			} else {
				adaptador = new ObjectOutputStream(flujoW);
			}
			adaptador.writeObject(t);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return cerrarFlujo(flujoW);
	}

	/**
	 * Elimina el archivo especificado.
	 * 
	 * @param path
	 *            Ruta hasta el fichero. Por ejemplo: Partiendo desde la raiz del
	 *            proyecto, "data/fichero".
	 * @return TRUE si el archivo ha sido eliminado o FALSE en caso contrario.
	 */
	public boolean borrarArchivo(String path) {
		File file = new File(path);

		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * Elimina un objeto de un fichero secuencial.
	 * 
	 * @param path
	 *            Ruta hasta el fichero. Por ejemplo: Partiendo desde la raiz del
	 *            proyecto, "data/fichero".
	 * @param posicion
	 *            Posicion que ocupa el objeto a eliminar dentro del fichero.
	 * @return Retorna TRUE si se ha eliminado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean borrarElemento(String path, Integer posicion) {
		int i = 0;
		T t = leer(path, i);
		while (t != null) {
			if (i != posicion) {
				grabar("copia", t, true);
			}
			i++;
			t = leer(path, i);
		}
		File original = new File(path);
		File copia = new File("copia");
		if (!copia.exists()) {
			try {
				copia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!original.delete() || !copia.renameTo(original)) {
			return false;
		}
		return true;
	}

	class MyObjectOutputStream extends ObjectOutputStream {
		public MyObjectOutputStream(OutputStream out) throws IOException {
			super(out);
		}

		@Override
		protected void writeStreamHeader() throws IOException {
			// Este metodo crea la cabecera. Es anulado.
		}
	}
}