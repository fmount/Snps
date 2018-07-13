package snps.gui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

public class Activator implements BundleActivator {
	private ServiceTracker tracker;
	
	public void start (BundleContext context) throws Exception {
		System.out.println("Avvio delle Servlet componenti la GUI!");
		tracker = new WebTracker(context);
	    tracker.open();
	}
	
	public void stop (BundleContext context) throws Exception {
		tracker.close();
		tracker = null;
	}

}