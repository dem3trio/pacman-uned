package level;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import pickups.Pickable;
import actors.*;
import logic.Config;
import logic.GhostConfig;
import hud.HUD;

/**
 * Clase que lleva la logica principal del juego: render y actualizacion
 * de los objetos. Obtiene la configuración principal de los fantasmas,
 * tamaño de las celdas del objeto Config que accede a un fichero JSON.
 * 
 * 
 */

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener{
	
	/**
	 * atributos con la configuracion principal, configuracion del nivel,
	 * jugador, fantasmas y el timer de swing
	 */
	
	private Config gameconfig;
	private Timer timer;
	private Level level;
	private Player player;
	private HUD hud;
	private ArrayList <Ghost> ghosts;
	private Font text_font;
	
	/**
	 * Atributos para definir e identificar las pantallas del juego
	 */
	
	private int game_screen;
	public static final int START_SCREEN = 0;
	public static final int WIN_SCREEN = 1;
	public static final int PAUSE_SCREEN = 2;
	public static final int GAME_OVER_SCREEN = 3;
	public static final int PLAY_SCREEN = 4;
	
	/**
	 * Atributos para manejar el modo invencible (comer un objeto grande)
	 */
	private boolean invincible_mode;
	private long start_invincible_mode;
	
	

	/**
	 * Constructor
	 */
	public Board()
	{
		// Cargamos las opciones del JPanel y aniadimos el listener para los eventos de 
		// teclado
		
		addKeyListener(new TAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		setBackground(Color.BLACK);
		setTextFont();
		// Cargamos una instancia de la configuración (fantasmas y tamanio de pixels)
		gameconfig = Config.getInstance();
		
		
		// Arrancamos con la pantalla de inicio
		game_screen = START_SCREEN;
		
		hud = new HUD();
		
		// Arrancamos el timer del juego.
		timer = new Timer(5, this);
		timer.start();
		
		
		
		
	}
	
	/**
	 * Carga y configuración del nivel
	 */
	
	private void configureGame() {
		invincible_mode = false;
		start_invincible_mode = 0;
		// Cargamos el nivel
		level = new LevelLoader().LoadLevel(1);

		// Cargamos fantasmas
		loadGhosts();
		
		// Obtenemos la posicion inicial del jugador...
		int[] player_pos = level.getPlayerInitialPosition();
		// ... e instanciamos al jugador
		player = new Player(player_pos[0], player_pos[1], gameconfig.getLevelBoxSize());
		
		// Reseteamos la puntuacion
		hud.resetScore();
		
	}
	
	/**
	 * Carga y configuración de los fantasmas
	 */
	
	private void loadGhosts(){

		ghosts = new ArrayList<Ghost>();
		String[] ghosts_in_level = level.getGhostsInLevel();
		for (String s: ghosts_in_level)
	    {
		  GhostConfig gc = gameconfig.getGhostConfig(s);
		  int[] pos = level.getGhostInitialPosition(gc.getId());
		  
		  Ghost g = new Ghost(	pos[0], 
				  				pos[1], 
				  				gameconfig.getLevelBoxSize(), 
				  				gc.getId(), 
				  				gc.getImageSet(), 
				  				gc.getHvsV(), 
				  				gc.getAccuracyThreshold());
	      ghosts.add(g);
	    }
	}
	
	/**************************************************
	 *       Logica de movimiento/actualizacion       *
	 **************************************************/

	/**
	 * Accion por defecto al suscribirse al Timer
	 * @param e objeto ActionEvent
	 */
	public void actionPerformed (ActionEvent e){
		
		// Comprobacion de juego pausado.
		if (game_screen == PLAY_SCREEN){ //!game_paused && !start_screen && !game_over && !game_win
			
			checkInvincibleMode();
			// Deteccion de colisiones
			checkPickables();
			checkPlayerGhosts();
			
			
			// mover fantasmas
			moveGhosts();
			// mover jugador			
			movePlayer();

		}
		
		// Redibujar escenario
		repaint();
	}

	
	/**
	 * Logica para gestionar la proxima direccion del fantasma en caso de colisionar.
	 */
	
	private void calculateGhostNextDirectionInCollision(Ghost g1, Ghost g2) {
		
		if(g1.getActualDirection() == g2.getActualDirection()) {
			if (g1.getActualDirection() == Movable.LEFT) {
				if (g1.GetX() < g2.GetX()) {
					g2.setOppositeDirection();
				} else {
					g1.setOppositeDirection();
				}
				return;
			}
			if (g1.getActualDirection() == Movable.RIGHT) {
				if (g1.GetX() > g2.GetX()) {
					g2.setOppositeDirection();
				} else {
					g1.setOppositeDirection();
				}
			}
			if (g1.getActualDirection() == Movable.UP) {
				if (g1.GetY() < g2.GetY()) {
					g2.setOppositeDirection();
				} else {
					g1.setOppositeDirection();
				}
			}
			
			if (g1.getActualDirection() == Movable.DOWN) {
				if (g1.GetX() > g2.GetX()) {
					g2.setOppositeDirection();
				} else {
					g1.setOppositeDirection();
				}
			}
			return;
		} else {
			if (g1.getActualDirection() == Movable.LEFT) {
				if (g1.GetX() > g2.GetX()) {
					g1.setOppositeDirection();
					g2.setOppositeDirection();
				}
			}
			
			if (g1.getActualDirection() == Movable.RIGHT) {
				if (g1.GetX() < g2.GetX()) {
					g1.setOppositeDirection();
				}
			}
			if (g1.getActualDirection() == Movable.UP) {
				if (g1.GetY() > g2.GetY()) {
					g1.setOppositeDirection();
				}
			}
			
			if (g1.getActualDirection() == Movable.DOWN) {
				if (g1.GetX() < g2.GetX()) {
					g1.setOppositeDirection();
				}
			}
			return;
		}
	}
	
	/**
	 * Logica para gestionar la colision de los fantasmas entre ellos
	 */
	
	private void checkGhostCollisions(Ghost ghost) {
		boolean collided = false;
		for (Ghost g: ghosts){
			// Comprobamos que no se chequee el fantasma consigomismo.
			if (ghost.getID().equals(g.getID())){
				continue;
			} else {
				
				Rectangle g1 = ghost.getBounds();
				Rectangle g2 = g.getBounds();
				
				
				if (g1.intersects(g2)) {
					
					if (ghost.isCollidingWithGhost(g.getID())) {
						continue;
					}
					
					collided = true;
					ghost.addGhostCollision(g.getID());
					g.addGhostCollision(ghost.getID());
					
					if(!ghost.hasCollided()){
						ghost.setHasCollided(true);
					}
					
					if(!g.hasCollided()){
						g.setHasCollided(true);
					}
					
					// se fuerza un calculo especial de direccion del fantasma
					calculateGhostNextDirectionInCollision(ghost, g);
						
				} else {
					
					// Si no se estan solapando, se quita en caso de existir la relacion entre
					// los fantasmas colisionados.
					ghost.removeGhostCollision(g.getID());
					g.removeGhostCollision(ghost.getID());
				}
			}
		}
		
		if(!collided) {
			ghost.setHasCollided(false);
		}
	}
	
	
	/**
	 * Logica para gestionar la colision de los fantasmas y el jugador
	 */
	
	private void checkPlayerGhosts() {
		
		Rectangle player_b = player.getBounds();
		Iterator<Ghost> iterator = ghosts.iterator();
		while(iterator.hasNext()){
			Ghost g = iterator.next();
			Rectangle g_b = g.getBounds();
			if (player_b.intersects(g_b)) {
				
				if(g.getState() == Ghost.DEAD) 
				{
					continue;
				}
				
				if(invincible_mode) {
					hud.incrementScore(100);
					g.setState(Ghost.DEAD);
					
					// Regenerar el fantasma en su posicion inicial.
					int[] g_position = level.getGhostInitialPosition(g.getID());
					g.SetX(g_position[0]);
					g.SetY(g_position[1]);

					
				} else {
					loadGameOverScreen();
				}
			}
		}
	}
	
	/**
	 * Logica de movimiento de los fantasmas. Aqui se gestiona la decision de
	 * calcular el proximo movimiento del fantasma, pero esa logica se gestiona
	 * dentro del propio fantasma
	 * 
	 * @see Ghost
	 */
	
	private void moveGhosts() {
		// Mover fantasmas
		Iterator<Ghost> iterator = ghosts.iterator();
		
		while(iterator.hasNext()){
			
			Ghost g = iterator.next();
			
			if (g.getState() == Ghost.DEAD)
			{
				g.checkDeadTime();
				continue;
			}
			
			// Comprueba las colisiones con otros fantasmas
			checkGhostCollisions(g);
			
			// Si colisiona con mas de 1 a la vez, paramos  al fantasma.
			if(g.withManyGhostCollides() > 1) {
				continue;
			} 
			
			
			// Si esta parado calcula la proxima direccion.
			if (g.getActualDirection() == 0 || g.getNextDirection() == 0) {
				g.calculateNextDirection(player.GetX(), player.GetY());
			}
			
			
			if (canMove(g)){
				if (!g.hasCollided()){
					if (hasToChangeToNextDirection(g)) {
						g.changeToNextDirection();
						g.calculateNextDirection(player.GetX(), player.GetY());
					}
				}
				g.move();
			} else {
				g.calculateNextDirection(player.GetX(), player.GetY());
			}
		}
	}
	
	/**
	 * Logica basica para saber si el jugador se puede mover en base a las reglas.
	 */
	
	private void movePlayer() {
		boolean can_move;
		// Manejamos la logica de movimiento del actor (En este caso jugador)
		// y comprobamos si puede moverse en base a las reglas
		
		if (hasToChangeToNextDirection(player)) {
			player.changeToNextDirection();
		}
		can_move = canMove(player);
		if (can_move){
			player.move();
		}
	}
	
	/**
	 * 
	 * Logica de colision del jugador con los elementos recolectables.
	 * Por cada elemento recolectable se comprueba si el jugador
	 * esta chocando con ellos.
	 * 
	 * En caso de encontrar un recolectable de poder, establece 
	 * el modo invencible.
	 * 
	 */
	
	private void checkPickables() {
		Rectangle player_b = player.getBounds();
		ArrayList<Pickable> picks = level.getPickables();
		Iterator<Pickable> iterator = picks.iterator();
		
		while (iterator.hasNext()) {
			Pickable pick = (Pickable) iterator.next();
			Rectangle pick_b = pick.getBounds();
			
			if (player_b.intersects(pick_b)) {
				
				hud.incrementScore(pick.getPoints());
				
				if (pick.getType().equals("SuperDot")) {
					setInvincibleMode();
				}
				
				iterator.remove();
			}
		}
		
		if (level.getPickables().isEmpty()){
			loadWinScreen();
		}
	}
	
	/**
	 * Logica para decidir si el actor debe cambiar la direccion. P.ej
	 * se ha llegado a una interseccion. O debe cambiar de direccion a la opuesta
	 * por que 2 fantasmas han colisionado, o el jugador ha decidido cambiar de direccion.
	 * 
	 * @param actor jugador o fantasma
	 * @return valor booleano indicando si el actor debe cambiar la direccion.
	 */
	
	private boolean hasToChangeToNextDirection(Movable actor) {
		int next_direction = actor.getNextDirection();
		int[] actor_position = actor.GetPosition();

		// Si el actor esta centrado en una celda de la rejilla
		// comprobamos si podemos cambiar de direccion
		if (actorIsCentered(actor_position)) {
			 // Comprobamos que la proxima celda sea navegable.
			 if (nextCellIsNavigable(actor_position, next_direction)) {
				 // Si lo es, cambiamos la direccion del actor:
				 return true;
			 }
		}
		
		// Si no esta centrado o la proxima celda del cambio de direccion no es navegable
		// comprobamos si la siguiente direccion es la opuesta:
		if (actor instanceof Player)
		{
			if(actor.isNextDirectionOppositeToActual()) {
				if (nextCellIsNavigable(actor_position, next_direction)) {
					 // Si lo es, cambiamos la direccion del actor:
					 return true;
				 }
			}
		}
		
		return false;
	}
	
	/**
	 * Logica principal para saber si el jugador puede moverse.
	 * @param actor fantasma o jugador.
	 * @return booleano con la decision de si el jugador puede moverse.
	 */
	
	private boolean canMove(Movable actor) {
		if (actor instanceof Ghost) {
			
		}
		int actual_direction = actor.getActualDirection();
		int[] actor_position = actor.GetPosition();
		
		
		if (hasToChangeToNextDirection(actor)) {
			return true;
		}
		
		// Por ultimo comprobamos si se puede mover en la direccion que iba:
		if (nextCellIsNavigable(actor_position, actual_direction) || (!actorIsCentered(actor_position))) {
			return true;
		}
		
		// En caso de que no sea navegable la siguiente zona
		return false;
	}
	
	/**
	 * 
	 * Arranca el modo invencible. Se utiliza un timestamp para saber
	 * cuando empezo el modo invencible.
	 * 
	 * Cambia el estado del fantasma a asustado en caso de no estar muerto.
	 * 
	 */
	
	private void setInvincibleMode() {
		start_invincible_mode = new Date().getTime();
		invincible_mode = true;
		Iterator<Ghost> iterator = ghosts.iterator();
		
		while(iterator.hasNext()){
			Ghost g = iterator.next();
			if (g.getState() != Ghost.DEAD) {
				g.setState(Ghost.SCARY);
			}
		}
	}
	
	/**
	 * Comprueba si debe finalizar el modo invencible.
	 */
	
	private void checkInvincibleMode() {
		long actual_milis = new Date().getTime();
		if (actual_milis >= (start_invincible_mode + gameconfig.getInvincibleModeDuration())) {
			unsetInvincibleMode();
		}
	}
	
	/**
	 * Desactiva el modo invencible
	 */
	
	private void unsetInvincibleMode() {
		invincible_mode = false;
		Iterator<Ghost> iterator = ghosts.iterator();
		
		while(iterator.hasNext()){
			Ghost g = iterator.next();
			if (g.getState() != Ghost.DEAD) {
				g.setState(Ghost.CHASING);
			}
		}
	}
	
	/**
	 * Determina si el jugador esta centrado en la celda.
	 * @param position_x_y int[] de 2 huecos con la posicion XY del jugador
	 * @return true si el actor esta centrado en la celda.
	 */
	
	private boolean actorIsCentered(int[] position_x_y) {
		int[] row_col = level.convertPixelsInRowCol(position_x_y[0], position_x_y[1]);
		int[] row_col_x_y = level.convertRowcolsToPixels(row_col[0], row_col[1]);
		return ((position_x_y[0] == row_col_x_y[0]) && (position_x_y[1] == row_col_x_y[1]));
	}
	
	/**
	 * Logica para saber si la proxima celda es navegable.
	 * 
	 * @param actor_position int[] de 2 huecos con la posicion x y del jugador
	 * @param next_direction entero con la direccion.
	 * @return verdadero si la proxima celda es navegable.
	 */
	
	private boolean nextCellIsNavigable(int[] actor_position, int next_direction){
		int centered_x = actor_position[0];
		int centered_y = actor_position[1];
		int[] row_col = level.convertPixelsInRowCol(centered_x, centered_y);
		
		// inicializamos la celda a la celda 0,0 (esquina superior izq, que siempre sera
		// no navegable
		int[] next_cell = new int[2];
		next_cell[0] = 0;
		next_cell[1] = 0;
		
		switch(next_direction) {
			case Movable.UP:
				next_cell = level.getUpperRowCol(row_col[0], row_col[1]);
				break;
			case Movable.DOWN:
				next_cell = level.getBottomRowCol(row_col[0], row_col[1]);
				break;
			case Movable.LEFT:
				next_cell = level.getLeftRowCol(row_col[0], row_col[1]);
				break;
			case Movable.RIGHT:
				next_cell = level.getRightRowCol(row_col[0], row_col[1]);
				break;
		}
		
		return level.isCellNavigable(next_cell[0], next_cell[1]);
		
	}
	
	/**
	 * Carga el juego.
	 */
	private void loadGame() {
		configureGame();
		game_screen = PLAY_SCREEN;
		
	}
	
	/**
	 * Establece que hay que mostrar la pantalla de exito
	 */
	private void loadWinScreen() {
		game_screen = WIN_SCREEN;
	}
	
	/**
	 * Establece que hay que mostrar la pantalla de game over
	 */
	
	private void loadGameOverScreen() {
		game_screen = GAME_OVER_SCREEN;
	}
	
	/**************************************************
	 *           Llamadas de renderizado              *
	 **************************************************/
	
	/**
	 * Llamada principal para dibujar cada frame. Se ha dividido la logica
	 * lo en funciones para facilitar la lectura del codigo.
	 * @param g Objeto Graphics
	 */
	
	
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setFont(text_font.deriveFont((float) ((float) gameconfig.getLevelBoxSize() * 0.70)));
		switch (game_screen) {
		case PAUSE_SCREEN:
			paintPauseScreen(g2d); break;
		case START_SCREEN:
			paintStartScreen(g2d); break;
		case WIN_SCREEN:
			paintWinScreen(g2d); break;
		case GAME_OVER_SCREEN:
			paintGameOverScreen(g2d); break;
		case PLAY_SCREEN:
		default:
			// dibujamos el fondo
			paintBackground(g2d);
			
			// pintamos los objetos recolectables
			paintPickables(g2d);
			
			// Dibujamos al jugador
			g2d.drawImage(player.getImage(), player.GetX(), player.GetY(), gameconfig.getLevelBoxSize(), gameconfig.getLevelBoxSize(), this);
			
			// Dibujamos a los fantasmas
			paintGhosts(g2d);
			
			paintInGameHUD(g2d);
		}
		
		Toolkit.getDefaultToolkit().sync();		
		g.dispose();
		
	}
	
	/**
	 * Dibuja a los fantasmas
	 * @param g2d: Objeto Graphics2D
	 */
	private void paintGhosts(Graphics2D g2d){
		Iterator<Ghost> iterator = ghosts.iterator();
		while (iterator.hasNext()) {
			Ghost g = iterator.next();
			g2d.drawImage(g.getImage(), g.GetX(), g.GetY(), gameconfig.getLevelBoxSize(), gameconfig.getLevelBoxSize(), this);
		}
	}
	
	/**
	 * 
	 * Dibuja  el fondo de pantalla.
	 * @param g2d: Objeto Graphics2D
	 */
	private void paintBackground (Graphics2D g2d) {
		
		int rows = level.getRowCount();
		int cols = level.getColCount();
		int r, c, bg_x, bg_y = 0;
		
		for (r = 0; r < rows; r++) {
			bg_x = 0;
			for (c = 0; c < cols; c++) {
				Image bg_image = level.getCellImage(r, c);
				g2d.drawImage(bg_image, bg_x, bg_y, gameconfig.getLevelBoxSize(), gameconfig.getLevelBoxSize(), this);
				bg_x = bg_x + gameconfig.getLevelBoxSize();
			}
			bg_y = bg_y + gameconfig.getLevelBoxSize();
		}
	}

	
	/**
	 * Dibuja los objetos recolectables del nivel.
	 * @param g2d Objeto Graphics2D	 
	 */
	
	private void paintPickables(Graphics2D g2d) {
		ArrayList<Pickable> picks = level.getPickables();
		Iterator<Pickable> iterator = picks.iterator();
		
		while (iterator.hasNext()) {
			Pickable pick = (Pickable) iterator.next();
			Image img = pick.getImage();
			
			int x = pick.getX();
			int y = pick.getY();
			
			g2d.drawImage(img, x, y, gameconfig.getLevelBoxSize(), 
					      gameconfig.getLevelBoxSize(), this);	
		}
	}
	
	/**
	 * Dibuja el HUD ingame.
	 * @param g2d Objeto Graphics2D
	 */
	
	private void paintInGameHUD(Graphics2D g2d) {
		Color color = (Color.WHITE); 
		g2d.setColor(color);
		drawString(g2d, hud.getScoreText(), 20, 2);
		drawString(g2d, hud.getScore(), 160, 2);
	}
	
	/**
	 * Dibuja la pantalla de pausa.
	 * @param g2d Objeto Graphics2D
	 */
	
	private void paintPauseScreen(Graphics2D g2d){
		Rectangle rect = getBounds();
		Color color = Color.BLACK;
		g2d.setColor(color);
		g2d.drawRect(rect.x, rect.y, rect.width, rect.height);  
	    g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
	    
	    color = Color.WHITE;
		g2d.setColor(color);
		drawString(g2d, hud.getPauseText(), (rect.width/2) - 170, 200);
	}
	
	
	/**
	 * Dibuja la pantalla de inicio.
	 * @param g2d Objeto Graphics2D
	 */
	
	private void paintStartScreen(Graphics2D g2d){
		Rectangle rect = getBounds();
		Color color = Color.BLACK;
		g2d.setColor(color);
		g2d.drawRect(rect.x, rect.y, rect.width, rect.height);  
	    g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
	    
	    color = Color.WHITE;
		g2d.setColor(color);
		drawString(g2d, hud.getStartText(), (rect.width/2) - 170, 200);
	    
	}
	
	/**
	 * Funcion de ayuda para dividir en varias lineas las cadenas de texto
	 * que tengan saltos de linea (con \n).
	 * @param g2d Objeto Graphics2D
	 */
	
	private void drawString(Graphics2D g2d, String text, int x, int y) {
        for (String line : text.split("\n"))
            g2d.drawString(line, x, y += g2d.getFontMetrics().getHeight());
    }
	
	/**
	 * Dibuja la pantalla de game over.
	 * @param g2d Objeto Graphics2D
	 */
	
	private void paintGameOverScreen(Graphics2D g2d){
		Rectangle rect = getBounds();
		Color color = Color.BLACK;
		g2d.setColor(color);
		g2d.drawRect(rect.x, rect.y, rect.width, rect.height);  
	    g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
	    
	    color = Color.WHITE;
		g2d.setColor(color);
		drawString(g2d, hud.getGameOverText(), (rect.width/2) - 170, 200);
	}
	
	/**
	 * Dibuja la pantalla de exito.
	 * @param g2d Objeto Graphics2D
	 */
	
	private void paintWinScreen(Graphics2D g2d){
		Rectangle rect = getBounds();
		Color color = Color.BLACK;
		g2d.setColor(color);
		g2d.drawRect(rect.x, rect.y, rect.width, rect.height);  
	    g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
	    
	    color = Color.WHITE;
		g2d.setColor(color);
		drawString(g2d, hud.getWinText(), (rect.width/2) - 170, 200);
	}
	
	/**
	 * Devuelve la fuente de texto
	 * @return
	 */
	private void setTextFont () {

        try {
            String fname = "src/resources/fonts/8BITWonder.ttf";
            File fontFile = new File(fname);
            text_font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(text_font);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
	
	
	/**************************************************
	 *           Gestion de entradas de teclado       *
	 **************************************************/
	
	
	/**
	 * Clase para manear los eventos del teclado.
	 * Se mandan al objeto jugador para fijar los movimientos.
	 * Ademas se controla la posibilidad de pausar el juego.
	 */
	private class TAdapter extends KeyAdapter {
		
		public void keyPressed (KeyEvent e){
			
			int key = e.getKeyCode();
			
			switch(game_screen) {
			case START_SCREEN:
				if (key == KeyEvent.VK_ENTER){
					loadGame();
				}
				break;
			case GAME_OVER_SCREEN:
			case WIN_SCREEN:
				if (key == KeyEvent.VK_ENTER){
					game_screen = START_SCREEN;
				}
				break;
			case PAUSE_SCREEN:
				if (key == KeyEvent.VK_P) {
					game_screen = PLAY_SCREEN;
				}
				break;
			case PLAY_SCREEN:
				if (player != null) {
					player.keyPressed(e);
				}

				// Pausamos
				if (key == KeyEvent.VK_P) {
					game_screen = PAUSE_SCREEN;
				}
				break;
			}

		}
	}

	
}