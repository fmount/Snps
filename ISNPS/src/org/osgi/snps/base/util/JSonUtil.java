package org.osgi.snps.base.util;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.osgi.snps.base.common.ABComponent;
import org.osgi.snps.base.common.Action;
import org.osgi.snps.base.common.SamplingPlan;
import org.osgi.snps.base.common.SensHybrid;
import org.osgi.snps.base.common.Sensor;
import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.json.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author francesco
 * 
 */
public class JSonUtil {

	public static String ABComponentToJSon(ABComponent s) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();

			json.key("sid");
			json.value(s.getID());

			json.key("name");
			json.value(s.getName());

			json.key("model");
			json.value(s.getModel());

			json.key("type");
			json.value(s.getType());

			json.key("state");
			json.value(s.getState());

			json.key("description");
			json.value(s.getDescription());

			json.key("nature");
			json.value(s.getNature());

			json = json.endObject();
			str = json.toString();

			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public static String HybridToJSON(SensHybrid s) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();

			json.key("sid");
			json.value(s.getID());

			json.key("name");
			json.value(s.getName());

			json.key("model");
			json.value(s.getModel());

			json.key("type");
			json.value(s.getType());

			json.key("state");
			json.value(s.getState());

			json.key("description");
			json.value(s.getDescription());

			json.key("nature");
			json.value(s.getNature());
			
			json.key("expression");
			json.value(s.getExpression());

			json.key("il");
			json.value(HLToJSon((HashMap<String, List<String>>) s
					.getINPUT_LIST()));

			json.key("ol");
			json.value(HLToJSon((HashMap<String, List<String>>) s
					.getOUTPUT_LIST()));

			json.key("netParams");
			json.value(HLToJSon((HashMap<String, List<String>>) s
					.getNetParams()));

			json.key("capabilities");
			json.value(HLToJSon((HashMap<String, List<String>>) s
					.getCapabilities()));

			json.key("position");
			json.value(HashMapToJSON((HashMap<String, String>) s.getPosition()));

			
			json.key("sensors");
			json.value(arrayLstToJSON(s.getSensors()));

			json = json.endObject();
			str = json.toString();

			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings("rawtypes")
	public static SensHybrid JSONToHybrid(String str) {

		//System.out.println("Hybrid: " + str);
		SensHybrid sd = new SensHybrid("sid1", "test", "test", "test",
				"active", "test", "temperature", new ArrayList<Sensor>());
		try {
			JSONObject json = new JSONObject(str);
			Iterator keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = (String) keys.next();
				if (key.equalsIgnoreCase("sid"))
					sd.setID(json.getString(key));
				else if (key.equalsIgnoreCase("name"))
					sd.setName(json.getString(key));
				else if (key.equalsIgnoreCase("model"))
					sd.setModel(json.getString(key));
				else if (key.equalsIgnoreCase("type"))
					sd.setType(json.getString(key));
				else if (key.equalsIgnoreCase("description"))
					sd.setDescription(json.getString(key));
				else if (key.equalsIgnoreCase("state"))
					sd.setState(json.getString(key));
				else if (key.equalsIgnoreCase("nature"))
					sd.setNature(json.getString(key));
				else if (key.equalsIgnoreCase("expression"))
					sd.setExpression(json.getString(key));
				else if (key.equalsIgnoreCase("il"))
					sd.setINPUT_LIST(JSonToHL((json.getString(key))));
				else if (key.equalsIgnoreCase("ol"))
					sd.setOUTPUT_LIST(JSonToHL((json.getString(key))));
				else if (key.equalsIgnoreCase("netparams"))
					sd.setNetParams(JSonToHL((json.getString(key))));
				else if (key.equalsIgnoreCase("capabilities"))
					sd.setCapabilities(JSonToHL((json.getString(key))));
				else if (key.equalsIgnoreCase("position"))
					sd.setPosition(JsonToHashMap((json.getString(key))));
				else if (key.equalsIgnoreCase("sensors")){
					List<ABComponent> sens = JSONTOAList(json.getString(key));
					//ArrayList<Sensor> sensors = new ArrayList<Sensor>();
					Iterator<ABComponent> sit = sens.iterator();
					while(sit.hasNext()){
						Sensor a = (Sensor) sit.next();
						//sensors.add(a);
						sd.getSensors().add(a);
						//sd.getSensids().add(a.getID());
					}
					//sd.setSensors(sensors);
				}
				else {
					System.out.println("[JSonConversion!]Error getting tag!!");
					return sd;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sd;

	}

	public static String ActionToJSON(Action a) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();

			json.key("component");
			json.value(a.getComponent());

			json.key("cmd");
			json.value(a.getCmd());

			json = json.endObject();
			str = json.toString();

			return str;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings("rawtypes")
	public static Action JsonToAction(String action) {
		Action act = new Action();
		try {
			JSONObject json = new JSONObject(action);
			Iterator keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = (String) keys.next();
				if (key.equalsIgnoreCase("component"))
					act.setComponent(json.getString(key));

				else if (key.equalsIgnoreCase("cmd"))
					act.setCmd(json.getString(key));
				else {
					System.out.println("[JSonConversion!]Error getting tag!!");
					return act;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return act;

	}

	public static String SensorToJSON(Sensor s) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();

			json.key("sid");
			json.value(s.getID());

			json.key("name");
			json.value(s.getName());

			json.key("model");
			json.value(s.getModel());

			json.key("type");
			json.value(s.getType());

			json.key("state");
			json.value(s.getState());

			json.key("description");
			json.value(s.getDescription());

			json.key("nature");
			json.value(s.getNature());

			json.key("il");
			json.value(HLToJSon((HashMap<String, List<String>>) s
					.getINPUT_LIST()));

			json.key("ol");
			json.value(HLToJSon((HashMap<String, List<String>>) s
					.getOUTPUT_LIST()));

			json.key("netParams");
			json.value(HLToJSon((HashMap<String, List<String>>) s
					.getNetParams()));

			json.key("capabilities");
			json.value(HLToJSon((HashMap<String, List<String>>) s
					.getCapabilities()));

			json.key("position");
			json.value(HashMapToJSON((HashMap<String, String>) s.getPosition()));

			json = json.endObject();
			str = json.toString();

			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public static Sensor JsonToSensor(String str) {
		//System.out.println("Sensor: " + str);
		Sensor sd = new Sensor("sid1", "test", "test", "test", "active",
				"test", "temperature", new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>(),
				new HashMap<String, String>());
		try {
			JSONObject json = new JSONObject(str);
			Iterator keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = (String) keys.next();
				if (key.equalsIgnoreCase("sid"))
					sd.setID(json.getString(key));
				else if (key.equalsIgnoreCase("name"))
					sd.setName(json.getString(key));
				else if (key.equalsIgnoreCase("model"))
					sd.setModel(json.getString(key));
				else if (key.equalsIgnoreCase("type"))
					sd.setType(json.getString(key));
				else if (key.equalsIgnoreCase("description"))
					sd.setDescription(json.getString(key));
				else if (key.equalsIgnoreCase("state"))
					sd.setState(json.getString(key));
				else if (key.equalsIgnoreCase("nature"))
					sd.setNature(json.getString(key));
				else if (key.equalsIgnoreCase("il"))
					sd.setINPUT_LIST(JSonToHL((json.getString(key))));
				else if (key.equalsIgnoreCase("ol"))
					sd.setOUTPUT_LIST(JSonToHL((json.getString(key))));
				else if (key.equalsIgnoreCase("netparams"))
					sd.setNetParams(JSonToHL((json.getString(key))));
				else if (key.equalsIgnoreCase("capabilities"))
					sd.setCapabilities(JSonToHL((json.getString(key))));
				else if (key.equalsIgnoreCase("position"))
					sd.setPosition(JsonToHashMap((json.getString(key))));
				else {
					System.out.println("[JSonConversion!]Error getting tag!!");
					return sd;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sd;

	}

	public static String SimpleDataToJSON(SimpleData data) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();

			json.key("sid");
			json.value(data.getSid());

			json.key("data");
			json.value(data.getData());

			json.key("ref");
			json.value(data.getRef());

			json.key("date");
			json.value(data.getDate());

			json.key("time");
			json.value(data.getTime());

			json.key("_id_meas");
			json.value(data.get_id_meas());

			json = json.endObject();
			str = json.toString();

			return str;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static SimpleData jsonToSimpleData(String data) {
		// System.out.println("DATA: " + data);
		SimpleData sd = new SimpleData();
		try {
			JSONObject json = new JSONObject(data);
			Iterator keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = (String) keys.next();
				if (key.equalsIgnoreCase("sid"))
					sd.setSid(json.getString(key));
				else if (key.equalsIgnoreCase("data"))
					sd.setData(json.getString(key));
				else if (key.equalsIgnoreCase("ref"))
					sd.setRef(json.getString(key));
				else if (key.equalsIgnoreCase("date"))
					sd.setDate(json.getString(key));
				else if (key.equalsIgnoreCase("time"))
					sd.setTime(json.getString(key));
				else if (key.equalsIgnoreCase("_id_meas"))
					sd.set_id_meas(json.getString(key));
				else {
					System.out.println("[JSonConversion!]Error getting tag!!");
					return sd;
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
		return sd;
	}

	public static String SamplingPlanToJSON(SamplingPlan plan) {

		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();

			json.key("splan_identifier");
			json.value(plan.getSplan_identifier());

			json.key("nodesId");
			json.value(ArrayListToJSON((ArrayList<String>) plan.getNodesId()));

//			json.key("th_min");
//			json.value(plan.getThreshold_min());
//
//			json.key("th_max");
//			json.value(plan.getThreshold_max());
			
			json.key("startDate");
			json.value(plan.getStartDate());
			
			json.key("endDate");
			json.value(plan.getEndDate());

			json.key("interval");
			json.value(plan.getInterval());

			json = json.endObject();
			str = json.toString();
			return str;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static SamplingPlan JsonToSamplingPlan(String plan) {
		SamplingPlan pl = new SamplingPlan();
		try {
			JSONObject json = new JSONObject(plan);
			Iterator keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = (String) keys.next();
				if (key.equalsIgnoreCase("nodesId"))
					pl.setNodesId(JsonToArrayList(json.getString(key)));
				else if (key.equalsIgnoreCase("th_min")){}
				else if (key.equalsIgnoreCase("th_max")){}
				else if (key.equalsIgnoreCase("startDate"))
					pl.setStartDate(json.getString(key));
				else if (key.equalsIgnoreCase("endDate"))
					pl.setEndDate(json.getString(key));
				else if (key.equalsIgnoreCase("interval"))
					pl.setInterval(json.getInt(key));
				else if (key.equalsIgnoreCase("splan_identifier"))
					pl.setSplan_identifier(json.getString(key));
				else {
					System.out.println("[JSonConversion!]Error getting tag!!");
					return pl;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pl;
	}

	public static String dArrayLstToJSON(List<SimpleData> list) {
		try {
			JSONArray json = new JSONArray();
			Iterator<SimpleData> it = list.iterator();
			while (it.hasNext()) {
				json.put(SimpleDataToJSON(it.next()));
			}
			StringWriter out = new StringWriter();
			json.write(out);
			String jsonText = out.toString();
			return jsonText;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;
	}

	public static String arrayLstToJSON(List<Sensor> list) {
		try {
			JSONArray json = new JSONArray();
			Iterator<Sensor> it = list.iterator();
			while (it.hasNext()) {
				json.put(SensorToJSON(it.next()));
			}
			StringWriter out = new StringWriter();
			json.write(out);
			String jsonText = out.toString();
			return jsonText;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;
	}

	public static ArrayList<ABComponent> JSONTOAList(String str) {

		try {
			JSONArray json = new JSONArray(str);
			ArrayList<ABComponent> lst = new ArrayList<ABComponent>();

			for (int i = 0; i < json.length(); i++) {
				if (new JSONObject(json.get(i).toString()).get("type").equals(
						"DETECTOR_COMPOSED")) {
					//System.out.println("Hybrid "+JSONToHybrid(json.get(i).toString()) + " : " + JSONToHybrid(json.get(i).toString()).getExpression());
					lst.add(JSONToHybrid(json.get(i).toString()));
				} else {
					lst.add(JsonToSensor(json.get(i).toString()));
				}
			}
			return lst;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;
	}

	public static ArrayList<SimpleData> dJSONTOAList(String str) {

		try {
			JSONArray json = new JSONArray(str);
			ArrayList<SimpleData> lst = new ArrayList<SimpleData>();

			for (int i = 0; i < json.length(); i++) {
				lst.add(jsonToSimpleData(json.get(i).toString()));
			}
			return lst;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;

	}

	@SuppressWarnings("rawtypes")
	public static String ArrayListToJSON(ArrayList<String> list) {
		try {
			JSONArray json = new JSONArray();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				json.put(it.next());
			}
			StringWriter out = new StringWriter();
			json.write(out);
			String jsonText = out.toString();
			return jsonText;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;
	}

	public static ArrayList<String> JsonToArrayList(String str) {
		try {
			JSONArray json = new JSONArray(str);
			ArrayList<String> lst = new ArrayList<String>();

			for (int i = 0; i < json.length(); i++) {
				lst.add(json.get(i).toString());
			}
			return lst;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;

	}

	@SuppressWarnings("rawtypes")
	public static String LinkedListToJSON(LinkedList<String> list) {
		try {
			JSONArray json = new JSONArray();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				json.put(it.next());
			}
			StringWriter out = new StringWriter();
			json.write(out);
			String jsonText = out.toString();
			return jsonText;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;

	}

	public static LinkedList<String> JsonToLinkedList(String str) {
		try {
			JSONArray json = new JSONArray(str);
			LinkedList<String> lst = new LinkedList<String>();

			for (int i = 0; i < json.length(); i++) {
				lst.add(json.get(i).toString());
			}
			return lst;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;
	}

	public static String HashMapToJSON(HashMap<String, String> hash) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();
			for (Object obj : hash.keySet()) {
				json.key((String) obj);
				json.value(hash.get(obj));
			}

			json = json.endObject();
			str = json.toString();
			return str;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, String> JsonToHashMap(String str) {
		try {
			JSONObject json = new JSONObject(str);
			HashMap<String, String> hash = new HashMap<String, String>();
			Iterator<String> keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = keys.next();
				hash.put(key, json.getString(key));
			}
			return hash;
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
			return null;
		}
	}

	public static String HashtableToJSON(Hashtable<String, String> hash) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();
			for (Object obj : hash.keySet()) {
				json.key((String) obj);
				json.value(hash.get(obj));
			}

			json = json.endObject();
			str = json.toString();
			return str;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Hashtable<String, String> JsonToHashtable(String str) {
		try {
			JSONObject json = new JSONObject(str);
			Hashtable<String, String> hash = new Hashtable<String, String>();
			Iterator<String> keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = keys.next();
				hash.put(key, json.getString(key));
			}
			return hash;
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
			return null;
		}
	}

	public static String HLToJSon(HashMap<String, List<String>> hl) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();
			for (Object obj : hl.keySet()) {
				json.key((String) obj);
				json.value(ArrayListToJSON((ArrayList<String>) hl.get(obj)));
			}
			json = json.endObject();
			str = json.toString();
			return str;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
	}

	public static String HLDToJSon(HashMap<String, Double> hl) {
		JSONWriter json = new JSONStringer();
		String str = null;
		try {
			json = json.object();
			for (Object obj : hl.keySet()) {
				json.key((String) obj);
				json.value(hl.get(obj));
			}
			json = json.endObject();
			str = json.toString();
			return str;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Double> JSonToHLD(String str) {
		try {
			JSONObject json = new JSONObject(str);
			HashMap<String, Double> hash = new HashMap<String, Double>();
			Iterator<String> keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = keys.next();
				hash.put(key, json.getDouble(key));
			}
			return hash;
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, List<String>> JSonToHL(String str) {
		try {
			JSONObject json = new JSONObject(str);
			HashMap<String, List<String>> hash = new HashMap<String, List<String>>();
			Iterator<String> keys = json.keys();
			String key;
			while (keys.hasNext()) {
				key = keys.next();
				hash.put(key, JsonToArrayList(json.getString(key)));
			}
			return hash;
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
			return null;
		}
	}
	
//	public static String getMathExpression (HashMap<String, List<String>> hm){
//		HashMap<String, List<String>> hl = hm;
//		String expression = null;
//		
//		Iterator<String> it = hl.keySet().iterator();
//		while(it.hasNext()){
//			String s = (String) it.next();
//			if(s.equalsIgnoreCase("MathFunctionOutput"))
//				expression = hl.get(s).get(0);
//		}
//		
//		return expression;
//		
//	}

	public static String DocumentTOJson(Document doc) {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		try {
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			// System.out.println("XML IN String format is: \n" +
			// writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	@SuppressWarnings("deprecation")
	public static Document JSONToDocument(String xml) {
		try {
			java.io.InputStream sbis = new java.io.StringBufferInputStream(xml);
			javax.xml.parsers.DocumentBuilderFactory b = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			b.setNamespaceAware(false);
			org.w3c.dom.Document doc = null;
			javax.xml.parsers.DocumentBuilder db = null;
			db = b.newDocumentBuilder();
			doc = db.parse(sbis);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/* TEST MAIN */
	public static void main(String argv[]) throws ParserConfigurationException,
			SAXException, IOException, XPathExpressionException,
			TransformerException {
		List<String> ids = new ArrayList<String>();
		ids.add("12343");
		ids.add("abcdoe");
		SamplingPlan plan = new SamplingPlan("12ewdnhso9d", ids, "05-01-2014 12:23:33", "06-01-2014 12:24:22",200);
		System.out.println(JSonUtil.SamplingPlanToJSON(plan));
		SamplingPlan newPlan = JSonUtil.JsonToSamplingPlan(JSonUtil
				.SamplingPlanToJSON(plan));
		newPlan.toString();

		SimpleData s = new SimpleData("test35", "194328532121", "simple",
				"1999-05-23", "15:25:32");
		// System.out.println(s.toString());
		System.out.println("DATA TO JSON: " + JSonUtil.SimpleDataToJSON(s));

		SimpleData news = JSonUtil.jsonToSimpleData(JSonUtil
				.SimpleDataToJSON(s));
		System.out.println("JSON TO SIMPLE DATA: " + news.toString());

		/* Sensor test */
		System.out.println("[CML] -> Generating Sensor..");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document description = builder.parse(new File("../../../SensorML.xml"));

		String d = DocumentTOJson(description);
		description = JSONToDocument(d);

		Sensor se = SensorMLParser.parse(description);
		String str = JSonUtil.SensorToJSON(se);
		// System.out.println("SENSOR TO JSON: "+str);
		// System.out.println("JSON TO SENSOR: "+JSonUtil.JsonToSensor(str));

		// COMPOSE..
		List<Sensor> l = new ArrayList<Sensor>();
		l.add(JSonUtil.JsonToSensor(str));
		l.add(JSonUtil.JsonToSensor(str));
		l.add(JSonUtil.JsonToSensor(str));
		// String Cid = s.getSid()+s.getSid()+s.getSid();
		// SensorMLParser.genDescription(l,Cid,"Virtual","boH","COMPOSED SENSOR","ACTIVE");
		SensHybrid sd = new SensHybrid("a", "b", "c", "d", "e", "f", "g", l);
		String str1 = HybridToJSON(sd);
		System.out.println("HybridTOJSON: " + str1);
		sd = JSONToHybrid(str1);
		System.out.println("JSONToHybrid: " + sd.toString());

		/****/
		Sensor s2 = l.get(0);
		System.out.println(s2.getNetParams());

	}
}