package org.osgi.snps.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.osgi.framework.BundleContext;
import org.osgi.snps.base.common.SimpleData;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Util {

	// static iCoreInterface Cservice;
	// static iRegistryInterface Rservice;

	public enum phenomena {
		temperature, humidity, light, voltage
	}

	public static boolean writeTmpData(String data) {
		try {
			File f = new File("data.txt");
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter outFile = new PrintWriter(bw);
			SimpleData sd = JSonUtil.jsonToSimpleData(data);
			outFile.println("-------------------------------");
			outFile.println("Sensor ID: " + sd.getSid());
			outFile.println("ID_MEAS: " + sd.get_id_meas());
			outFile.println("Data received: " + sd.getData());
			outFile.println("Data Ref: " + sd.getRef());
			outFile.println("Date: " + sd.getDate());
			outFile.println("Time: " + sd.getTime());
			outFile.println("-------------------------------");
			outFile.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean LogInfo(String msg, String fname) {
		try {
			File f = new File(fname);
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter outFile = new PrintWriter(bw);
			outFile.println("-------------------------------------------");
			outFile.println(msg);
			outFile.println("-------------------------------------------");
			outFile.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Document StringToDescription(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		return builder.parse(new ByteArrayInputStream(xml.getBytes()));
	}

	public static String DescriptionToString(Document description) {
		try {

			DOMSource domSource = new DOMSource(description);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static boolean checkSensors(List<String> sensors,
			BundleContext context) {
		Iterator<String> it = sensors.iterator();
		while (it.hasNext()) {
			if (!(sensorExist(it.next(), context)))
				return false;
		}
		return true;

	}

	public static boolean sensorExist(String sId, BundleContext context) {
		// Recupero i servizi offerti dal Core e dal Registry..
		/*
		 * ServiceReference reference; reference = context
		 * .getServiceReference("org.osgi.snps.iregistry.RegisterService");
		 * Rservice = (iRegistryInterface) context.getService(reference);
		 * 
		 * reference = context
		 * .getServiceReference("org.osgi.snps.core.CoreServices"); Cservice =
		 * (iCoreInterface) context.getService(reference);
		 * 
		 * try { // Controllo di primo livello (in memoria) if
		 * ((Cservice.getSensList().get(sId)) != null) return true;
		 * 
		 * Sensor s = Rservice.getImageComponent(3, sId); if (s != null) {
		 * return true; } } catch (Exception e) { e.printStackTrace(); return
		 * false; }
		 */

		// return false;
		return true;
	}

	public static boolean evalExpr(String exp) {
		// String regex = "[p][r][l][(]";
		// PATTERN: prl(....);
		Pattern pattern = Pattern.compile("prl\\(([^<]+)\\);");
		System.out.println(exp.substring(0, 4));
		// if(!(exp.substring(0,4).matches(regex)))
		Matcher matcher = pattern.matcher(exp);
		if (matcher.find()) {
			// System.out.println("Sottosequenza : "+matcher.group());
			System.out.println("Sottogruppo 1 : " + matcher.group(1));
			return true;
		}
		return false;
	}

	public static String IdGenerator() {
		UUID idOne = UUID.randomUUID();
		// UUID idTwo = UUID.randomUUID();
		// System.out.println("[GENERATOR: Info] -> Generate id: "+idOne);
		return String.valueOf(idOne);
	}

	public static void describe(String fname) {
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(fname);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				System.out.println(strLine);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static String whatDayIsToday() {
		/*
		 * Date now = new Date(System.currentTimeMillis());
		 * //System.out.println("Today is: "+String.valueOf(now)); return
		 * String.valueOf(now);
		 */
		Date n = new Date(System.currentTimeMillis());
		String now = new SimpleDateFormat("dd-MM-yyyy").format(n);
		return now; // 2011-01-18
	}

	public static String whatTimeIsIt() {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// System.out.println( sdf.format(cal.getTime()));
		return sdf.format(cal.getTime());
	}

	public static String loadConfiguration(String path) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// description = builder.parse(new File("sensorDescription.xml"));
			Document description = builder.parse(new File(path));
			XPath xpath = XPathFactory.newInstance().newXPath();
			String ip = getIP(xpath, description);
			int port = Integer.parseInt(getPort(xpath, description));
			return "http://" + ip + ":" + port + "/wii?wsdl";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
	}

	public static String loadRosgiConfiguration(String path) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// description = builder.parse(new File("sensorDescription.xml"));
			Document description = builder.parse(new File(path));
			XPath xpath = XPathFactory.newInstance().newXPath();
			String ip = getIP(xpath, description);
			int port = Integer.parseInt(getPort(xpath, description));
			return ip + ":" + port;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
	}
	public static String getIP(XPath xpath, Document document) {
		String ip = "";
		try {
			XPathExpression expr = xpath.compile("//config/ip/text()");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				// System.out.println(nodes.item(i).getNodeValue());
				ip = nodes.item(i).getNodeValue();
			}
			return ip;
		} catch (Exception e) {
			e.printStackTrace();
			return ip;
		}
	}

	public static String getPort(XPath xpath, Document document) {
		String port = "";
		try {
			XPathExpression expr = xpath.compile("//config/port/text()");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				// System.out.println(nodes.item(i).getNodeValue());
				port = nodes.item(i).getNodeValue();
			}
			return port;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getSampleValue(String nature) {
		float result;
		float min = 0;
		float max = 0;
		String tmp[] = whatTimeIsIt().split(":");
		String phen[] = nature.split(":");
		int hour = Integer.parseInt(tmp[0]);
		
		switch (phenomena.valueOf(phen[4])) {

		case temperature:
			if ((hour >= 0 && hour < 8) || (hour >= 20 && hour <= 23)) {
				min = 20;
				max = 21;
			} else if ((hour >= 8 && hour < 11) || (hour >= 17 && hour < 20)) {
				min = 22;
				max = 23;
			} else if (hour >= 11 && hour < 17) {
				min = 24;
				max = 25;
			}
			break;
		case light:
			if (hour >= 0 && hour < 6) {
				min = 5;
				max = 6;
			} else if (hour >= 6 && hour < 8) {
				min = 6;
				max = 90;
			} else if (hour >= 8 && hour < 17) {
				min = 190;
				max = 412;
			} else if (hour >= 17 && hour <= 23) {
				min = 100;
				max = 130;
			}
			break;
		case humidity:
			min = 46;
			max = 47;
			break;
		case voltage:
			min = 2.28f;
			max = 2.33f;
			break;
		default:
			min = 0;
			max = 100;
		}

		float rand = (float) (min + (Math.random() * (max - min)));
		result = (float) (Math.round(rand * 1000.0) / 1000.0);

		return String.valueOf(result);
	}

	public static void main(String[] argvs) {
		String exp = "prl(s1,s2,s3,s4,s5,s6,(((i1+i2+i3)*1.5)/(3*i2)+i5));";
		// Prima fase: Valuto la correttezza sintattica dell'espressione..
		System.out.println(Util.evalExpr(exp));
		List<String> elems = new ArrayList<String>();
		String operation = "";
		StringTokenizer st1 = new StringTokenizer(exp.substring(4,
				exp.length() - 2), ",");
		while (st1.hasMoreTokens()) {
			elems.add(st1.nextToken());
		}
		System.out.println(elems.toString());
		// Remove Operation..
		operation = elems.get(elems.size() - 1);
		elems.remove(elems.size() - 1);

		System.out.println("SENSORS: " + elems.toString() + "\nOPERATION: "
				+ operation);

		System.out.println(IdGenerator());

		System.out.println("DATE: " + whatDayIsToday());
		System.out.println("TIME: " + whatTimeIsIt());
	}
}
