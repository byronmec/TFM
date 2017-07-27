package cluster;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.csvreader.CsvWriter;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class Cluster extends JFrame{
	
	public static String URL_DATOS     = "./datos/acciones 13500 registros.arff";
	public static String URL_DATOS_CSV = "./salida/datos.csv";
	
	public static double TARIFA_BAJA  = 0.099;
	public static double TARIFA_MEDIA = 0.139;
	public static double TARIFA_ALTA  = 0.15;
	
	public Cluster()
	{
		try {
			BufferedReader datafile = new BufferedReader(new FileReader(URL_DATOS));
			Instances data = new Instances(datafile);
			
			// create the model 
			SimpleKMeans kmeans = new SimpleKMeans();
			kmeans.setSeed(10);
			kmeans.setPreserveInstancesOrder(true);
			kmeans.setNumClusters(10);
			kmeans.buildClusterer(data); 
			
			// Añadimos la tabla
			Object[][] datos = {};
			String[] columnNames = {"Centroid","Accion","Estancia","Tiempo","Tarifa","Usuario","-Uso-","-Tarifa-"};
		    DefaultTableModel dtm= new DefaultTableModel(datos,columnNames);
		    final JTable table = new JTable(dtm);
		    
		    // Genearamos el CSV
		    File file = new File(URL_DATOS_CSV);
	        file.getParentFile().mkdirs();
		    CsvWriter csvOutput = new CsvWriter(new FileWriter(URL_DATOS_CSV, true), ',');
            // Contruimos la cabecera de la tabla csv
            csvOutput.write("Centroid");
            csvOutput.write("Accion");
            csvOutput.write("Estancia");
            csvOutput.write("Tiempo");
            csvOutput.write("Tarifa");
            csvOutput.write("Usuario");
            csvOutput.write("-Uso-");
            csvOutput.write("-Tarifa-");
            csvOutput.endRecord();
            
            // This array returns the cluster number (starting with 0) for each instance
    		// The array has as many elements as the number of instances
            
    		int[] assignments = kmeans.getAssignments();
    		
    		int numeroInstancias[] = new int[10];
    		
    		int contador = 0;
    		int total = 0;
    		
            for(int i=0;i<assignments.length;i++)
            {
                for(int j=0;j<assignments.length;j++)
                {
                    if(assignments[i]==assignments[j])
                    	contador++;
                }
                numeroInstancias[assignments[i]]= contador;
                
                contador=0;
            }
            
            double porcentaje =0.0;
    		
    		for(int i = 0; i < numeroInstancias.length; i++) 
    		{  
    			// obtenemos el total de elemento 
    			total = total + numeroInstancias[i];
    			
    		}
    		SortedMap <Integer, Integer> tm= new TreeMap<Integer, Integer>(java.util.Collections.reverseOrder());
    		for(int i = 0; i < numeroInstancias.length; i++) 
    		{  
    			porcentaje = (float)((numeroInstancias[i]*100)/total);
    			System.out.println("----> Cluster: " + i + " instancias: " + numeroInstancias[i] + " porcentaje: " + porcentaje +" %");
    		    
    			
    			tm.put(numeroInstancias[i], i);
    		}
    		
    		 
			// print out the cluster centroids
		    Instances centroids = kmeans.getClusterCentroids(); 
		    // Get a set of the entries
  	      	Set set = tm.entrySet();
  	      	Iterator y = set.iterator();
  	      	while(y.hasNext()) 
  	      	{	
	  	      	Map.Entry me = (Map.Entry)y.next();
		        int indice = (int) me.getValue();
		        
		    	String uso    = new String();
		    	String tarifa    = new String();
		    	if((centroids.instance(indice).stringValue(0).equals("luz")) || (centroids.instance(indice).stringValue(0).equals("calefaccion")) || (centroids.instance(indice).stringValue(0).equals("aire_acondicionado")))
		    	{
		    		tarifa = calcularTarifa(centroids.instance(indice).value(3));
		    		uso = "MAL USO";
		    		if(tarifa.equals("BAJA"))
		    		{		    			
		    			tarifa = "";
		    		}
		    		
		    	}
		    	else if((centroids.instance(indice).stringValue(0).equals("planchado")) || (centroids.instance(indice).stringValue(0).equals("secado")))
		    	{
		    		tarifa = calcularTarifa(centroids.instance(indice).value(3));
		    		if(tarifa.equals("BAJA"))
		    		{
		    			uso = "BUEN USO";
		    			tarifa = "";
		    		}
		    		else if((tarifa.equals("MEDIA")) || (tarifa.equals("ALTA")))
		    		{
		    			uso = "MAL USO";
		    		}
		    	}
		    	
		    	System.out.println( "| --------------------------------- |");
		        System.out.println( "Centroid " + (indice) + " ---> \n  accion: " + centroids.instance(indice).stringValue(0) + "\n  estancia: " + centroids.instance(indice).stringValue(1) + "\n  tiempo: " + centroids.instance(indice).value(2) + "\n  tarifa: " + centroids.instance(indice).value(3)+ "\n  usuario: " + centroids.instance(indice).stringValue(4)+ "\n\n  Uso: " + uso+ "\n  Tarifa: " + tarifa); 
		        System.out.println( "| --------------------------------- |");
		       
		        // Agregar nueva fila
			    Object[] newRow={indice,centroids.instance(indice).stringValue(0),centroids.instance(indice).stringValue(1),centroids.instance(indice).value(2),centroids.instance(indice).value(3),centroids.instance(indice).stringValue(4),uso,tarifa};
			    dtm.addRow(newRow);
			    
			    // Añadimos cada fila al csv
			    csvOutput.write(String.valueOf(indice));
                csvOutput.write(centroids.instance(indice).stringValue(0));
                csvOutput.write(centroids.instance(indice).stringValue(1));
                csvOutput.write(String.valueOf(centroids.instance(indice).value(2)));
                csvOutput.write(String.valueOf(centroids.instance(indice).value(3)));
                csvOutput.write(centroids.instance(indice).stringValue(4));
                csvOutput.write(uso);
                csvOutput.write(tarifa);
                csvOutput.endRecord();  			    
		    } 
		    
		    //cerramos el csv
		    csvOutput.close();
		    		    
			table.setPreferredScrollableViewportSize(new Dimension(600, 200));
    		JScrollPane scrollPane = new JScrollPane(table);
    		getContentPane().add(scrollPane, BorderLayout.CENTER);
    		
    		addWindowListener(new WindowAdapter()
    		{
    			public void windowClosing(WindowEvent e)
    			{
    				System.exit(0);
    			}
    		});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String calcularTarifa(double tarifa)
	{
		String tarifaAux = new String("");
		
		double rango_Bajo =  (TARIFA_BAJA + TARIFA_MEDIA) / 2;
		double rango_Alto =  (TARIFA_MEDIA + TARIFA_ALTA) / 2;
		
		
		if(tarifa < TARIFA_BAJA)
		{
			tarifaAux = "BAJA"; 
		}
		else if((tarifa >= TARIFA_BAJA) && (tarifa < rango_Bajo))
		{
			tarifaAux = "BAJA"; 
		}
		else if((tarifa >= rango_Bajo) && (tarifa < TARIFA_MEDIA))
		{
			tarifaAux = "MEDIA"; 
		}
		else if((tarifa >= TARIFA_MEDIA) && (tarifa < rango_Alto))
		{
			tarifaAux = "MEDIA"; 
		}
		else if((tarifa >= rango_Alto) && (tarifa<=TARIFA_ALTA))
		{
			tarifaAux = "ALTA"; 
		}
		else if(tarifa > TARIFA_ALTA)
		{
			tarifaAux = "ALTA"; 
		}
		
		
		return tarifaAux;
	}

	public static void main(String[] args)
	{
		Cluster cluster = new Cluster();
		cluster.pack();
		cluster.setVisible(true);
		
		
	}

}
