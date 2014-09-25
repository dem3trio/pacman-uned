
package level;

import java.util.ArrayList;
import java.util.HashMap;

import java.awt.Image;

import javax.swing.ImageIcon;

import pickups.*;
import logic.Config;

/**
 * Clase Level,
 * 
 * almacena la información referente al nivel:
 *   - Estructura del nivel
 *   - Los objetos recolectables
 *   - Numero de filas y columnas
 *   - Los identificadores de los fantasmas disponibles en el nivel
 *   - Posicion inicial de cada fantasma y del jugador.
 *   - El patrón de imágenes del nivel.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 * 
 */
public class Level {
	
	/**
	 * Carpeta con el set de imatenes del nivel
	 */
	private String level_image_pattern; // "default";

	/**
	 * Estructura del nivel
	 */
	private int[][] cells;
	/**
	 * Cantidad de filas y columnas
	 */
	private int rowcount;
	private int colcount;
	/**
	 * Identificativos de los fantasmas en el nivel.
	 */
	private String[] ghosts_in_level;
	/**
	 * HashMap con las imagenes del nivel
	 */
	private HashMap <String, Image> images;
	/**
	 * Los objetos recolectables
	 */
	private ArrayList<Pickable> pickables;
	
	/**
	 * Las posiciones inciciales se guardan en arrays de 2 huecos.
	 * - Hueco 0 para eje X
	 * - Hueco 1 para eje Y
	 * TODO:
	 *  - Crear clase "position"
	 */
	private int[] player_initial_position;
	private int[] red_ghost_initial_position;
	private int[] blue_ghost_initial_position;
	private int[] orange_ghost_initial_position;
	private int[] pink_ghost_initial_position;
	
	private Config config;
	
	/**
	 * Constructor con valores por defecto. 
	 */
	public Level(){
		level_image_pattern = "default";
		
		images =  new HashMap <String, Image>();
		pickables = new ArrayList<Pickable>();
		player_initial_position = new int[2];
		player_initial_position[0] = 0;
		player_initial_position[1] = 0;
		
		config = Config.getInstance();
		
	}
	
	/**
	 * Establece la carpeta con el set de imagenes.
	 * @param pattern la carpeta con el set de imagenes
	 */
	public void setLevelImagePatternName(String pattern) {
		level_image_pattern = pattern;
	}
	
	
	/**
	 * devuelve el nombre de la carpeta con el set de imagenes 
	 * @return nombre de la carpeta con el set de imagenes
	 */
	
	public String getLevelImagePatternName() {
		return level_image_pattern;
	}
	
	
	/**
	 * Devuelve la ruta con el set de imagenes 
	 * @return ruta con set de imagenes
	 */
	
	public String getLevelImagePatternFolder() {
		return "../resources/images/tilesets/" + level_image_pattern + "/";
	}
	
	
	
	/**
	 * Inicializa el array bidimensional para almacenar el patron
	 * del nivel
	 * @param rows Numero de filas
	 * @param cols Numero de columnas
	 */
	
	public void createCellBoard(int rows, int cols) {
		rowcount = rows;
		colcount = cols;
		cells = new int[rows][cols];
	}
	
	/**
	 * Devuelve el numero de filas del nivel
	 * @return numero de filas
	 */
	
	public int getRowCount() {
		return rowcount;
	}
	
	
	/**
	 * Devuelve el numero de columnas del nivel
	 * @return numero de columnas
	 */
	
	public int getColCount() {
		return colcount;
	}
	
	/**
	 * Devuelve los objetos recolectables
	 * @return objetos recolectables
	 */
	
	public ArrayList<Pickable> getPickables() {
		return pickables;
	}
	
	
	/**
	 * Inicializa una celda
	 * @param row fila de la celda
	 * @param col columna de la celda
	 * @param value valor de la celda.
	 */
	
	public void setCell(int row, int col, int value) {
		cells[row][col] = value;
		
		
		
		int[] px_position;
		switch(value) {
			case 1: // Dot
				px_position = convertRowcolsToPixels(row, col);
				Dot dot = new Dot(px_position[0], px_position[1]);
				dot.setSize(config.getLevelBoxSize());
				dot.setPoints(10);
				pickables.add(dot);
				break;
			case 2: // SuperDot
				px_position = convertRowcolsToPixels(row, col);
				SuperDot s_dot = new SuperDot(px_position[0], px_position[1]);
				s_dot.setSize(config.getLevelBoxSize());
				s_dot.setPoints(20);
				pickables.add(s_dot);
				break;
			case 3: // Posicion inicial de jugador
				player_initial_position = convertRowcolsToPixels(row, col);
				break;
			case 4: // Posicion inicial de fantasma rojo
				red_ghost_initial_position = convertRowcolsToPixels(row, col);
				break;
			case 5: // Posicion inicial de fantasma naranja
				orange_ghost_initial_position = convertRowcolsToPixels(row, col);
				break;
			case 6: // Posicion inicial de fantasma azul
				blue_ghost_initial_position = convertRowcolsToPixels(row, col);
				break;
			case 7: // Posicion inicial de fantasma rosa
				pink_ghost_initial_position = convertRowcolsToPixels(row, col);
				break;
		}
	}
	
	/**
	 * Obtiene una celda
	 * @param row fila de la celda a devolver
	 * @param col columna de la celda a devolver
	 * @return el valor de la celda
	 */
	public int getCell(int row, int col) 
	{
		return cells[row][col];
	}
	
	/**
	 * Devuelve el valor de la posicion inicial del jugador
	 * @return int[] de 2 posiciones con la posicion x e y
	 */
	
	public int[] getPlayerInitialPosition() 
	{
		return player_initial_position;
	}
	
	
	/**
	 * Devuelve la imagen correspondiente a la celda
	 * @param row fila de la celda
	 * @param col columna de la celda
	 * @return imagen de la celda
	 */
	
	public Image getCellImage(int row, int col) {
		if (images == null) {
			createImageSet();
		}
		if ( cells[row][col] < 0 ) {
			if(!images.containsKey("not_navigable"+cells[row][col])) {
				createImage("not_navigable"+cells[row][col], cells[row][col] );
			}
			return images.get("not_navigable"+cells[row][col]);
		} else {
			return images.get("navigable");
		}
	}
	
	/**
	 * Crea una imagen en caso de no existir
	 * @param image_name nombre de la imagen en el HashMap
	 * @param filename nombre del fichero (sin extension)
	 */
	
	private void createImage (String image_name, int filename){

		String image_folder = getLevelImagePatternFolder();
		
		String resource = filename+".png";
		ImageIcon ii = new ImageIcon(this.getClass().getResource(image_folder + resource));
        images.put(image_name, ii.getImage());
	}
	
	/**
	 * Crea el set de imagenes para almacenarlo en memoria.
	 */
	
	public void createImageSet() {
		
		
		String image_folder = getLevelImagePatternFolder();
        
        String resource = "0.png";
        ImageIcon ii = new ImageIcon(this.getClass().getResource(image_folder + resource));
        images.put("navigable", ii.getImage());

	}
	
	/**
	 * Funcion de ayuda para convertir la posicion de filas-columnas en eje XY
	 * @param row fila
	 * @param col columna
	 * @return int[] de 2 posiciones con la posicion XY
	 */
	
	public int[] convertRowcolsToPixels(int row, int col) {
		int[] px_position = new int[2];
		px_position[0] = col * config.getLevelBoxSize();
		px_position[1] = row * config.getLevelBoxSize();
		return px_position;
	}
	
	/**
	 * Funcion de ayuda para obtener el centro en pixels (XY) de
	 * la celda situada en la fila row y la columna col
	 * @param row fila
	 * @param col columna
	 * @return int[] de 2 posiciones con la posicion XY centrada
	 */
	
	public int[] convertRowcolsToPixelsCentered(int row, int col) {
		int[] px_position = convertRowcolsToPixels(row, col);
		px_position[0] = px_position[0] + (config.getLevelBoxSize() / 2) ;
		px_position[1] = px_position[1] + (config.getLevelBoxSize() / 2) ;
		return px_position;
	}
	
	/**
	 * Funcion de ayuda para obtener la fila y columna en base a 
	 * eje xy
	 * @param x posicion X a obtener 
	 * @param y posicion Y a obtener
	 * @return int[] de 2 posiciones con la fila y la columna
	 */
	
	public int[] convertPixelsInRowCol(int x, int y) {
		int[] row_col = new int[2];
		// filas
		row_col[0] = y / config.getLevelBoxSize();
		// columnas
		row_col[1] = x / config.getLevelBoxSize();
		return row_col;
	}
	
	/**
	 * Devuelve el valor de la celda superior
	 * @return entero con el valor de la celda superior
	 */
	
	public int[] getUpperRowCol(int row, int col) {
		int[] row_col = new int[2];
		row_col[0] = row -1;
		row_col[1] = col;
		return row_col;
	}
	
	/**
	 * Devuelve el valor de la celda inferior
	 * @return entero con el valor de la celda inferior
	 */
	
	public int[] getBottomRowCol(int row, int col) {
		int[] row_col = new int[2];
		row_col[0] = row + 1;
		row_col[1] = col;
		return row_col;
	}
	
	/**
	 * Devuelve el valor de la celda a la izquierda
	 * @return entero con el valor de la celda a la izquierda
	 */
	
	public int[] getLeftRowCol(int row, int col) {
		int[] row_col = new int[2];
		row_col[0] = row;
		row_col[1] = col - 1;
		return row_col;
	}
	
	/**
	 * Devuelve el valor de la celda a la derecha
	 * @return entero con el valor de la celda a la derecha
	 */
	
	public int[] getRightRowCol(int row, int col) {
		int[] row_col = new int[2];
		row_col[0] = row;
		row_col[1] = col + 1;
		return row_col;
	}
	
	/**
	 * Determina si una celda es navegable por fantasmas o jugador.
	 * @return true si la celda es navegable.
	 */
	
	public boolean isCellNavigable(int row, int col) {
		return (cells[row][col] >= 0);
	}

	/**
	 * Establece los ids de los fantasmas disponibles en el nivel
	 * @param ghosts
	 */
	
	public void setGhosts(String[] ghosts){
		ghosts_in_level = ghosts;
	}
	
	/**
	 * Devuelve el set de ids de los fantasmas disponibles en el nivel
	 * @return set de ids de los fantasmas disponibles en el nivel
	 */
	
	public String[] getGhostsInLevel(){
		return ghosts_in_level;
	}
	
	
	/**
	 * Posicion inicial del fantasma solicitado
	 * @param id Identificador del fantasma a solicitar
	 * @return int[] de 2 huecos con posiciones XY
	 */
	
	public int[] getGhostInitialPosition (String id) {
		int[] retval = new int[2];
		retval[0] = 0;
		retval[1] = 0;
		
		if (id.equals("red"))
		{
			retval = red_ghost_initial_position;
		}
		
		if (id.equals("blue"))
		{
			retval = blue_ghost_initial_position;
		}
		
		if (id.equals("orange"))
		{
			retval = orange_ghost_initial_position;
		}
		
		if (id.equals("pink"))
		{
			retval = pink_ghost_initial_position;
		}
		
		return retval;
		
	}
}
