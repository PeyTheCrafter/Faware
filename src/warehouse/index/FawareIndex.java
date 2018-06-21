package warehouse.index;

import java.util.TreeMap;
import warehouse.Indexable;
import warehouse.dao.DAO;

/**
 * FawareIndex permite grabar objetos en un fichero secuencialmente y almacenar
 * en un indice una clave que lleva como valor asociado, la posicion dentro del
 * archivo en la que se encuentra cada objeto.
 * 
 * @author joseemilio
 *
 * @param <T>
 *            Objeto a almacenar.
 * @param <K>
 *            Clave del indice.
 */
public class FawareIndex<T, K> {

	private String pathIndice;
	private String pathDatos;
	private TreeMap<K, Integer> indice;
	private DAO<Object> dao;

	/**
	 * Inicializa los atributos
	 * 
	 * @param pathIndice
	 *            Ruta de acceso al fichero que almacenara el indice.
	 * @param pathDatos
	 *            Ruta de acceso al fichero que almacenara los objetos
	 *            secuencialmente.
	 */
	public FawareIndex(String pathIndice, String pathDatos) {
		super();
		this.pathIndice = pathIndice;
		this.pathDatos = pathDatos;
		this.indice = new TreeMap<>();
		this.dao = new DAO<>();
		assert validate();
	}

	private boolean validate() {
		return this.pathIndice != null && this.pathDatos != null;
	}

	private void recargaIndice() {
		indice = new TreeMap<>();
		int posicion = 0;
		T t = (T) dao.leer(pathDatos, posicion);
		while (t != null) {
			Indexable<K> elemento = (Indexable<K>) t;
			K k = elemento.getKey();
			indice.put(k, posicion);
			posicion++;
			t = (T) dao.leer(pathDatos, posicion);
		}
	}

	private void leerIndice() {
		indice = (TreeMap<K, Integer>) dao.leer(pathIndice);
	}

	/**
	 * Almacena el objeto pasado por parametro en el fichero secuencial e inserta un
	 * nuevo registro en el indice.
	 * 
	 * @param t
	 *            Objeto a grabar en el fichero.
	 * @param k
	 *            Clave del indice.
	 * @return Retorna TRUE si se ha grabado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean grabar(T t, K k) {
		Integer value = indice.size();
		if (indice.put(k, value) == null) {
			if (dao.grabar(pathDatos, t, true)) {
				dao.grabar(pathIndice, indice);
				return true;
			} else {
				leerIndice();
			}
		}
		return false;
	}

	/**
	 * Busca y devuelve un objeto almacenado en el fichero secuencial.
	 * 
	 * @param k
	 *            Clave del indice que lleva asociada un valor que representa la
	 *            posicion que ocupa el objeto que buscamos dentro del fichero.
	 * @return Retorna el objeto buscado o NULL en caso de no ser encontrado.
	 */
	public T obtener(K k) {
		leerIndice();
		if (indice == null) {
			indice = new TreeMap<>();
			dao.grabar(pathIndice, indice);
		}
		T retorno = null;
		Integer posicion = indice.get(k);
		if (posicion != null) {
			retorno = (T) dao.leer(pathDatos, posicion);
		}
		return retorno;
	}

	/**
	 * Borra un elemento del fichero de objetos.
	 * 
	 * @param k
	 *            Clave referenciada al objeto que se desea borrar.
	 * @return Retorna TRUE si se ha eliminado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean borrar(K k) {
		leerIndice();
		boolean retorno = false;
		if (indice.containsKey(k)) {
			Integer posicion = indice.remove(k);
			if (posicion != null) {
				retorno = dao.borrarElemento(pathDatos, posicion);
				if (!retorno) {
					leerIndice();
				} else {
					recargaIndice();
					dao.grabar(pathIndice, indice);
				}

			}
		}
		return retorno;
	}

}
