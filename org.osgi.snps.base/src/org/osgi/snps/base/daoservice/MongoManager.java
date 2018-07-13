package org.osgi.snps.base.daoservice;

import java.util.List;
import java.util.Map;

import org.osgi.snps.base.common.SimpleData;

import com.mongodb.DB;

public class MongoManager implements DataInterface {

	DB mongo;

	public MongoManager(DB db) {
		mongo = db;
	}

	@Override
	public boolean insertComponent(String key, String type, String description,
			String sensor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateComponent(String key, String type, String description,
			String sensor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSensorImage(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSensorDescription(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllSensorImages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeComponent(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertEntry(String sensId, String nodeId, String zoneId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEntry(String sensId) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Map<String, String> getSensorPosition(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getSensList(String nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getNodeList(String zoneId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getZoneList(String netId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean registerMeas(SimpleData data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeMeas(String id_meas) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> history(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> Allhistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSingleDetection(String id_meas) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDetectionByDate(String sid, String initDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDetectionByTime(String sid, String date,
			String time1, String time2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSensorBYType(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertZone(String zoneId, String description, String name,
			String edificio, String piano) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertBS(String bsId, String description, String name,
			String localIp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeZone(String zoneId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeBaseStation(String bsId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, String> getSensBS(String sId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getSensZone(String sId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getDetectionByDateAndTime(String key, String initDate,
			String endDate, String initTime, String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getSensListByZone(String zoneId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSensorsByZone(String zoneid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSensorsByNode(String bsid) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
