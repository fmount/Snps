package org.osgi.snps.base.interfaces;

import java.util.List;
import java.util.Map;

import org.osgi.snps.base.common.SimpleData;

public interface iDaoInterface {

	/***********/
	public String sayHello();

	/***********/
	
	/*************
	 * 
	 * METHODS ON SENSOR NODES TABLE
	 * 
	 * */

	public boolean registerComponent(int opcode, String key, String description,String nature,
			String sensor);


	boolean updateComponent(int opcode, String key, String description,
			String nature, String sensor);

	public boolean removeComponent(int opcode, String key);

	public String getImageComponent(int opcode, String key);

	public String getComponentDescription(int opcode, String key);
	
	public List<String> getAllImages(int opcode);

	public List<String> getSensorBYType(int opcode,String key);
	
	
	
	/*************
	 * 
	 * METHODS ON MEASURES TABLE
	 * 
	 * */
	
	public boolean recDetect(int opcode, SimpleData data);
		
	public boolean rmvDetect(int opcode, String key);
		
	public String getSingleDetection(int opcode,String id_meas);
	
	public List<String> getDetectionByDate(int opcode,String sid,
			String initDate, String endDate);
	
	public List<String> getDetectionByTime(int opcode,String sid, String date,
			String time1, String time2);
	
	public List<String> getDetectionByDateAndTime(int opcode, String key,
			String initDate, String endDate, String initTime, String endTime);
	
	public List<String> history(int opcode, String key);
	
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
	
	boolean insertEntry(int opcode, String sensId, String nodeId, String zoneId);
	
	public boolean removeEntry(int opcode,String sensId);
	
	//public boolean updateEntry(int opcode,String sensId, String nodeId,String zoneId, String netId);
	
	public Map<String,String> getSensorPosition(int opcode,String key);
	
	public Map<String,List<String>> getSensList(int opcode,String nodeId);
	
	public Map<String,List<String>> getNodeList(int opcode,String zoneId);
	
	public Map<String,List<String>> getZoneList(int opcode,String netId);

	Map<String, List<String>> getSensListByZone(int opcode, String zoneId);

	public List<String> getSensorByZone(int opcode, String zoneid);

	public List<String> getSensorByNode(int opcode, String bsid);
	
}
