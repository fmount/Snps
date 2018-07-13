package org.osgi.remote.wsn;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
//import java.util.Random;

import org.eclipse.core.runtime.dynamichelpers.IFilter;
import org.eclipse.osgi.service.resolver.extras.DescriptionReference;
import org.osgi.demo.actuator.HttpExecutor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
//import org.osgi.rxtx.arduino.SerialTest;
import org.osgi.snps.base.common.SamplingPlan;
import org.osgi.snps.base.common.ServiceData;
import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.interfaces.*;
import org.osgi.snps.base.util.JSonUtil;
import org.osgi.snps.base.util.Util;
import org.w3c.dom.Document;

import smlparser.Parser;
import ch.ethz.iks.r_osgi.RemoteOSGiException;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;

public class WSNService implements iWsnInterface {

	BundleContext context;
	Parser pservice;
	iGWInterface igwservice;
	ConcurrentHashMap<String, Timer> splans;

	// SerialTest t;
	// //Map<String, Timer> splans;
	//
	// public WSNService(BundleContext context, SerialTest s) {
	// this.context = context;
	// this.t = s;
	// //splans = new HashMap<String, Timer>();
	// }
	public WSNService(BundleContext context) {
		this.context = context;
		splans = new ConcurrentHashMap<String, Timer>();
		// this.t = s;
	}

	/**
	 * Messaggio di benvenuto per ogni bundle, al fine di testare la
	 * comunicazione (sia remota che locale)
	 */
	@Override
	public String helloMsg() {
		String str = "[WSN GATEWAY:Info] -> I'm the WSN Gateway!!";
		return str;
	}

	@Override
	public boolean setSPlan(String plan) {
		/*
		 * Setto il piano di campionamento per il sensore dialogando con il
		 * sensore fisico..una volta che il comando è andato a buon fine,
		 * invocare il metodo push(String data) per inviare dati al middleware..
		 * N.B. Eseguire controlli di priorità rstopSPlanispetto ai parametri
		 * settati nel piano di campionamento (range ed intervalli)..
		 */
		// TEST MODE ###########################################################
		System.out.println("AUTOMAGICAMENTE: " + plan);
		SamplingPlan splan = JSonUtil.JsonToSamplingPlan(plan);

		if (splan.getStartDate().equals("") || splan.getEndDate().equals("")
				|| splans.containsKey(splan.getSplan_identifier())) {
			return false;
		} else {
			new ServiceData(context, splan.getNodesId(), "async",
					splan.getStartDate(), splan.getInterval(),
					splan.getEndDate(), splans, splan.getSplan_identifier());
			// new ServiceData(context, splan.getNodesId(), "async",
			// splan.getStartDate(), splan.getInterval(),
			// splan.getEndDate());
			for (String s : splans.keySet()) {
				System.out.println("CONCURRENT SPLAN " + s);
			}
			return true;
		}
		// #######################################################################

		// return true;
	}

	@Override
	public boolean stopSPlan(String splanId) {
		for (String s : splans.keySet()) {
			System.out.println("stop plan in map" + s);
		}
		if (splans.containsKey(splanId)) {

			System.out.println("Stop PLAN " + splanId);
			Timer timerToStop = splans.remove(splanId);
			timerToStop.cancel();
			timerToStop.purge();
			return true;
		} else {
			System.out.println("PLAN " + splanId + " not in map ");
			return false;
		}
	}

	@Override
	public String getData(String sensorId, String[] options) {
		/**
		 * XXX: Qui sta il codice per recuperare il dato dal sensore fisico, per
		 * poi wrapparlo nella classe SimpleData (convertita in JSON attraverso
		 * le opportune librerie) e ritornare il tutto.
		 * **/

		/*
		 * SerialTest main = new SerialTest(); main.initialize();
		 * 
		 * Thread t=new Thread() { public void run() { //the following line will
		 * keep this app alive for 1000 seconds, //waiting for events to occur
		 * and responding to them (printing incoming messages to console). try
		 * {Thread.sleep(1000000);} catch (InterruptedException ie) {} } };
		 * 
		 * t.start(); System.out.println("Started");
		 */

		// Creo un dato fittizio..

		SimpleData sd = new SimpleData(sensorId, String.valueOf(0 + Math
				.random() * 10), "", Util.whatDayIsToday(), Util.whatTimeIsIt());

		/* RETRIEVE DATA FROM ARDUINO... */
		// SimpleData sd = new SimpleData(sensorId, t.getPhysicData(sensorId),
		// "",
		// Util.whatDayIsToday(), Util.whatTimeIsIt());
		//
		// sd.set_id_meas(Util.IdGenerator().replace("-", ""));
		if (options != null) {
			sd.setRef(options[0]);
		}
		// System.out.println(JSonUtil.SimpleDataToJSON(sd));

		// Lo converto in JSon..
		// return null;
		return JSonUtil.SimpleDataToJSON(sd);
	}

	@Override
	public boolean sendCommand(List<String> sids, String command) {

		/**
		 * XXX: Qui sta il codice per propagare il comando verso la wsn e
		 * ritornare true or false al chiamante in funzione dello stato
		 * dell'operazione (success / failure)
		 * **/

		// Selezionare una porta random [0-8]
		// Random random = new Random();
		// Create a number between 0 and 8
		// int nextInt = random.nextInt(8);

		// STUPIDAMENTE...
		// HttpExecutor.exec(command + ":" + 0);
		System.out.println("Sended command " + command + ":" + 1 + " to: "
				+ sids.toString());
		return true;
	}

	/**
	 * Metodo utilizzato ogni qual volta c'è necessità di inviare un dato al
	 * middleware: un piano di campionamento, ad esempio, deve far uso di questo
	 * metodo perché periodicamente deve inviare (prendendo l'iniziativa) un
	 * dato al middleware, richiamando l'apposito servizio messo a disposizione
	 * dal gateway.
	 * -----------------------------------------------------------------------
	 * -----------------------------------------------------------------------
	 * N.B. Si suppone che il parametro "data" sia già in formato JSON,
	 * utilizzando l'apposito metodo di conversione che si trova all'interno
	 * della JSONUtil.
	 * -----------------------------------------------------------------------
	 * -----------------------------------------------------------------------
	 * 
	 * **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean sendDataToMiddleware(String data) {
		// ServiceReference serviceRef = context
		// .getServiceReference(RemoteOSGiService.class.getName());
		// if (serviceRef == null) {
		// System.out.println("R-OSGi service not found!");
		// } else {
		// final RemoteOSGiService remote = (RemoteOSGiService) context
		// .getService(serviceRef);
		// // URI uri = new URI("r-osgi://127.0.0.1:9279");
		// URI uri = new URI("r-osgi://127.0.0.1:9278");
		// try {
		// remote.connect(uri);
		//
		// final RemoteServiceReference[] references = remote
		// .getRemoteServiceReferences(uri,
		// iGWInterface.class.getName(), null);
		// if (references == null) {
		// System.out.println("[WSN to MDW] -> Service not found!");
		// } else {
		// final iGWInterface igw = (iGWInterface) remote
		// .getRemoteService(references[0]);
		// return igw.push(data, null);
		// }
		// } catch (RemoteOSGiException e) {
		// // e.printStackTrace();
		// System.out.println("No NetworkChannelFactory for r-osgi found");
		// } catch (IOException e) {
		// // e.printStackTrace();
		// System.out.println("No NetworkChannelFactory for r-osgi found");
		// } finally {
		// // bundleContext.ungetService(serviceRef);
		// }
		// }
		igwservice = setRemoteConnection();

		if (igwservice != null)
			return igwservice.push(data, null);
		else {
			System.out
					.println("\n [Connection Problem] -> Impossible to communicate with MDW ");
			return false;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Document getDocument(String SMLPath) {
		ServiceReference serviceRef = context.getServiceReference(Parser.class
				.getName());
		pservice = (Parser) context.getService(serviceRef);
		// return pservice.getDOMDocument(SMLPath);
		return pservice.getDocument(SMLPath);
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public String getSId(Document doc) {
		ServiceReference serviceRef = context.getServiceReference(Parser.class
				.getName());
		pservice = (Parser) context.getService(serviceRef);
		// return pservice.DOMSID(doc);
		try {

			XPath xpath = XPathFactory.newInstance().newXPath();
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return pservice.getId(xpath, doc);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String registerSensor() {
		Document doc = getDocument("/home/marco/SensorML.xml");
		if (doc == null) {
			return "Malformed XML";
		}
		String ns = getSId(doc);
		igwservice = setRemoteConnection();
		if (igwservice != null)
			return igwservice.registerSensor(ns, doc);
		else
			return "\n [Connection Problem] -> Impossible to communicate with MDW ";

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public iGWInterface setRemoteConnection() {
		ServiceReference serviceRef = context
				.getServiceReference(RemoteOSGiService.class.getName());
		boolean connected = false;
		if (serviceRef == null) {
			System.out.println("R-OSGi service not found!");
			return null;
		} else {
			final RemoteOSGiService remote = (RemoteOSGiService) context
					.getService(serviceRef);
			URI uri = new URI("r-osgi://127.0.0.1:9278");
			int attempt = 1;
			// URI uri = new URI("r-osgi://127.0.0.1:9279");
			do {
				igwservice = remoteConnection(remote, uri);
				if (igwservice != null) {
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
			//
			// final RemoteServiceReference[] references = remote
			// .getRemoteServiceReferences(uri,
			// iGWInterface.class.getName(), null);
			// if (references == null) {
			// System.out.println("[WSN to MDW] -> Service not found!");
			// } else {
			// igwservice = (iGWInterface) remote
			// .getRemoteService(references[0]);
			// // return igw.registerSensor("t1", doc);
			// return igwservice;
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
		return igwservice;

	}

	public iGWInterface remoteConnection(RemoteOSGiService remote, URI uri) {
		try {
			remote.connect(uri);

			final RemoteServiceReference[] references = remote
					.getRemoteServiceReferences(uri,
							iGWInterface.class.getName(), null);
			if (references == null) {
				System.out.println("[WSN to MDW] -> Service not found!");
				return null;
			} else {
				igwservice = (iGWInterface) remote
						.getRemoteService(references[0]);
				// return igw.registerSensor("t1", doc);
				return igwservice;
			}
		} catch (RemoteOSGiException e) {
			e.printStackTrace();
			System.out.println("No NetworkChannelFactory for r-osgi found");
			igwservice = null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No NetworkChannelFactory for r-osgi found");
			igwservice = null;
		} finally {
			// bundleContext.ungetService(serviceRef);
		}
		return igwservice;
	}

}
