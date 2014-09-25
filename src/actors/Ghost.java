package actors;
import graphics.AnimationManager;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;

import actors.Movable;

/**
 * Clase Ghost que maneja las instancias de los fantasmas. Hereda
 * de la clase Movable que gestiona la acción en general del movimiento.
 * En esta clase se gestiona la inteligencia artificial de los fantasmas
 * en base a los parametros HvsV (Horizontal vs Vertical) y accuracy_threshold (límite
 * de eficacia).
 * 
 * El ancho y el alto (width y height) son virtuales. No corresponden con el 
 * ancho y alto de la imagen del fantasma. De esta manera podemos permitir cierto
 * solapamiento para dar la sensación de que efectivamente han tocado al jugador ya que
 * han invadido el espacio del mismo.
 * 
 * Gracias al sistema de configuración vía JSON y al algoritmo de búsqueda mediante
 * los parámetros anteriormente citados, no es necesario crear una clase
 * específica por cada fantasma. 
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 * @see Movable
 * 
 */

public class Ghost extends Movable{
	
	/**
	 * Propiedades de la clase Ghost:
	 * has_collided    - Utilizada para determinar si ha colisionado con otro 
	 *                   fantasma, agilizar las busquedas de colisiones, y evitar
	 *                   problemas con dobles colisiones.
	 * start_dead_time - Hora en milisegundos a la que empezó el estado DEAD
	 * state           - Estado actual del fantasma.
	 * width           - Ancho virtual del fantasma
	 * height          - Altura virtual del fantasma
	 * HvsV            - Preferencia a la hora de elegir buscar en horizontal o 
	 *                   vertical. (A menor valor, es más probable que elija horizontal
	 *                   y viceversa)
	 * accuracy_threshold - límite o umbral de eficacia. Cuanto menor sea el valor menos
	 *                      se preferirá buscar al jugador. De esta manera se evitan
	 *                      atascos del fantasma recalculando la próxima dirección.
	 * id              - Identificador del fantasma (red, blue, orange y pink)
	 * imageset        - Set de imagenes del fantasma
	 * name            - Nombre del fantasma (definido en el fichero JSON)
	 */

	private boolean has_collided;
	private ArrayList<String> collides_with;
	private long start_dead_time;
	private int state, width, height, HvsV, accuracy_threshold;
	private String id, imageset, name;

	
	/**
	 * Sistema de animacion
	 */
	private AnimationManager anim_manager;
	
	/**
	 * Variables estáticas para definir los estados del fantasma.
	 * CHASING - Persiguiendo
	 * SCARY   - Asustado
	 * DEAD    - Muerto
	 */
	
	public static final int CHASING = 1;
	public static final int SCARY = 2;
	public static final int DEAD = 3;
	
	/**
	 * Constructor.Inicializa los valores iniciales del fantasma. El ancho y alto 
	 * se asignan automaticamente por el parametro size. Se reduce en un 90% el tamanio
	 * para dar la sensacion de solapamiento sobre el jugador u otros fantasmas.
	 * 
	 * 
	 * @param x  Posicion X inicial del fantasma
	 * @param y  Posicion Y inicial del fantasma
	 * @param size Tamanio virtual del fantasma. Se asigna a ancho y alto automaticamente
	 *             transformandolo a un 90% del valor dado.
	 * @param id   Identificador del fantasma
	 * @param imageset Set de imagenes
	 * @param HvsV Preferencia de horizontal vs vertical
	 * @param accuracy_threshold limite de precisión del fantasma.
	 */
	
	public Ghost(int x, int y, int size, String id, String imageset, int HvsV, int accuracy_threshold)
	{
		super(x, y);
		state = CHASING;
		this.id = id;
		this.imageset = imageset;
		this.HvsV = HvsV;
		this.accuracy_threshold = accuracy_threshold;
		this.width = (int)(((long) size) * 0.95);
		this.height = (int)(((long) size) * 0.95);
		this.has_collided = false;
		this.collides_with = new ArrayList<String>();
		loadImageSet();
	}
	
	/**
	 * Devuelve el identificador del fantasma.
	 * @return Un String con el id del fantasma
	 */
	
	public String getID() {
		return id;
	}
	
	
	/**
	 * Establece si el fantasma ha chocado o no. 
	 * @param collided boolean que define si ha colisionado o ha dejado de colisionar
	 */
	
	public void setHasCollided(boolean collided) {
		has_collided = collided;
	}
	
	
	/**
	 * Metodo para saber si esta colisionando con un fantasma en concreto
	 * @param id id del fantasma
	 * @return true si esta colisionando con el fantasma informado
	 */
	public boolean isCollidingWithGhost(String id){
		return (collides_with.indexOf(id) != -1);
	}
	
	
	/**
	 * Metodo para quitar de la lista el fantasma con el que esta colisionando
	 * @param id identificador del fantasma a quitar la colision.
	 */
	public void removeGhostCollision(String id) {
		int ghost_index = collides_with.indexOf(id);
		if (ghost_index >= 0) {
			collides_with.remove(ghost_index);
		}
	}
	
	
	/**
	 * Aniadir a la lista de fantasmas colisionados
	 * @param id anaiade el id del fantasma a la lista de fantasmas colisionados
	 */
	public void addGhostCollision(String id) {
		int ghost_index = collides_with.indexOf(id);
		if (ghost_index < 0) {
			collides_with.add(id);
		}
	}
	
	/**
	 * Devuelve el numero de fantasmas con los que esta colisionando
	 * @return numero de fantasmas con el que esta colisionando
	 */
	
	public int withManyGhostCollides(){
		return collides_with.size();
	}
	
	/**
	 * Devuelve el estado del fantasma. Comparar con los atributos
	 * estáticos de la clase.
	 * 
	 * @return devuelve un entero con el estado del fantasma
	 * @see Movable
	 * 
	 */
	
	public int getState() {
		return state;
	}
	
	
	/**
	 * Establece el estado del personaje. En caso de ser el estado DEAD,
	 * inicializa la hora a la que murio, para que empiece a hacerse la comprobacion
	 * de si debe volver a la vida.
	 * 
	 * @param new_state nuevo estado, los valores posibles son los establecidos
	 *                  como variables estaticas de la clase.
	 */
	
	public void setState(int new_state) {
		state = new_state;
		if (new_state == DEAD)
		{
			start_dead_time = new Date().getTime();
		}
	}
	
	
	/**
	 * Comprueba el tiempo que lleva en estado DEAD.
	 * En caso de llevar 5 segundos o más, inicializa el valor
	 * de la hora a 0, y cambia el estado a CHASING.
	 */
	
	public void checkDeadTime() {
		long new_time = new Date().getTime();
		if (new_time >= start_dead_time + 5000) {
			state = CHASING;
			start_dead_time = 0;
		}
	}
	
	
	/**
	 * Getter para saber si el fantasma ya ha colisionado con otro fantasma
	 * previamente.
	 * @return Devuelve un valor booleano indicando si ha colisionado
	 *         o no.
	 */
	
	public boolean hasCollided() {
		return has_collided;
	}
	
	
	/**
	 * Método con la lógica principal de búsqueda del fantasma.
	 * Se utilizan números aleatorios y los valores de los atributos HvsV
	 * (horizontal vs vertical) y accuracy_threshold.
	 * Cuanto menor sea la configuración de HvsV, es posible que el fantasma
	 * prefiera buscar en horizontal. Y cuanto menor sea el número de 
	 * accuracy_threshold será menos probable que el fantasma decida buscar al jugador.
	 * 
	 * @param player_x posicion X actual del jugador
	 * @param player_y posicion Y actual del jugador
	 */
	
	
	public void calculateNextDirection(int player_x, int player_y) {
		
		int rn;
		Random r = new Random();
		
		// Se ha establecido un umbral de error para no preferir siempre horizontal o vertical
		// para evitar estancamientos de los fantasmas.
		
		// Cuanto mas bajo sea el numero aleatorio mas se preferira horizontal en base al atributo HvsV
		rn = r.nextInt(100);
		boolean search_vertical = (HvsV < rn);

		if (search_vertical) {
			if (player_y > GetY()) {
				setNextDirection(DOWN);
			} else {
				setNextDirection(UP);
			}
		} else {
			if (player_x > GetX()) {
				setNextDirection(RIGHT);
			} else {
				setNextDirection(LEFT);
			}
		}
		
		// Para evitar estancamientos, ahora decidiremos si vamos a ir en busca del jugador
		
		rn = r.nextInt(100);
		if (accuracy_threshold < rn) {
			// No buscamos a pacman.
			int nextdir;
			do{
				nextdir = r.nextInt(4);
			}while(nextdir == getActualDirection());
			setNextDirection(nextdir);
		}
		


	}
	
	
	/**
	 * Carga las imágenes del fantasma
	 * 
	 */
	
	private void loadImageSet() {
		anim_manager = new AnimationManager();
			try {
				
				String[] anim = new String[2];
				anim[0] = getImageSetFolder() + "up-1.png";
				anim[1] = getImageSetFolder() + "up-2.png";
				
				anim_manager.addAnimation("up", 200, 0, anim);
				
				anim[0] = getImageSetFolder() + "down-1.png";
				anim[1] = getImageSetFolder() + "down-2.png";
				
				anim_manager.addAnimation("down", 200, 0, anim);
				
				anim[0] = getImageSetFolder() + "left-1.png";
				anim[1] = getImageSetFolder() + "left-2.png";
				
				anim_manager.addAnimation("left", 200, 0, anim);
				
				anim[0] = getImageSetFolder() + "right-1.png";
				anim[1] = getImageSetFolder() + "right-2.png";
				
				anim_manager.addAnimation("right", 200, 0, anim);
				
				anim[0] = getImageSetFolder("scary") + "scary-1.png";
				anim[1] = getImageSetFolder("scary") + "scary-2.png";
				
				anim_manager.addAnimation("scary", 200, 0, anim);
				
				anim = new String[1];
				anim[0] = getImageSetFolder("dead") + "dead.png";

				anim_manager.addAnimation("dead", 1000, 0, anim);
				
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
	
	}
	
	
	/**
	 * Funcion polimorfica: devuelve la ruta del set de imagenes del fantasma.
	 * 
	 * @return Cadena de texto con la ruta de las imagenes.
	 */
	
	private String getImageSetFolder() {
		return "../resources/images/actors/ghosts/" + imageset + "/";
	}
	
	/**
	 * Devuelve la cadena de texto con la ruta de la imagen. Esta version
	 * fuerza a otro set de imagenes.
	 * 
	 * @param other_imageset
	 * @return Cadena de texto con la ruta de la imagenes.
	 */
	
	private String getImageSetFolder(String other_imageset) {
		return "../resources/images/actors/ghosts/" + other_imageset + "/";
	}
	
	/**
	 * Devuelve la imagen en base al estado actual del fantasma.
	 * @return Devuelve la imagen.
	 * @see Image
	 */
	
	public Image getImage() {
		//System.out.println();
		switch(state){
			default:
			case CHASING:
				switch(getActualDirection()) {
					case Movable.UP:
						return anim_manager.getImage("up");
					case Movable.DOWN:
						return anim_manager.getImage("down");
					case Movable.LEFT:
						return anim_manager.getImage("left");
					case Movable.RIGHT:
					default:
						return anim_manager.getImage("right");
				}
				
			case SCARY:
				return anim_manager.getImage("scary");
			case DEAD:
				return anim_manager.getImage("dead");
		}
	}
	
	
	/**
	 * Devuelve un objeto Bounds con la posicion actual y tamanio virtual
	 * del fantasma.
	 * 
	 * @return objeto Bounds con la posicion y tamaño del objeto.
	 * @see Rectangle
	 */
	
	public Rectangle getBounds() {
		Rectangle b = new Rectangle(GetX(), GetY(), width, height);
		return b;
	}

	
	/**
	 * Establece el nombre del fantasma
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * Devuelve el el nombre del fantasma.
	 * @return devuelve el nombre del fantasma.
	 */
	public String getName() {
		return name;
	}
}
