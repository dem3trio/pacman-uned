package graphics;

import graphics.Animation;

import java.util.HashMap;
import java.awt.Image;
import java.io.FileNotFoundException;

/**
 * Clase AnimationManager:
 * 
 * Esta clase tiene el propósito de gestionar animaciones de los
 * personajes del juego.
 * 
 * TODO:
 * - Establecer un sistema de pausa de animación.
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 * 
 */

public class AnimationManager {
	
	private String current_animation;
	private HashMap <String, Animation> animations;
	
	
	/**
	 * Constructor
	 */
	public AnimationManager() {
		animations = new HashMap<String, Animation>();
	}
	
	
	/**
	 * Método para insertar una animación en el gestor de animaciones.
	 * 
	 * @param name		Nombre de la animación a insertar
	 * @param duration	Duración en milisegundos.
	 * @param loop		Numero de vueltas que dará la animacion (0 infinito)
	 * @param files		Array con los ficheros pertenecientes a la animación.
	 * @throws FileNotFoundException 
	 */
	
	public void addAnimation(String name, int duration, int loop, String[] files) throws FileNotFoundException {
		Animation anim = new Animation(name, duration, loop, files);
		animations.put(name, anim);
	}
	
	
	/**
	 * Metodo para obtener la imagen de una animación.
	 * 
	 * @param animation El nombre de la animación de la que se 
	 * quiere obtener la imagen.
	 * @return La imagen de la animación especificada.
	 * @see Image
	 */
	public Image getImage(String animation) {
		current_animation = animation;
		return animations.get(animation).getImage();
	}
	
	/**
	 * Devuelve la animacion actual
	 * @return el nombre de la animación actual.
	 */
	public String getCurrentAnimation() {
		return current_animation;
	}

}
