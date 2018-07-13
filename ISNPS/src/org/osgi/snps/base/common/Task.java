package org.osgi.snps.base.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.interfaces.iWsnInterface;
import org.osgi.snps.base.util.JSonUtil;
import org.osgi.snps.base.util.Util;

class Task extends TimerTask {

	// Sensor s;
	List<String> sids;
	BundleContext context;
	String mode;
	String action;
	iWsnInterface wsnservice;
	// iCoreInterface coreservice;
	Date end;
	Timer timer;
	String splanId;
	ConcurrentHashMap<String, Timer> splans;

	public Task(List<String> sids, BundleContext context, Timer timer, Date end) {
		this.sids = sids;
		this.context = context;
		this.timer = timer;
		this.end = end;
	}

	public Task(List<String> sids, BundleContext context, Timer timer,
			Date end, String splanId, ConcurrentHashMap<String, Timer> splans) {
		this.sids = sids;
		this.context = context;
		this.timer = timer;
		this.end = end;
		this.splans = splans;
		this.splanId = splanId;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void run() {
		ServiceReference reference;
		reference = context.getServiceReference(iWsnInterface.class.getName());
		wsnservice = (iWsnInterface) context.getService(reference);
		SimpleData data = new SimpleData();

		String today = Util.whatDayIsToday() + " " + Util.whatTimeIsIt();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			Date datetoday = formatter.parse(today);
			System.out.println(end.getTime() - datetoday.getTime());
			if ((end.getTime() - datetoday.getTime()) >= 0) {
				for (String id : sids) {
					System.out.println("START GET DATA REQUEST");
					data.setSid(id);
					wsnservice.sendDataToMiddleware(JSonUtil
							.SimpleDataToJSON(data));
					System.out.println("DATA TO MW: " + data);
				}
			} else {
				splans.remove(splanId);
				if (splans.containsKey(splanId)) {
					for (String s : splans.keySet()) {
						System.out.println("PLAN In Map: " + s);
					}
				}else{
					System.out.println("Plan: " + splanId + " ended ");
				}
				timer.cancel();
				timer.purge();
				// System.out.println("ID SAMPLING PLAN " + splanId);
			}
			// System.out.println("MAP SAMPLING PLANS TASK: " + splans.size() +
			// " " + splans.get(splanId));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}
