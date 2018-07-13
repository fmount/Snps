package snps.interpreter;

import java.io.IOException;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.w3c.dom.Document;
import org.osgi.snps.base.common.SamplingPlan;
import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.interfaces.*;
import org.osgi.snps.base.util.JSonUtil;
import org.osgi.snps.base.util.Util;

import ch.ethz.iks.r_osgi.RemoteOSGiException;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;

public class MDWExportService implements iGWInterface {

	BundleContext context;
	iWsnInterface wsnService;
	iEventPublisherInterface ipubservice;
	iCoreInterface icore;

	public MDWExportService(BundleContext context) {
		this.context = context;
	}

	@Override
	public String sayhello() {
		return "[INTERPRETER: Info] -> Hello, I'm SNPS Interpreter";
	}

	/* SERVIZI VERSO IL MIDDLEWARE */

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String registerSensor(String Sensorid, Document description) {
		try {
			System.out.println("Registering: " + Sensorid);
			System.out
					.println("[INTERPRETER: Info] -> Persist Request sent to core");
			ServiceReference reference = context
					.getServiceReference(iCoreInterface.class.getName());
			icore = (iCoreInterface) context.getService(reference);
			return icore.regCall("image", 3, Sensorid, description, null, "",
					null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("[INTERPRETER: Alert] -> Error retrieving Core Services");
			System.out.println("[INTERPRETER: Alert] -> Registration Abort");
			return "";
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean push(String data, String[] options) {
		try {
			System.out.println(data);
			SimpleData sd = JSonUtil.jsonToSimpleData(data);
			// System.out.println("[PUSH]-> "+sd.toString());
			ServiceReference reference = context
					.getServiceReference(iCoreInterface.class.getName());
			icore = (iCoreInterface) context.getService(reference);
			
			// TEST MODE #################################################
//			if (sd.get_id_meas().equals("") || sd.get_id_meas() == null) {
//				String sdata = icore.getData(sd.getSid(), "sync", "");
//				sd = JSonUtil.jsonToSimpleData(sdata);
//				if (sd == null)
//					return false;
//			}
			// ############################################################
			
			// mode=test: write file - mode=persist: write into db
			icore.processorCall("push", sd, "persist", options);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

	}

	/* SERVIZI VERSO LA WSN */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean setSPlan(SamplingPlan sPlan) {
		ServiceReference reference = context
				.getServiceReference(iEventPublisherInterface.class.getName());
		ipubservice = (iEventPublisherInterface) context.getService(reference);
		ipubservice.sendEvent(JSonUtil.SamplingPlanToJSON(sPlan), "splan");

		wsnService = setRemoteConnection();
		if (wsnService != null)
			return wsnService.setSPlan(JSonUtil.SamplingPlanToJSON(sPlan));
		else {
			System.out
					.println("\n [Connection Problem] -> Impossible to communicate with WSN bundle ");
			return false;
		}
	}

	@Override
	public boolean stopSPlan(String sPlanId) {
		ServiceReference reference = context
				.getServiceReference(iEventPublisherInterface.class.getName());
		ipubservice = (iEventPublisherInterface) context.getService(reference);
		ipubservice.sendEvent(sPlanId, "splanStop");

		wsnService = setRemoteConnection();
		if (wsnService != null)
			return wsnService.stopSPlan(sPlanId);
		else {
			System.out
					.println("\n [Connection Problem] -> Impossible to communicate with WSN bundle ");
			return false;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getData(String id_meas_to_set, String sid, String mode,
			String[] options, String action) {
		// wsnService = setRemoteConnection();
		ServiceReference reference = context
				.getServiceReference(iEventPublisherInterface.class.getName());
		if (mode.equals("sync")) {

			/*
			 * DISABLING WSN INTERACTION.. return
			 * wsnService.getData(sid,options);
			 */

			/**************** CLOSING INTERACTION *****************/
			SimpleData sd = new SimpleData(sid, String.valueOf(0 + Math
					.random() * 10), "", Util.whatDayIsToday(),
					Util.whatTimeIsIt());
			// sd.set_id_meas(Util.IdGenerator().replace("-", ""));
			sd.set_id_meas(id_meas_to_set);

			if (options != null) {
				sd.setRef(options[0]);
			}

			return JSonUtil.SimpleDataToJSON(sd);
			/*******************************/

		} else if (mode.equals("async")) {
			/*
			 * DISABLING WSN INTERACTION.. String data =
			 * wsnService.getData(sid,options);
			 */

			/**************** CLOSING INTERACTION *****************/
			SimpleData sd = new SimpleData(sid, String.valueOf(0 + Math
					.random() * 10), "", Util.whatDayIsToday(),
					Util.whatTimeIsIt());
			// sd.set_id_meas(Util.IdGenerator().replace("-", ""));
			sd.set_id_meas(id_meas_to_set);

			if (options != null) {
				sd.setRef(options[0]);
			}

			String data = JSonUtil.SimpleDataToJSON(sd);
			System.out.println(data);
			/*******************************/

			ipubservice = (iEventPublisherInterface) context
					.getService(reference);
			ipubservice.sendDataEventWithAction(sid, data, action);

			return "[PUSH] -> Event";

		}

		return "ERROR!!!";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getData(String id_meas_to_set, String sid, String Nature,
			String mode, String[] options, String action) {
		wsnService = setRemoteConnection();
		ServiceReference reference = context
				.getServiceReference(iEventPublisherInterface.class.getName());
		if (mode.equals("sync")) {

			
			// DISABLING WSN INTERACTION.. 
			return wsnService.getData(sid,options);
			 

			/**************** CLOSING INTERACTION *****************/
//			SimpleData sd = new SimpleData(sid, Util.getSampleValue(Nature),
//					"", Util.whatDayIsToday(), Util.whatTimeIsIt());
//			// sd.set_id_meas(Util.IdGenerator().replace("-", ""));
//			sd.set_id_meas(id_meas_to_set);

//			if (options != null) {
//				sd.setRef(options[0]);
//			}

//			return JSonUtil.SimpleDataToJSON(sd);
			/*******************************/

		} else if (mode.equals("async")) {
			
			// DISABLING WSN INTERACTION.. 
			String data = wsnService.getData(sid,options);
			 

			/**************** CLOSING INTERACTION *****************/
//			SimpleData sd = new SimpleData(sid, Util.getSampleValue(Nature),
//					"", Util.whatDayIsToday(), Util.whatTimeIsIt());
//			// sd.set_id_meas(Util.IdGenerator().replace("-", ""));
//			sd.set_id_meas(id_meas_to_set);
//
//			if (options != null) {
//				sd.setRef(options[0]);
//			}
//
//			String data = JSonUtil.SimpleDataToJSON(sd);
//			System.out.println(data);
			/*******************************/

			ipubservice = (iEventPublisherInterface) context
					.getService(reference);
			ipubservice.sendDataEventWithAction(sid, data, action);

			return "[PUSH] -> Event";

		}

		return "ERROR!!!";
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean sendCommand(String command, List<String> sids, String mode) {
		System.out.println("[INTERPRETER: SEND_COMMAND]-> [" + command + ","
				+ sids + "," + mode + "]");
		ServiceReference reference = context
				.getServiceReference(iEventPublisherInterface.class.getName());
		ipubservice = (iEventPublisherInterface) context.getService(reference);
		ipubservice.sendEvent("INTERPRETER: SEND_COMMAND]-> [" + command + ","
				+ sids + "," + mode + "]", "command");

		wsnService = setRemoteConnection();
		if (wsnService == null) {
			System.out
					.println("\n [Connection Problem] -> Impossible to communicate with WSN bundle ");
			return false;
		}

		return wsnService.sendCommand(sids, command);

		/**************** CLOSING INTERACTION *****************/
		//return true;
		/*******************************/
	}

	/**
	 * SET R-OSGI REMOTE CONNECTION..
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public iWsnInterface setRemoteConnection() {
		// Get WSN SERVICES..
		boolean connected = false;
		ServiceReference serviceRef = context
				.getServiceReference(RemoteOSGiService.class.getName());
		if (serviceRef == null) {
			System.out.println("R-OSGi service not found!");
			return null;
		} else {
			final RemoteOSGiService remote = (RemoteOSGiService) context
					.getService(serviceRef);
			String url = Util.loadRosgiConfiguration("rosgiconfig.xml");
			if(url.equalsIgnoreCase("")){
	        	url = "127.0.0.1:9278";
	        	System.out.println("default configuration");
	        }else{
	        	System.out.println("Configuration loaded from: " + url);
	        }
			URI uri = new URI("r-osgi://"+url);
			int attempt = 1;
			// URI uri = new URI("r-osgi://127.0.0.1:9279");
			do {
				wsnService = remoteConnection(remote, uri);
				if (wsnService != null) {
					connected = true;
				} else {
					System.out.println("Attempt " + attempt
							+ " failed, trying to reconnect \n");
					attempt++;
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				System.out.println("Connection status: " + connected
						+ " Attempt: " + attempt);
			} while (!connected && attempt < 3);

			// try {
			// remote.connect(uri);
			// final RemoteServiceReference[] references = remote
			// .getRemoteServiceReferences(uri,
			// iWsnInterface.class.getName(), null);
			// if (references == null) {
			// System.out.println("[MDW] -> Service not found!");
			// return null;
			// } else {
			// wsnService = (iWsnInterface) remote
			// .getRemoteService(references[0]);
			// return wsnService;
			// }
			// } catch (RemoteOSGiException e) {
			// e.printStackTrace();
			// System.out.println("No NetworkChannelFactory for r-osgi found");
			// } catch (IOException e) {
			// e.printStackTrace();
			// System.out.println("No NetworkChannelFactory for r-osgi found");
			// } finally {
			// // bundleContext.ungetService(serviceRef);
			// }
		}
		return wsnService;

	}

	public iWsnInterface remoteConnection(RemoteOSGiService remote, URI uri) {

		try {
			remote.connect(uri);
			final RemoteServiceReference[] references = remote
					.getRemoteServiceReferences(uri,
							iWsnInterface.class.getName(), null);
			if (references == null) {
				System.out.println("[MDW] -> Service not found!");
				return null;
			} else {
				wsnService = (iWsnInterface) remote
						.getRemoteService(references[0]);
				return wsnService;
			}
		} catch (RemoteOSGiException e) {
			e.printStackTrace();
			System.out.println("No NetworkChannelFactory for r-osgi found");
			wsnService = null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No NetworkChannelFactory for r-osgi found");
			wsnService = null;
		} finally {
			// bundleContext.ungetService(serviceRef);
		}
		return wsnService;
	}

	@Override
	public String isAlive(String sid) {
		// wsnService = setRemoteConnection();
		// return wsnService.isAlive(sid);

		return "IS_ALIVE";
	}
}
