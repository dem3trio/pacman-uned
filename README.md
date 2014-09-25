Pacman Java - UNED 2013-2014
======

Pacman realizado para practica de la asignatura de Programación Orientada a Objetos de la UNED curso 2013-2014

Lo primero decir, que esta no es la mejor implementación de Pacman en java ni mucho menos. Sólo es una más.

Se incluye en este README el análisis de la aplicación incluido en la memoria entregada al tutor para que puedas entender cómo se ha planeado la práctica.

Analisis de la aplicacion
-----

Para la realización de la practica se ha utilizado el paradigma MVC intentando modularizar las clases buscando una alta cohesión y un bajo acoplamiento. 

La configuración del nivel y de los fantasmas se ha optado por utilizar archivos json (localizados bajo la carpeta /resources/

Este sistema de configuración dispone de 2 clases: 

  * level.LevelLoader, que carga la configuración del nivel. Dentro de la configuración disponible se encuentra: la carpeta con los 'tilesets' del nivel, array de fantasmas disponibles en el nivel, la estructura del nivel (con números que van desde:
    * Menores o iguales que -1: zonas no navegables. Se utilizan diferentes números para mostrar diferentes imágenes (esquinas, bordes izquierdos/derechos...)
    * Mayores o iguales que 0: zonas navegables. Donde 0 es una celda normal, 1 contiene un punto pequeño, 2 punto grande, 3 salida del jugador, 4-7 salidas de los fantasmas.
  * Y logic.Config que guarda la configuración común de la aplicación. Este ultimo objeto se ha construido como un “Singleton” ya que su información es sensible de ser utilizada en varias clases de la aplicación. Se ha creado además una clase de ayuda “GhostConfig” para simplificar la lectura de la configuración de los fantasmas del fichero JSON.

Para el algoritmo de navegación de los fantasmas, se ha usado 2 variables numéricas (1-100). Una para determinar si se está buscando al jugador ('accuracy_threshold', cuanto mayor sea su valor, más posible es que se busque al jugador), y otra para saber si al cambiar la dirección ('HvsV' Horizontal vs Vertical, cuanto mayor sea su valor es mas probable que se prefiera dirección horizontal), la siguiente dirección será horizontal o vertical. Utilizando números aleatorios y comparándolo con estas variables numéricas, podemos evitar que siempre busquen al jugador (no hay que olvidar que esto es un videojuego y se le debe dar cierta ventaja al jugador), y también evitar que los fantasmas se queden bloqueados intentando buscar siempre al jugador.

Sólo se ha creado una clase “Ghost” por que al utilizar este tipo de algoritmo de búsqueda, y una configuración en JSON, ya no es necesaria la generación de más clases. Además, si en un hipotético futuro se desease añadir más fantasmas, sólo habría que añadir más entradas en el fichero json.

De esta manera también se busca facilitar el trabajo a hipotéticos compañeros de trabajo, separando en la medida de lo posible la parte de programación, arte y game design. Los grafistas pueden modificar imágenes tocando sólo en el json indicando donde está la carpeta de imagenes del fantasma en cuestión, y los diseñadores (game designers) pueden ajustar la dificultad tocando el fichero de configuración.

Información adicional sobre la práctica
----
Para el desarrollo de la práctica se ha utilizado:
  * Como sistema operativo Ubuntu 12.04.
  * Como IDE de desarrollo Eclipse.
  * Libreria json-simple-1.1.1 (Licencia Apache 2.0) https://code.google.com/p/json-simple/
  * Los Sprites de fantasmas y pacman, extraidos de http://forum.thegamecreators.com/?m=forum_view&t=67876&b=1 Se desconoce la licencia. 
  * Las imágenes del fondo de pantalla han sido extraidas de un paquete de imágenes de la web http://kenney.nl/assets licencia CC Zero
