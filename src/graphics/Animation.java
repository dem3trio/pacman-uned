package graphics;

import java.awt.Image;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;

/**
 * Clase Animation:
 * 
 * Esta clase tiene por finalidad gestionar una animación.
 * 
 * 
 * @author Daniel González Zaballos <dgzaballos@gmail.com>
 * 
 */

public class Animation {
	
	/**
	 * Los parametros de la clase son los siguientes:
	 * name: 				nombre de la animación.
	 * duration:			duración en milisegundos de la animación
	 * loops:				repeticiones de la animación (0 para infinito)
	 * actual_loop:			Loop de la animación actual.
	 * image_number:		Numero de imágenes de la animación.
	 * image_interval:		Milisegundos que se muestra cada frame de la animación (calculado
	 * 						en base al numero de imagenes y la duración)
	 * animation_start_time:Milisegundos en el que empezó la animación.
	 * anim_images:			Listado de las imagenes de la animación.
	 */

	private String name;
	private int duration;
	private int loops;
	private int actual_loop;
	private int image_number;
	private int image_interval;
	private long animation_start_time;
	private ArrayList <Image> anim_images;
	
	/**
	 * Constructor de la animación:
	 * @param anim_name		Nombre de la animación
	 * @param anim_duration	Duración de la animación
	 * @param anim_loop		Numero de vueltas de la animación (0 para infinito)
	 * @param images_path	Array con las rutas a las imágenes
	 */

	public Animation(String anim_name, int anim_duration, int anim_loop, String[] images_path) throws FileNotFoundException {
		
		// Rellenamos los parametros base
		name = anim_name;
		duration = anim_duration;
		loops = anim_loop;
		actual_loop = 0;
		
		// Rellenamos el arraylist de imagenes
		anim_images = new ArrayList<Image>();
		for (String i: images_path){
			ImageIcon ii = new ImageIcon(this.getClass().getResource(i));
			anim_images.add( ii.getImage() );
		} 
		
		// Cálculo del numero de imágenes y el intervalo de las mismas en la animación.
		image_number = images_path.length;
		image_interval = duration / image_number;
		
		// Inicialización del parámetro a 0 para saber que la animación 
		// empieza desde el primer frame.
		animation_start_time = 0;
	}
	
	
	/**
	 * Devuelve la imagen que corresponda con el intervalo de la animación
	 * en el que nos encontremos.
	 * 
	 * @return La imagen correspondiente de la animación.
	 */
	
	public Image getImage() {
		
		if (animation_start_time == 0) {
			restartAnimation();
		}
		
		return anim_images.get(getFrameNumber());
		
	}
	
	/**
	 * Reinicia la animación, para ello obtiene la hora en milisegundos
	 * a la que ha comenzado la nueva animación.
	 */
	
	public void restartAnimation() {
		animation_start_time = new Date().getTime();
	}
	
	
	/**
	 * Resetea la animación para que pueda ejecutarse en caso de haber
	 * alcanzado el máximo de vueltas.
	 */
	
	public void resetAnimation(){
		animation_start_time = 0;
		actual_loop = 0;
	}
	
	
	/**
	 * Obtiene el número del frame correspondiente a la animación.
	 * @return
	 */
	
	private int getFrameNumber() {
		
		
		long actual_milis = new Date().getTime();
		int interval = (int) (actual_milis - animation_start_time);
		
		// Si se ha superado el numero de vueltas máximo, y se ha llegado al final
		// de la animación, se devuelve siempre el ultimo frame.
		if ( (interval >= duration) && (loops != 0) && (actual_loop >= loops)) {
			return image_number - 1;
		}
		
		// Si se ha llegado al final de la animacion, se reinicia para
		// que el siguiente frame sea de nuevo el primero de la animación.
		// En este caso es necesario volver a coger los milisegundos actuales
		// despues de reiniciar la animación.
		
		if (interval >= duration) {
			
			restartAnimation();
			
			if (loops != 0) {
				actual_loop++;
				
				// Nueva comprobación para saber si se ha llegado al tope de
				// vueltas
				
				if (actual_loop >= loops) {
					return image_number - 1;
				}
			}
			
			actual_milis = new Date().getTime();
			interval = (int) (actual_milis - animation_start_time);
		}
		
		
		// Por ultimo, calculamos el frame a mostrar	
		if (interval == 0) {
			return 0;
		}
		int frame_number = ((int) interval) / image_interval;
		return frame_number;
		
	}
	
	/**
	 * Devuelve el nombre de la animacion.
	 * @return el nombre de la animación
	 */
	public String getAnimationName() {
		return name;
	}
	
	
}
