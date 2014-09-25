package actors;

/**
 * Clase principal que gestiona el movimiento y la posición de
 * los objetos que hereden de ella. La lógica de si puede moverse 
 * en una dirección u otra, o la elección de la dirección se delega
 * en otras clases.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 *
 */

public class Movable {
	
	/**
	 * 
	 * Atributos de la clase:
	 * x: posicion x actual del objeto
	 * y: posicion y actual del objeto
	 * dx: X delta cantidad de pixeles a desplazarse en el eje x
	 * dy: Y delta cantidad de pixeles a desplazarse en el eje y
	 * moving: devuelve si el objeto esta moviendose.
	 * velocity: velocidad del objeto.
	 * 
	 */
	
	private int x;
	private int y;
	private int dx;  // Delta x
	private int dy;  // Delta y
	private boolean moving;
	private double velocity;
	
	/**
	 * Variables estáticas con las posibles direcciones
	 */
	
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int LEFT = 4;
	
	/**
	 * Variables con la dirección actual y la próxima.
	 * Almacenando estos valores se consigue un movimiento fluido sin
	 * que el jugador tenga que estar pulsando constantemente.
	 */
	
	private int actual_direction;
	private int next_direction;
	
	/**
	 * Constructor, inicializa todos los atributost por defecto
	 * y la posicion inicial vía parametros
	 * @param new_x posición x inicial
	 * @param new_y posicion y inicial
	 */
	
	public Movable(int new_x, int new_y)
	{
		x = new_x;
		y = new_y;
		dy = 1;
		dx = 1;
		moving = false;
		velocity = 1.0;
		actual_direction = 0;
		next_direction = 0;
	}
	
	/**
	 * Establece la dirección actual
	 * @param direction dirección a seguir (UP, DOWN, LEFT, RIGHT)
	 */
	
	public void setDirection(int direction) {
		actual_direction = direction;
	}
	
	
	/**
	 * Mueve el objeto en base a la dirección actual.
	 */
	
	public void move()
	{
		switch(actual_direction)
		{
			case UP: MoveUp(); break;
			case DOWN: MoveDown(); break;
			case RIGHT: MoveRight(); break;
			case LEFT: MoveLeft(); break;
			default: break; //do nothing
		}
	}

	/**
	 * Devuelve una matriz de 2 huecos con la posición.
	 * TODO:
	 * - Crear un objeto Position con los atributos X e Y
	 * 
	 * @return Matriz de enteros, posicion 0: x, posicion 1: y
	 */
	
	public int[] GetPosition()
	{

		int[] ret_val = new int[2];
		
		ret_val[0] = this.x;
		ret_val[1] = this.y;
		
		return ret_val; 
	}
	
	
	/**
	 * Desplaza al objeto arriba
	 */
	
	public void MoveUp()
	{
		this.y = this.y - this.dy;
	}
	
	
	/**
	 * Desplaza al objeto abajo
	 */
	
	public void MoveDown()
	{
		this.y = this.y + this.dy;
	}

	
	/**
	 * Desplaza al objeto a la derecha
	 */
	
	public void MoveRight()
	{
		this.x = this.x + this.dx;
	}
	
	
	/**
	 * Desplaza al objeto a la izquierda
	 */
	
	public void MoveLeft()
	{
		this.x = this.x - this.dx;
	}
	
	
	/**
	 * Devuelve si el objeto se está moviendo
	 * @return valor del estado de movimiento.
	 */
	
	public boolean IsMoving()
	{
		return this.moving;
	}
		
	
	/**
	 * Establece la velocidad del objeto
	 * @param velocity valor con la velocidad.
	 */
	
	public void SetVelocity(double velocity)
	{
		this.velocity = velocity;
	}
	
	
	/**
	 * Devuelve la velocidad del objeto
	 * @return un double con la velocidad
	 */
	
	public double GetVelocity()
	{
		return this.velocity;
	}
	
	
	/**
	 * Devuelve la posicion x del objeto
	 * @return posición X del objeto
	 */
	
	public int GetX()
	{
		return this.x;
	}
	
	
	/**
	 * Establece la nueva posición x del objeto
	 * @param x nueva posicion en el eje x del objeto
	 */
	
	public void SetX(int x)
	{
		this.x = x;
	}
	
	
	/**
	 * Devuelve la posicion y del objeto
	 * @return posición Y del objeto
	 */
	
	public int GetY()
	{
		return this.y;
	}
	
	
	/**
	 * Establece la nueva posición y del objeto
	 * @param y nueva posicion en el eje y del objeto.
	 */
	
	public void SetY(int y)
	{
		this.y = y;
	}
	
	
	/**
	 * Devuelve el valor delta x. El desplazamiento en píxeles en eje x
	 * @return Devuelve valor DeltaX
	 */
	
	public int GetDeltaX()
	{
		return this.dx;
	}
	
	
	/**
	 * Establece el valor delta x. El desplazamiento en píxeles en eje x
	 * @param dx El nuevo valor de DeltaX
	 */
	
	public void SetDeltaX(int dx)
	{
		this.dx = dx;
	}
	
	
	/**
	 * Devuelve el valor delta y. El desplazamiento en píxeles en eje y
	 * @return Devuelve valor DeltaY
	 */
	
	public int GetDeltaY()
	{
		return this.dy;
	}
	
	
	/**
	 * Establece el valor delta y. El desplazamiento en píxeles en eje y
	 * @param dy El nuevo valor de DeltaY
	 */
	
	public void SetDeltaY(int dy)
	{
		this.dy = dy;
	}
	
	
	/**
	 * Detiene el objeto.
	 */
	
	public void stop()
	{
		actual_direction = 0;
		next_direction = 0;
		moving = false;
	}
	
	
	/**
	 * Devuelve la dirección actual.
	 * @return entero con la direccion
	 */
	
	public int getActualDirection() {
		return actual_direction;
	}
	
	
	/**
	 * Devuelve la próxima dirección del objeto.
	 * @return entero con la direccion siguiente.
	 */
	
	public int getNextDirection() {
		return next_direction;
	}
	
	
	/**
	 * Establece la proxima dirección del objeto. Si la actual
	 * es 0, establece la actual.
	 * @param direction La proxima dirección del objeto.
	 */
	
	public void setNextDirection(int direction){
		if(actual_direction == 0) {
			actual_direction = direction;
			next_direction = 0;
		} else {
			next_direction = direction;
		}
		
	}
	
	/**
	 * Comprueba si la proxima dirección es la opuesta a la actual.
	 * @return devuelve true si la proxima dirección es la opuesta a la actual.
	 */
	
	public boolean isNextDirectionOppositeToActual() {
		return (actual_direction == UP && next_direction == DOWN) || 
			 (actual_direction == DOWN && next_direction == UP) ||
			 (actual_direction == RIGHT && next_direction == LEFT) ||
			 (actual_direction == LEFT && next_direction == RIGHT);
	}
	
	
	/**
	 * Cambia la dirección actual por la proxima dirección.
	 */

	public void changeToNextDirection(){
		actual_direction = next_direction;
		next_direction = 0;
	} 
	
	
	/**
	 * Cambia la dirección a la dirección contraria. Usada
	 * en las colisiones entre fantasmas.
	 */
	
	public void setOppositeDirection(){
		switch(actual_direction) {
			case UP: actual_direction = DOWN; break;
			case DOWN: actual_direction = UP; break;
			case LEFT: actual_direction = RIGHT; break;
			case RIGHT: actual_direction = LEFT; break;
		}
	}
	
	
	/**
	 * Devuelve la direccion contraria a la actual.
	 * @return entero con la direccion contraria a la actual
	 */
	
	public int getOppositeDirection() {
		switch(actual_direction) {
			case UP: return DOWN;
			case DOWN: return UP;
			case LEFT: return RIGHT;
			case RIGHT: return LEFT; 
		}
		return 0;
	}
	
}

