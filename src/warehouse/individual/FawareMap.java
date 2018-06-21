package warehouse.individual;

import java.util.Map;

import warehouse.dao.DAO;

/**
 * FawareMap permite tratar con un mapa de par clave/valor serializado.
 * 
 * @author joseemilio
 *
 * @param <K>
 *            Tipo de clave del mapa.
 * @param <V>
 *            Tipo de valor del mapa.
 */
public class FawareMap<K, V> {

	private Map<K, V> mapa;
	private String rutaMapa;
	private DAO<Map<K, V>> dao;

	/**
	 * @param mapa
	 * @param rutaMapa
	 *            Ruta hasta el fichero donde se almacenara el mapa.
	 */
	public FawareMap(Map<K, V> mapa, String rutaMapa) {
		super();
		this.mapa = mapa;
		this.rutaMapa = rutaMapa;
		dao = new DAO<>();
		getMapa();
	}

	private void getMapa() {
		Map<K, V> temporal = dao.leer(rutaMapa);
		if (temporal == null) {
			dao.grabar(rutaMapa, mapa);
		} else {
			mapa = temporal;
		}
	}

	/**
	 * Obtiene el valor asociado a una clave del mapa.
	 * 
	 * @param k
	 *            Clave de busqueda.
	 * @return Retorna el valor asociado a la clave de busqueda o NULL en caso de no
	 *         encontrarse la clave.
	 */
	public V obtener(K k) {
		getMapa();
		try {
			return mapa.get(k);
		} catch (NullPointerException e) {
			System.out.println("no existe");
		}
		return null;
	}

	/**
	 * Registra un par clave/valor en el mapa.
	 * 
	 * @param k
	 *            Nueva clave.
	 * @param v
	 *            Nuevo valor.
	 * @return Retorna TRUE si se ha registrado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean grabar(K k, V v) {
		getMapa();
		if (mapa.put(k, v) == null && dao.grabar(rutaMapa, mapa)) {
			return true;
		}
		return false;
	}

	/**
	 * Borra un registro del mapa.
	 * 
	 * @param k
	 *            Clave del registro a borrar.
	 * @return Retorna TRUE si se ha eliminado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean borrar(K k) {
		if (mapa.remove(k) == null) {
			return false;
		}
		return true;
	}

	public String getRutaMapa() {
		return rutaMapa;
	}

	/**
	 * @return Retorna la longitud del mapa.
	 */
	public int getSize() {
		return mapa.size();
	}
}
