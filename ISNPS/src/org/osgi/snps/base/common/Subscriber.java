package org.osgi.snps.base.common;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.snps.base.interfaces.iCoreInterface;
import org.osgi.snps.base.interfaces.iDaoInterface;
import org.osgi.snps.base.interfaces.iDataFlow;
import org.osgi.snps.base.util.JSonUtil;
import org.osgi.snps.base.util.Util;



public class Subscriber implements EventHandler {

	public String refId="";
	Action ac=null;
	String type="";
	BundleContext context;
	iDaoInterface daoservice;
	iCoreInterface coreService;
	iDataFlow df;
	
	//private static Subscriber sub;
	
	
	public Subscriber(String refTo,String type, BundleContext context, Action ac){
		refId=refTo;
		this.type = type;
		this.context = context;
		this.ac = ac;
		if(ac!=null) 
			System.out.println("ACTION: "+ac.toString());
	}
	
	/*public static Subscriber getInstance(String refTo,String type, BundleContext context){
	    if (sub == null){
	      sub = new Subscriber(refTo,type,context);
	    }
	    return sub;
	  }*/
	
	@Override
	public void handleEvent(Event event) {
		/*for (String propertyName :	event.getPropertyNames()) {
			System.out.println(propertyName+" - "+event.getProperty(propertyName));
		}*/
		System.out.println("Type: " + type);
		try{
			if(type.equalsIgnoreCase("logger")){
				LogMessage(event);
				return;
			}
			
			if(type.equalsIgnoreCase("sensdata")){
				LogData(event);
				return;
			}			
			if(type.equalsIgnoreCase("monitor")){
				Monitor(event,ac);
				return;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void Monitor(Event event, Action ac){
		try {
			String received="";
			String action="";
			SimpleData sd;
			//PROCESSING DATA..
			for (String propertyName :	event.getPropertyNames()) {
				//System.out.println("[MONITOR:Info]-> Captured event "
							//+propertyName+" - "+event.getProperty(propertyName));
				if(propertyName.equalsIgnoreCase("data")){
					received = String.valueOf(event.getProperty(propertyName));
				}
				
				if(propertyName.equalsIgnoreCase("action")){
					action = String.valueOf(event.getProperty(propertyName));
					System.out.println("ACTI:: "+action);
				}
			}
			if(action.equals("none") || action.equals("")) return;
			
			if(!(action.equals(ac.getComponent()+":"+ac.getCmd()))){
				return;
			}
			System.out.println("[Monitor:Info] -> Process event with action "+ac.toString());
			//VERIFICO LA CONSISTENZA DEL DATO RICEVUTO..
			if(!(received.equals(""))){
				sd = JSonUtil.jsonToSimpleData(received);
				System.out.println("Get Consistent value: "+sd.getData());
			}
			else{
				System.out.println("Cannot process this data!");
				return;
			}
			//ESEGUO L'AZIONE RICHIESTA SU QUEL DATO..
			/*if((ac.getComponent().equalsIgnoreCase(sd.getSid())) && 
					(ac.getRestriction()<Double.parseDouble(sd.getData()))){
				System.out.println("I can exec");
			}*/
			if(ac.getRestriction()<Double.parseDouble(sd.getData())){
				System.out.println("I can exec action: "+ac.getCmd());
				List<String> sids = new ArrayList<String>();
				sids.add(ac.getComponent());

				//ESEGUO L'AZIONE SULL'ATTUATORE SPECIFICO..
				ServiceReference reference = context.getServiceReference(iCoreInterface.class.getName());
				if(reference!=null){
					coreService = (iCoreInterface) context.getService(reference);
					coreService.interprCall(ac.getCmd(), null,sids, "");
					
					//coreService.monitorCall("stop", ac.getComponent()+":"+ac.getCmd());
				}
			}
			else{
				System.out.println("SKIP: "+ac.restriction+" -> "+sd.getData());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Action getAc() {
		return ac;
	}

	public void setAc(Action ac) {
		this.ac = ac;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public void LogData(Event event){
		try {
			String sid ="";
			String data ="";
			String action = "";
			Action act=null;
			
			for (String propertyName :	event.getPropertyNames()) {
					if(propertyName.equalsIgnoreCase("sid")){
						sid = String.valueOf(event.getProperty(propertyName));
					}
					if(propertyName.equalsIgnoreCase("data")){
						data = String.valueOf(event.getProperty(propertyName));
					}
					if(propertyName.equalsIgnoreCase("action")){
						action = String.valueOf(event.getProperty(propertyName));
						System.out.println("ACC:::"+action);
					}
					
			}
			
				SimpleData sd = JSonUtil.jsonToSimpleData(data);				
				
				//Chiamo il dataflow che processa il dato..
				ServiceReference reference = context.getServiceReference(iDataFlow.class.getName());
				if(reference!=null){
					df = (iDataFlow) context.getService(reference);
					//df.pushData(sd, "test", context);
					df.pushData(sd, "persist", context);
				
				
				}else{
					System.out.println("Error getting reference..");
				}
				
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	
	
	public void LogMessage(Event event){
		String msg ="[LOGGING]-> \n ";
		for (String propertyName :	event.getPropertyNames()) {
			//System.out.println("\t" + propertyName + " = " +event.getProperty(propertyName));
			msg += " "+propertyName+ " = "+event.getProperty(propertyName)+"\n";
			
			/*	if(propertyName.equalsIgnoreCase("registration")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Sensor Registered: "+type);
					String msg = "[LOGGING]-> Sensor Registered: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("regmeasure")){
					System.out.println("AA");
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Measure Registered: "+type);
					String msg = "[LOGGING]-> Measure Registered: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				
				if(propertyName.equalsIgnoreCase("update")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Update Sensor: "+type);
					String msg = "[LOGGING]-> Update Sensor: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				
				if(propertyName.equalsIgnoreCase("remove")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Remove Sensor: "+type);
					String msg = "[LOGGING]-> Remove Sensor: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("getimage")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Sensor Image: "+type);
					String msg = "[LOGGING]-> Get Sensor Image: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				
				if(propertyName.equalsIgnoreCase("getdescription")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Sensor Description: "+type);
					String msg = "[LOGGING]-> Get Sensor Description: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("splan")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Set Sampling Plan: "+type);
					String msg = "[LOGGING]-> Set Sampling Plan: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				/**************START****************/

		/*		if(propertyName.equalsIgnoreCase("getAllSensors")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get All Sensors: "+type);
					String msg = "[LOGGING]-> Get All Sensors: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("sensType")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Sensors By Type: "+type);
					String msg = "[LOGGING]-> Get Sensors By Type: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("history")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Hystory: "+type);
					String msg = "[LOGGING]-> Hystory: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("allhistory")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> All History: "+type);
					String msg = "[LOGGING]-> All History: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("getSensInfo")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Sens Info: "+type);
					String msg = "[LOGGING]-> Get Sens Info: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("getsenslist")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Sens List: "+type);
					String msg = "[LOGGING]-> Get Sens List: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("getzonelist")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Zone List: "+type);
					String msg = "[LOGGING]-> Get Zone List: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("inszone")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Zone Registered: "+type);
					String msg = "[LOGGING]-> Zone Registered: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("insbs")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Base Station Registered: "+type);
					String msg = "[LOGGING]-> Base Station Registered: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("rmvzone")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Zone Removed: "+type);
					String msg = "[LOGGING]-> Zone Removed: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("rmvbs")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Base Station removed: "+type);
					String msg = "[LOGGING]-> Base Station removed: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("getbsinfo")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Base Station Info: "+type);
					String msg = "[LOGGING]-> Get Base Station Info: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("getzoneinfo")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Zone Info: "+type);
					String msg = "[LOGGING]-> Get Zone Info: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("rmventry")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Remove Entry: "+type);
					String msg = "[LOGGING]-> Remove Entry: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("insertentry")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Entry Registered: "+type);
					String msg = "[LOGGING]-> >Entry Registered: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("updateentry")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Entry Updated: "+type);
					String msg = "[LOGGING]-> Entry Updated: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				if(propertyName.equalsIgnoreCase("getnodelist")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Get Node List: "+type);
					String msg = "[LOGGING]-> Get Node List: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				
				if(propertyName.equalsIgnoreCase("sendCommand")){
					String type = String.valueOf(event.getProperty(propertyName));
					System.out.println("[LOGGING]-> Send Command: "+type);
					String msg = "[LOGGING]-> Send Command: "+type;
					Util.LogInfo(msg, "org.osgi.snps.log");
				}
				
				/*******************ENDDDDD*******************/
		}
		msg +="---------------------------\n";
		System.out.println(msg);
		Util.LogInfo(msg, "org.osgi.snps.log");
	}
}