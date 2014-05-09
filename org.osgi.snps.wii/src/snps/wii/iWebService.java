package snps.wii;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.snps.base.common.SamplingPlan;
import org.osgi.snps.base.common.Sensor;
import org.osgi.snps.base.interfaces.iCoreInterface;
import org.osgi.snps.base.interfaces.iWebIntegrationInterface;
import org.osgi.snps.base.util.JSonUtil;
import org.w3c.dom.Document;

import smlparser.Parser;

public class iWebService implements iWebIntegrationInterface{

	BundleContext context;
	protected ComponentContext contex;
	iCoreInterface service;
	Parser pservice;
	private final int MY_SQL= 3;
	
	public enum commands {
		getSensor, getSDesc,getAllSensors, sensType, history, allhistory, 
		getSensInfo, getsenslist, getzonelist, inszone, insbs, rmvzone, rmvbs,
		getbsinfo, getzoneinfo, insertentry, rmventry, updateentry, getnodelist,
		detection, detectbydate, detectbytime, detectbydateandtime, detectbyzone,
		getSensorbyzone, getSensorbynode, updateComponent
	}
	
	/* protected void activate(ComponentContext ctx){
	    	contex = ctx;
	    	service = (iCoreInterface) contex.locateService("iCoreInterface");
	        System.out.println(service.sayHello());
	        System.out.println("FINISH..");
	    	
	 }*/
	
	public iWebService(BundleContext cntx){
		this.context = cntx;
		System.out.println("[Wii:Info]-> Started");
	}
	 
	@Override
	public String sayhello(){
		Random random = new Random();
		// Create a number between 0 and 5
		int nextInt = random.nextInt(6);
		switch (nextInt) {
		case 0:
			return "[WII]: Hello, I'm SNPS Wii!: 1.0 [English Style]\n";
		case 1:
			return "[WII]: Hola, soy el SNPS Wii [Spanish Style]\n";
		case 2:
			return "[WII]: Hello, Saya SNPS Wii! [Malaysian Style]\n";
		case 3:
			return "[WII]: Bonjour, je suis SNPS Wii [French Style]\n";
		case 4:
			return "[WII]: Hallo, ich bin SNPS Wii [German Style]\n";
		default:
			return "[WII]: Ciao, sono il Wii bundle di SNPS! [Italian Style]\n";
		}
	}

	
	/**
	 * @param command: it accepts enable/disable
	 * @param sids: sensors' id list 
	 * @param mode: sync/async (default async mode)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String sendCommand(String command, List<String> sids, String mode) {
		try {
			ServiceReference reference;
			reference = context
					.getServiceReference(iCoreInterface.class.getName());

			service = (iCoreInterface) context.getService(reference);
			// TRUE OR FALSE..
			return service.interprCall(command, null, sids, mode);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "Error!";
		}
	}

	/**
	 * @param splan: json format of samplingPlan, provided by JsonUtil transformation Class
	 *	
	 *
	 *  String id = Util.IdGenerator().replace("-","");
	 *	sp.setSplan_identifier(id);
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String setSPlan(String splan) {
		try {
			ServiceReference reference;
			reference = context
					.getServiceReference(iCoreInterface.class.getName());

			service = (iCoreInterface) context.getService(reference);
			SamplingPlan sp = JSonUtil.JsonToSamplingPlan(splan);
			String id = sp.getSplan_identifier();
			service.interprCall("splan", sp, null, "");
			return id;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "Error!";
		}
	}

	@Override
	public String stopSPlan(String idPlan) {
		return "Feature not supported..";
	}

	
	/**
	 * @param sensors: list of sensors identifiers
	 * @param schema: template (or operator) that contain function for data processing
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String buildVirtualSensor(List<String> sensors, String schema) {
		try {
			ServiceReference reference;
			reference = context
					.getServiceReference(iCoreInterface.class.getName());

			service = (iCoreInterface) context.getService(reference);
			return service.composerCall("compose", sensors, "");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "Error!";
		}
	}

	
	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String discoverySensorsAndMeasurements(String command, String sid,String type,
			String id_detection,String initDate, String endDate, String initTime, 
			String endTime,String zoneid,String bsid) {
		ServiceReference reference;
		reference = context
				.getServiceReference(iCoreInterface.class.getName());

		service = (iCoreInterface) context.getService(reference);
		List<String> params = new ArrayList<String>();
		
		switch (commands.valueOf(command)) {
		
		case getSensor:
			return service.regCall(command, MY_SQL, sid, null, null,"", null);
						
		/*case getSDesc:
			return service.regCall(command, MY_SQL, sid, null, null, "",null); */
			
		case getAllSensors:			
			return service.regCall(command, MY_SQL, "", null, null,"",null);

		case sensType:
			return service.regCall(command, MY_SQL, "", null, null,type,null);

		case getSensInfo:
			return service.regCall(command, MY_SQL, sid, null, null, "",null);

		case getbsinfo:
			return service.regCall(command, MY_SQL, sid, null, null, "",null);

		case getzoneinfo:
			return service.regCall(command, MY_SQL, sid, null, null, "",null);
		
		case getSensorbyzone:
			if(zoneid.equalsIgnoreCase(""))
				return "Error processing request!";
			
			params.add(zoneid);
			return service.regCall(command, MY_SQL, "", null, null, "",params);
			
		case getSensorbynode:
			if(bsid.equalsIgnoreCase(""))
				return "Error processing request!";
			
			params.add(bsid);
			return service.regCall(command, MY_SQL, "", null, null, "",params);

		case history:
			return service.regCall(command, MY_SQL, sid, null, null, "",null);

		case allhistory:
			return service.regCall(command, MY_SQL, "", null, null, "",null);

		case detection:
			if(id_detection.equalsIgnoreCase("")) 
					return "Error processing request!";
			params.add(id_detection);
			return service.regCall(command, MY_SQL, "",null,null,"",params);
			
		case detectbydate:
			if(initDate.equalsIgnoreCase(""))
				return "Error processing request!";
			if(endDate.equalsIgnoreCase(""))
				endDate = initDate;
			
			params.add(initDate);
			params.add(endDate);
			return service.regCall(command, MY_SQL, sid,null,null,"",params);
		
		case detectbytime:
			if(initDate.equalsIgnoreCase("") || initTime.equalsIgnoreCase(""))
				return "Error processing request!";
			if(endTime.equalsIgnoreCase(""))
				endTime="23:59:59";
			params.add(initDate);
			params.add(initTime);
			params.add(endTime);
			return service.regCall(command, MY_SQL, sid,null,null,"",params);
		
		case detectbydateandtime:
			if(initDate.equalsIgnoreCase("") || initTime.equalsIgnoreCase(""))
				return "Error processing request!";
			if(endDate.equalsIgnoreCase(""))
				endDate = initDate;
			if(endTime.equalsIgnoreCase(""))
				endTime="23:59:59";
			params.add(initDate);
			params.add(endDate);
			params.add(initTime);
			params.add(endTime);
			return service.regCall(command, MY_SQL, sid,null,null,"",params);
		
		case detectbyzone:
			if(zoneid.equalsIgnoreCase(""))
				return "Error processing request!";
			params.add(zoneid);
			return service.regCall(command, MY_SQL, sid,null,null,"",params);
		default:
			return null;
		}
	}

	
	/**
	 * @param sId : sensor identifier
	 * @param mode: sync/async
	 * @param action: json action format (specificare meglio)..
	 */
	@Override
	public String getData(String sId, String mode,String action) {
		try {
			return service.getData(sId, mode,action);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "Error Sending getData request!";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getSensorConfiguration(String sid) {
		ServiceReference reference;
		reference = context.getServiceReference(iCoreInterface.class.getName());
		service = (iCoreInterface) context.getService(reference);
		return service.regCall("getSDesc", MY_SQL, sid, null, null, "",null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String setSensorConfiguration(String sid, String sensormlpath) {
		try {	
			
			Document description;
			ServiceReference reference = context.getServiceReference(Parser.class.getName());
			Sensor s;
			if (reference != null) {
				description = JSonUtil.JSONToDocument(sensormlpath);
				pservice = (Parser) context.getService(reference);
				//description = pservice.getDocument(sensormlpath);
				s = pservice.parse(description);
				s.setID(sid);
				reference = context.getServiceReference(iCoreInterface.class.getName());
				service = (iCoreInterface) context.getService(reference);
				return service.regCall("updateComponent",MY_SQL, sid, description, s, "",null);
			}
			return "Service not available";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String isAlive(String sId) {
		try {
			ServiceReference reference;
			reference = context.getServiceReference(iCoreInterface.class.getName());
			service = (iCoreInterface) context.getService(reference);
			return service.isAlive(sId);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "Error Sending isAlive request!";
	}
}
