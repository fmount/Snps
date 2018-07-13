package org.osgi.snps.core.cmline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.snps.base.util.*;
import org.osgi.snps.base.common.Sensor;
import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.interfaces.*;
import org.osgi.snps.base.common.*;
import org.osgi.snps.core.dataservice.ServiceData;
import org.w3c.dom.Document;

import ch.ethz.iks.r_osgi.RemoteOSGiException;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import smlparser.Parser;

/*import publish.EventPublisher;
 import publish.iEventPublisherInterface;*/

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author francesco
 * 
 */
@SuppressWarnings({ "unused", "deprecation" })
public class CommandLine implements Runnable {

	public static BundleContext context;
	static iCoreInterface service;
	static iDaoInterface daoservice;
	static iRegistryInterface registryservice;
	static iWsnInterface wsnservice;
	static iGWInterface interpreterservice;
	static iDataFlow dataflowservice;
	static iMonitor monitorservice;

	static iWebIntegrationInterface iwebService;
	static iEventPublisherInterface ipubservice;

	static Parser pservice;

	// static iCompose composerService;

	public enum commands {
		sayhello, regSensor, samplPlan, compose, getData, viewSensors, dao, importsensors, sendCommand, dataflow, viewcomponent, help, quit, test_prova, getconfiguration, setconfiguration, sensor_cu_1, sensor_cu_2, sensor_cu_5, sensor_cu_6, sensor_cu_7, hellowsn, sensor_cu_3_4, isAlive
	}

	public enum modes {
		sync, async
	}

	public enum usecases {
		sRegis, sPlan, getSimpleData, enable, disable
	}

	public enum searchCommands {
		history, allhistory, detectbytime, detection, detectbydate, detectbydateandtime, detectbyzone, sensorsbyzone, sensorsbynode
	}

	public CommandLine(BundleContext bundleContext) {
		context = bundleContext;
		// Thread t = new Thread(this);
		// t.start();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void invokeCommand() {
		try {
			System.out.println("[CML] -> Start\n");
			menu();
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			String answer = "";
			ServiceReference reference;
			reference = context.getServiceReference(iCoreInterface.class
					.getName());

			service = (iCoreInterface) context.getService(reference);

			boolean result, jump = false;
			do {
				System.out.println("[CML:Info] Type help for commands list\n");
				System.out.println("[CML:Accepting Command]\n");
				String userInput, command = "";
				List<String> options = new ArrayList<String>();
				answer = "";
				userInput = stdIn.readLine();
				jump = false;
				if (userInput != null) {
					StringTokenizer stnk = new StringTokenizer(userInput, " ");
					command = stnk.nextToken();
					while (stnk.hasMoreTokens()) {

						options.add(stnk.nextToken());
					}
					try {
						switch (commands.valueOf(command)) {

						case sayhello: // OK
							try {
								System.out.println(service.sayHello());
							} catch (Exception e) {
								e.printStackTrace();
							}

							break;

						case isAlive:
							try {
								System.out
										.println("[CML:Info] -> Is the Sensor ALIVE????????");
								if (options.isEmpty())
									System.out
											.println("[CML:Alert] -> Arguments Error!!");
								else if (options.get(0).equals("--sId")) {
									System.out
											.println("Check availability for Sensor: "
													+ options.get(1));
									try {
										ABComponent s = service.getSensList()
												.get(options.get(1));
										// service.regCall("updateComponent", 3,
										// options.get(1), description, s,
										// "",null);
										/** EXAMPLE USING WII BUNDLE **/
										reference = context
												.getServiceReference(iWebIntegrationInterface.class
														.getName());
										iwebService = (iWebIntegrationInterface) context
												.getService(reference);

										// IT SEEMS OK..
										System.out.println(iwebService
												.isAlive(options.get(1)));
										System.out.println("Done");
									} catch (Exception e) {
										System.out.println(e.getMessage());
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case importsensors:
							try {
								Document description;
								reference = context
										.getServiceReference(Parser.class
												.getName());
								ABComponent s;
								if (reference != null) {
									String originPath = Costants.SMLPATH;
									final File folder = new File(originPath);
									List<String> flist = listFilesForFolder(folder);
									System.out.println("Found " + flist.size()
											+ " files");
									pservice = (Parser) context
											.getService(reference);
									for (int i = 0; i < flist.size(); i++) {
										System.out.println("Import file: "
												+ flist.get(i));
										description = pservice
												.getDocument(flist.get(i));
										s = pservice.parse(description);
										if(s instanceof SensHybrid){
											ArrayList<Sensor> sensors = new ArrayList<Sensor>();
											ArrayList<String> sensids = new ArrayList<String>();
											sensids = ((SensHybrid) s).getSensids();
											for(String sid : sensids){
												sensors.add((Sensor) service.getSensList().get(sid));
											}
											((SensHybrid) s).setSensors(sensors);
										}
										String id = service.regCall("persist",
												3, s.getID(), description, s,
												s.getNature(), null);
										System.out
												.println("[CML:Info]-> New Sensor: "
														+ id);
									}
								} else
									System.out.println("Reference error!");
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case regSensor: // OK
							try {
								System.out
										.println("[CML] -> Generating Sensor..");
								Document description;
								reference = context
										.getServiceReference(Parser.class
												.getName());
								ABComponent s;
								if (reference != null) {

									pservice = (Parser) context
											.getService(reference);
									System.out.println(options.toString());
									if (options.isEmpty()) {
										System.out.println("Standard!");
										/*
										 * description = pservice
										 * .getDOMDocument("SensorML.xml");
										 * 
										 * APACHE XERCES
										 */
										// USING XPATH..
										description = pservice
												.getDocument("SensorML.xml");
									} else {
										// description =
										// pservice.getDOMDocument(options.get(0));
										/*
										 * description = pservice
										 * .getDOMDocument(options.get(0));
										 * 
										 * APACHE XERCES
										 */
										// USING XPATH..
										description = pservice
												.getDocument(options.get(0));
										if (description != null) {
											s = pservice.parse(description);
											if (s != null) {
												if(s instanceof SensHybrid){
													ArrayList<Sensor> sensors = new ArrayList<Sensor>();
													ArrayList<String> sensids = new ArrayList<String>();
													sensids = ((SensHybrid) s).getSensids();
													Sensor sensIntoHybrid;
													for(String sid : sensids){
														sensIntoHybrid = (Sensor) service.getSensList().get(sid);
														if(!sensIntoHybrid.getReferToHybrid().contains(s.getID())){
															sensIntoHybrid.getReferToHybrid().add(s.getID());
														}
														sensors.add(sensIntoHybrid);
													}
													((SensHybrid) s).setSensors(sensors);
												}
												String id = service.regCall(
														"persist", 3,
														s.getID(), description,
														s, s.getNature(), null);

												System.out
														.println("[CML:Info]-> New Sensor: "
																+ id);
											} else {
												System.out
														.println("[CML:Alert]-> Missing required fields");
											}

										}
									}
								} else {
									System.err
											.println("Error getting Parser service!");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;

						case samplPlan: // OK
							// EX: samplPlan --sId id1 id2 ..idN -a/-s
							List<String> ids = new ArrayList<String>();
							boolean opres = false;
							if (options.isEmpty()) {

								System.out
										.println("Generate Sampling plan for a test sensor -> "
												+ "Parameters: [sId - threshold_min "
												+ "- threshold_max - interval]");

								Sensor s = (Sensor) service.getSensList().get(
										"test");
								ids.add(s.getID());
								String splanId = org.osgi.snps.base.util.Util
										.IdGenerator().replace("-", "");
								SamplingPlan sPlan = new SamplingPlan(splanId,
										ids, "", "", 200);
								opres = Boolean.parseBoolean(service
										.interprCall("splan", sPlan, null, ""));

							} else {
								try {
									if (options.get(0).equals("--sId")) {
										String respMode = options.get(options
												.size() - 1);
										for (int i = 1; i < options.size() - 1; i++) {
											ids.add(options.get(i));
										}
										String splanId = org.osgi.snps.base.util.Util
												.IdGenerator().replace("-", "");
										SamplingPlan sPlan = new SamplingPlan(
												splanId, ids, "", "", 5000);

										opres = Boolean.parseBoolean(service
												.interprCall("splan", sPlan,
														null, ""));
									} else {
										System.out
												.println("[CML:Alert] -> Malformed Command: Please Retry!");
										System.out
												.println("[CML:Info] -> Usage: samplPlan --sId id1 id2 ..idN ");
									}
								} catch (Exception e) {
									System.out
											.println("[CML:Alert] -> Malformed Command: Please Retry!");
									System.out
											.println("[CML:Info] -> Usage: samplPlan --sId id1 id2 ..idN ");
									opres = false;
								}
							}
							if (opres)
								System.out.println("[CML: Test passed]");
							else
								System.out.println("[CML: Error!]");
							options.clear();
							break;

						case compose: // OKOK
							// Phase 1: Register sensors..
							List<String> tocompose = new ArrayList<String>();
							String expression="none";
							if (options.isEmpty()) {
								System.out
										.println("[CML:Alert] -> Arguments Error!!");
							} else if (options.get(0).equals("--test")) {
								System.out
										.println("---------------------------");
								System.out.println("Composition test");
								System.out
										.println("---------------------------");
								if (!(composition("SensorML.xml",
										"SensorML1.xml").equals("")))
									System.out.println("Test Passed!");
								options.clear();
								break;

							} else if (options.get(0).equals("--sId")) {
								if (options.size() < 3) {
									System.out
											.println("[CML:Alert] -> Arguments Error!!");
									options.clear();
									break;
								}
								options.remove(0);
								if(options.get(options.size()-2).equals("-e") ){
									expression = options.get(options.size()-1);
									options.remove(options.size()-2);
									options.remove(options.size()-1);
								}
								if (service.sensorExist(options)) {
									System.out.println("I CAN COMPOSE");
									System.out.println(service.composerCall(
											"compose", options, expression));
									options.clear();
									break;
								} else {
									System.out
											.println("[CML:Alert] -> Loading sensors error!!!");
									options.clear();
									break;
								}

							} else {
								System.out
										.println("[CML:Alert] -> Arguments Error!!");
								break;
							}
							options.clear();
							break;

						case viewSensors: // OK
							/*
							 * System.out.println("---------------------------");
							 * System
							 * .out.println("List of available sensors: ");
							 * System
							 * .out.println("---------------------------");
							 * Map<String, ABComponent> mList = service
							 * .getSensList(); for (Entry<String, ABComponent>
							 * entry : mList .entrySet()) { /*
							 * System.out.println("Sid:" + entry.getKey() +
							 * " - Value: " + entry.getValue());
							 */
							/*
							 * System.out.println( "Sid:" + entry.getKey()+ " -
							 * "" + entry.getValue());
							 */
							// }
							viewSensors();
							break;
						case getconfiguration:
							System.out
									.println("[CML:Info] -> Get Sensor configuration");
							if (options.isEmpty())
								System.out
										.println("[CML:Alert] -> Arguments Error!!");
							else if (options.get(0).equals("--sId")) {
								System.out
										.println("Get Configuration from Sensor: "
												+ options.get(1));
								System.out.println(service.getSensList()
										.get(options.get(1)).toString());
								System.out
										.println("[CML:Info] -> Getting Sensor XML registered");
								// System.out.println(service.regCall("getSDesc",
								// 3, options.get(1).toString(), null, null,
								// "",null));
								/** EXMAPLE USING WII **/
								reference = context
										.getServiceReference(iWebIntegrationInterface.class
												.getName());
								iwebService = (iWebIntegrationInterface) context
										.getService(reference);
								try {
									// IT SEEMS OK
									System.out.println(iwebService
											.getSensorConfiguration(options
													.get(1)));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							break;

						case setconfiguration:
							System.out
									.println("[CML:Info] -> Set Sensor configuration");
							if (options.isEmpty())
								System.out
										.println("[CML:Alert] -> Arguments Error!!");
							else if (options.get(0).equals("--sId")) {
								System.out
										.println("Set Configuration to Sensor: "
												+ options.get(1));
								if (options.get(2).equals("-xml")) {
									System.out
											.println("Apply Configuration provided by: "
													+ options.get(3).toString());

									try {
										reference = context
												.getServiceReference(Parser.class
														.getName());
										pservice = (Parser) context
												.getService(reference);
										Document description = pservice
												.getDocument(options.get(3)
														.toString());
										ABComponent s = pservice.parse(description);
										s.setID(options.get(1));
										if(s instanceof SensHybrid){
											ArrayList<Sensor> sensors = new ArrayList<Sensor>();
											ArrayList<String> sensids = new ArrayList<String>();
											sensids = ((SensHybrid) s).getSensids();
											for(String sid : sensids){
												sensors.add((Sensor) service.getSensList().get(sid));
											}
											((SensHybrid) s).setSensors(sensors);
										}
										System.out.println("SENSORS: " + ((SensHybrid) s).getSensors().toString());
										// ABComponent s =
										// service.getSensList().get(options.get(1));
										service.regCall("updateComponent", 3,
												options.get(1), description, s,
												"", null);
										/** EXAMPLE USING WII BUNDLE **/
										// reference =
										// context.getServiceReference(iWebIntegrationInterface.class.getName());
										// iwebService =
										// (iWebIntegrationInterface)
										// context.getService(reference);

										// IT SEEMS OK..
										// System.out.println(iwebService.setSensorConfiguration(options.get(1),
										// options.get(3).toString()));
										System.out.println("Done");
										// viewSensors();
									} catch (Exception e) {
										System.out.println(e.getMessage());
									}
								}
							}
							break;
						case getData:
							List<String> testcmds = new ArrayList<String>();
							System.out.println("[CML:Info] -> Get data test");
							if (options.isEmpty()) {
								System.out
										.println("[CML:Alert] -> Arguments Error!!");
							}
							// Chiamo un dato per un certo sensore..
							// getData --sId <ID> -s/-a (-s: sync, -a: async)
							else if (options.get(0).equals("--sId")) {
								System.out
										.println("Options: " + options.get(2));
								// buildSensors(100);

								if (options.get(2).equals("-s")) {
									System.out.println("Select SYNC MODE");

									System.out.println(service.getData(
											options.get(1), "sync", "none"));
								}
								// System.out.println(service.getData(options.get(1)));
								else if (options.get(2).equals("-a")) {
									System.out.println("Select ASYNC MODE");

									String ac = defineAction(options.get(1));
									System.out.println("ACC: " + ac);
									System.out.println(service.getData(
											options.get(1), "async", ac));
									break;
									/*
									 * System.out .println(
									 * "[CML:Info] -> Receiving data: enabling Event Mode.."
									 * ); testcmds.add(options.get(1));
									 */

									/*
									 * reference = context
									 * .getServiceReference(iEventPublisherInterface
									 * .class .getName());
									 * 
									 * ipubservice = (iEventPublisherInterface)
									 * context .getService(reference);
									 * ipubservice.sendEventt("registration",
									 * testcmds);
									 */
								} else {
									System.out
											.println("[CML:Alert] -> Malformed Command: Please Retry!");
									System.out
											.println("[CML:Info] -> Usage: getData --all -l <limit> -t <time>");
									break;
								}
							}
							// getData --all -l 10 -t 100 -s/-a (-s: sync, -a:
							// async)
							else if (options.get(0).equals("--all")) {
								System.out.println("[CML:Info] -> Stress Test");
								try {
									System.out.println("Options: "
											+ options.toString());
									buildSensors(100);
									StressTest(
											Integer.parseInt(options.get(2)),
											Integer.parseInt(options.get(4)),
											options.get(5));
								} catch (Exception e) {
									System.out
											.println("[CML:Alert] -> Malformed Command: Please Retry!");
									System.out
											.println("[CML:Info] -> Usage: getData --all -l <limit> -t <time>");
								}
							} else if (options.get(0).equals("-m")) {
								System.out
										.println("[CML:Info] -> Monitor mode");
								monitor(options.get(1), options.get(2));

							} else
								System.out.println("Error typing command!");
							options.clear();
							break;

						case dao:
							System.out
									.println("[CML:Info] -> Testing Dao Layer Services");
							if (options.isEmpty()) {
								System.out
										.println("[CML:Alert] -> Arguments Error!!");
							}

							if (options.get(0).equals("--greet")) {
								reference = context
										.getServiceReference(iDaoInterface.class
												.getName());
								daoservice = (iDaoInterface) context
										.getService(reference);
								System.out.println(daoservice.sayHello());
							}

							else if (options.get(0).equals("--all")) {

								reference = context
										.getServiceReference(iDaoInterface.class
												.getName());
								daoservice = (iDaoInterface) context
										.getService(reference);
								daoTest();
							}

							else if (options.get(0).equals("--search")) {
								reference = context
										.getServiceReference(iDaoInterface.class
												.getName());
								daoservice = (iDaoInterface) context
										.getService(reference);
								try {
									if (options.get(1).equalsIgnoreCase("-sid")) {
										String s = daoservice
												.getImageComponent(3,
														options.get(2));
										System.out.println(s.toString());
									} else {
										System.out
												.println("[CML:Alert] -> MALFORMED COMMAND");
										System.out
												.println("[CML:Usage] -> ex: dao --greet/--all");
									}
								} catch (Exception e) {
									System.out
											.println("[CML:Alert] -> MALFORMED COMMAND");
									System.out
											.println("[CML:Usage] -> ex: dao --greet/--all");
								}
							} else if (options.get(0).equals("--get")) {
								System.out
										.println("[CML:Info] -> Search Engine");
								try {
									searchEngine(options);

								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								System.out
										.println("[CML:Alert] -> MALFORMED COMMAND");
								System.out
										.println("[CML:Usage] -> ex: dao --greet/--all");
							}
							options.clear();
							break;

						case sendCommand:
							// Ex: sendCommand --sId id1 id2..idN -e / -d
							// (enable/disable)
							System.out
									.println("[CML:Info] -> Send Command to Sensors");
							List<String> Ids = new ArrayList<String>();
							boolean opr = false;
							if (options.isEmpty()) {
								System.out
										.println("[CML:Alert] -> Malformed Command: Please Retry!");
								System.out
										.println("[CML:Info] -> Usage: sendCommand -m <command> --sId "
												+ "id1 id2 ..idN -s/-a (sync/async)");
							} else {
								try {

									// System.out.println(options.toString());

									String mode = options
											.get(options.size() - 1);

									if (options.get(0).equalsIgnoreCase("-m")
											&& (mode.equals("-s") || mode
													.equals("-a"))) {

										if (mode.equals("-a"))
											mode = "async";

										if (mode.equals("-s"))
											mode = "sync";

										String com = options.get(1);
										if (options.get(2).equalsIgnoreCase(
												"--sId")) {
											for (int i = 3; i < options.size() - 1; i++) {
												Ids.add(options.get(i));
											}

											opr = Boolean.parseBoolean(service
													.interprCall(com, null,
															Ids, mode));
										} else {
											System.out
													.println("[CML:Alert] -> Malformed Command: Please Retry!");
											System.out
													.println("[CML:Info] -> Usage: sendCommand -m <command> --sId "
															+ "id1 id2 ..idN -s/-a (sync/async)");
										}
									} else {
										System.out
												.println("[CML:Alert] -> Malformed Command: Please Retry!");
										System.out
												.println("[CML:Info] -> Usage: sendCommand -m <command> --sId "
														+ "id1 id2 ..idN -s/-a (sync/async)");
									}

								} catch (Exception e) {
									System.out
											.println("[CML:Alert] -> Malformed Command: Please Retry!");
									System.out
											.println("[CML:Info] -> Usage: sendCommand -m <command> --sId "
													+ "id1 id2 ..idN -s/-a (sync/async)");
									opres = false;
								}

							}
							if (opr)
								System.out.println("[CML: Test passed]");
							else
								System.out.println("[CML: Error!]");
							options.clear();
							break;

						case viewcomponent:
							// example: viewcomponent -n <number>
							System.out
									.println("[CML:Info] -> Component List\n");

							String[] bdls = { "SNPS Core Bundle",
									"DAO Service Bundle",
									"SNPS Registry Bundle",
									"SNPS Interpreter Bundle",
									"WSN Gateway Bundle" };
							if (options.isEmpty()) { // Li visualizzo tutti..
								for (int i = 0; i < 5; i++)
									System.out.println(bdls[i] + " -> "
											+ viewState(i));
							} else if (options.get(0).equals("-n")
									&& Integer.parseInt(options.get(1)) < 5) {
								System.out.println(bdls[Integer
										.parseInt(options.get(1))]
										+ " -> "
										+ viewState(Integer.parseInt(options
												.get(1))));
							} else {
								System.out
										.println("[CML:Alert] -> MALFORMED COMMAND");
								System.out
										.println("[CML:Usage] -> ex: viewcomponent -n <number>");
							}

							options.clear();
							break;
						case dataflow:
							System.out
									.println("[CML:Info] -> Test Data Flow Services:\n");

							reference = context
									.getServiceReference(iDataFlow.class
											.getName());
							dataflowservice = (iDataFlow) context
									.getService(reference);

							try {

								if (options.get(0).equals("--greet")) {
									System.out.println(dataflowservice
											.sayHello());
								} else if (options.get(0).equals("--send")) {
									try {
										if (options.get(1).equals("-s")) {
											dataflowservice
													.pushData(
															new SimpleData(
																	options.get(2),
																	String.valueOf((Math
																			.random() * 10)),
																	"",
																	org.osgi.snps.base.util.Util
																			.whatDayIsToday(),
																	org.osgi.snps.base.util.Util
																			.whatTimeIsIt()),
															"test", context);
										}
									} catch (Exception e) {
										System.out
												.println("[CML:Alert] -> Wrong"
														+ " Option! Sending random Data");

										dataflowservice
												.pushData(
														new SimpleData(
																"test",
																String.valueOf((Math
																		.random() * 10)),
																"simple",
																org.osgi.snps.base.util.Util
																		.whatDayIsToday(),
																org.osgi.snps.base.util.Util
																		.whatDayIsToday()),
														"test", context);

										System.out
												.println("[CML:Alert] -> MALFORMED COMMAND");
										System.out
												.println("[CML:Usage] -> ex: dataflow --send -s sensor4356");
									}
								}
								options.clear();
							} catch (Exception e) {
								System.out
										.println("[CML:Alert] -> MALFORMED COMMAND");
								System.out
										.println("[CML:Usage] -> ex: dataflow --send -s sensor4356");
							}
							break;
						case help:
							menu();
							jump = true;
							break;
						case quit:
							System.out
									.println("[CML:Info] -> Exit CML Platform.. \n");
							answer = "n";
							jump = true;
							break;
						case test_prova:
							/*
							 * EventPublisher publisher = getPublisher(context);
							 * publisher.sendEventt("anonymous",new
							 * ArrayList<String>());
							 */
							break;
						case sensor_cu_1:
							// SENSOR REGISTRATION
							sensor_cu_1(reference, "scu1.txt");
							break;
						case sensor_cu_2:
							// SETTING SPLAN
							sensor_cu_2(reference, "scu2.txt");
							break;
						case sensor_cu_3_4:
							// SENSOR ACTIVATION
							sensor_cu_3_4(reference, "scu_3_4.txt");
							break;
						case sensor_cu_5:
							// TODO: BENESSERE
							sensor_cu_5("scu5.txt");
							/*
							 * reference =
							 * context.getServiceReference(MathService
							 * .class.getName()); test = (MathService)
							 * context.getService(reference);
							 * System.out.println(test.add(3,4));
							 */
							break;
						case sensor_cu_6:
							// BUILD VIRTUAL SENSOR (COMPOSE)
							sensor_cu_6("scu6.txt");
							reference = context
									.getServiceReference(iWebIntegrationInterface.class
											.getName());
							iwebService = (iWebIntegrationInterface) context
									.getService(reference);
							System.out.println(iwebService.sayhello());

							break;
						case sensor_cu_7:
							// GET DATA FROM VIRTUAL SENSOR
							String mode = "sync";
							BufferedReader stdin = new BufferedReader(
									new InputStreamReader(System.in));
							System.out
									.println("Select mode: sync / async (s: sync - a: async)");
							String a = stdin.readLine();
							if (!(a.equalsIgnoreCase("a"))
									&& !(a.equalsIgnoreCase("s"))) {
								System.out.println("Error selecting mode!");
								return;
							}
							if (a.equalsIgnoreCase("a"))
								mode = "async";
							if (a.equalsIgnoreCase("s"))
								mode = "sync";

							// TODO: DEFINE ACTION

							sensor_cu_7("scu7.txt", mode);
							break;

						case hellowsn:
							reference = context
									.getServiceReference(iWsnInterface.class
											.getName());
							wsnservice = (iWsnInterface) context
									.getService(reference);
							System.out.println(wsnservice.helloMsg());
							break;
						default:
							System.out
									.println("[CML] -> Command not found!!\n");
							break;
						}
					} catch (Exception ex) {
						// ex.printStackTrace();
						System.out
								.println("[CML:Info] -> Command not found!!\n");
					}
				}
				if (!jump) {
					System.out
							.println("Do you want to try another command? [Y/N default=Y]\n");
					answer = stdIn.readLine();
					// System.out.println(answer);
				}
			} while (!answer.equals("n"));
			System.out.println("[CML:Info] -> Stopping Test Services..");

			/*
			 * ServiceReference ref =
			 * context.getServiceReference(PackageAdmin.class.getName());
			 * PackageAdmin pa = (ref == null) ? null : (PackageAdmin)
			 * context.getService(ref);
			 * pa.getBundle(iEventPublisherInterface.class).stop();
			 */

			System.out.println("[CML] -> Stop\n");

		} catch (Exception e) {
			System.out.println("Generic Error: Reload CML " + e.getMessage());
		}
	}

	public static void searchEngine(List<String> options) {
		try {
			String choice = options.get(1);
			switch (searchCommands.valueOf(choice)) {

			case sensorsbyzone:
				if (options.get(2).equalsIgnoreCase("-z")) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(3));
					System.out.println(service.regCall("getSensorbyzone", 3,
							"", null, null, "", params));
				}
				break;
			case sensorsbynode:
				if (options.get(2).equalsIgnoreCase("-b")) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(3));
					System.out.println(service.regCall("getSensorbynode", 3,
							"", null, null, "", params));
				}
				break;
			case history:
				if (options.get(2).equalsIgnoreCase("-sId")) {
					System.out.println(service.regCall("history", 3,
							options.get(3), null, null, null, null));
				} else {
					System.out.println("Malformed command!");
				}
				break;

			case allhistory:
				System.out.println(service.regCall("allhistory", 3,
						options.get(1), null, null, null, null));
				break;

			case detection:
				if (options.get(2) != null) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(2));
					System.out.println(service.regCall("detection", 3, "",
							null, null, null, params));
				}
				break;
			case detectbydate:
				if ((options.get(2).equalsIgnoreCase("-sId"))
						&& ((options.get(4).equalsIgnoreCase("-d")))) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(5));
					params.add(options.get(6));
					System.out.println(service.regCall("detectbydate", 3,
							options.get(3), null, null, "", params));
				} else if (options.get(2).equalsIgnoreCase("-d")) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(3));
					params.add(options.get(4));
					System.out.println(service.regCall("detectbydate", 3, "",
							null, null, "", params));
				}
				break;
			case detectbytime:
				if ((options.get(2).equalsIgnoreCase("-sId"))
						&& ((options.get(4).equalsIgnoreCase("-d")) && (options
								.get(6).equalsIgnoreCase("-t")))) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(5));
					params.add(options.get(7));
					params.add(options.get(8));
					System.out.println(service.regCall("detectbytime", 3,
							options.get(3), null, null, "", params));
				} else if ((options.get(2).equalsIgnoreCase("-d"))
						&& (options.get(4).equalsIgnoreCase("-t"))) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(3));
					params.add(options.get(5));
					params.add(options.get(6));
					System.out.println(service.regCall("detectbytime", 3, "",
							null, null, "", params));
				}
				break;
			case detectbydateandtime:
				if ((options.get(2).equalsIgnoreCase("-sId"))
						&& ((options.get(4).equalsIgnoreCase("-d")) && (options
								.get(7).equalsIgnoreCase("-t")))) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(5));
					params.add(options.get(6));
					params.add(options.get(8));
					params.add(options.get(9));
					System.out.println(service.regCall("detectbydateandtime",
							3, options.get(3), null, null, "", params));
				} else if ((options.get(2).equalsIgnoreCase("-d"))
						&& (options.get(5).equalsIgnoreCase("-t"))) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(3));
					params.add(options.get(4));
					params.add(options.get(6));
					params.add(options.get(7));
					System.out.println(service.regCall("detectbydateandtime",
							3, "", null, null, "", params));
				}
				break;
			case detectbyzone:
				if (options.get(2).equalsIgnoreCase("-z")) {
					List<String> params = new ArrayList<String>();
					params.add(options.get(3));
					System.out.println(service.regCall("detectbyzone", 3, "",
							null, null, "", params));
				}
				break;
			default:
				System.out.println("Command not found!");
				break;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String defineAction(String sid) {
		viewSensors();
		System.out.println("Sensor ID: " + sid);
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println("-----------------\n");
		System.out.println("AVAILABLE ACTIONS\n");
		System.out.println("-----------------\n");
		System.out.println("0. None\n");
		System.out.println("1. Actuator\n");
		System.out.println("What action?\n");

		try {
			int answ = Integer.parseInt(stdIn.readLine());
			switch (answ) {
			case 0:
				return "none";

			case 1:
				List<String> act = service.getActuators();
				String exec = "enable";
				String restriction = "";
				if (!act.isEmpty()) {
					for (int i = 0; i < act.size(); i++) {
						System.out.println(i + 1 + ") " + act.get(i) + "\n");
					}
					System.out.println("Select ACT id: ");
					int actid = Integer.parseInt(stdIn.readLine());
					System.out.println("ACTUATOR SELECTED: "
							+ act.get(actid - 1));
					System.out.println("1. Enable\n");
					System.out.println("2. Disable\n");
					int ex = Integer.parseInt(stdIn.readLine());
					if (ex == 1) {
						exec = "enable";
					} else if (ex == 2) {
						exec = "disable";
					} else {
						System.out
								.println("[CML:Info] -> Error Selecting command");
						System.out.println("[CML:Info] -> SKIP");
						System.out.println("returning: none");
						return "none";
					}
					System.out.println("Enter restriction value: ");
					double rVal = Double.parseDouble(stdIn.readLine());
					Action a = new Action((service.getSensList()).get(
							(act.get(actid - 1))).getID(), exec, rVal);
					ServiceReference reference = context
							.getServiceReference(iMonitor.class.getName());

					// CONTROLLO SE ESISTE UN AZIONE DI QUESTO TIPO SUL
					// SENSORE..
					if (service.getActions().containsKey(
							sid + "_" + a.getComponent() + "_" + exec)) {
						System.out.println("Action already exist..");
						return a.getComponent() + ":" + exec;
					}

					monitorservice = (iMonitor) context.getService(reference);
					monitorservice.monitor(sid, a);
					service.addToActionList(sid + "_" + a.getComponent() + "_"
							+ exec, exec);
					System.out.println("returning: " + a.getComponent() + ":"
							+ exec);
					return a.getComponent() + ":" + exec;
				}
				System.out.println("No actuators: None Action!");
				System.out.println("[CML:Info] -> SKIP");
				return "none";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println("[CML:Alert] -> SKIP");
			return "none";
		}
		return "none";
	}

	public static void viewSensors() {
		System.out.println("---------------------------");
		System.out.println("List of available sensors: ");
		System.out.println("---------------------------");
		Map<String, ABComponent> mList = service.getSensList();
		for (Entry<String, ABComponent> entry : mList.entrySet()) {
			/*
			 * System.out.println("Sid:" + entry.getKey() + " - Value: " +
			 * entry.getValue());
			 */
			System.out.println(/*
								 * "Sid:" + entry.getKey()+ " -
								 */"" + entry.getValue());
		}
	}

	public static void monitor(String sid, String mode) {
		try {
			String com = "";
			if (mode.equals("-a")) {
				com = "async";
			} else if (mode.equals("-s")) {
				com = "sync";
			}
			switch (modes.valueOf(com)) {
			case sync:
				System.out.println("SYNC MODE: " + com);
				for (int i = 0; i < 100; i++) {
					String key = service.getSensList().get(sid).getID();
					System.out.println(service.getData(key, "sync", "none"));
				}
				break;
			case async:
				String ac = "none";
				if (!sid.equals("all")) {
					ac = defineAction(sid);
				}
				System.out.println("ACTION: " + ac);
				System.out.println("ASYNC MODE: " + com);
				System.out.println("Rate for monitoring: (time in ms)");
				BufferedReader stdIn = new BufferedReader(
						new InputStreamReader(System.in));
				String rate = stdIn.readLine();
				System.out.println("How long? (time in ms)");
				String finish = stdIn.readLine();

				if (sid.equals("all")) {
					new ServiceData(context, null, com, null,
							Integer.parseInt(rate), Integer.parseInt(finish),
							ac);
				} else {
					new ServiceData(context, service.getSensList().get(sid),
							com, null, Integer.parseInt(rate),
							Integer.parseInt(finish), ac);
				}

				break;
			default:
				System.out.println("UNAVAILABLE MODE!");
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static iWsnInterface setRemoteConnection() {
		// Get WSN SERVICES..
		ServiceReference serviceRef = context
				.getServiceReference(RemoteOSGiService.class.getName());
		if (serviceRef == null) {
			System.out.println("R-OSGi service not found!");
			return null;
		} else {
			final RemoteOSGiService remote = (RemoteOSGiService) context
					.getService(serviceRef);
			URI uri = new URI("r-osgi://127.0.0.1:9281");
			try {
				remote.connect(uri);
				final RemoteServiceReference[] references = remote
						.getRemoteServiceReferences(uri,
								iWsnInterface.class.getName(), null);
				if (references == null) {
					System.out.println("[MDW] -> Service not found!");
					return null;
				} else {
					wsnservice = (iWsnInterface) remote
							.getRemoteService(references[0]);
					return wsnservice;
				}
			} catch (RemoteOSGiException e) {
				e.printStackTrace();
				System.out.println("No NetworkChannelFactory for r-osgi found");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("No NetworkChannelFactory for r-osgi found");
			} finally {
				// bundleContext.ungetService(serviceRef);
			}
		}
		return wsnservice;
	}

	@SuppressWarnings({ "rawtypes" })
	public static void sensor_cu_1(ServiceReference reference, String fname) {
		try {

			wsnservice = setRemoteConnection();
			org.osgi.snps.base.util.Util.describe(fname);
			// System.out.println("[CML] -> Generating Sensor..");
			/*
			 * DocumentBuilderFactory factory = DocumentBuilderFactory
			 * .newInstance(); DocumentBuilder builder =
			 * factory.newDocumentBuilder(); Document description =
			 * builder.parse(new File("SensorML.xml"));
			 */
			// Document description = wsnservice.getDocument("SensorML.xml");
			String id = wsnservice.registerSensor();
			System.out.println("[CML:Info]-> New Sensor: " + id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sensor_cu_2(ServiceReference reference, String fname) {
		try {
			reference = context
					.getServiceReference(iWebIntegrationInterface.class
							.getName());
			if (reference != null) {
				iwebService = (iWebIntegrationInterface) context
						.getService(reference);
				org.osgi.snps.base.util.Util.describe(fname);
				List<String> ids = new ArrayList<String>();
				ids.add("localhostSensorA");
				ids.add("localhostSensorB");
				SamplingPlan plan = new SamplingPlan("", ids, "", "", 200);
				plan.setSplan_identifier(org.osgi.snps.base.util.Util
						.IdGenerator().replace("-", ""));

				System.out.println("[CML:Info] -> "
						+ "New SPlan identifier: "
						+ iwebService.setSPlan(JSonUtil
								.SamplingPlanToJSON(plan)));
			} else {
				System.out.println("Error getting reference..");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void sensor_cu_3_4(ServiceReference reference, String fname) {
		try {
			reference = context
					.getServiceReference(iWebIntegrationInterface.class
							.getName());
			if (reference != null) {
				org.osgi.snps.base.util.Util.describe(fname);
				List<String> ids = new ArrayList<String>();
				ids.add("test");
				iwebService = (iWebIntegrationInterface) context
						.getService(reference);
				BufferedReader stdin = new BufferedReader(
						new InputStreamReader(System.in));
				System.out.println("What command? (enable/disable available)");

				try {
					String cmd = stdin.readLine();
					if (cmd.equalsIgnoreCase("enable")
							|| cmd.equalsIgnoreCase("disable")) {
						iwebService.sendCommand(cmd, ids, "sync");
					} else {
						System.out
								.println("This Use case support only enable/disable cmds!! ");
						return;
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sensor_cu_5(String fname) {
		// org.osgi.snps.base.util.Util.describe(fname);
		/*
		 * ServiceReference reference = context
		 * .getServiceReference("org.osgi.snps.wii.iWebServices"); iwebService =
		 * (iWebIntegrationInterface) context .getService(reference);
		 */
		System.out.println("TODO: MISURA DEL BENESSERE..");
		/*
		 * ServiceReference reference = context
		 * .getServiceReference(MathService.class.getName()); test11 =
		 * (MathService) context.getService(reference);
		 * System.out.println(test11.add(3, 4));
		 */
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void sensor_cu_6(String fname) {
		// buildSensors(100);
		org.osgi.snps.base.util.Util.describe(fname);
		ServiceReference reference = context
				.getServiceReference(iWebIntegrationInterface.class.getName());
		iwebService = (iWebIntegrationInterface) context.getService(reference);

		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));

		System.out.println("---------------------------");
		System.out.println("List of available sensors: ");
		System.out.println("---------------------------");

		Map<String, ABComponent> mList = service.getSensList();

		for (Entry<String, ABComponent> entry : mList.entrySet()) {
			System.out.println(/* "Sid:" + entry.getKey()+ " - */""
					+ entry.getValue());
		}

		String answ = "y";
		List<String> slist = new ArrayList<String>();
		do {
			try {
				System.out.println("What Sensors? (type ids)");
				String s = stdin.readLine();
				slist.add(s);
				System.out.println("Another one? (y/n) [Default yes]");
				answ = stdin.readLine();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} while (!answ.equals("n"));
		if (slist.isEmpty())
			return;
		System.out.println("New Sensor id: "
				+ iwebService.buildVirtualSensor(slist, ""));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void sensor_cu_7(String fname, String mode) {
		List<String> tcmd = new ArrayList<String>();
		System.out.println(mode);
		org.osgi.snps.base.util.Util.describe(fname);
		ServiceReference reference = context
				.getServiceReference(iWebIntegrationInterface.class.getName());
		iwebService = (iWebIntegrationInterface) context.getService(reference);

		Map<String, ABComponent> mp = service.getSensList();
		List<ABComponent> l = new ArrayList<ABComponent>();

		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			// System.out.println(pairs.getKey() + " = " + pairs.getValue());
			ABComponent s = (ABComponent) pairs.getValue();
			if (s.getClass().getSimpleName().equalsIgnoreCase("senshybrid")) {
				// System.out.println("Hybrid!");
				l.add(s);
			}
		}
		System.out.println(l.toString());
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			System.out.println("What Hybrid sensor? (type ids)");
			String s = stdin.readLine();
			System.out.println(s);

			iwebService.getData(s, mode, "none");
			// service.addTestCmd(s);
			tcmd.add(s);
			/*
			 * ServiceReference ref = context
			 * .getServiceReference(PackageAdmin.class .getName()); PackageAdmin
			 * pa = (ref == null) ? null : (PackageAdmin) context
			 * .getService(ref); System.out.println(pa.getBundle(
			 * iEventPublisherInterface.class) .getBundleId());
			 * pa.getBundle(iEventPublisherInterface.class) .update();
			 */
			reference = context
					.getServiceReference(iEventPublisherInterface.class
							.getName());
			ipubservice = (iEventPublisherInterface) context
					.getService(reference);
			ipubservice.sendEventt("registration", tcmd);
			/*
			 * EventPublisher publisher = getPublisher(context);
			 * publisher.sendEventt("registration",tcmd);
			 */
			service.getTestCmd().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		invokeCommand();
	}

	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" }) private static
	 * EventPublisher getPublisher(BundleContext context) {
	 * System.out.println(EventAdmin.class.getName()); ServiceReference ref =
	 * context.getServiceReference(EventAdmin.class .getName()); EventPublisher
	 * publisher = null; if (ref != null) { ServiceReference reference =
	 * context.getServiceReference(iEventPublisherInterface.class.getName());
	 * ipubservice = (iEventPublisherInterface) context.getService(reference);
	 * ipubservice.sendEvent(JSonUtil.SimpleDataToJSON(sd),"regmeasure");
	 * EventAdmin eventAdmin = (EventAdmin) context.getService(ref); publisher =
	 * new EventPublisher(eventAdmin, context,"osgi/testEvent"); } else{
	 * System.out.println("Null reference.."); }
	 * System.out.println("Return publisher.."); return publisher; }
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String viewState(int code) {
		ServiceReference ref;
		PackageAdmin pa;
		try {
			switch (code) {
			case 0:
				ref = context.getServiceReference(PackageAdmin.class.getName());
				pa = (ref == null) ? null : (PackageAdmin) context
						.getService(ref);
				if (pa.getBundle(iCoreInterface.class).getState() == 32)
					return "ACTIVE";
				else
					return "INACTIVE";
			case 1:
				ref = context.getServiceReference(PackageAdmin.class.getName());
				pa = (ref == null) ? null : (PackageAdmin) context
						.getService(ref);
				if (pa.getBundle(iDaoInterface.class).getState() == 32)
					return "ACTIVE";
				else
					return "INACTIVE";
			case 2:
				ref = context.getServiceReference(PackageAdmin.class.getName());
				pa = (ref == null) ? null : (PackageAdmin) context
						.getService(ref);
				if (pa.getBundle(iRegistryInterface.class).getState() == 32)
					return "ACTIVE";
				else
					return "INACTIVE";
			case 3:
				// INTERPRETE..
				ref = context.getServiceReference(PackageAdmin.class.getName());
				pa = (ref == null) ? null : (PackageAdmin) context
						.getService(ref);
				if (pa.getBundle(iGWInterface.class).getState() == 32)
					return "ACTIVE";
				else
					return "INACTIVE";
			case 4:
				ref = context.getServiceReference(PackageAdmin.class.getName());
				pa = (ref == null) ? null : (PackageAdmin) context
						.getService(ref);
				if (pa.getBundle(iWsnInterface.class).getState() == 32)
					return "ACTIVE";
				else
					return "INACTIVE";
				/*
				 * case 5: ref =
				 * context.getServiceReference(PackageAdmin.class.getName()); pa
				 * = (ref == null) ? null : (PackageAdmin)
				 * context.getService(ref);
				 * if(pa.getBundle(iEventPublisherInterface
				 * .class).getState()==32) return "ACTIVE"; else return
				 * "INACTIVE";
				 */
			default:
				return "BUNDLE ERROR";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "BUNDLE ERROR OR MALFORMED COMMAND";
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void daoTest() {
		System.out.println("[CML:Info] -> SNPS DAO TEST STARTED..");
		System.out
				.println("[CML:Info] -> Generating sensor from default SensorML.xml");
		// try {

		ServiceReference reference = context.getServiceReference(Parser.class
				.getName());

		if (reference != null) {

			pservice = (Parser) context.getService(reference);
			/*
			 * USING APACHE XERCES... Document description =
			 * pservice.getDOMDocument("SensorML.xml");
			 */
			// USING XPATH..
			Document description = pservice.getDocument("SensorML.xml");
			System.out.println("Document obtained!");
			try {
				ABComponent s = pservice.parse(description);

				reference = context.getServiceReference(iCoreInterface.class
						.getName());

				service = (iCoreInterface) context.getService(reference);

				// La rendo persistente..
				System.out.println("[CML: STEP 1] -> Persist image..");

				if(s instanceof SensHybrid){
					ArrayList<Sensor> sensors = new ArrayList<Sensor>();
					ArrayList<String> sensids = new ArrayList<String>();
					sensids = ((SensHybrid) s).getSensids();
					for(String sid : sensids){
						sensors.add((Sensor) service.getSensList().get(sid));
					}
					((SensHybrid) s).setSensors(sensors);
				}
				service.regCall("persist", 3, s.getID(), description, s, "",
						null);

				// La modifico..
				System.out.println("[CML: STEP 2] -> Update sensor..");
				System.out.println("[CML: STEP 2] -> OLD NAME: " + s.getName());
				s.setName("ThermistorUnict@diit");
				System.out.println("[CML: STEP 2] -> NEW NAME: " + s.getName());

				service.regCall("updateComponent", 3, s.getID(), description,
						s, "", null);

				/*** TEMP: TESTING GET SENSOR INFO.. ****/
				String a = service.regCall("getSDesc", 3, s.getID(), null,
						null, "", null);
				System.out.println("Retrieved Desc: " + a);
				a = service.regCall("getSensor", 3, s.getID(), null, null, "",
						null);
				// Sensor stemp = JSonUtil.JsonToSensor(a);
				System.out.println("Retrieved Sensor Image: " + a);
				/*************/

				System.out.println("[CML: STEP 3] -> Removing component..");

				// Lo elimino..
				boolean result = Boolean.parseBoolean(service.regCall(
						"removeComponent", 3, s.getID(), null, s, "", null));

				if (result)
					System.out.println("[CML:Info] -> Test Passed");
				else
					System.out.println("[CML:Alert] -> Error!!");

			} catch (Exception e) {
				System.out.println(e.getMessage());
				return;
			}
		} else {
			System.out.println("Error..");
			return;

		}

	}

	/**
	 * 
	 * @param limit
	 *            numero di messaggi generati..
	 * @param ms
	 *            tempo di inattivita' tra 2 messaggi consecutivi
	 * 
	 */

	public static void StressTest(int limit, int ms, String mode) {
		String com = "";
		if (mode.equals("-a")) {
			com = "async";
		} else if (mode.equals("-s")) {
			com = "sync";
		}
		switch (modes.valueOf(com)) {
		case sync:
			System.out.println("SYNC MODE: " + com);
			Random random = new Random();
			int size = service.getSensList().size() - 1;
			for (int i = 0; i < limit; i++) {
				String key = service.getSensList()
						.get("test" + random.nextInt(size)).getID();
				System.out.println(service.getData(key, "sync", "none"));
			}
			break;
		case async:
			System.out.println("ASYNC MODE: " + com);
			String ac = defineAction("");
			random = new Random();
			for (int i = 0; i < limit; i++) {
				String key = "test" + random.nextInt(100);
				service.getData(key, "async", ac);
			}

			break;
		default:
			System.out.println("UNAVAILABLE MODE!");
			break;
		}
	}

	public static void buildSensors(int num) {
		for (int i = 0; i < num; i++) {
			String key = "test" + i;
			Sensor value = new Sensor(key, "test", "test", "test", "on",
					"test", "temperature", new HashMap<String, List<String>>(),
					new HashMap<String, List<String>>(),
					new HashMap<String, List<String>>(),
					new HashMap<String, List<String>>(),
					new HashMap<String, String>());
			;
			service.addToSensList(key, value);
		}
		// System.out.println(service.getSensList().size() + "\n"
		// + service.getSensList().toString());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String composition(String f1, String f2) {
		System.out.println("[CML:Info] -> SNPS COMPOSITION TEST STARTED..");
		System.out.println("[CML:Info] -> Generating two sensors");
		try {
			ServiceReference reference = context
					.getServiceReference(Parser.class.getName());
			System.out.println("[CML:Info] -> Sensor Registration");
			pservice = (Parser) context.getService(reference);

			/*
			 * USING XERCES.. Document description =
			 * pservice.getDOMDocument("SensorML.xml"); String f11 =
			 * pservice.DOMSID(description);
			 */

			/*
			 * USING XERCES.. description =
			 * pservice.getDOMDocument("SensorML1.xml"); String f12 =
			 * pservice.DOMSID(description);
			 */

			// Boolean.parseBoolean(service.regCall("image", 0, "",
			// description,null, ""));

			// USING XPATH..

			XPath xpath = XPathFactory.newInstance().newXPath();
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document description;

			System.out.println("[CML:Info] -> Sensor Registration");

			description = builder.parse(new File(f1));
			service.regCall("image", 0, "", description, null, "", null);
			// f1 = SensorMLParser.getId(xpath, description);
			f1 = pservice.getId(xpath, description);

			description = builder.parse(new File(f2));
			service.regCall("image", 0, "", description, null, "", null);
			f2 = pservice.getId(xpath, description);

			System.out.println("[CML:Info] -> Composing sensors");

			List<String> toCompose = new ArrayList<String>();
			/*
			 * toCompose.add(f11); toCompose.add(f12);
			 */

			toCompose.add(f1);
			toCompose.add(f2);

			// System.out.println(service.composerCall("compose", toCompose,
			// ""));
			return service.composerCall("compose", toCompose, "");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errorex");
			return "";
		}
	}

	/* List of Available commands */
	@SuppressWarnings("rawtypes")
	public static void menu() {
		System.out.println("[CML:Info] -> List of available commands:\n");
		for (Enum current : commands.class.getEnumConstants()) {
			if (current.toString().equals("getData"))
				System.out.println("[CML:Command] ->" + current
						+ " --sId <Sensor Id> / --all <limit> / -m <Sensor Id>"
						+ " <options> (-s: Sync Mode, -a: Async mode)");
			else if (current.toString().equals("dao"))
				System.out.println("[CML:Command] ->" + current + ""
						+ " <options> [--greet/--all/ecc...]");
			else if (current.toString().equals("sendCommand"))
				System.out
						.println("[CML:Command] ->"
								+ current
								+ " -m <command> --sId id1 id2..idN -a / -s (sync/async)");
			else if (current.toString().equals("viewcomponent"))
				System.out.println("[CML:Command] ->" + current + " -n "
						+ "<component number>");
			else if (current.toString().equals("dataflow"))
				System.out.println("[CML:Command] ->" + current
						+ " --greet / --send " + "-s <sensor Id>");
			else if (current.toString().equals("samplPlan"))
				System.out.println("[CML:Command] ->" + current
						+ " --sId id1 id2 ..idN -s/-a");
			else if (current.toString().equals("compose"))
				System.out.println("[CML:Command] ->" + current
						+ " --test / --sId id1 id2 ..idN ");
			else
				System.out.println("[CML:Command] ->" + current);
		}
	}

	public static List<String> listFilesForFolder(final File folder) {
		List<String> flist = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				flist.add(fileEntry.getAbsolutePath());
				System.out.println(fileEntry.getName());
			}
		}
		return flist;
	}
}
