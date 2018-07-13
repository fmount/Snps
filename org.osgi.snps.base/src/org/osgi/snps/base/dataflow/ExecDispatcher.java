package org.osgi.snps.base.dataflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.ABComponent;
import org.osgi.snps.base.common.Accumulator;
import org.osgi.snps.base.common.SensHybrid;
import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.common.Template;
import org.osgi.snps.base.interfaces.iCoreInterface;
import org.osgi.snps.base.interfaces.iEventPublisherInterface;
import org.osgi.snps.base.util.JSonUtil;
import org.osgi.snps.base.util.Util;


public class ExecDispatcher implements Runnable {

	Accumulator acc;
	int thr;
	String hid;
	String action;
	
	BundleContext context;
	iEventPublisherInterface pubservice;
	iCoreInterface coreservice;
	
	public ExecDispatcher(Accumulator acc,String hid,
			int thr,BundleContext context,String action){
		this.acc = acc;
		this.thr = thr;
		this.hid = hid;
		this.context = context;
		this.action = action;
		Thread t = new Thread(this);
        t.start();
	}
	
	@Override
	public void run() {
		System.out.println("Waiting for dim: "+thr);
		while(acc.getBQ(hid).size()<thr){}
		process(new Template("avg"), acc.getBQ(hid),action);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(Template t, BlockingQueue<String> bq,String action){
		System.out.println("I CAN PROCESS! :D");
		String id;
		List<String> inputs = new ArrayList<String>();
		Iterator<String> it = bq.iterator();
		
		while(it.hasNext()){
			inputs.add(it.next());
		}
		
//		List<Map<String,String>> inputs = new ArrayList<Map<String,String>>();
//		Iterator it = bq.iterator();
//		while(it.hasNext()){
//			inputs.add((Map<String, String>) it.next());
//		}
		
		ServiceReference servRef = context
				.getServiceReference(iCoreInterface.class
						.getName());
		coreservice = (iCoreInterface) context.getService(servRef);
		
		String tmp [] = hid.split("#");
		id = tmp[0];
		SensHybrid component = (SensHybrid) coreservice.getSensList().get(id);
		//TODO ADD CUSTOM TEMPLATE
		String dataprocessed = t.processExpression(inputs, component.getExpression());
		//String finalData = t.process(inputs);
		//Build new Data..
//		SimpleData sd = new SimpleData(getHid(hid), finalData, "", 
//				Util.whatDayIsToday(),Util.whatTimeIsIt());
//		sd.set_id_meas(getId_Meas(hid));
		SimpleData sd = new SimpleData(getHid(hid), dataprocessed, "", 
				Util.whatDayIsToday(),Util.whatTimeIsIt());
		sd.set_id_meas(getId_Meas(hid));
		
		
		ServiceReference serviceRef = context
				.getServiceReference(iEventPublisherInterface.class
						.getName());
		pubservice = (iEventPublisherInterface) context.getService(serviceRef);
		//System.out.println("HYBRID ACTION: "+action);
		pubservice.sendDataEventWithAction(getHid(hid), JSonUtil.SimpleDataToJSON(sd),action);
	}
	
	public String getHid(String hd){
		StringTokenizer st = new StringTokenizer(hd,"#");
		if(st.hasMoreTokens()){
			return st.nextToken();
		}
		return "";
	}
	
	public String getId_Meas(String hd){
		StringTokenizer st = new StringTokenizer(hd,"#");
		if(st.hasMoreTokens()){
			st.nextToken();
			return st.nextToken();
		}
		return "";
	}
	
}
