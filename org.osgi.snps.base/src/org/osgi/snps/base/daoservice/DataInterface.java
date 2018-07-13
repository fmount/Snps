package org.osgi.snps.base.daoservice;

import java.util.List;
import java.util.Map;

import org.osgi.snps.base.common.SimpleData;



public interface DataInterface {
	
	/* Gestione dei sensori */
	
	public boolean insertComponent(String key, String type, String description, String sensor);
	
	boolean updateComponent(String key, String type, String description,String sensor);
	
	public String getSensorImage(String key);
	
	public String getSensorDescription(String key);
	
	public List<String> getAllSensorImages();
	
	public boolean removeComponent(String key);
	
	/* Gestione della mia mappa della rete sottostante */
	
	public boolean insertEntry(String sensId, String nodeId,String zoneId);
	
	public boolean removeEntry(String sensId);
	
	//public boolean updateEntry(String sensId, String nodeId,String zoneId);
	
	public Map<String,String> getSensorPosition(String key);
	
	public Map<String,List<String>> getSensList(String nodeId);
	
	public Map<String, List<String>> getSensListByZone(String zoneId);
	
	public Map<String,List<String>> getNodeList(String zoneId);
	
	public Map<String,List<String>> getZoneList(String netId);
	
	
	/* Gestione della tabella relativa alle rilevazioni effettuate dai Sensori */
	
	public boolean registerMeas(SimpleData data);
	
	public boolean removeMeas(String id_meas);
		
	public List<String> history(String key);
	
	public Map<String, List<String>> Allhistory();
	
	public String getSingleDetection(String id_meas);
	
	public List<String> getDetectionByDate(String sid,
			String initDate, String endDate);
	
	public List<String> getDetectionByTime(String sid, String date, String time1,
			String time2);
	
	
	/*Algoritmo di riferimento: per ogni sensore -> d1 ---------- d2
	 *Dopo aver recuperato le date, per ogni data-> t1 --------- t2
	 * 
	 * */
	/**************************************/

	public List<String> getSensorBYType(String key);

	public boolean insertZone(String zoneId, String description, String name,
			String edificio, String piano);

	public boolean insertBS(String bsId, String description, String name,
			String localIp);

	public boolean removeZone(String zoneId);

	public boolean removeBaseStation(String bsId);

	public Map<String, String> getSensBS(String sId);

	public Map<String, String> getSensZone(String sId);

	public List<String> getSensorsByZone(String zoneid);

	public List<String> getSensorsByNode(String bsid);

}
