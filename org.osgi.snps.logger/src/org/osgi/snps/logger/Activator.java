package org.osgi.snps.logger;

import java.util.Dictionary;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.snps.base.common.Subscriber;


public class Activator implements BundleActivator {

	private static BundleContext context;
	//final static String[] topics = { "osgi/snps/log", "osgi/testEvent" }; 
	final static String[] topics = {"osgi/testEvent" };
	
	
	
	@SuppressWarnings("rawtypes")
	public Dictionary dict;
	
	Subscriber sbs;
	
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		dict = new Properties();
		addSubs(context);
		System.out.println("[LOGGER:Info]-> ACTIVE");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public void addSubs(BundleContext context){
		if(sbs!=null) return;
		dict.put(EventConstants.EVENT_TOPIC,topics);
		System.out.println("[LOGGER SUBSCRIBER]: Create subs on topics: "+dict.get(EventConstants.EVENT_TOPIC).toString());
		sbs = new Subscriber("","logger",context,null);
		//Registro l'EventHandler del subscriber, in modo da poter abilitarlo all'ascolto sul topic..
		ServiceRegistration  registration = context.registerService(EventHandler.class.getName(), sbs, dict);
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
		System.out.println("[Logger:] -> Filter changed to: "+filter);
	}

}

