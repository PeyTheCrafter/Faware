package warehouse.individual;

import java.util.NavigableSet;
import warehouse.dao.DAO;

/**
 * FawareIndividualSet permite tratar con cualquier set (conjunto de objetos no
 * repetidos) heredero de NavigableSet. Podemos almacenar y recuperar objetos de
 * dicho set que, al mismo tiempo, sera serializado.
 * 
 * @author joseemilio
 *
 * @param <T>
 *            Tipo de objeto.
 */
public class FawareIndividualSet<T> {

	private NavigableSet<T> conjunto;
	private String path;
	private DAO<NavigableSet<T>> dao;

	/**
	 * @param set
	 *            Set que implementa NavigableSet.
	 * @param path
	 *            Ruta del archivo que almacenara el set.
	 */
	public FawareIndividualSet(NavigableSet<T> set, String path) {
		super();
		assert set != null && path != null;
		this.conjunto = set;
		this.path = path;
		dao = new DAO<>();
	}

	private void getSet() {
		NavigableSet<T> temporal = dao.leer(path);
		if (temporal == null) {
			dao.grabar(path, conjunto);
		} else {
			conjunto = temporal;
		}
	}

	/**
	 * Recupera el primer elemento del set.
	 * 
	 * @return Retorna el primer objeto del set o NULL si no existe.
	 */
	public T first() {
		getSet();
		try {
			return conjunto.first();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Retorna el ultimo elemento del set.
	 * 
	 * @return Retorna el ultimo elemento del set o NULL si no existe.
	 */
	public T last() {
		getSet();
		return conjunto.last();
	}

	/**
	 * Obtiene un objeto almacenado en una posicion del set.
	 * 
	 * @param index
	 *            Posicion del objeto almacenado.
	 * @return Retorna el objeto buscado o NULL en caso contrario.
	 */
	public T obtener(int index) {
		assert index >= 0;
		getSet();
		if (index < conjunto.size()) {
			return (T) conjunto.toArray()[index];
		}
		return null;
	}

	/**
	 * Permite grabar un objeto en el set.
	 * 
	 * @param t
	 *            Objeto a grabar.
	 * @return Retorna TRUE si se ha grabado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean grabar(T t) {
		assert t != null;
		boolean retorno = false;
		getSet();
		if (conjunto.add(t) && dao.grabar(path, conjunto)) {
			retorno = true;
		}
		return retorno;
	}
}
