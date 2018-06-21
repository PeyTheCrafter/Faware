package warehouse.path;

import java.io.File;
import java.util.TreeMap;

import warehouse.dao.DAO;
import warehouse.individual.FawareMap;

/**
 * FawareMappedPath permite grabar un objeto en un fichero. En el mapa asociado
 * se almacena la clave de busqueda del objeto y el nombre del archivo donde se
 * almacena dicho objeto.
 * 
 * @author joseemilio
 *
 * @param <T>
 *            Tipo de objeto que se desea almacenar.
 * @param <K>
 *            Tipo de clave del mapa.
 * @param <V>
 *            Tipo de valor del mapa.
 */
public class FawareMappedPath<T, K, V> {

	private DAO<Object> dao;
	private FawareMap<K, V> mapa;
	private String extension;
	private String pathDatos, nombreArchivoMapa;

	/**
	 * En este constructor se definen los diferentes parametros necesarios.
	 * 
	 * @param extension
	 *            La extension que tendran los archivos. Un archivo por objeto.
	 * @param pathDatosyMapa
	 *            La ruta donde se almacenaran los archivos de indice y objetos.
	 * @param nombreArchivoMapa
	 *            Nombre del archivo mapa junto su extension.
	 */
	public FawareMappedPath(String extension, String pathDatosyMapa, String nombreArchivoMapa) {
		super();
		this.pathDatos = pathDatosyMapa;
		this.extension = extension;
		this.nombreArchivoMapa = nombreArchivoMapa;
		assert validate();
		dao = new DAO();
		crearMapa();
	}

	private void crearMapa() {
		String pathname = "./" + this.pathDatos;
		File file = new File(pathname);
		if (!file.exists()) {
			file.mkdirs();
		}
		pathname = pathname + "/" + nombreArchivoMapa;
		this.mapa = new FawareMap<>(new TreeMap<K, V>(), pathname);
	}

	private boolean validate() {
		return this.pathDatos != null && extension != null;
	}

	private String estableceRuta(V i) {
		return pathDatos + "/" + String.valueOf(i) + "." + extension;
	}

	/**
	 * Devuelve el objeto almacenado en el fichero cuyo nombre esta asociado a la
	 * clave que se le pasa por parametro.
	 * 
	 * @param k
	 *            Clave del nombre del fichero que alberga el objeto.
	 * @return Retorna el objeto buscado o NULL en caso contrario.
	 */
	public T obtener(K k) {
		V v = mapa.obtener(k);
		T t = null;
		if (v != null) {
			t = (T) dao.leer(estableceRuta(v));
		}
		return t;
	}

	/**
	 * Almacena un objeto en un fichero unico y se registra en el mapa con un par
	 * clave/valor.
	 * 
	 * @param t
	 *            Objeto a almacenar.
	 * @param k
	 *            Clave a partir de la cual buscar el objeto.
	 * @param i
	 *            Valor de la clave. Nombre del fichero.
	 * @return Retorna TRUE si ha sido grabado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean grabar(T t, K k, V i) {
		if (mapa.grabar(k, i)) {
			if (dao.grabar(estableceRuta(i), t)) {
				return true;
			} else {
				mapa.borrar(k);
			}
		}
		return false;
	}

	public int obtenNumero() {
		return mapa.getSize();
	}

}
