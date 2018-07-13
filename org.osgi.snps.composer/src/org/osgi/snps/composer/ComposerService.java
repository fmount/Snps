package org.osgi.snps.composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.snps.base.common.*;
import org.osgi.snps.base.interfaces.iCompose;
import org.osgi.snps.base.interfaces.iEventPublisherInterface;
import org.osgi.snps.base.interfaces.iRegistryInterface;
import org.osgi.snps.base.util.JSonUtil;
import org.osgi.snps.base.util.SensorMLParser;
import org.osgi.framework.ServiceReference;

public class ComposerService implements iCompose {

	BundleContext context;
	iRegistryInterface registryservice;
	iEventPublisherInterface pubservice;

	public ComposerService(BundleContext context) {
		this.context = context;

	}

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SensHybrid compose(List<Sensor> slist) {
		SensHybrid s;
		try {

			s = new SensHybrid(genId(slist), genName(slist), "",
					genType(slist), genState(slist), genDesc(slist),
					genNature(slist), slist);

			s.setCapabilities(genCapabilities(slist));
			s.setINPUT_LIST(genIl(slist));
			s.setOUTPUT_LIST(genOl("none"));
			s.setNetParams(genNetParams(slist));
			s.setPosition(genPosition(slist));

			// TODO: DECOMMENTARE PER DIALOGARE CON IL REGISTRY E SALVARE IL
			// SENSORE VIRTUALE..

			/*
			 * ServiceReference serviceRef = context
			 * .getServiceReference("org.osgi.snps.iregistry.RegisterService");
			 * registryservice = (iRegistryInterface) context
			 * .getService(serviceRef);
			 * 
			 * registryservice.serializeComponent(3, sid,
			 * SensorMLParser.genDescription(slist, sid, "aaa",cname,
			 * "DETECTOR", "ACTIVE"), s);
			 */
			ServiceReference serviceRef = context
					.getServiceReference(iRegistryInterface.class.getName());
			registryservice = (iRegistryInterface) context
					.getService(serviceRef);

			registryservice.serializeComponent(3, s.getID(), JSonUtil
					.DocumentTOJson(SensorMLParser.genDescription(slist, s)),
					"hybridNature", JSonUtil.HybridToJSON(s));
			ServiceReference reference = context
					.getServiceReference(iEventPublisherInterface.class
							.getName());
			pubservice = (iEventPublisherInterface) context
					.getService(reference);
			pubservice.sendEvent(s.getID(), "registration");
			/*
			 * EventPublisher publisher = getPublisher(context);
			 * publisher.sendEvent(s.getID(),"registration");
			 */

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return s;
	}

	@Override
	public SensHybrid compose(List<Sensor> slist, String expr) {
		SensHybrid s;
		try {

			s = new SensHybrid(genId(slist), genName(slist), "",
					genType(slist), genState(slist), genDesc(slist),
					genNature(slist), slist);
			s.setCapabilities(genCapabilities(slist));
			s.setINPUT_LIST(genIl(slist));
			s.setOUTPUT_LIST(genOl(expr));
			s.setNetParams(genNetParams(slist));
			s.setPosition(genPosition(slist));
			s.setExpression(expr);
			ServiceReference serviceRef = context
					.getServiceReference(iRegistryInterface.class.getName());
			registryservice = (iRegistryInterface) context
					.getService(serviceRef);

			registryservice.serializeComponent(3, s.getID(), JSonUtil
					.DocumentTOJson(SensorMLParser.genDescription(slist, s)), s
					.getNature(), JSonUtil.HybridToJSON(s));

			ServiceReference reference = context
					.getServiceReference(iEventPublisherInterface.class
							.getName());
			pubservice = (iEventPublisherInterface) context
					.getService(reference);
			pubservice.sendEvent(s.getID(), "registration");

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return s;

	}

	/* UTILITY DI COMPOSIZIONE */

	public String genId(List<Sensor> slist) {
		String id = "";
		for (int i = 0; i < slist.size(); i++) {
			if (i == 0)
				id += slist.get(i).getID();
			else
				id += "_" + slist.get(i).getID();
		}
		return id;
	}

	public String genName(List<Sensor> slist) {
		String name = "";
		for (int i = 0; i < slist.size(); i++) {
			if (i == 0)
				name += "A Virtual Sensor composed by: " + slist.get(i).getID();
			else
				name += "," + slist.get(i).getID();
		}
		return name;
	}

	public String genType(List<Sensor> slist) {
		String type = "DETECTOR";
		String toret = "";
		for (int i = 0; i < slist.size(); i++) {
			if (slist.get(i).getType().equalsIgnoreCase(type)) {
				toret = "DETECTOR_COMPOSED";
			} else
				toret = "ACTUATOR";
		}
		return toret;
	}
	
	public String genModel(){
		return "Hybrid Model";
	}

	public String genDesc(List<Sensor> slist) {
		String desc = "";
		for (int i = 0; i < slist.size(); i++) {
			if (i == 0)
				desc += "Virtualization with these features: " + "+ "
						+ slist.get(i).getDescription();
			else
				desc += "" + "+ " + slist.get(i).getDescription();
		}
		return desc;
	}

	public String genNature(List<Sensor> slist) {
		return "Hybrid Nature";
	}

	/**
	 * Regola: se almeno uno dei sensori che lo compongono è nello stato off, di
	 * default anche il sensore ibrido si troverà nello stato off; on
	 * altrimenti.
	 * 
	 * @param slist
	 * @return
	 */
	public String genState(List<Sensor> slist) {
		String state = "on";
		for (int i = 0; i < slist.size(); i++) {
			if (slist.get(i).getState().equalsIgnoreCase("off"))
				state = "off";
		}
		return state;
	}

	public Map<String,List<String>> genIl(List<Sensor> slist){
		Map<String,List<String>> hmap = new HashMap<String, List<String>>();
		for(Sensor sens : slist){
			Iterator it = sens.getINPUT_LIST().keySet().iterator();
			while(it.hasNext()){
				String s = (String) it.next();
				if(hmap.containsKey(s)){
					for(String str : sens.getINPUT_LIST().get(s)){
						if(!hmap.get(s).contains(str)){
							hmap.get(s).add(str);
						}
					}
				}
				else{
					hmap.put(s, sens.getINPUT_LIST().get(s));
				}
			}
//			hmap.putAll(sens.getINPUT_LIST());
		}
		return hmap;
	}

	public Map<String, List<String>> genCapabilities(List<Sensor> slist) {
		return slist.get(0).getCapabilities();
	}

	public Map<String, String> genPosition(List<Sensor> slist) {
		Map<String, String> hmap = slist.get(0).getPosition();
		Iterator it = hmap.keySet().iterator();
		while (it.hasNext()) {
			String s = (String) it.next();
			hmap.put(s, "hybrid");
		}
		return hmap;
	}

	public Map<String, List<String>> genNetParams(List<Sensor> slist) {
		Map<String, List<String>> hmap = slist.get(0).getNetParams();
		Iterator it = hmap.keySet().iterator();
		while (it.hasNext()) {
			String s = (String) it.next();
			List<String> list = new ArrayList<String>();
			int i = 0;
			while (i < hmap.get(s).size()) {
				list.add("hybrid");
				i++;
			}
			hmap.put(s, list);
		}
		return hmap;
	}

	public Map<String, List<String>> genOl(String expr) {
		Map<String, List<String>> mathfunc = new HashMap<String, List<String>>();
		ArrayList<String> exprfunc = new ArrayList<String>();
		exprfunc.add(expr);
		mathfunc.put("MathFunctionOutput", exprfunc);
		return mathfunc;
	}

	@Override
	public boolean destroy(List<String> isd) {
		// TODO Auto-generated method stub
		return false;
	}

}
