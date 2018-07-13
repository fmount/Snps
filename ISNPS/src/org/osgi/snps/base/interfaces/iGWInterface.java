package org.osgi.snps.base.interfaces;

import java.util.List;

import org.osgi.snps.base.common.SamplingPlan;
import org.w3c.dom.Document;

public interface iGWInterface {
	
	/*
	 * Metodi esposti alla WSN per comunicare con i componenti del Middleware..
	 */
	public String registerSensor(String bs_id, Document description);
	
	public boolean push(String data,String[] options);
	
	
	/*
	 * Metodi esposti al Middleware per comunicare con la WSN..
	 */


	//public String getData(String sid, String mode,String[] options);
	
	public boolean sendCommand(String command,List<String> sid, String mode); 
	
	public String sayhello();

	boolean setSPlan(SamplingPlan sPlan);
	
	boolean stopSPlan(String sPlanId);

	//public String getData(String sid, String mode, String[] options, String action);

	public String getData(String id_meas_to_set, String sid, String mode,
			String[] options, String action);
	
	public String getData(String id_meas_to_set, String sid, String Nature, String mode,
			String[] options, String action);

	public String isAlive(String sid);
}
