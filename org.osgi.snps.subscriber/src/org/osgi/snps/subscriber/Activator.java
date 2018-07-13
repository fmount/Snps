package org.osgi.snps.subscriber;

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
	
	final static String[] topics = {"osgi/dataEvent" };
	
	
	
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
		//addFilter("sender.uri", "r-osgi://localhost.localdomain:9279");
		System.out.println("[DATA EVENT SUBSCRIBER]: Create subs on topics: "+dict.get(EventConstants.EVENT_TOPIC).toString());
		sbs = new Subscriber("","sensdata",context,null);
		//Registro l'EventHandler del subscriber, in modo da poter abilitarlo all'ascolto sul topic..
		ServiceRegistration  registration = context.registerService(EventHandler.class.getName(), sbs, dict);
	}
	
/*	@SuppressWarnings({ "unchecked" })
	public void addFilter(String prop,String filter){
		//System.out.println("Adding filter: "+filter);
		dict.put(EventConstants.EVENT_FILTER, "("+prop+"="+filter+")");
	}
	
	public void removeFilter(){
		//System.out.println("Removing filter: "+dict.get(EventConstants.EVENT_FILTER));
		dict.remove(EventConstants.EVENT_FILTER);
	}
	
	public void updateFilter(String prop,String filter){
		removeFilter();
		addFilter(prop,filter);
		System.out.println("[DATA EVENT SUBSCRIBER:] -> Filter proper "+prop+" changed to: "+filter);
	}
*/
}
