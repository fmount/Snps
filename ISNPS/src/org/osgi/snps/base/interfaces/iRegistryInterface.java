package org.osgi.snps.base.interfaces;



import java.util.List;
import java.util.Map;

import org.osgi.snps.base.common.SimpleData;

public interface iRegistryInterface {
	
	public String sayHello();
	
	boolean serializeComponent(int opcode, String key, String description,
			String nature, String sensor);
		
	boolean updateComponent(int opcode, String key, String description,
			String nature, String sensor);
	
	public String getImageComponent(int code, String key);
	
	public String getComponentDescription(int code, String key);
	
	public boolean removeComponent(int code, String key);
	
	public List<String> getAllImages(int opcode);
	
	public List<String> getSensorBYType(int opcode,String key);
	
	/*************
	 * 
	 * METHODS ON MEASURES TABLE
	 * 
	 * */
	
	public boolean recDetect(int opcode, SimpleData data);
		
	public boolean rmvDetect(int opcode, String key);
	
	public String getSingleDetect(int opcode, String key);
	
	public List<String> getDetectionByDate(int opcode, String key, String initDate, String endDate);
	
	public List<String> getDetectionByTime(int opcode, String key, String date, String initTime, String endTime);
	
	public List<String> getDetectionByDateAndTime(int opcode, String key, String initDate,String endDate, String initTime, String endTime);
		
	public List<String> history(int opcode, String key);
	
	public String getDetectionByZone(int opcode, String zoneId);
	
	public Map<String,List<String>> Allhistory(int opcode);
	
	
	/*******************
	 * METHODS FOR SENSORS' INFOS
	 * 
	 */
	
	public boolean insertZone(int opcode,String zoneId, String description, String name,
			String edificio, String piano);

	public boolean insertBS(int opcode,String bsId, String description, String name,
			String localIp);

	public boolean removeZone(int opcode,String zoneId);

	public boolean removeBaseStation(int opcode,String bsId);

	public Map<String, String> getSensBS(int opcode,String sId);

	public Map<String, String> getSensZone(int opcode,String sId);
	
	
	
	/**** ASSOCIATIVE TABLE 
	 * @return ***/
	
	public boolean insertEntry(int opcode, String key, String nodeId, String zoneId);
	
	public boolean removeEntry(int opcode,String sensId);
	
	//public boolean updateEntry(int opcode,String sensId, String nodeId,String zoneId, String netId);
	
	public Map<String,String> getSensorPosition(int opcode,String key);
	
	public Map<String,List<String>> getSensList(int opcode,String nodeId);
	
	public Map<String,List<String>> getNodeList(int opcode,String zoneId);
	
	public Map<String,List<String>> getZoneList(int opcode,String netId);

	public List<String> getSensorBYZone(int opcode, String key);

	public List<String> getSensorBYNode(int opcode, String key);



}
