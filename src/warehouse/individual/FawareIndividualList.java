package warehouse.individual;

import java.util.AbstractList;
import warehouse.dao.DAO;

/**
 * FawareIndividualList permite tratar con cualquier lista heredera de
 * AbstractList. Podemos almacenar y recuperar objetos de dicha lista que, al
 * mismo tiempo, sera serializada.
 * 
 * @author joseemilio
 *
 * @param <T>
 *            Tipo de objeto a tratar.
 */
public class FawareIndividualList<T> {

	private AbstractList<T> list;
	private String path;
	private DAO<AbstractList<T>> dao;

	/**
	 * @param list
	 *            Ejemplar de clase heredera de AbstractList.
	 * @param path
	 *            Ruta hasta el fichero que guarda la lista.
	 */
	public FawareIndividualList(AbstractList<T> list, String path) {
		super();
		assert list != null && path != null;
		this.list = list;
		this.path = path;
		dao = new DAO<AbstractList<T>>();
	}

	private void getList() {
		AbstractList<T> temporal = dao.leer(path);
		if (temporal == null) {
			dao.grabar(path, list);
		} else {
			list = temporal;
		}
	}

	/**
	 * Recupera un objeto de la lista serializada.
	 * 
	 * @param index
	 *            Posicion que ocupa el objeto en la lista serializada.
	 * @return Retorna el objeto buscado o NULL en caso contrario.
	 */
	public T obtener(int index) {
		getList();
		if (index < list.size()) {
			return list.get(index);
		}
		return null;
	}

	/**
	 * Almacena un objeto en la lista.
	 * 
	 * @param t
	 *            Objeto a almacenar en la lista serializada.
	 * @return Retorna TRUE si se ha almacenado correctamente en la lista y esta se
	 *         ha serializado con exito o FALSE en cualquiera de los casos
	 *         contrarios.
	 */
	public boolean grabar(T t) {
		assert t != null;
		boolean retorno = false;
		getList();
		if (list.add(t) && dao.grabar(path, list)) {
			retorno = true;
		}
		return retorno;
	}
}
