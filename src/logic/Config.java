package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import logic.GhostConfig;


/**
 * 
 * Clase Config que guarda la configuración general de la aplicación.
 * De momento:
 *   - Tamanio en pixels de las celdas
 *   - Fantasmas disponibles y su configuración.
 *   
 * Se instancia siguiendo un patrón Singleton ya que su información
 * es sensible de ser utilizada en cualquier punto de la aplicación,
 * y sólo es necesario cargar su información 1 vez.
 * 
 * Recoge la información de un fichero JSON situdo en la carpeta:
 *   - src/resources/config.json
 * 
 * TODO:
 *   - Incluir en la configuración los objetos pickables (Dot y SuperDot)
 *     para quitar esas clases.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 *
 */

public class Config {
	
	/**
	 * Atributos de la clase:
	 * - level_box_size: Tamanio de las celdas del juego en pixeles.
	 * - ghost_c: HashMap que contiene la configuración de cada fantasma. 
	 */
	private int level_box_size;
	private long invincible_mode_duration;
	
	HashMap<String, GhostConfig> ghosts_c;
	
	/** 
	 * atributo que contiene la instancia del objeto.
	 */
	private static Config instance = null;
	
	/**
	 * Constructor, declarado como protegido para obligar a utilizar el método
	 * getInstance().
	 * Recoge el fichero JSON
	 */
	protected Config() {
		
		ghosts_c = new HashMap<String, GhostConfig>();
		JSONObject obj = getConfigFile();
		
		Long size = (Long) obj.get("level_box_size");
		level_box_size = size.intValue();
		invincible_mode_duration = (Long) obj.get("invincible_mode_duration");
		
		JSONArray ghosts = (JSONArray) obj.get("ghosts");
		
		Iterator <JSONObject> iterator = ghosts.iterator();
		while(iterator.hasNext()) {
			JSONObject g = iterator.next();
			String id = (String) g.get("id");
			String name = (String) g.get("name");
			String imageset = (String) g.get("imageset");
			Long HvsV = (Long) g.get("HvsV");
			Long speed = (Long) g.get("speed");
			Long accuracy = (Long) g.get("accuracy_threshold");
			GhostConfig ghost_c = new GhostConfig(id, name, imageset,speed.intValue(),accuracy.intValue(),HvsV.intValue());
			ghosts_c.put(id, ghost_c);
		}
	}
	
	
	/** 
	 * Método estático que gestiona la instancia Singleton de la clase. 
	 * @return devuelve una instancia inicializada de la clase.
	 */
	
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	
	/**
	 * Devuelve el tamanio en pixels de las celdas de la aplicacion
	 * @return tamanio en pixels de las celdas de la aplicacion
	 */
	
	public int getLevelBoxSize() {
		return level_box_size;
	}
	
	
	/**
	 * Devuelve el valor en milisegundos del modo invencible
	 * @return milisegundos de la duracion de modo invencible.
	 */
	
	public long getInvincibleModeDuration(){
		return invincible_mode_duration;
	}
	
	/**
	 * 
	 * @param id el identificador del fantasma
	 * @return objeto GhostConfig con la configuracion del fantasma solicitado
	 * @see GhostConfig
	 */
	
	public GhostConfig getGhostConfig(String id) {
		return ghosts_c.get(id);
	}
	
	
	/**
	 * Método privado para obtener un objeto JSONObject con la información
	 * del fichero cargada.
	 * 
	 * @return objeto JSONObject con la informacion del fichero cargada.
	 * @see JSONObject
	 */
	private JSONObject getConfigFile() {
		try {
			String path = new File(".").getCanonicalPath();
			String file_path = path +"/src/resources/config/config.json";
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(new FileReader(file_path));
			JSONObject ret_value = (JSONObject) obj; 
			
			return ret_value;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
