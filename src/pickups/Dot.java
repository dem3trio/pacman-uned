package pickups;

import javax.swing.ImageIcon;


/**
 * Clase Dot, gestiona los objetos recolectables pequenios del nivel. Hereda
 * de Pickable.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 * @see Pickable
 */

public class Dot extends Pickable{
	
	/**
	 * Constructor de la clase Dot que inicializa el tipo, los puntos y la imagen
	 * 
	 * @param posx posicion inicial x
	 * @param posy posicion inicial y
	 * 
	 */
	public Dot (int posx, int posy) {
		super (posx, posy);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("../resources/images/pickups/dot.png"));
		image = ii.getImage();
		points = 10;
		type = "Dot";
	}	
}
