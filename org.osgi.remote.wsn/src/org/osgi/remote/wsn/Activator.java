package org.osgi.remote.wsn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.remote.wsnclient.WSNClient;
//import org.osgi.rxtx.arduino.SerialTest;
import org.osgi.snps.base.interfaces.iWsnInterface;

import ch.ethz.iks.r_osgi.RemoteOSGiService;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(BundleContext context) throws Exception {
		// FASE DI ESPORTAZIONE DEI SERVIZI MESSI A DISPOSIZIONE DALLA WSN
		final Hashtable properties = new Hashtable();
		properties.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);

		// context.registerService(iWsnInterface.class.getName(), new
		// WSNService(context,new SerialTest()), properties);
		context.registerService(iWsnInterface.class.getName(), new WSNService(
				context), properties);
		System.out.println("[WSN GATEWAY:Info] -> State: ACTIVE");

		// Debug Mode
		// -------------------------------------------------------------------
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String testwsnmode = "n";
		System.out
				.println("Do you want to start the Test wsn mode? [y/n default n] \n");
		testwsnmode = br.readLine();
		if (testwsnmode != null) {
			System.out.println("Your choice: " + testwsnmode);
		}else{
			System.out.println("Null read");
		}
		if (!testwsnmode.equals("n")) {
			WSNClient wsnclient = new WSNClient(context);
			wsnclient.run();
		}
		// -------------------------------------------------------------------------------
		/*
		 * SerialTest main = new SerialTest(); main.initialize();
		 * main.getPhysicData();
		 */

		/*
		 * Thread t=new Thread() { public void run() { //the following line will
		 * keep this app alive for 1000 seconds, //waiting for events to occur
		 * and responding to them (printing incoming messages to console). try
		 * {Thread.sleep(1000000);} catch (InterruptedException ie) {} } };
		 * 
		 * t.start(); System.out.println("Started");
		 */

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
