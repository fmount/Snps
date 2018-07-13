package org.osgi.snps.base.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.osgi.snps.base.common.SensHybrid;
import org.osgi.snps.base.common.Sensor;
import org.w3c.dom.*;

import javax.xml.xpath.*;
import javax.xml.parsers.*;

import org.xml.sax.SAXException;

public class SensorMLParser {

	public enum tags {
		capabilities, inputList, outputList, netParams
	}

	public static Map<String, List<String>> getBSReference(XPath xpath,
			Document description) {
		Map<String, List<String>> netParams = new HashMap<String, List<String>>();
		String expr = "//Component/identification/IdentifierList/identifier/@name";
		netParams = SensorMLParser.getNetReferences(xpath, description, expr);
		// System.out.println("[SENSOR] BS REF: "+netParams.toString());
		return netParams;
	}

	public static Map<String, List<String>> getCapabilities(XPath xpath,
			Document description) {
		Map<String, List<String>> capabilities = new HashMap<String, List<String>>();
		String expr = "//Component/capabilities/DataRecord/field/@name";
		capabilities = SensorMLParser.getComplex(xpath, description, expr,
				"capabilities");
		return capabilities;
	}

	public static Map<String, List<String>> getInputList(XPath xpath,
			Document description) {
		Map<String, List<String>> il = new HashMap<String, List<String>>();
		String expr = "//Component/inputs/InputList/input/@name";
		il = SensorMLParser.getComplex(xpath, description, expr, "inputList");
		return il;
	}

	public static Map<String, List<String>> getOutputList(XPath xpath,
			Document description) {
		Map<String, List<String>> ol = new HashMap<String, List<String>>();
		String expr = "//Component/outputs/OutputList/output/@name";
		ol = SensorMLParser.getComplex(xpath, description, expr, "outputList");
		return ol;
	}

	public static String getDescription(XPath xpath, Document document) {
		String description = "";
		try {
			XPathExpression expr = xpath
					.compile("//Component/description/text()");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				// System.out.println(nodes.item(i).getNodeValue());
				description = nodes.item(i).getNodeValue();
			}
			return description;
		} catch (Exception e) {
			e.printStackTrace();
			return description;
		}
	}

	public static String getName(XPath xpath, Document document) {
		String name = "";
		try {
			XPathExpression expr = xpath.compile("//Component/name/text()");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				name = nodes.item(i).getNodeValue();
			}
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return name;
		}
	}

	public static String getState(XPath xpath, Document document) {
		String state = "";
		try {
			XPathExpression expr = xpath.compile("//Component/state/text()");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				state = nodes.item(i).getNodeValue();
			}
			return state;
		} catch (Exception e) {
			e.printStackTrace();
			return state;
		}
	}

	public static String getNature(XPath xpath, Document document) {
		String nature = "";
		try {
			XPathExpression expr = xpath.compile(""
					+ "//Component/inputs/InputList/input/Count/@definition");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				// System.out.println(nodes.item(i).getNodeValue());
				nature = nodes.item(i).getNodeValue();
			}
			return nature;
		} catch (Exception e) {
			e.printStackTrace();
			return nature;
		}
	}

	public static String getType(XPath xpath, Document document) {
		String type = "";
		try {
			XPathExpression expr = xpath.compile("//Component/type/text()");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				// System.out.println(nodes.item(i).getNodeValue());
				type = nodes.item(i).getNodeValue();
			}
			return type;
		} catch (Exception e) {
			e.printStackTrace();
			return type;
		}
	}

	public static String getIdentifier(XPath xpath, Document document,
			String type) {
		String meta = "";
		try {
			XPathExpression expr = xpath
					.compile("//Component/identification/IdentifierList/identifier/@name");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				// System.out.println(nodes.item(i).getNodeValue());
				if (nodes.item(i).getNodeValue().equals(type)) {
					XPathExpression exp = xpath
							.compile("//Component/identification/IdentifierList/identifier/Term/value/text()");
					Object res = exp.evaluate(document, XPathConstants.NODESET);
					NodeList nod = (NodeList) res;
					meta = nod.item(i).getNodeValue();
				}
			}
			return meta;
		} catch (Exception e) {
			e.printStackTrace();
			return meta;
		}
	}

	public static Map<String, String> getPosition(XPath xpath, Document document) {
		String x, y = "";
		Map<String, String> coord = new HashMap<String, String>();
		try {
			XPathExpression expr = xpath
					.compile("//Component/Position/Location/coordinate/@name");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeValue().equalsIgnoreCase("x")) {
					XPathExpression exp = xpath
							.compile("//Component/Position/Location/coordinate/Quantity/text()");
					Object res = exp.evaluate(document, XPathConstants.NODESET);
					NodeList nod = (NodeList) res;
					x = nod.item(i).getNodeValue();
					coord.put("x", x);
				}
				if (nodes.item(i).getNodeValue().equalsIgnoreCase("y")) {
					XPathExpression exp = xpath
							.compile("//Component/Position/Location/coordinate/Quantity/text()");
					Object res = exp.evaluate(document, XPathConstants.NODESET);
					NodeList nod = (NodeList) res;
					y = nod.item(i).getNodeValue();
					coord.put("y", y);
				}
			}
			// System.out.println(coord.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return coord;
	}

	public static String getId(XPath xpath, Document document) {
		String id = "";
		try {
			XPathExpression expr = xpath.compile("//Component/@id");
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				id = nodes.item(i).getNodeValue();
			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return id;
		}
	}

	public static Map<String, List<String>> getNetReferences(XPath xpath,
			Document document, String ex) {
		Map<String, List<String>> netinfo = new HashMap<String, List<String>>();
		try {
			XPathExpression expr = xpath.compile(ex);
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeValue().equals("NetParams")) {
					// System.out.println(nodes.item(i).getNodeValue());
					XPathExpression exe = xpath
							.compile("//Component/identification/IdentifierList/identifier[@name='"
									+ nodes.item(i).getNodeValue()
									+ "']/Term/@definition");
					Object re = exe.evaluate(document, XPathConstants.NODESET);
					NodeList n = (NodeList) re;
					for (int j = 0; j < n.getLength(); j++) {
						// System.out.println(n.item(j).getNodeValue());
						netinfo.put(
								n.item(j).getNodeValue(),
								getValues(xpath, document, n.item(j)
										.getNodeValue(), "netParams"));
					}
				}
			}
			// System.out.println(capab.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return netinfo;
	}

	public static Map<String, List<String>> getComplex(XPath xpath,
			Document document, String ex, String tag) {
		Map<String, List<String>> capab = new HashMap<String, List<String>>();
		try {
			XPathExpression expr = xpath.compile(ex);
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				capab.put(
						nodes.item(i).getNodeValue(),
						getValues(xpath, document,
								nodes.item(i).getNodeValue(), tag));
			}
			// System.out.println(capab.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return capab;
	}

	public static List<String> getValues(XPath xpath, Document document,
			String node, String tag) {
		List<String> values = new ArrayList<String>();
		XPathExpression ex;
		Object result;
		NodeList nodes;
		try {
			switch (tags.valueOf(tag)) {
			case capabilities:
				ex = xpath
						.compile("//Component/capabilities/DataRecord/field[@name='"
								+ node + "']/Quantity/value/text()");
				result = ex.evaluate(document, XPathConstants.NODESET);
				nodes = (NodeList) result;
				for (int k = 0; k < nodes.getLength(); k++) {
					// System.out.println("SELECTED FOR "+node+": "+n.item(k).getNodeValue());
					values.add(nodes.item(k).getNodeValue());
				}
				return values;
			case inputList:
				ex = xpath.compile("//Component/inputs/InputList/input[@name='"
						+ node + "']/Count/uom/@href");
				result = ex.evaluate(document, XPathConstants.NODESET);
				nodes = (NodeList) result;
				for (int k = 0; k < nodes.getLength(); k++) {
					// System.out.println("SELECTED FOR "+node+": "+n.item(k).getNodeValue());
					values.add(nodes.item(k).getNodeValue());
				}
				return values;
			case outputList:
				ex = xpath
						.compile("//Component/outputs/OutputList/output[@name='"
								+ node + "']/Count/uom/@href");
				result = ex.evaluate(document, XPathConstants.NODESET);
				nodes = (NodeList) result;
				for (int k = 0; k < nodes.getLength(); k++) {
					// System.out.println("SELECTED FOR "+node+": "+n.item(k).getNodeValue());
					values.add(nodes.item(k).getNodeValue());
				}
				return values;
			case netParams:
				if (node.equalsIgnoreCase("zone")) {
					ex = xpath
							.compile("//Component/identification/IdentifierList/identifier[@name='NetParams']/Term[@definition='"
									+ node + "']/value/text()");
					result = ex.evaluate(document, XPathConstants.NODESET);
					nodes = (NodeList) result;

					values.add(getZoneId(xpath, document, node));
					values.add(getZoneFields(xpath, document, node,
							"description"));
					values.add(getZoneFields(xpath, document, node, "name"));
					values.add(getZoneFields(xpath, document, node, "edificio"));
					values.add(getZoneFields(xpath, document, node, "piano"));

					return values;
				} else if (node.equalsIgnoreCase("base_station")) {
					values.add(getBSId(xpath, document, node));
					values.add(getBSFields(xpath, document, node, "description"));
					values.add(getBSFields(xpath, document, node, "name"));
					values.add(getBSFields(xpath, document, node, "localip"));
				}

			default:
				break;
			}
			// System.out.println("VALUES: "+values.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	@SuppressWarnings("unused")
	public static String getZoneFields(XPath xpath, Document document,
			String node, String what) throws XPathExpressionException {
		XPathExpression ex = xpath
				.compile("//Component/identification/IdentifierList/identifier[@name='NetParams']/Term[@definition='"
						+ node + "']/zone/" + what + "/text()");
		Object result = ex.evaluate(document, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;

		for (int k = 0; k < nodes.getLength(); k++) {
			// System.out.println("SELECTED FOR "+node+" - "+what+
			// ": "+nodes.item(k).getNodeValue());
			// values.add(nodes.item(k).getNodeValue());
			return nodes.item(k).getNodeValue();
		}
		return "";
	}

	@SuppressWarnings("unused")
	public static String getZoneId(XPath xpath, Document document, String node)
			throws XPathExpressionException {
		XPathExpression ex = xpath
				.compile("//Component/identification/IdentifierList/identifier[@name='NetParams']/Term[@definition='"
						+ node + "']/zone/@id");
		Object result = ex.evaluate(document, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;

		for (int k = 0; k < nodes.getLength(); k++) {
			// System.out.println("SELECTED FOR "+node+": "+nodes.item(k).getNodeValue());
			return nodes.item(k).getNodeValue();
		}
		return "";
	}

	@SuppressWarnings("unused")
	public static String getBSFields(XPath xpath, Document document,
			String node, String what) throws XPathExpressionException {
		XPathExpression ex = xpath
				.compile("//Component/identification/IdentifierList/identifier[@name='NetParams']/Term[@definition='"
						+ node + "']/bs/" + what + "/text()");
		Object result = ex.evaluate(document, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;

		for (int k = 0; k < nodes.getLength(); k++) {
			// System.out.println("SELECTED FOR "+node+" - "+what+
			// ": "+nodes.item(k).getNodeValue());
			// values.add(nodes.item(k).getNodeValue());
			return nodes.item(k).getNodeValue();
		}
		return "";
	}

	public static String getBSId(XPath xpath, Document document, String node)
			throws XPathExpressionException {
		XPathExpression ex = xpath
				.compile("//Component/identification/IdentifierList/identifier[@name='NetParams']/Term[@definition='"
						+ node + "']/bs/@id");
		Object result = ex.evaluate(document, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		// System.out.println("SELECTED FOR "+node+": "+nodes.item(0).getNodeValue());
		return nodes.item(0).getNodeValue();
	}

	public static boolean writeSML(Document description) {

		/**
		 * Recupero i dati di interesse relativi al sensore
		 */
		XPath xpath = XPathFactory.newInstance().newXPath();
		Map<String, List<String>> netParams = new HashMap<String, List<String>>();
		netParams = SensorMLParser.getBSReference(xpath, description);
		String BsId = netParams.get("node_id").get(0);
		String SensorId = SensorMLParser.getId(xpath, description);
		TransformerFactory fact = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = fact.newTransformer();
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		}
		Result result1 = new StreamResult(new File(SensorId + "_" + BsId
				+ ".xml"));
		Source source = new DOMSource(description);
		try {
			transformer.transform(source, result1);
			System.out.println("Generated file: " + result1.getSystemId());
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void printXmlDocument(Document document)
			throws TransformerException {
		TransformerFactory fact = TransformerFactory.newInstance();
		Transformer transformer = null;
		transformer = fact.newTransformer();
		Source source = new DOMSource(document);
		StreamResult result = new StreamResult(System.out);
		transformer.transform(source, result);
	}

	// public static void genDescription(List<String> slist, SensHybrid hb){
	public static Document genDescription(List<Sensor> slist, SensHybrid hb)
			throws TransformerException {
		try {
			Document newXmlDocument = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();

			Attr attr;

			Element root = newXmlDocument.createElement("sml:SensorML");

			// Phase 1: create ns
			createNS(newXmlDocument, root);

			Element member = newXmlDocument.createElement("sml:member");

			// Phase 2: Generic Infos
			Element el1 = newXmlDocument.createElement("sml:Component");
			attr = newXmlDocument.createAttribute("gml:id");
			attr.setValue(hb.getID());
			el1.setAttributeNode(attr);

			// Elementi Semplici..
			SensorMLParser.createSimpleElement(newXmlDocument, el1,
					"gml:description", hb.getDescription());

			SensorMLParser.createSimpleElement(newXmlDocument, el1, "gml:name",
					hb.getName());

			SensorMLParser.createSimpleElement(newXmlDocument, el1, "gml:type",
					hb.getType());

			SensorMLParser.createSimpleElement(newXmlDocument, el1,
					"gml:state", hb.getState());

			Element identification = newXmlDocument
					.createElement("sml:identification");
			el1.appendChild(identification);

			Element identifierList = newXmlDocument
					.createElement("sml:IdentifierList");
			identification.appendChild(identifierList);

			Element identifierModel = SensorMLParser.addElementAttr(
					newXmlDocument, identifierList, "sml:identifier",
					"gml:name", "Model_Number");

			Element termModel = SensorMLParser.addElementAttr(newXmlDocument,
					identifierModel, "sml:Term", "definition",
					"Model_Number_Definition");
			SensorMLParser.createSimpleElement(newXmlDocument, termModel,
					"sml:value", "Hybrid");

			Element identifierNetParams = SensorMLParser.addElementAttr(
					newXmlDocument, identifierList, "sml:identifier",
					"gml:name", "NetParams");

			Element termNetParams = SensorMLParser.addElementAttr(
					newXmlDocument, identifierNetParams, "sml:Term",
					"definition", "zone");
			ArrayList<String> params = new ArrayList<String>();
			for (String s : hb.getNetParams().keySet()) {
				if (s.equalsIgnoreCase("zone")) {
					params = (ArrayList<String>) hb.getNetParams().get(s);
					Element zone = SensorMLParser.addElementAttr(
							newXmlDocument, termNetParams, s, "gml:id",
							params.get(0));
					SensorMLParser.createSimpleElement(newXmlDocument, zone,
							"gml:description", params.get(1));
					SensorMLParser.createSimpleElement(newXmlDocument, zone,
							"gml:name", params.get(2));
					SensorMLParser.createSimpleElement(newXmlDocument, zone,
							"edificio", params.get(3));
					SensorMLParser.createSimpleElement(newXmlDocument, zone,
							"piano", params.get(4));
				} else if (s.equalsIgnoreCase("basestation")) {
					params = (ArrayList<String>) hb.getNetParams().get(s);
					Element termBs = SensorMLParser.addElementAttr(
							newXmlDocument, identifierNetParams, "sml:Term",
							"definition", s);
					Element bsId = SensorMLParser.addElementAttr(
							newXmlDocument, termBs, "bs", "gml:id",
							params.get(0));
					SensorMLParser.createSimpleElement(newXmlDocument, bsId,
							"gml:description", params.get(1));
					SensorMLParser.createSimpleElement(newXmlDocument, bsId,
							"gml:name", params.get(2));
					SensorMLParser.createSimpleElement(newXmlDocument, bsId,
							"gml:localip", params.get(3));
				}
			}

			Element position = newXmlDocument.createElement("sml:Position");
			el1.appendChild(position);
			Element location = SensorMLParser.addElementAttr(newXmlDocument,
					position, "swe:Location", "definition",
					"urn:ogc:def:phenomenon:location");
			Element coordX = SensorMLParser.addElementAttr(newXmlDocument,
					location, "swe:coordinate", "name", "X");
			Element quantity = SensorMLParser.addElementAttr(newXmlDocument,
					coordX, "swe:Quantity", "definition",
					"urn:ogc:def:phenomenon:distance");
			Attr quantAttr = newXmlDocument.createAttribute("axixCode");
			quantAttr.setValue("x");
			quantity.setAttributeNode(quantAttr);
			quantAttr = newXmlDocument.createAttribute("uom");
			quantAttr.setValue("urn:ogc:def:unit:meter");
			quantity.setAttributeNode(quantAttr);
			quantity.setTextContent(hb.getPosition().get("x"));
			Element coordY = SensorMLParser.addElementAttr(newXmlDocument,
					location, "swe:coordinate", "name", "y");
			quantity = SensorMLParser.addElementAttr(newXmlDocument, coordY,
					"swe:Quantity", "definition",
					"urn:ogc:def:phenomenon:distance");
			quantAttr = newXmlDocument.createAttribute("axixCode");
			quantAttr.setValue("y");
			quantity.setAttributeNode(quantAttr);
			quantAttr = newXmlDocument.createAttribute("uom");
			quantAttr.setValue("urn:ogc:def:unit:meter");
			quantity.setAttributeNode(quantAttr);
			quantity.setTextContent(hb.getPosition().get("y"));

			Element capabilities = SensorMLParser.addElementAttr(
					newXmlDocument, el1, "capabilities", "name",
					"MeasurementProperties");
			Element dataRecord = SensorMLParser.addElementAttr(newXmlDocument,
					capabilities, "swe:DataRecord", "definition",
					"urn:ogc:def:property:measurementProperties");
			for (String s : hb.getCapabilities().keySet()) {
				Element field = SensorMLParser.addElementAttr(newXmlDocument,
						dataRecord, "swe:field", "name", s);
				quantAttr = newXmlDocument.createAttribute("xlink:arcrole");
				quantAttr.setValue("urn:ogc:def:property:" + s);
				field.setAttributeNode(quantAttr);
				quantity = SensorMLParser.addElementAttr(newXmlDocument, field,
						"swe:Quantity", "definition", "urn:ogc:def:property:"
								+ s);
				SensorMLParser.addElementAttr(newXmlDocument, quantity,
						"swe:uom", "code", "%");
				ArrayList<String> val = (ArrayList<String>) hb
						.getCapabilities().get(s);
				for (String sval : val) {
					SensorMLParser.createSimpleElement(newXmlDocument,
							quantity, "swe:value", sval);
				}
			}

			// field = SensorMLParser.addElementAttr(newXmlDocument, dataRecord,
			// "swe:field", "name", "range");
			// quantAttr = newXmlDocument.createAttribute("xlink:arcrole");
			// quantAttr.setValue("urn:ogc:def:property:range");
			// field.setAttributeNode(quantAttr);
			// quantity = SensorMLParser.addElementAttr(newXmlDocument, field,
			// "swe:Quantity", "definition", "urn:ogc:def:property:range");
			// SensorMLParser.addElementAttr(newXmlDocument, quantity,
			// "swe:uom",
			// "code", "%");
			// SensorMLParser.createSimpleElement(newXmlDocument, quantity,
			// "swe:value", hb.getCapabilities().get("range").get(0));
			//
			// field = SensorMLParser.addElementAttr(newXmlDocument, dataRecord,
			// "swe:field", "name", "accuracy");
			// quantAttr = newXmlDocument.createAttribute("xlink:arcrole");
			// quantAttr.setValue("urn:ogc:def:property:accuracy");
			// field.setAttributeNode(quantAttr);
			// quantity = SensorMLParser.addElementAttr(newXmlDocument, field,
			// "swe:Quantity", "definition",
			// "urn:ogc:def:property:accuracy");
			// SensorMLParser.addElementAttr(newXmlDocument, quantity,
			// "swe:uom",
			// "code", "%");
			// SensorMLParser.createSimpleElement(newXmlDocument, quantity,
			// "swe:value", hb.getCapabilities().get("accuracy").get(0));

			Element elinputs = newXmlDocument.createElement("sml:inputs");

			Element elil = newXmlDocument.createElement("sml:InputList");
			elinputs.appendChild(elil);
			for (String sin : hb.getINPUT_LIST().keySet()) {
				Element elin = newXmlDocument.createElement("sml:input");
				Attr attrname = newXmlDocument.createAttribute("gml:name");
				attrname.setValue(sin);
				elin.setAttributeNode(attrname);
				elil.appendChild(elin);
				for (String sc : hb.getINPUT_LIST().get(sin)) {
					Element elcount = newXmlDocument.createElement("swe:Count");
					Attr attrdef = newXmlDocument
							.createAttribute("gml:definition");
					attrdef.setValue("phenomenon");
					elcount.setAttributeNode(attrdef);
					elin.appendChild(elcount);

					Element elunit = newXmlDocument.createElement("swe:uom");
					Attr attrunit = newXmlDocument
							.createAttribute("xlink:href");
					attrunit.setValue(sc);
					elunit.setAttributeNode(attrunit);
					elcount.appendChild(elunit);
					el1.appendChild(elinputs);
				}
			}

			for (String sout : hb.getOUTPUT_LIST().keySet()) {
				Element elouts = newXmlDocument.createElement("sml:outputs");
				Element elol = newXmlDocument.createElement("sml:OutputList");
				elouts.appendChild(elol);
				Element elout = newXmlDocument.createElement("sml:output");
				Attr attrname = newXmlDocument.createAttribute("gml:name");
				attrname.setValue(sout);
				elout.setAttributeNode(attrname);
				elol.appendChild(elout);

				Element elcount = newXmlDocument.createElement("swe:Count");
				Attr attrdef = newXmlDocument.createAttribute("gml:definition");
				attrdef.setValue("MathFunction");
				elcount.setAttributeNode(attrdef);
				elout.appendChild(elcount);

				Element elunit = newXmlDocument.createElement("swe:uom");
				Attr attrunit = newXmlDocument.createAttribute("xlink:href");

				if (hb.getExpression() != null) {

					attrunit.setValue(hb.getExpression());

				} else {
					attrunit.setValue("none");
				}

				elunit.setAttributeNode(attrunit);
				elcount.appendChild(elunit);

				el1.appendChild(elouts);
			}
			// Componenti della composizione..
			Element el3 = newXmlDocument.createElement("sml:components");
			el1.appendChild(el3);

			Element sensList = newXmlDocument
					.createElement("sml:ComponentList");
			el3.appendChild(sensList);

			// Phase 3: Add Sensors..
			// Sensori che compongono l'ibrido..
			Iterator<Sensor> it = slist.iterator();
			while (it.hasNext()) {
				Sensor s = it.next();
				SensorMLParser.addSensorElement(newXmlDocument, sensList,
						"sml:component", s.getID());
			}

			// Phase 4: TODO Add template

			// Phase 5: Close document and print!
			member.appendChild(el1);
			root.appendChild(member);
			// printXmlDocument(newXmlDocument);
			return newXmlDocument;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void createSimpleElement(Document newXmlDocument,
			Element prev, String current, String text) {
		Element el = newXmlDocument.createElement(current);
		el.appendChild(newXmlDocument.createTextNode(text));
		prev.appendChild(el);
	}

	public static void createNS(Document newXmlDocument, Element root) {
		Attr attr;

		attr = newXmlDocument.createAttribute("xmlns:a");
		attr.setValue("http://relaxng.org/ns/compatibility/annotations/1.0");
		root.setAttributeNode(attr);

		attr = newXmlDocument.createAttribute("xmlns:gml");
		attr.setValue("http://www.opengis.net/gml");
		root.setAttributeNode(attr);

		attr = newXmlDocument.createAttribute("xmlns:rng");
		attr.setValue("http://relaxng.org/ns/structure/1.0");
		root.setAttributeNode(attr);

		attr = newXmlDocument.createAttribute("xmlns:sml");
		attr.setValue("http://www.opengis.net/sensorML/1.0.1");
		root.setAttributeNode(attr);

		attr = newXmlDocument.createAttribute("xmlns:swe");
		attr.setValue("http://www.opengis.net/swe/1.0.1");
		root.setAttributeNode(attr);

		attr = newXmlDocument.createAttribute("xmlns:xlink");
		attr.setValue("http://www.w3.org/1999/xlink");
		root.setAttributeNode(attr);

		attr = newXmlDocument.createAttribute("xmlns:xng");
		attr.setValue("http://xng.org/1.0");
		root.setAttributeNode(attr);

		newXmlDocument.appendChild(root);
	}

	public static void addSensorElement(Document newXmlDocument, Element prev,
			String current, String id) {// , String xlink){
		Attr attr;
		Element el = newXmlDocument.createElement(current);

		attr = newXmlDocument.createAttribute("id");
		attr.setValue(id);
		el.setAttributeNode(attr);

		/*
		 * attr = newXmlDocument.createAttribute("xlink:href");
		 * attr.setValue(xlink); el.setAttributeNode(attr);
		 */

		prev.appendChild(el);
	}

	public static Element addElementAttr(Document newXmlDocument, Element prev,
			String current, String at, String value) {// , String xlink){
		Attr attr;
		Element el = newXmlDocument.createElement(current);

		attr = newXmlDocument.createAttribute(at);
		attr.setValue(value);
		el.setAttributeNode(attr);

		/*
		 * attr = newXmlDocument.createAttribute("xlink:href");
		 * attr.setValue(xlink); el.setAttributeNode(attr);
		 */

		prev.appendChild(el);
		return el;
	}

	public static Sensor parse(Document description)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {

		System.out.println("Start parsing...");
		XPath xpath = XPathFactory.newInstance().newXPath();

		// PARSING A SENSOR..

		// STEP 1: GET INFORMATIONS (META+OTHERS)
		System.out.println("[STEP 1]");
		String id = SensorMLParser.getId(xpath, description);
		System.out.println("[SENSOR] id: " + id);

		String name = SensorMLParser.getName(xpath, description);
		System.out.println("[SENSOR] name: " + name);

		String model = SensorMLParser.getIdentifier(xpath, description,
				"Model_Number");
		System.out.println("[SENSOR] Model: " + model);

		String desc = SensorMLParser.getDescription(xpath, description);
		System.out.println("[SENSOR] description: " + desc);

		String type = SensorMLParser.getType(xpath, description);
		System.out.println("[SENSOR] TYPE: " + type);

		String state = SensorMLParser.getState(xpath, description);
		System.out.println("[SENSOR] STATE: " + state);

		String nature = SensorMLParser.getNature(xpath, description);
		System.out.println("[SENSOR] Nature: " + nature);

		// STEP 2: GET INPUT LIST
		System.out.println("[STEP 2]");
		Map<String, List<String>> il = new HashMap<String, List<String>>();
		il = SensorMLParser.getInputList(xpath, description);
		System.out.println("[SENSOR] Input List: " + il.toString());

		// STEP 3: GET OUTPUT LIST
		System.out.println("[STEP 3]");
		Map<String, List<String>> ol = new HashMap<String, List<String>>();
		ol = SensorMLParser.getOutputList(xpath, description);
		System.out.println("[SENSOR] Output List: " + ol.toString());

		// STEP 4: GET BS REFERENCE
		System.out.println("[STEP 4]");
		Map<String, List<String>> netParams = new HashMap<String, List<String>>();
		netParams = SensorMLParser.getBSReference(xpath, description);
		System.out.println("[SENSOR] BS REF: " + netParams.toString());

		// STEP 5: GET CAPABILITIES
		System.out.println("[STEP 5]");
		Map<String, List<String>> capabilities = new HashMap<String, List<String>>();
		capabilities = SensorMLParser.getCapabilities(xpath, description);
		System.out.println("[SENSOR] Capabilities: " + capabilities.toString());

		// STEP 6: GET POSITION (X,Y)
		System.out.println("[STEP 6]");
		Map<String, String> position = SensorMLParser.getPosition(xpath,
				description);
		System.out.println("[SENSOR] Position: " + position.toString());

		// FINAL STAGE
		Sensor sens = new Sensor(id, name, model, type, state, desc, nature,
				il, ol, netParams, capabilities, position);

		sens.setCapabilities(capabilities);
		sens.setNetParams(netParams);

		/*
		 * Map<String,String> bs = new HashMap<String, String>();
		 * 
		 * List<String> bsF = netParams.get("base_station");
		 * 
		 * if(bsF.size() == 4){ System.out.println("ZIP"); bs.put("id",
		 * bsF.get(0)); bs.put("description", bsF.get(1)); bs.put("name",
		 * bsF.get(2)); bs.put("localip", bsF.get(3)); sens.setBase_station(bs);
		 * System.out.println(sens.getBase_station()); } else{
		 * sens.setBase_station(new HashMap<String, String>()); }
		 * 
		 * 
		 * Map<String,String> zone = new HashMap<String, String>();
		 * 
		 * List<String> zoneF = netParams.get("zone");
		 * 
		 * if(zoneF.size() == 5){ System.out.println("ZIP"); zone.put("id",
		 * zoneF.get(0)); zone.put("description", zoneF.get(1));
		 * zone.put("name", zoneF.get(2)); zone.put("edificio", zoneF.get(3));
		 * zone.put("piano", zoneF.get(4)); sens.setZone(zone);
		 * System.out.println(sens.getZone()); } else{ sens.setZone(new
		 * HashMap<String, String>()); }
		 */

		return sens;
	}
}
