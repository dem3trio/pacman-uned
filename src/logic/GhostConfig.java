package logic;


/**
 * Clase GhostConfig para almacenar la configuración de un fantasma.
 * Para evitar utilizar objetos json cada vez que se requiere la información
 * de un fantasma complicando la legibilidad y escritura del código, se ha 
 * optado por la creación de esta clase de ayuda.
 * 
 * Como su finalidad es de sólo lectura, sólo se crean los "getters" de los 
 * diferentes atributos
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 *
 */
public class GhostConfig {
	String id;
	String name;
	String imageset;
	int speed;
	int accuracy_threshold;
	int HvsV;
	
	public GhostConfig (String id, String name, String imageset, int speed, int accuracy_threshold, int HvsV) {
		this.id = id;
		this.name = name;
		this.imageset = imageset;
		this.speed = speed;
		this.accuracy_threshold = accuracy_threshold;
		this.HvsV = HvsV;
	}
	
	/**
	 * Devuelve el id anotado en la configuración del fantasma
	 * @return el id del fantasma
	 */
	
	public String getId() {
		return id;
	}
	
	/**
	 * Devuelve el nombre anotado en la configuración del fantasma
	 * @return el nombre del fantasma
	 */
	
	public String getName() {
		return name;
	}
	
	/**
	 * Devuelve el set de imagenes anotado en la configuración del fantasma
	 * @return el set de imagenes del fantasma
	 */
	
	public String getImageSet() {
		return imageset;
	}
	
	
	/**
	 * Devuelve la velocidad anotado en la configuración del fantasma
	 * @return la velocidad del fantasma
	 */
	public int getSpeed() {
		return speed;
	}
	
	
	/**
	 * Devuelve el umbral de precisión anotado en la configuración del fantasma
	 * @return el umbral de precisión del fantasma
	 */
	
	public int getAccuracyThreshold() {
		return accuracy_threshold;
	}
	
	/**
	 * Devuelve el valor del parametro HvsV anotado en la configuración 
	 * del fantasma
	 * @return el valor HvsV del fantasma
	 */
	
	public int getHvsV() {
		return HvsV;
	}

}
