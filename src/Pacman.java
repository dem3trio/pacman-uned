

import javax.swing.JFrame;

import level.Board;


/**
 * 
 * Clase principal del programa Pacman.  
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 * @see JFrame
 */

@SuppressWarnings("serial")
public class Pacman extends JFrame{


	/**
	 * Constructor. Inicializa los atributos correspondientes a JFrame
	 * y aniade un objeto Board (hijo de JPanel).
	 * 
	 */
	
	public Pacman()
	{
		add(new Board());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(570, 676);
        setLocationRelativeTo(null);
       
		setTitle("Pacman");
		setVisible(true);
	}
	
	public static void main(String [] args)
	{
		new Pacman();
		
	}
}

/**
 * El siguiente código comentado es una prueba de un loop de juego que
 * se ha intentado implementar para evitar usar el Timer de swing.
 * Es una adaptación a java extraida del pseudocódigo que aparece en esta pagina:
 * 
 * http://www.koonsolo.com/news/dewitters-gameloop/
 * 
 * Este algoritmo intenta adaptarse a los frames dibujados por la maquina pero 
 * sin que se vea comprometida la capacidad de calculo.
 * 
 * Se ha desechado el uso de este algoritmo por falta de tiempo para probarlo
 * e implementarlo. Su uso obligaría a la utilización de un parametro (basado en 
 * los frames por segundo actuales) para interpolar los movimientos de los fantasma 
 * y el jugador en cojunción al parámetro velocidad de los mismos.
 * 
 */

/*
int TICKS_PER_SECOND = 25;
int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
int MAX_FRAMESKIP = 5;

long next_game_tick = System.currentTimeMillis();
int loops;

bool game_is_running = true;
while( game_is_running ) {

    loops = 0;
    while( System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {
        update_game();

        next_game_tick += SKIP_TICKS;
        loops++;
    }

    repaint();
}
*/


