package org.osgi.snps.core;

//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import org.osgi.snps.core.cmline.CommandLine;
//
//import java.util.Dictionary;
//import java.util.Properties;
//import org.osgi.service.event.EventConstants;
//import org.osgi.service.event.EventHandler;
//import org.osgi.snps.core.dataservice.Subscriber;

import java.util.Hashtable;
import org.osgi.framework.*;
import org.osgi.snps.base.interfaces.iCoreInterface;



public class Activator implements BundleActivator{

	private static BundleContext context;
	
	//Topic per ricevere dataEvent ai fini dei test..
	//final static String[] topics = { "osgi/testEvent" }; 
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		ServiceRegistration registration;		

		iCoreInterface service = new CoreServices(context);
		registration = bundleContext.registerService(
				iCoreInterface.class.getName(),service, new Hashtable());
		
		
				
		/*DEBUG MODE*/
//		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//        String userInput="N";
//        System.out.println("Enable SNPS Debug Mode?[Y/N] [Default: N]");
//        userInput = stdIn.readLine();
//        System.out.println("Your choice: "+userInput);
//        if(userInput.equalsIgnoreCase("Y")){
//        		CommandLine c = new CommandLine(context);
//        		c.run();
//        }
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}
