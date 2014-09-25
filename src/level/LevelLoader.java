package level;

import level.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Cargador de niveles. Coge la informacion de un fichero JSON, y lo carga en un objeto
 * de tipo Level.
 * La información a recoger es de momento:
 *   - La estructura del mapa
 *   - Fantasmas disponibles en el mapa.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 * @see Level
 */

public class LevelLoader {
	

	/**
	 * Constructor. Necesita el numero del nivel a cargar.
	 * Carga la información del nivel en un objeto Level.
	 * 
	 * @param levelnumber El numero del nivel a cargar.
	 * @return Devuelve un objeto Level con la informacion del nivel.
	 * @see Level
	 */
	
	public Level LoadLevel(int levelnumber){
		
		Level board = new Level();
		
		JSONObject obj = getJSONFile(levelnumber);
		
		board.setLevelImagePatternName((String) obj.get("level_image_pattern"));
		
		board.createImageSet();		
 
		JSONArray pattern = (JSONArray) obj.get("level_pattern");
		
		// Obtenemos el primer elemento para saber cuantas columnas tiene
		JSONArray firstelement = (JSONArray) pattern.get(0);
		int cols = firstelement.size();
		
		// Vaciamos firstelement ya que no lo necesitamos mas
		firstelement = null;
		
		// Obtenemos las filas las filas
		int rows = pattern.size();
		
		// Inicializamos las celdas en base al tamanio
		board.createCellBoard(rows, cols);
		
		Iterator<JSONArray> rowiterator = pattern.iterator();
		
		int r = 0;
		while (rowiterator.hasNext()) {
			
			JSONArray actual_row = rowiterator.next();
			Iterator<Long> coliterator = actual_row.iterator();
			
			int c = 0;
			while(coliterator.hasNext()) {
				int value = coliterator.next().intValue();
				board.setCell(r, c, value);
				c++;
			}
			r++;
		}
		
		JSONArray ghosts_in_level = (JSONArray) obj.get("ghosts");
		String[] ghosts = new String[ghosts_in_level.size()];
		
		int i = 0;
		
		Iterator<String> ghosts_iterator = ghosts_in_level.iterator();
		while(ghosts_iterator.hasNext()) {
			ghosts[i] = ghosts_iterator.next();
			i++;
		}
		board.setGhosts(ghosts);
		
		return board;

		
	}
	
	
	/**
	 * Obtiene el objeto json con la informacion del nivel.
	 * 
	 * @param levelnumber
	 * @return devuelve el objeto json con la informacion del nivel.
	 * @see JSONObject
	 */
	
	private JSONObject getJSONFile(int levelnumber) {
		try {
			String path = new File(".").getCanonicalPath();
			String levelfile = "level" + levelnumber + ".json";
			String file_path = path +"/src/resources/levels/" + levelfile;
			
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
