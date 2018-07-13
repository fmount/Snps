package snps.registry;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.interfaces.iDaoInterface;
import org.osgi.snps.base.interfaces.iEventPublisherInterface;
import org.osgi.snps.base.interfaces.iRegistryInterface;
import org.osgi.snps.base.util.JSonUtil;



public class RegisterService implements iRegistryInterface {

	public static iDaoInterface daoservice;
	public static BundleContext context;
	public static iEventPublisherInterface ipubservice;

	
	public RegisterService(BundleContext c) {
		context = c;
	}

	public boolean checkDAO() {
		try {
			System.out.println(daoservice.sayHello());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String sayHello() {
		return "I'm SNPS Registry and I'm Active!";
	}
	
	
	
	/****************************SENSORNODES QUERY****************************/
	
	

	/**
	 * @param opcode: per selezionare il db su cui serializzare l'elemento (3 per
	 * MySQL) 
	 * @param key: id del sensore da registrare 
	 * @param description: contiene l'intero Document che viene fuori dal parsing 
	 * del file xml (SensorML) Il metodo,non fa altro che rendere persistente la 
	 * descrizione inviata dalla WSN.
	 * @param sensor: Immagine del Sensore generata dal core
	 * 
	 * @return boolean: Risultato dell'operazione
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean serializeComponent(int opcode, String key,
			String description,String nature, String sensor) {
		System.out.println("Perform Insert Component!");
		ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
		ipubservice = (iEventPublisherInterface) context.getService(reference);
		ipubservice.sendEvent(key,"registration");
		reference = context
				.getServiceReference(iDaoInterface.class.getName());
		daoservice = (iDaoInterface) context.getService(reference);
		return daoservice.registerComponent(opcode, key, description,nature, sensor);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean updateComponent(int opcode, String key,
			String description,String nature, String sensor) {
		System.out.println("Perform Update Component!");
		ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
		ipubservice = (iEventPublisherInterface) context.getService(reference);
		ipubservice.sendEvent(key,"update");
		reference = context
				.getServiceReference(iDaoInterface.class.getName());
		daoservice = (iDaoInterface) context.getService(reference);
		return daoservice.updateComponent(opcode, key, description,nature,sensor);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean removeComponent(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"remove");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.removeComponent(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getImageComponent(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"getimage");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getImageComponent(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getComponentDescription(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"getdescription");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getComponentDescription(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getAllImages(int opcode) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent("Sys","getAllSensors");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getAllImages(opcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getSensorBYType(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"SensType");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getSensorBYType(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getSensorBYZone(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"SensType");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getSensorByZone(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getSensorBYNode(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"SensType");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getSensorByNode(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/*******************************************************************/

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean recDetect(int opcode, SimpleData data) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(data.toString(),"recMeas");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.recDetect(opcode, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getSingleDetect(int opcode, String key){
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"getMeas");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getSingleDetection(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getDetectionByDate(int opcode, String key, String initDate, String endDate){
		
		try{
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"getMeas");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getDetectionByDate(opcode, key, initDate, endDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getDetectionByTime(int opcode, String key, String date,
			String initTime, String endTime){
		try{
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"getMeas");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getDetectionByTime(opcode, key, date, initTime, endTime);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getDetectionByDateAndTime(int opcode, String key, 
			String initDate,String endDate, String initTime, String endTime){
		try{
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"getMeas");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getDetectionByDateAndTime(opcode, key, initDate,endDate, initTime, endTime);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean rmvDetect(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"rmvMeas");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.rmvDetect(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getDetectionByZone(int opcode, String zoneId){
		try {
			Map<String,List<String>> meas = new HashMap<String,List<String>>();
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(zoneId,"getMeas");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			Map<String,List<String>> mList = daoservice.getSensListByZone(opcode, zoneId);
			System.out.println(mList.toString());
			Iterator it = mList.entrySet().iterator();
		    
			while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        List<String> ids = (List<String>)pairs.getValue();
		        //List<String> measTmp = new ArrayList<String>();
		        for(int i=0;i<ids.size();i++){
		        	List<String> measTmp = daoservice.history(opcode, ids.get(i));
		        	meas.put(ids.get(i), measTmp);
		        }
		        
		    }
			return JSonUtil.HLToJSon((HashMap<String, List<String>>) meas);
			
		} catch (Exception e) {
				e.printStackTrace();
				return null;
		}		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> history(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"history");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.history(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, List<String>> Allhistory(int opcode) {
		try {			
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent("Sys","AllHistory");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.Allhistory(opcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean insertEntry(int opcode, String sensId, String nodeId,String zoneId) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(sensId,"insertEntry");
			System.out.println("P3: "+sensId+" - "+nodeId+" - "+zoneId);
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.insertEntry(opcode, sensId, nodeId, zoneId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean removeEntry(int opcode, String sensId) {
		try {			
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(sensId,"rmvEntry");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.removeEntry(opcode, sensId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean insertZone(int opcode, String zoneId, String description,
			String name, String edificio, String piano) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(zoneId,"insZone");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.insertZone(opcode, zoneId, description, name, edificio, piano);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean insertBS(int opcode, String bsId, String description,
			String name, String localIp) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(bsId,"insBS");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.insertBS(opcode, bsId, description, name, localIp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean removeZone(int opcode, String zoneId) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(zoneId,"rmvZone");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.removeZone(opcode, zoneId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean removeBaseStation(int opcode, String bsId) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(bsId,"rmvBS");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.removeBaseStation(opcode, bsId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, String> getSensBS(int opcode, String sId) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(sId,"getBSInfo");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getSensBS(opcode, sId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, String> getSensZone(int opcode, String sId) {
		try {			
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(sId,"getZoneInfo");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getSensZone(opcode, sId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
/*	@Override
	public boolean updateEntry(int opcode, String sensId, String nodeId,
			String zoneId, String netId) {
		try {
			
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(sensId,"updateEntry");
			return daoservice.updateEntry(opcode, sensId, nodeId, zoneId, netId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}*/

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, String> getSensorPosition(int opcode, String key) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(key,"getSensInfo");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getSensorPosition(opcode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, List<String>> getSensList(int opcode, String nodeId) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(nodeId,"getSensList");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getSensList(opcode, nodeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, List<String>> getNodeList(int opcode, String zoneId) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(zoneId,"getNodeList");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getNodeList(opcode, zoneId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, List<String>> getZoneList(int opcode, String netId) {
		try {
			ServiceReference reference = context.getServiceReference(iEventPublisherInterface.class.getName());
			ipubservice = (iEventPublisherInterface) context.getService(reference);
			ipubservice.sendEvent(netId,"getZoneList");
			reference = context
					.getServiceReference(iDaoInterface.class.getName());
			daoservice = (iDaoInterface) context.getService(reference);
			return daoservice.getZoneList(opcode, netId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

