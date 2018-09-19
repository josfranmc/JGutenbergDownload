# JGutenbergDownload
Descarga de ficheros desde los repositorios del proyecto Gutenberg.

## Contenido
+ jar-flat: fichero jar con las clases de la aplicación junto a las dependencias necesarias, que se ubican en la carpeta lib
+ jar-shaded: fichero jar con las clases de la aplicación y con las dependencias necesarias incluidas en él
+ javadoc: documentación del código
+ src: Código fuente

## Uso
Desde línea de comandos:

java -jar JGutenbergDownload-1.0-shaded.jar [*opciones*]

opciones:

-t tipo fichero (por defecto txt)  
-i idioma (por defecto en)  
-d tiempo de espera en milisegundos (por defecto 2000)  
-s ruta donde guardar las descargas  
-m total_ficheros_a_descargar (por defecto 10, el valor 0 descarga todo)  
-z descomprimir (true/false, por defecto true)  
-o sobreescribir existentes (true/false, por defecto false)  
-x modo de descarga (SOFT/GREEDY, por defecto SOFT)  

-h muestra lista de opciones

Nota:  
Si se usa el jar flat hay que asegurarse que exista la carpeta lib dentro de la carpeta desde la que ejecutemos la aplicación.
