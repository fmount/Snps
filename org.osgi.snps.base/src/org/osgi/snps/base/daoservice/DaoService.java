package org.osgi.snps.base.daoservice;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.interfaces.iDaoInterface;

public class DaoService implements iDaoInterface {

	@Override
	public String sayHello() {
		Random random = new Random();
		// Create a number between 0 and 5
		int nextInt = random.nextInt(6);
		switch (nextInt) {
		case 0:
			return "I'm DAO Service for SNPS base [English Style]";
		case 1:
			return "DAO es el servicio ofrecido por el SNPS Base [Spanish Style]";
		case 2:
			return "DAO ist der Service von SNPS Basis angeboten [German Style]";
		case 3:
			return "DAO est le service offert par SNPS base [French Style]";
		case 4:
			return "DAO est cultus offertur ab SNPS Base [Latin Style]";
		default:
			return "Sono il servizio DAO offerto da SNPS Base [Italian Style]";
		}
	}

	@Override
	public boolean registerComponent(int opcode, String key,
			String description,String nature,String sensor) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().insertComponent(key, nature,
						description, sensor);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				
				return da.getDataManager().insertComponent(key, nature,description,sensor);
				
			default:
				System.out.println("Error!");
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error!");
			return false;
		}
	}


	@Override
	public boolean updateComponent(int opcode, String key,
			String description,String nature, String sensor) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().updateComponent(key,nature,description,sensor);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().updateComponent(key, nature,
						description, sensor);
			default:
				System.out.println("Error!");
				return false;
			}
		
		} catch (Exception e) {
			System.out.println("Error!");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean removeComponent(int opcode, String key) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().removeComponent(key);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().removeComponent(key);
			default:
				System.out.println("Error!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getImageComponent(int opcode, String key) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensorImage(key);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().getSensorImage(key);
			default:
				System.out.println("Error!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getAllImages(int opcode) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getAllSensorImages();

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().getAllSensorImages();
			default:
				System.out.println("Error!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getComponentDescription(int opcode, String key) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensorDescription(key);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().getSensorDescription(key);
			default:
				System.out.println("Error!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean recDetect(int opcode, SimpleData data) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().registerMeas(data);

			case 3:
				System.out.println("Registering on MySQL!");
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().registerMeas(data);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}

	@Override
	public boolean rmvDetect(int opcode, String key) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().removeMeas(key);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().removeMeas(key);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}

	@Override
	public List<String> history(int opcode, String key) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().history(key);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().history(key);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public Map<String, List<String>> Allhistory(int opcode) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().Allhistory();

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().Allhistory();

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public List<String> getSensorBYType(int opcode,String type) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensorBYType(type);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getSensorBYType(type);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public boolean insertZone(int opcode,String zoneId, String description, String name,
			String edificio, String piano) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().insertZone(zoneId, description, name, edificio, piano);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().insertZone(zoneId, description, name, edificio, piano);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}

	@Override
	public boolean insertBS(int opcode,String bsId, String description, String name,
			String localIp) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().insertBS(bsId, description, name, localIp);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().insertBS(bsId, description, name, localIp);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}

	@Override
	public boolean removeZone(int opcode,String zoneId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().removeZone(zoneId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().removeZone(zoneId);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}

	@Override
	public boolean removeBaseStation(int opcode,String bsId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().removeBaseStation(bsId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().removeBaseStation(bsId);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}

	@Override
	public Map<String, String> getSensBS(int opcode,String sId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensBS(sId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getSensBS(sId);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public Map<String, String> getSensZone(int opcode,String sId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensZone(sId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getSensZone(sId);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}
	
	@Override
	public List<String> getSensorByZone(int opcode, String zoneid){
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensorsByZone(zoneid);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getSensorsByZone(zoneid);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}
	
	@Override
	public List<String> getSensorByNode(int opcode, String bsid){
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensorsByNode(bsid);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getSensorsByNode(bsid);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public boolean insertEntry(int opcode,String sensId, String nodeId, String zoneId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().insertEntry(sensId, nodeId, zoneId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().insertEntry(sensId, nodeId, zoneId);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}

	@Override
	public boolean removeEntry(int opcode,String sensId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().removeEntry(sensId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().removeEntry(sensId);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}

	/*@Override
	public boolean updateEntry(int opcode,String sensId, String nodeId, String zoneId,
			String netId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().updateEntry(sensId, nodeId, zoneId, netId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().updateEntry(sensId, nodeId, zoneId, netId);

			default:
				System.out.println("Error DB Select!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return false;
		}
	}*/

	@Override
	public Map<String, String> getSensorPosition(int opcode,String key) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensorPosition(key);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getSensorPosition(key);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public Map<String, List<String>> getSensList(int opcode,String nodeId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensList(nodeId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getSensList(nodeId);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public Map<String, List<String>> getNodeList(int opcode,String zoneId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getNodeList(zoneId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getNodeList(zoneId);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}
	
	@Override
	public Map<String, List<String>> getSensListByZone(int opcode,String zoneId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSensListByZone(zoneId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getSensListByZone(zoneId);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public Map<String, List<String>> getZoneList(int opcode,String netId) {
		try {
			switch (opcode) {
			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getZoneList(netId);

			case 3:
				MySqlDAOFactory d = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return d.getDataManager().getZoneList(netId);

			default:
				System.out.println("Error DB Select!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error DB Select!");
			return null;
		}
	}

	@Override
	public String getSingleDetection(int opcode,String id_meas) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getSingleDetection(id_meas);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().getSingleDetection(id_meas);
			default:
				System.out.println("Error!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getDetectionByDate(int opcode,String sid, String initDate,
			String endDate) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getDetectionByDate(sid, initDate, endDate);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().getDetectionByDate(sid, initDate, endDate);
			default:
				System.out.println("Error!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getDetectionByTime(int opcode,String sid, String date,
			String time1, String time2) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getDetectionByTime(sid, date,time1,time2);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().getDetectionByTime(sid, date,time1,time2);
			default:
				System.out.println("Error!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<String> getDetectionByDateAndTime(int opcode, String key,
			String initDate, String endDate, String initTime, String endTime) {
		try {
			switch (opcode) {

			case 2:
				MongoDAOFactory dao = (MongoDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return dao.getMongoManager().getDetectionByDateAndTime(key,initDate,endDate,initTime,endTime);

			case 3:
				MySqlDAOFactory da = (MySqlDAOFactory) DataManagerDaoFactory
						.getDAOFactory(opcode);
				return da.getDataManager().getDetectionByDateAndTime(key,initDate,endDate,initTime,endTime);
			default:
				System.out.println("Error!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
