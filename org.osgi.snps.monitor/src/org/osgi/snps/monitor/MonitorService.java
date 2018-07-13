package org.osgi.snps.monitor;

import java.util.Dictionary;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.snps.base.common.Action;
import org.osgi.snps.base.common.Subscriber;
import org.osgi.snps.base.interfaces.iMonitor;

public class MonitorService implements iMonitor{

	final static String[] topics = {"osgi/dataEvent"};
	BundleContext context;
	@SuppressWarnings("rawtypes")
	ServiceRegistration registration;


	public MonitorService(BundleContext context){
		this.context = context;
	}

	@SuppressWarnings("rawtypes")
	public Dictionary dict;
	
	Subscriber sbs;
	
	@Override
	public void monitor(String sid, Action act) {
		dict = new Properties();
		addSubs(context,act,sid);
	}
	
	@SuppressWarnings("unchecked")
	public void addSubs(BundleContext context,Action ac,String filter){
		//if(sbs!=null) return;
		dict.put(EventConstants.EVENT_TOPIC,topics);
		//if(!filter.equalsIgnoreCase(""))
		System.out.println("FILTER: "+filter);
		if(!(filter.equals("")))		
					addFilter(filter);
		else
			System.out.println("No filter required!");
		System.out.println("[MONITOR DATA EVENT]: Create subs on topics: "+dict.get(EventConstants.EVENT_TOPIC).toString());
		sbs = new Subscriber("","monitor",context,ac);
		//Registro l'EventHandler del subscriber, in modo da poter abilitarlo all'ascolto sul topic..
		registration = context.registerService(EventHandler.class.getName(), sbs, dict);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean removeSubs(String param){
		String e = sbs.getAc().getComponent()+":"+sbs.getAc().getCmd();
		System.out.println("AC: "+e+" PARAM: "+param);
		if(param.equalsIgnoreCase(e)){
			ServiceReference sr = context.getServiceReference(EventHandler.class.getName());
			context.ungetService(sr);
		//context.ungetService(EventHandler.class.getName());
			this.sbs = null;
			return true;
		}
		return false;
	}
	
	@SuppressWarnings({ "unchecked" })
	public void addFilter(String filter){
		//System.out.println("Adding filter: "+filter);
		dict.put(EventConstants.EVENT_FILTER, "(sid="+filter+")");
	}
	
	public void removeFilter(){
		//System.out.println("Removing filter: "+dict.get(EventConstants.EVENT_FILTER));
		dict.remove(EventConstants.EVENT_FILTER);
	}
	
	public void updateFilter(String filter){
		removeFilter();
		addFilter(filter);
		System.out.println("[MONITOR:Info] -> Filter changed to: "+filter);
	}

	@Override
	public boolean stop(String param) {
		return removeSubs(param);
	}

}
