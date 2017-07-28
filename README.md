# TFM
Este proyecto ha sido  desarrollado  como  parte  del  TFG  en  la  Universidad Internacional de la Rioja.
 	

#Para ejecutar desde ECLIPSE:
- File import 
- Existing project into the workspace
- Seleccionar folder cluster
- Una vez cargado: Botón derecho sobre la clase y pulsar en Run as java application

  Si se desea ver la ejecución sin ordenar los centroides es decir sin tener en cuenta el hábito dominante, copiar el fichero
  denominado 	cluster (Sin ordenar por centroides).java	dentro de la carpeta cluster y ejecutarlo.
	Cluster (ordenando la salida por las porcentaje de centroides).java 	commit TFM 	an hour ago
  Cluster (Version definitiva).java es un backup del fichero definitivo ya introducido por defecto en la carpeta cluster


#Banco de datos BD tratada:
Se puede cargar los dos bancos de prueba adjuntos, localizados en:
..\clu#ter\datos
Un banco de pruebas de 13500 registros
Un banco de pruebas de 122500 registros

#Cambiar tarifas:
Para ello modificar las líneas: 
public static double TARIFA_BAJA   = 0.099;
public static double TARIFA_MEDIA  = 0.139;
public static double TARIFA_ALTA   = 0.15;

#Cambiar número de clusters
Para ello modificar las líneas: 
public static int NUMERO_CLUSTERS  = 50;

#Salidas:
Las salidas disponibles por la aplicación son:
1) En pantalla el sistema de recomendación muestra las recomendaciones finales.
2) En fichero "./salida/datos.csv" muestra las recomendaciones finales acumuladas de todas las ejecuciones.
3) En la consola de Java:
   Muestra todos los clusters con el número de intancias y su porcentaje
   También muestra las recomendaciones finales.

#Licencias
Se hace uso de las liberías Java de Weka con licencia GNU.

Partes de este código incorpora fragmentos de código de proyectos de código abierto
obtenidos en:
http://www.programcreek.com/java-api-examples/index.php?api=weka.clusterers.SimpleKMeans
http://www.programcreek.com/2014/02/k-means-clustering-in-java/

