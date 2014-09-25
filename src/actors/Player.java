package actors;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

import actors.Movable;
import graphics.AnimationManager;

/**
 * 
 * Clase Player. Su finalidad es gestionar la animación, posición
 * y movimiento del jugador. Se le transmite la pulsación del teclado
 * desde la clase que la instancia.
 * 
 * El ancho y el alto (width y height) son virtuales. No corresponden con el 
 * ancho y alto de la imagen del fantasma. De esta manera podemos permitir cierto
 * solapamiento para dar la sensación de que efectivamente han tocado al jugador ya que
 * han invadido el espacio del mismo.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 *
 */

public class Player extends Movable{
	
	/**
	 * Atributos de la clase player:
	 * width : ancho virtual de la clase.
	 * height: alto virtual de la clase
	 * anim_manager : objeto con el gestor de animación.
	 * 
	 */
	
	private int width, height;
	private AnimationManager anim_manager;

	
	/**
	 * Constructor. Se inicializa la posición en el eje X,Y
	 * 
	 * @param x posicion x inicial del jugador.
	 * @param y posicion y inicial del jugador
	 * @param size tamanio virtual del objeto.
	 */
	public Player(int x, int y, int size)
	{
		super(x,y);
		
		setSize(size);
		
		anim_manager = new AnimationManager();
		
		setPlayerImages();
		
	}
	
	/**
	 * Establece el set de imagenes del jugador
	 */
	
	private void setPlayerImages() {
		

		try {
			
			String[] anim = new String[2];
			anim[0] = "../resources/images/actors/player/up-1.png";
			anim[1] = "../resources/images/actors/player/up-2.png";
			
			anim_manager.addAnimation("up", 200, 0, anim);
			
			anim[0] = "../resources/images/actors/player/down-1.png";
			anim[1] = "../resources/images/actors/player/down-2.png";
			
			anim_manager.addAnimation("down", 200, 0, anim);
			
			anim[0] = "../resources/images/actors/player/left-1.png";
			anim[1] = "../resources/images/actors/player/left-2.png";
			
			anim_manager.addAnimation("left", 200, 0, anim);
			
			anim[0] = "../resources/images/actors/player/right-1.png";
			anim[1] = "../resources/images/actors/player/right-2.png";
			
			anim_manager.addAnimation("right", 200, 0, anim);
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Devuelve la imagen que corresponda a la dirección actual del jugador.
	 * @return devuelve la imagen correspondiente al jugador
	 * @see Image
	 */
	
	public Image getImage() {
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
		
	}
	
	
	/**
	 * Dependiendo de la tecla pulsada establece la dirección
	 * del jugador.
	 * 
	 * @param e
	 * @see KeyEvent
	 * @see Movable
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch(key) {
			case KeyEvent.VK_LEFT:
					setNextDirection(LEFT);
					break;
			case KeyEvent.VK_RIGHT:
					setNextDirection(RIGHT);
					break;
			case KeyEvent.VK_UP:
					setNextDirection(UP);
					break;
			case KeyEvent.VK_DOWN:
					setNextDirection(DOWN);
					break;
		}

	}
	
	/**
	 * Devuelve el tamaño virtual y la posicion del jugador.
	 * 
	 * @return el tamaño virtual y posicion del jugador.
	 * @see Rectangle
	 */
	
	public Rectangle getBounds() {
		Rectangle b = new Rectangle(GetX(), GetY(), width, height);
		return b;
	}
	
	/**
	 * Establece el tamaño virtual del jugador. 
	 * @param size Tamanio en pixels.
	 */
	public void setSize(int size){
		width = (int)(((long) size) * 0.90);
		height = (int)(((long) size) * 0.90);
	}
	
	
}
