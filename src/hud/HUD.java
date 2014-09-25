package hud;

/**
 * Clase encargada de almacenar los valores del HUD del juego.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 *
 */

public class HUD{
	
	private String score;
	private String score_txt;
	private String start_txt;
	private String pause_txt;
	private String win_txt;
	private String game_over_txt;
	
	
	/**
	 * Constructor
	 */
	public HUD (){
		score_txt = "Puntuacion";
		score = "0";
		start_txt = "Bienvenido a PACMAN UNED\nPulsa Intro para empezar";
		pause_txt = "Juego pausado\nPulsa P para continuar";
		win_txt = "Enhorabuena has ganado\n\nPulsa Intro para ir a\nla pantalla de inicio";
		game_over_txt = "Has perdido\n\nPulsa Intro para ir a\nla pantalla de inicio";
	}
	
	/**
	 * Incrementa la puntuación del HUD
	 * 
	 * @param value valor a incrementar la puntuacion. 
	 */
	
	public void incrementScore(int value) {
		int new_value = Integer.parseInt(score) + value;
		score = String.valueOf(new_value);
	}
	

	/**
	 * Devuelve la puntuacion actual
	 * @return la puntuacion actual.
	 */

	public String getScore() {
		return score;
	}
	
	
	/**
	 * Resetea el valor a 0 (por ejemplo al reiniciar el juego)
	 */
	
	public void resetScore() {
		score = "0";
	}
	
	/**
	 * Devuelve el literal del texto de puntuacion.
	 * @return literal del texto de puntuacion.
	 */
	
	public String getScoreText() {
		return score_txt;
	}
	
	
	/**
	 * Devuelve el literal del texto de la pantalla de inicio.
	 * @return literal del texto de la pantalla de inicio
	 */
	
	public String getStartText() {
		return start_txt;
	}
	
	/**
	 * Devuelve el literal del texto de la pantalla de pausa.
	 * @return literal del texto de la pantalla de pausa.
	 */
	
	public String getPauseText() {
		return pause_txt;
	}
	
	/**
	 * Devuelve el literal del texto de la pantalla de exito.
	 * @return literal del texto de la pantalla de exito.
	 */
	
	public String getWinText() {
		return win_txt;
	}
	
	/**
	 * Devuelve el literal del texto de la pantalla de Game Over.
	 * @return literal del texto de la pantalla de Game Over.
	 */
	
	public String getGameOverText() {
		return game_over_txt;
	}
	
	
}
