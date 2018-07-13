package org.osgi.snps.core.dataservice;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.ABComponent;
import org.osgi.snps.base.interfaces.iCoreInterface;

import java.util.Timer;

public class ServiceData {

	Thread t;
	BundleContext context;
	// Sensor s;
	ABComponent s;

	public ServiceData(BundleContext context, ABComponent sensor, String mode,
			String[] options, int time, int finish, String action) {
		this.context = context;
		s = sensor;
		System.out.println("Starting Service Data Thread..");
		Timer timer = new Timer();
		timer.schedule(new Task(sensor, context, mode, options, action), 0,
				time);
		try {
			Thread.sleep(finish);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timer.cancel();
		timer.purge();
	}
}
