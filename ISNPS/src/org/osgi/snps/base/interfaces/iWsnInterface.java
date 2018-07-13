package org.osgi.snps.base.interfaces;

import java.util.List;





public interface iWsnInterface {
	
	//public Document getDocument(String SMLPath);
	
	public boolean setSPlan(String splan);
	
	public boolean stopSPlan(String splanId);
	
	public String getData(String sensorId,String[] options);
	
	public boolean sendCommand(List<String> sids, String command);
	
	public String registerSensor();

	public boolean sendDataToMiddleware(String data);
	
	String helloMsg();
		
}
