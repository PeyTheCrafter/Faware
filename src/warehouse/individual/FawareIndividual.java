package warehouse.individual;

import warehouse.dao.DAO;

/**
 * FawareIndividual es una version simplificada de DAO.
 * 
 * @author joseemilio
 *
 * @param <T>
 *            Tipo de objeto.
 */
public class FawareIndividual<T> {
	
	private DAO<T> dao;
	
	public FawareIndividual() {
		dao = new DAO<>();
	}

	/**
	 * Obtiene el primer objeto serializado de un fichero.
	 * 
	 * @param path
	 *            Ruta hasta el fichero que contiene los objetos.
	 * @return Retorna el objeto encontrado o NULL en caso contrario.
	 */
	public T obtener(String path) {
		return dao.leer(path);
	}

	/**
	 * Graba un objeto en un fichero.
	 * 
	 * @param path
	 *            Ruta hasta el fichero que almacenara el objeto
	 * @param t
	 *            Objeto a grabar.
	 * @return Retorna TRUE si se ha grabado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean grabar(String path, T t) {
		return dao.grabar(path, t);
	}

}
