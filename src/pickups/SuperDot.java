package pickups;

import javax.swing.ImageIcon;


/**
 * Clase Dot, gestiona los objetos recolectables grandes del nivel. Hereda
 * de Pickable.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 * @see Pickable
 */
public class SuperDot extends Pickable {
	/**
	 * Constructor de la clase SuperDot que inicializa el tipo, los puntos y la imagen
	 * 
	 * @param x posicion inicial x
	 * @param y posicion inicial y
	 * 
	 */
	public SuperDot (int x, int y) {
		super(x,y);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("../resources/images/pickups/bigdot.png"));
		image = ii.getImage();
		points = 10;
		type = "SuperDot";
	}
}
