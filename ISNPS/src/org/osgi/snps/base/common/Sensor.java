package org.osgi.snps.base.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.interfaces.iGWInterface;
import org.osgi.snps.base.util.Util;

public class Sensor extends ABComponent implements Component, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Map<String, List<String>> netParams;
	public Map<String, List<String>> capabilities;
	private Map<String, List<String>> INPUT_LIST;
	private Map<String, List<String>> OUTPUT_LIST;
	public Map<String, String> position;
	public List<String> referToHybrid;

	// final static String[] topics = { "osgi/testEvent" };
	// final static String[] topics = { "osgi/dataEvent" };

	/*
	 * @SuppressWarnings("rawtypes") public Dictionary dict;
	 * 
	 * Subscriber sbs;
	 */
	BundleContext context;

	/**
	 * Costruttore di base, per sensori interni che non fanno parte della WSN
	 * 
	 * @param id
	 * @param name
	 * @param model
	 * @param type
	 * @param state
	 * @param description
	 * @param nature
	 * @param IL
	 * @param OL
	 */
	public Sensor(String id, String name, String model, String type,
			String state, String description, String nature,
			Map<String, List<String>> IL, Map<String, List<String>> OL,
			Map<String, String> position) {
		super(id, name, model, type, state, description, nature);
		netParams = new HashMap<String, List<String>>();
		capabilities = new HashMap<String, List<String>>();
		this.position = new HashMap<String, String>();
		INPUT_LIST = IL;
		OUTPUT_LIST = OL;
		/*
		 * sbs=null; dict = new Properties(); addFilter(this.getID());
		 */

		referToHybrid = new ArrayList<String>();
	}

	/**
	 * Costruttore che rispecchia il SensorML di un sensore proveniente dalla
	 * WSN
	 * 
	 * @param id
	 * @param name
	 * @param model
	 * @param description
	 * @param state
	 * @param nature
	 * @param IL
	 * @param OL
	 * @param netParams
	 * @param capabilities
	 * @param position
	 */
	public Sensor(String id, String name, String model, String type,
			String state, String description, String nature,
			Map<String, List<String>> IL, Map<String, List<String>> OL,
			Map<String, List<String>> netParams,
			Map<String, List<String>> capabilities, Map<String, String> position) {
		super(id, name, model, type, state, description, nature);
		this.capabilities = capabilities;
		this.netParams = netParams;
		this.position = position;
		INPUT_LIST = IL;
		OUTPUT_LIST = OL;

		/*
		 * dict = new Properties(); addFilter(this.getID()); sbs=null;
		 */

		referToHybrid = new ArrayList<String>();
	}

	/* GETTERS AND SETTERS */

	public List<String> getReferToHybrid() {
		return referToHybrid;
	}

	public void setReferToHybrid(List<String> referToHybrid) {
		this.referToHybrid = referToHybrid;
	}

	public Map<String, String> getPosition() {
		return position;
	}

	public void setPosition(Map<String, String> position) {
		this.position = position;
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

	public void setINPUT_LIST(Map<String, List<String>> IL) {
		INPUT_LIST = IL;
	}

	public Map<String, List<String>> getOUTPUT_LIST() {
		return OUTPUT_LIST;
	}

	public void setOUTPUT_LIST(Map<String, List<String>> OL) {
		OUTPUT_LIST = OL;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += ",capabilities: " + this.getCapabilities().toString()
				+ ",net parameters: " + this.getNetParams().toString()
				+ ",position: " + this.getPosition().toString()
				+ ",inputList: " + getINPUT_LIST().toString() + ",outputList: "
				+ getOUTPUT_LIST().toString() + ",ReferToHybrids: "
				+ getReferToHybrid() + "}";
		return str;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getData(BundleContext context, String mode, String[] options,
			String action) {
		//if (getState().equalsIgnoreCase("ON")) {
			if (mode.equals("sync")) {
				try {
					// System.out.println("MODE: "+mode);
					iGWInterface gw;
					ServiceReference serviceRef;
					serviceRef = context.getServiceReference(iGWInterface.class
							.getName());
					gw = (iGWInterface) context.getService(serviceRef);
					String id_meas_to_set = Util.IdGenerator().replace("-", "");
					// String received =
					// gw.getData(id_meas_to_set,getID(),mode,options,action);
					String received = gw.getData(id_meas_to_set, getID(),
							getNature(), mode, options, action);
					Util.writeTmpData(received);
					
					/**
					 * Ritorno i dati al chiamante, il quale si occupera' di
					 * processarli..
					 */
					return received;
				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
			} else if (mode.equals("async")) {
				iGWInterface gw;
				ServiceReference serviceRef;
				serviceRef = context.getServiceReference(iGWInterface.class
						.getName());
				gw = (iGWInterface) context.getService(serviceRef);
				String id_meas_to_set = Util.IdGenerator().replace("-", "");
				// gw.getData(id_meas_to_set,getID(),mode,options,action);
				gw.getData(id_meas_to_set, getID(), getNature(), mode, options,
						action);
				return "Get Data Request sent To Wsn with id: "
						+ id_meas_to_set;
			}

			else {
				return "Error sending GetData Request!!";
			}

	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Override
	public String isAlive(BundleContext context) {
		try {
			// System.out.println("MODE: "+mode);
			iGWInterface gw;
			ServiceReference serviceRef;
			serviceRef = context.getServiceReference(iGWInterface.class
					.getName());
			gw = (iGWInterface) context.getService(serviceRef);
			String id_meas_to_set = Util.IdGenerator().replace("-", "");
			// String received =
			// gw.getData(id_meas_to_set,getID(),mode,options,action);
			String received = gw.isAlive(this.getID());
			// Util.writeTmpData(received);
			/**
			 * Ritorno i dati al chiamante, il quale si occupera' di
			 * processarli..
			 */
			return received;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "Error sending request";
		}
	}

	/*
	 * @SuppressWarnings({ "unchecked", "unused", "rawtypes" }) public void
	 * addSubs(BundleContext context){
	 * 
	 * if(sbs!=null) return; // Creo un subs.. dict = new Properties();
	 * System.out.println("Create new subs..");
	 * dict.put(EventConstants.EVENT_TOPIC,"osgi/dataEvent/"+this.getID());
	 * addFilter(this.getID());
	 * System.out.println("[SUBSCRIBER "+this.getID()+"]: Create subs on topics: "
	 * +dict.get(EventConstants.EVENT_TOPIC).toString()); sbs = new
	 * Subscriber(this.getID(),"sensdata",context); //Registro l'EventHandler
	 * del subscriber, in modo da poter abilitarlo all'ascolto sul topic..
	 * ServiceRegistration registration =
	 * context.registerService(EventHandler.class.getName(), sbs, dict); }
	 * 
	 * @SuppressWarnings({ "unchecked" }) public void addFilter(String filter){
	 * //System.out.println("Adding filter: "+filter);
	 * dict.put(EventConstants.EVENT_FILTER, "(sid="+filter+")"); }
	 * 
	 * public void removeFilter(){
	 * //System.out.println("Removing filter: "+dict.
	 * get(EventConstants.EVENT_FILTER));
	 * dict.remove(EventConstants.EVENT_FILTER); }
	 * 
	 * public void updateFilter(String filter){ removeFilter();
	 * addFilter(filter);
	 * System.out.println("[Sensor: "+this.getID()+"] -> Filter changed to: "
	 * +filter); }
	 */
}
