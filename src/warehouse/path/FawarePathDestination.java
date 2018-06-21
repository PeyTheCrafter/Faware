package warehouse.path;

import java.io.File;

import warehouse.dao.DAO;

/**
 * FawarePathDestination permite grabar objetos en ficheros y directorios
 * diferentes a partir de una ruta inicial.
 * 
 * @author joseemilio
 *
 * @param <T>
 *            Tipo de objeto.
 */
public class FawarePathDestination<T> {

	private DAO<Object> dao;
	private String pathInicial;
	private String extension;

	/**
	 * @param pathInicial
	 *            Ruta del directorio inicial a partir del cual se crearan los
	 *            diferentes directorios y ficheros.
	 * @param extension
	 *            Extension de los ficheros.
	 */
	public FawarePathDestination(String pathInicial, String extension) {
		super();
		this.pathInicial = pathInicial;
		this.extension = extension;
		this.dao = new DAO<>();
	}

	private void quitarExtension(String[] elements) {
		String[] definitivos = new String[elements.length];
		for (int i = 0; i < elements.length; i++) {
			definitivos[i] = elements[i].split("\\.")[0];
		}
	}

	/**
	 * Graba un objeto en un fichero.
	 * 
	 * @param pathDestino
	 *            Ruta del directorio donde se almacenara el fichero con el objeto
	 *            serializado.
	 * @param nombreElemento
	 *            Nombre del fichero.
	 * @param t
	 *            Objeto a grabar.
	 * @return Retorna TRUE si se ha grabado correctamente o FALSE en caso
	 *         contrario.
	 */
	public boolean grabar(String pathDestino, String nombreElemento, T t) {
		File path = new File(pathInicial + "/" + pathDestino);
		if (!path.exists()) {
			path.mkdirs();
		}
		return dao.grabar(pathInicial + "/" + pathDestino + "/" + nombreElemento + "." + extension, t);
	}

	/**
	 * Obtiene el objeto almacenado en un fichero.
	 * 
	 * @param pathDestino
	 *            Directorio donde se encuentra el fichero con el objeto.
	 * @param nombreElemento
	 *            Nombre del fichero que almacena el objeto.
	 * @return Retorna el objeto buscado o NULL en caso contrario.
	 */
	public T obtener(String pathDestino, String nombreElemento) {

		return (T) dao.leer(pathInicial + "/" + pathDestino + "/" + nombreElemento + "." + extension);
	}

	/**
	 * Devuelve el nombre de los archivos que estan dentro de una carpeta.
	 * 
	 * @param pathDestino
	 *            Ruta del directorio que almacena los archivos.
	 * @return Retorna un vector de tipo String con los nombres (sin la extension)
	 *         de los ficheros que almacena.
	 */
	public String[] getFiles(String pathDestino) {
		String[] elements = null;
		File carpeta = new File(pathInicial + "/" + pathDestino);
		if (carpeta.exists() && carpeta.isDirectory()) {
			elements = carpeta.list();
			quitarExtension(elements);
		}
		return elements;
	}
}
