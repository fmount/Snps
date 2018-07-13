package org.osgi.snps.base.dataflow;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.Accumulator;
import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.interfaces.*;
import org.osgi.snps.base.json.JSONObject;
import org.osgi.snps.base.util.JSonUtil;
import org.osgi.snps.base.util.Util;





public class DataFlowService implements iDataFlow {

	iDaoInterface daoservice;
	iEventPublisherInterface pservice;
	
	Accumulator acc = new Accumulator();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean pushData(SimpleData data, String mode,BundleContext context) {
				
		try {
			if (mode.equalsIgnoreCase("test")) {
				System.out.println("[PROCESSOR:Info] -> PUSHING DATA");
				System.out.println("-------------------------------");
				System.out.println("Sensor ID: " + data.getSid());
				System.out.println("ID_MEAS: " + data.get_id_meas());
				System.out.println("Data received: " + data.getData());
				System.out.println("Data Ref: " + data.getRef());
				System.out.println("Date: " + data.getDate());
				System.out.println("Time: " + data.getTime());
				System.out.println("-------------------------------");
				if(!data.getRef().equals("")){
					//System.out.println("Data for the Hybrid: "+data.getRef());
					HashMap<String, String> hm = new HashMap<String,String>();
					hm.put(data.getSid(), data.getData());
					//acc.addElementToBQ(data.getRef(), data.getData());
				}
				//System.out.println("[ACCUMULATOR]: "+acc.getBQ(data.getRef()).toString());
				return Util.writeTmpData(JSonUtil.SimpleDataToJSON(data));
				
			} else {
				// DAOSERVICE.PUSHDATA..
				System.out.println("[PROCESSOR:Info] -> PUSHING DATA");
				ServiceReference reference = context
						.getServiceReference(iDaoInterface.class.getName());
				daoservice = (iDaoInterface) context.getService(reference);
				
				System.out.println("[PROCESSOR:Info] -> STORE");
				System.out.println("-------------------------------");
				System.out.println("Sensor ID: " + data.getSid());
				System.out.println("ID_MEAS: " + data.get_id_meas());
				System.out.println("Data received: " + data.getData());
				System.out.println("Data Ref: " + data.getRef());
				System.out.println("Date: " + data.getDate());
				System.out.println("Time: " + data.getTime());
				System.out.println("-------------------------------");
				if(!data.getRef().equals("")){
					//System.out.println("Data for the Hybrid: "+data.getRef());

					JSONObject json = new JSONObject();
					json.put("id", data.getSid());
					json.put("value", data.getData());
					//System.out.println(json.toString());

					acc.addElementToBQ(data.getRef(), json.toString());
				}
				
				//System.out.println("[ACCUMULATOR]: " + acc.getBQ(data.getRef()), json.toString());
				System.out.println(JSonUtil.SimpleDataToJSON(data));
				Util.writeTmpData(JSonUtil.SimpleDataToJSON(data));
				return daoservice.recDetect(3, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	@Override
	public String sayHello() {
		Random random = new Random();
		// Create a number between 0 and 5
		int nextInt = random.nextInt(6);
		switch (nextInt) {
		case 0:
			return "[DataFlow]: Hello, I am the manager of the "
					+ "data streams of SNPS! [English Style]\n";
		case 1:
			return "[DataFlow]: Hola, soy el administrador de "
					+ "los flujos de datos de SNPS![Spanish Style]\n";
		case 2:
			return "[DataFlow]: Hello, saya pengurus aliran data "
					+ "SNPS! [Malaysian Style]\n";
		case 3:
			return "[DataFlow]: Bonjour, je suis le gestionnaire"
					+ " des flux de données de SNPS![French Style]\n";
		case 4:
			return "[DataFlow]: Hallo, ich bin der Manager der "
					+ "Datenströme von SNPS![German Style]\n";
		default:
			return "[DataFlow]: Ciao, sono il gestore dei flussi di "
					+ "dati di SNPS! [Italian Style]\n";
		}
	}
	
	@Override
	public Accumulator getAccumulator(){
		return this.acc;
	}
	
	@Override
	public void Accumulate(String hid, BlockingQueue<String> bq,
			BundleContext context,String action){
		this.acc.Accumulate(hid, bq);
		System.out.println("[ACCUMULATOR] -> Add entry for: "+hid + " size: " + bq.remainingCapacity());
		new ExecDispatcher(acc, hid, bq.remainingCapacity(),context,action);
	}

}
