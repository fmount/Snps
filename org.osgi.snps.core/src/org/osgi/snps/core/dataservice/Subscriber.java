package org.osgi.snps.core.dataservice;

import java.io.*;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;


public class Subscriber implements EventHandler {

	@Override
	public void handleEvent(Event event) {
		System.out.println("Received data event on topic = " +event.getTopic());
		try{
			File f = new File("data.txt");
			FileWriter fw = new FileWriter (f,true);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter outFile = new PrintWriter (bw);
			outFile.println("-------------------------------------------");
			for (String propertyName :	event.getPropertyNames()) {
					System.out.println("\t" + propertyName + " = " +event.getProperty(propertyName));
					outFile.println("\t" + propertyName + " = " +event.getProperty(propertyName)); 				
			}
			outFile.println("-------------------------------------------");
			outFile.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}