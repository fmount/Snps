package org.osgi.snps.base.common;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.snps.base.interfaces.iCoreInterface;
import org.osgi.snps.base.interfaces.iDataFlow;
import org.osgi.snps.base.interfaces.iGWInterface;
import org.osgi.snps.base.json.JSONException;
import org.osgi.snps.base.json.JSONObject;
import org.osgi.snps.base.util.JSonUtil;


public class SensHybrid extends ABComponent implements Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Sensor> sensors;
	ArrayList<String> sensids;
	private String expression="";
	private Map<String, List<String>> netParams;
	private Map<String, List<String>> capabilities;
	private Map<String, List<String>> INPUT_LIST;
	private Map<String, List<String>> OUTPUT_LIST;
	private Map<String, String> position;
	

	@SuppressWarnings("rawtypes")
	public Dictionary dict;

	Subscriber sbs;

	final static String[] topics = { "osgi/testEvent" };

	public enum modes {
		sync, async
	}
	
	public SensHybrid(String id, String name, String model, String type,
			String state, String description, String nature,
			List<Sensor> sensors) {
		super(id, name, model, type, state, description, nature);
		this.sensors = sensors;
		this.expression = "none";
		dict = new Properties();

		//sbs = null;
		
		//addFilter(this.getID());
	}
	
	public SensHybrid(String id, String name, String model, String type,
			String state, String description, String nature,
			ArrayList<String> sensids) {
		super(id, name, model, type, state, description, nature);
		this.sensids = sensids;
		this.expression = "none";
		dict = new Properties();
	}
	
	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	@Override
	public String toString() {
		String str = super.toString() + " , expression: " + expression;
		str += " , Sensors: [ ";
		for(int i=0;i<getSensors().size();i++){
			str += getSensors().get(i).toString();
		}
		str +=" ]";
		return str;

	}

	@Override
	public String getData(BundleContext context, String mode,String[]options,String action) {
		List<Sensor> sensList = this.getSensors();
		Iterator<Sensor> it = sensList.iterator();
		// Recupero i dati per ogni sensore..
		int i = 0;
		switch (modes.valueOf(mode)) {
		case sync:
			List<String> values = new ArrayList<String>();
			
			ServiceReference servRef = context
					.getServiceReference(iCoreInterface.class
							.getName());
			iCoreInterface coreservice = (iCoreInterface) context.getService(servRef);
			
//			iDataFlow dataflowservice;
//			ServiceReference serviceRef;
//			serviceRef = context.getServiceReference(iDataFlow.class
//					.getName());
//			dataflowservice = (iDataFlow) context.getService(serviceRef);
			SimpleData data = null;
			while (it.hasNext()) {
				System.out.println("Getting data " + i);
				Sensor s = it.next();
				// s.updateFilter(this.getID());
				data = JSonUtil.jsonToSimpleData(s.getData(context, mode,options,action));
				JSONObject json = new JSONObject();
//				System.out.println("DATA FROM SENSOR: " + data.toString());
				try {
					json.put("id", data.getSid());
					json.put("value", data.getData());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				dataflowservice.pushData(data, mode, context);
				values.add(json.toString());// mode: sync or async
				i++;
			}
			String expression = ((SensHybrid) coreservice.getSensList().get(this.getID())).getExpression();
			String tmp[] = options[0].split("#");
			data.setSid(this.getID());
			data.setData(new Template().processExpression(values, expression));
			data.setRef("");
			data.set_id_meas(tmp[1]);

			/*
			 * Ritorno i dati al chiamante..
			 */
			return JSonUtil.SimpleDataToJSON(data);
			//return values.toString();
		case async:
			while (it.hasNext()) {
				System.out.println("Getting data " + i);
				Sensor s = it.next();
				// s.updateFilter(this.getID());
				s.getData(context, mode,options,"none");// mode: sync or async
				i++;
			}
			//addSubs(context);
			return "Return something..";

		default:
			return "Error!!";
		}
	}

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public void addSubs(BundleContext context) {
		if(sbs!=null) return;
		// Creo un subs..
		dict.put(EventConstants.EVENT_TOPIC, topics);
		System.out.println("[SUBSCRIBER " + this.getID()
				+ "]: Create subs on topics: "
				+ dict.get(EventConstants.EVENT_TOPIC).toString());

		// Registro l'EventHandler del subscriber, in modo da poter
		// abilitarlo all'ascolto sul topic..

		
		sbs = new Subscriber(this.getID(),"sensdata",context,null);
		//Registro l'EventHandler del subscriber, in modo da poter abilitarlo all'ascolto sul topic..
		ServiceRegistration  registration = context.registerService(EventHandler.class.getName(), sbs, dict);
	}

	@SuppressWarnings({ "unchecked" })
	public void addFilter(String filter) {
		// System.out.println("Adding filter: "+filter);
		dict.put(EventConstants.EVENT_FILTER, "(sid=" + filter + ")");
	}

	public void removeFilter() {
		// System.out.println("Removing filter: "+dict.get(EventConstants.EVENT_FILTER));
		dict.remove(EventConstants.EVENT_FILTER);
	}

	public void updateFilter(String filter) {
		removeFilter();
		addFilter(filter);
		System.out.println("[Sensor: " + this.getID()
				+ "] -> Filter changed to: " + filter);
	}


	@Override
	public String isAlive(BundleContext context) {
		List<Sensor> sensList = this.getSensors();
		List<String> alivelist = new ArrayList<String>();
		Iterator<Sensor> it = sensList.iterator();
		while (it.hasNext()) {
			Sensor s = it.next();
			alivelist.add(s.isAlive(context));
		}
		return alivelist.toString();
	}

	public String getExpression() {
		return expression;
	}


	public void setExpression(String expression) {
		this.expression = expression;
	}

	public ArrayList<String> getSensids() {
		return sensids;
	}

	public void setSensids(ArrayList<String> sensids) {
		this.sensids = sensids;
	}

	public Map<String, List<String>> getNetParams() {
		return netParams;
	}

	public void setNetParams(Map<String, List<String>> netParams) {
		this.netParams = netParams;
	}

	public Map<String, List<String>> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(Map<String, List<String>> capabilities) {
		this.capabilities = capabilities;
	}

	public Map<String, List<String>> getINPUT_LIST() {
		return INPUT_LIST;
	}

	public void setINPUT_LIST(Map<String, List<String>> iNPUT_LIST) {
		INPUT_LIST = iNPUT_LIST;
	}

	public Map<String, List<String>> getOUTPUT_LIST() {
		return OUTPUT_LIST;
	}

	public void setOUTPUT_LIST(Map<String, List<String>> oUTPUT_LIST) {
		OUTPUT_LIST = oUTPUT_LIST;
	}

	public Map<String, String> getPosition() {
		return position;
	}

	public void setPosition(Map<String, String> position) {
		this.position = position;
	}

	
}
