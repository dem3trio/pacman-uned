package pickups;

import java.awt.Image;
import java.awt.Rectangle;


/**
 * Clase Pickable. Es la clase principal de los objetos recolectables.
 * Establece los atributos y métodos minimos para cada uno de ellos.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 *
 */
public class Pickable {
	
	private int x;
	private int y;
	private int width;
	private int height;
	protected Image image;
	protected int points;
	protected String type;

	/**
	 * Constructor. Inicializa la posicion inicial en el eje XY
	 * @param x posicion X inicial del objeto
	 * @param y posicion Y inicial del objeto
	 */
	public Pickable(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Devuelve la imagen del objeto recolectable
	 * @return la imagen del objeto recolectable.
	 * @see Image
	 */
	
	public Image getImage()
	{
		return image;
	}
	
	
	/**
	 * Devuelve la posicion X del objeto recolectable en el eje XY
	 * @return la posicion x del elemento
	 */
	
	public int getX()
	{
		return x;
	}
	
	/**
	 * Devuelve la posicion Y del objeto recolectable en el eje XY
	 * @return la posicion y del elemento
	 */
	
	public int getY()
	{
		return y;
	}
	
	/**
	 * Devuelve la posicion el tipo de elemento (inicializado en objetos hijos)
	 * @return el tipo de elemento.
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * Devuelve los puntos del elemento
	 * @return los puntos del elemento.
	 */
	
	public int getPoints()
	{
		return points;
	}
	
	
	/**
	 * Establece los puntos del elemento
	 * @param points
	 */
	
	public void setPoints(int points)
	{
		this.points = points;
	}
	
	/**
	 * Establece el tamaño virtual del recolectable. 
	 * @param size Tamanio en pixels.
	 */
	
	public void setSize(int size) {
		width = (int)(((long) size) * 0.90);
		height = (int)(((long) size) * 0.90);
	}
	
	
	/**
	 * Devuelve el tamaño virtual y la posicion del recolectable.
	 * 
	 * @return el tamaño virtual y la posición del recolectable
	 * @see Rectangle
	 */
	
	public Rectangle getBounds() {
		Rectangle b = new Rectangle(x, y, width, height);
		return b;
	}
	
}
