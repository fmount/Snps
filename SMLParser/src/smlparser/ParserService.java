package smlparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.ABComponent;
import org.osgi.snps.base.common.SensHybrid;
import org.osgi.snps.base.common.Sensor;
import org.osgi.snps.base.interfaces.iCoreInterface;
//import org.osgi.snps.base.util.JSonUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
//import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;
//import org.apache.xerces.parsers.DOMParser;

public class ParserService implements Parser {

	public enum tags {
		capabilities, inputList, outputList, netParams
	}

	@Override
	public String sayhello() {
		return "I'm SNPS Parser..";
	}

	/*
	 * public String DOMSID(Document doc){ NodeList LOP =
	 * doc.getElementsByTagName("sml:Component"); String id=""; for (int s = 0;
	 * s < LOP.getLength(); s++) {
	 * 
	 * Node FPN = LOP.item(s);
	 * 
	 * if (FPN.getNodeType() == Node.ELEMENT_NODE) { Element firstPElement =
	 * (Element) FPN;
	 * 
	 * try { id= getDOMAttribute(firstPElement, "gml:id"); } catch (IOException
	 * e) { e.printStackTrace(); } } } return id; }
	 * 
	 * 
	 * public Sensor DOMparse(Document doc) { try {
	 * System.out.println(doc.getXmlEncoding());
	 * System.out.println("Root element of the doc is " +
	 * doc.getDocumentElement().getNodeName()); NodeList LOP =
	 * doc.getElementsByTagName("sml:Component"); for (int s = 0; s <
	 * LOP.getLength(); s++) {
	 * 
	 * Node FPN = LOP.item(s);
	 * 
	 * if (FPN.getNodeType() == Node.ELEMENT_NODE) { Element firstPElement =
	 * (Element) FPN;
	 * 
	 * System.out.println("[SENSOR:Id]-> " + getDOMAttribute(firstPElement,
	 * "gml:id"));
	 * 
	 * System.out.println("[SENSOR:Name]-> " +
	 * getDOMSimpleElement(firstPElement, "gml:name"));
	 * 
	 * System.out.println("[SENSOR:Description]-> " +
	 * getDOMSimpleElement(firstPElement, "gml:description"));
	 * 
	 * System.out.println("[SENSOR:Type]-> " +
	 * getDOMSimpleElement(firstPElement, "gml:type"));
	 * 
	 * System.out.println("[SENSOR:Model]-> " + getDOMModel(firstPElement,
	 * "sml:identification"));
	 * 
	 * System.out.println("[SENSOR:State]-> " +
	 * getDOMSimpleElement(firstPElement, "gml:state"));
	 * 
	 * // TODO: NATURA DEL SENSORE.. /* System.out.println("[SENSOR:Nature]-> "
	 * + getDOMSimpleElement(firstPElement, "sml:state"));
	 */

	/*
	 * System.out.println("[SENSOR:Position]-> " + getDOMPosition(firstPElement,
	 * "sml:Position") .toString());
	 * 
	 * System.out.println("[SENSOR:Info]-> " +
	 * getDOMIdentificationList(firstPElement,
	 * "sml:identification").toString());
	 * 
	 * System.out .println("[SENSOR:Capabilities]-> " +
	 * getDOMCapabilities(firstPElement, "capabilities"));
	 * 
	 * System.out.println("[SENSOR:InputList]-> " +
	 * getDOMInputList(firstPElement, "sml:inputs"));
	 * 
	 * System.out.println("[SENSOR:OutputList]-> " +
	 * getDOMOutputList(firstPElement, "sml:outputs"));
	 * 
	 * Sensor sensor = new Sensor(getDOMAttribute(firstPElement, "gml:id"),
	 * getDOMSimpleElement(firstPElement, "gml:name"),
	 * getDOMModel(firstPElement, "sml:identification"), getDOMSimpleElement(
	 * firstPElement, "gml:type"), getDOMSimpleElement( firstPElement,
	 * "gml:state"), getDOMSimpleElement( firstPElement, "gml:description"),
	 * "TODO:nature", getDOMInputList(firstPElement, "sml:inputs"),
	 * getDOMOutputList(firstPElement, "sml:outputs"),
	 * getDOMIdentificationList(firstPElement, "sml:identification"),
	 * getDOMCapabilities( firstPElement, "capabilities"),
	 * getDOMPosition(firstPElement, "sml:Position")); return sensor; } // end
	 * of if clause } // end of for loop with variable s } catch (Throwable t) {
	 * t.printStackTrace(); } return null;
	 * 
	 * }
	 * 
	 * public void processNode(Node node, String spacer) throws IOException { if
	 * (node == null) return; switch (node.getNodeType()) { case
	 * Node.ELEMENT_NODE: String name = node.getNodeName();
	 * System.out.print(spacer + "<" + name); NamedNodeMap nnm =
	 * node.getAttributes();
	 * 
	 * for (int i = 0; i < nnm.getLength(); i++) { Node current = nnm.item(i);
	 * System.out.print(" " + current.getNodeName() + "= " +
	 * current.getNodeValue()); }
	 * 
	 * System.out.print(">");
	 * 
	 * NodeList nl = node.getChildNodes();
	 * 
	 * if (nl != null) { for (int i = 0; i < nl.getLength(); i++) {
	 * processNode(nl.item(i), ""); } } System.out.println(spacer + "</" + name
	 * + ">"); break; case Node.TEXT_NODE:
	 * System.out.print(node.getNodeValue()); break; case
	 * Node.CDATA_SECTION_NODE: System.out.print("" + node.getNodeValue() + "");
	 * break; case Node.ENTITY_REFERENCE_NODE: System.out.print("&" +
	 * node.getNodeName() + ";"); break; case Node.ENTITY_NODE:
	 * System.out.print("<ENTITY: " + node.getNodeName() + "> </" +
	 * node.getNodeName() + "/>"); break; case Node.DOCUMENT_NODE: NodeList
	 * nodes = node.getChildNodes(); if (nodes != null) { for (int i = 0; i <
	 * nodes.getLength(); i++) { processNode(nodes.item(i), ""); } } break;
	 * 
	 * case Node.DOCUMENT_TYPE_NODE: DocumentType docType = (DocumentType) node;
	 * System.out.print("<!DOCTYPE " + docType.getName()); if
	 * (docType.getPublicId() != null) { System.out.print(" PUBLIC " +
	 * docType.getPublicId() + " "); } else { System.out.print(" SYSTEM "); }
	 * System.out.println(" " + docType.getSystemId() + ">"); break; default:
	 * break; } }
	 * 
	 * /* public Document getDOMDocument(String path){ try {
	 * System.out.println("Parsing XML File: " + path + "\n\n"); DOMParser
	 * parser = new DOMParser(); parser.parse(path); Document doc =
	 * parser.getDocument(); System.out.println(doc.getXmlEncoding()); return
	 * doc; }catch(Exception e){ e.printStackTrace(); } return null; }
	 * 
	 * public Sensor parse(String path) { try {
	 * System.out.println("Parsing XML File: " + path + "\n\n"); DOMParser
	 * parser = new DOMParser(); parser.parse(path); Document doc =
	 * parser.getDocument(); System.out.println(doc.getXmlEncoding());
	 * System.out.println("Root element of the doc is " +
	 * doc.getDocumentElement().getNodeName()); NodeList LOP =
	 * doc.getElementsByTagName("sml:Component"); for (int s = 0; s <
	 * LOP.getLength(); s++) {
	 * 
	 * Node FPN = LOP.item(s);
	 * 
	 * if (FPN.getNodeType() == Node.ELEMENT_NODE) { Element firstPElement =
	 * (Element) FPN;
	 * 
	 * System.out.println("[SENSOR:Id]-> " + getDOMAttribute(firstPElement,
	 * "gml:id"));
	 * 
	 * System.out.println("[SENSOR:Name]-> " +
	 * getDOMSimpleElement(firstPElement, "gml:name"));
	 * 
	 * System.out.println("[SENSOR:Description]-> " +
	 * getDOMSimpleElement(firstPElement, "gml:description"));
	 * 
	 * System.out.println("[SENSOR:Type]-> " +
	 * getDOMSimpleElement(firstPElement, "gml:type"));
	 * 
	 * System.out.println("[SENSOR:Model]-> " + getDOMModel(firstPElement,
	 * "sml:identification"));
	 * 
	 * System.out.println("[SENSOR:State]-> " +
	 * getDOMSimpleElement(firstPElement, "gml:state"));
	 * 
	 * // TODO: NATURA DEL SENSORE.. /* System.out.println("[SENSOR:Nature]-> "
	 * + getDOMSimpleElement(firstPElement, "sml:state"));
	 */

	/*
	 * System.out.println("[SENSOR:Position]-> " + getDOMPosition(firstPElement,
	 * "sml:Position") .toString());
	 * 
	 * System.out.println("[SENSOR:Info]-> " +
	 * getDOMIdentificationList(firstPElement,
	 * "sml:identification").toString());
	 * 
	 * System.out .println("[SENSOR:Capabilities]-> " +
	 * getDOMCapabilities(firstPElement, "capabilities"));
	 * 
	 * System.out.println("[SENSOR:InputList]-> " +
	 * getDOMInputList(firstPElement, "sml:inputs"));
	 * 
	 * System.out.println("[SENSOR:OutputList]-> " +
	 * getDOMOutputList(firstPElement, "sml:outputs"));
	 * 
	 * Sensor sensor = new Sensor(getDOMAttribute(firstPElement, "gml:id"),
	 * getDOMSimpleElement(firstPElement, "gml:name"),
	 * getDOMModel(firstPElement, "sml:identification"), getDOMSimpleElement(
	 * firstPElement, "gml:type"), getDOMSimpleElement( firstPElement,
	 * "gml:state"), getDOMSimpleElement( firstPElement, "gml:description"),
	 * "TODO:nature", getDOMInputList(firstPElement, "sml:inputs"),
	 * getDOMOutputList(firstPElement, "sml:outputs"),
	 * getDOMIdentificationList(firstPElement, "sml:identification"),
	 * getDOMCapabilities( firstPElement, "capabilities"),
	 * getDOMPosition(firstPElement, "sml:Position"));
	 * 
	 * System.out.println(JSonUtil.SensorToJSON(sensor)); return sensor; } //
	 * end of if clause } // end of for loop with variable s } catch
	 * (SAXParseException err) { System.out.println(err.getMessage()); } catch
	 * (SAXException e) { e.printStackTrace(); } catch (Throwable t) {
	 * t.printStackTrace(); } return null; }
	 * 
	 * public Map<String, List<String>> getDOMOutputList(Element firstPElement,
	 * String elem) {
	 * 
	 * Map<String, List<String>> ol = new HashMap<String, List<String>>();
	 * String attr = ""; List<String> vals = new ArrayList<String>();
	 * 
	 * NodeList list = firstPElement.getElementsByTagName(elem); Element element
	 * = (Element) list.item(0); // System.out.println("I'm in: " +
	 * element.getNodeName());
	 * 
	 * NodeList datarecord = element.getElementsByTagName("sml:OutputList"); for
	 * (int i = 0; i < datarecord.getLength(); i++) {
	 * 
	 * element = (Element) datarecord.item(i); // System.out.println("I'm in: "
	 * + element.getNodeName());
	 * 
	 * NodeList inputs = element.getElementsByTagName("sml:output"); for (int j
	 * = 0; j < inputs.getLength(); j++) { vals.clear(); element = (Element)
	 * inputs.item(j); // System.out.println("Output Attribute: " + //
	 * element.getAttribute("name")); attr = element.getAttribute("name");
	 * NodeList vls = element.getElementsByTagName("swe:Count");
	 * 
	 * for (int z = 0; z < vls.getLength(); z++) {
	 * 
	 * element = (Element) vls.item(z); // System.out.println("Output Value: " +
	 * // element.getAttribute("definition"));
	 * 
	 * vals.add(element.getAttribute("definition")); } ol.put(attr, vals); } }
	 * return ol; }
	 * 
	 * public Map<String, List<String>> getDOMInputList(Element firstPElement,
	 * String elem) {
	 * 
	 * Map<String, List<String>> il = new HashMap<String, List<String>>();
	 * String attr = ""; List<String> vals = new ArrayList<String>();
	 * 
	 * NodeList list = firstPElement.getElementsByTagName(elem); Element element
	 * = (Element) list.item(0); // System.out.println("I'm in: " +
	 * element.getNodeName());
	 * 
	 * NodeList datarecord = element.getElementsByTagName("sml:InputList"); for
	 * (int i = 0; i < datarecord.getLength(); i++) { vals.clear(); element =
	 * (Element) datarecord.item(i); // System.out.println("I'm in: " +
	 * element.getNodeName());
	 * 
	 * NodeList inputs = element.getElementsByTagName("sml:input"); for (int j =
	 * 0; j < inputs.getLength(); j++) { element = (Element) inputs.item(j); //
	 * System.out.println("Input Attribute: " + //
	 * element.getAttribute("name")); attr = element.getAttribute("name");
	 * 
	 * NodeList vls = element.getElementsByTagName("swe:Count");
	 * 
	 * for (int z = 0; z < vls.getLength(); z++) { element = (Element)
	 * vls.item(z); // System.out.println("Input Value: " + //
	 * element.getAttribute("definition"));
	 * vals.add(element.getAttribute("definition")); } il.put(attr, vals); } }
	 * return il; }
	 * 
	 * public Map<String, List<String>> getDOMCapabilities(Element
	 * firstPElement, String elem) {
	 * 
	 * Map<String, List<String>> capabilities = new HashMap<String,
	 * List<String>>(); String attr = ""; List<String> vals = new
	 * ArrayList<String>();
	 * 
	 * NodeList list = firstPElement.getElementsByTagName(elem); Element element
	 * = (Element) list.item(0); // System.out.println("I'm in: " +
	 * element.getNodeName()); NodeList datarecord =
	 * element.getElementsByTagName("swe:DataRecord"); element = (Element)
	 * datarecord.item(0); // System.out.println("I'm in: " +
	 * element.getNodeName()); NodeList fields =
	 * element.getElementsByTagName("swe:field"); for (int i = 0; i <
	 * fields.getLength(); i++) { vals.clear(); element = (Element)
	 * fields.item(i); // System.out.println("I'm in: " +
	 * element.getNodeName()); //
	 * System.out.println("Attribute: "+element.getAttribute("name")); // //GET
	 * CAP NAME attr = element.getAttribute("name");
	 * 
	 * NodeList quantities = element.getElementsByTagName("swe:Quantity"); for
	 * (int j = 0; j < quantities.getLength(); j++) { element = (Element)
	 * quantities.item(j); NodeList values =
	 * element.getElementsByTagName("swe:value"); element = (Element)
	 * values.item(0); //
	 * System.out.println("Value: "+element.getFirstChild().getNodeValue()); //
	 * //GET CAP VALUE vals.add(element.getFirstChild().getNodeValue()); }
	 * capabilities.put(attr, vals); } return capabilities; }
	 * 
	 * public Map<String, String> getDOMPosition(Element firstPElement, String
	 * elem) {
	 * 
	 * Map<String, String> position = new HashMap<String, String>(); String
	 * value = ""; // List<String> values = new ArrayList<String>(); String attr
	 * = ""; NodeList list = firstPElement.getElementsByTagName(elem); Element
	 * element = (Element) list.item(0); // System.out.println("I'm in: " +
	 * element.getNodeName()); NodeList location =
	 * element.getElementsByTagName("swe:Location"); element = (Element)
	 * location.item(0); // System.out.println("I'm in: " +
	 * element.getNodeName()); NodeList coord =
	 * element.getElementsByTagName("swe:coordinate"); for (int i = 0; i <
	 * coord.getLength(); i++) { // values.clear(); element = (Element)
	 * coord.item(i);
	 * 
	 * // System.out.println("Attribute: "+element.getAttribute("name")); //
	 * //GET COORD NAME attr = element.getAttribute("name"); NodeList quantities
	 * = element.getElementsByTagName("swe:Quantity"); for (int j = 0; j <
	 * quantities.getLength(); j++) { element = (Element) quantities.item(j);
	 * 
	 * // System.out.println("Value: "+element.getFirstChild().getNodeValue());
	 * // //GET COORD VALUE //
	 * values.add(element.getFirstChild().getNodeValue()); value =
	 * element.getFirstChild().getNodeValue(); } position.put(attr, value); }
	 * return position; }
	 * 
	 * public Map<String, List<String>> getDOMIdentificationList( Element
	 * firstPElement, String elem) { NodeList list =
	 * firstPElement.getElementsByTagName(elem); Element element = (Element)
	 * list.item(0); // System.out.println("I'm in: " + element.getNodeName());
	 * NodeList identifierList = element
	 * .getElementsByTagName("sml:IdentifierList"); element = (Element)
	 * identifierList.item(0); // System.out.println("I'm in: " +
	 * element.getNodeName());
	 * 
	 * return getDOMSMLIdentifier(element, "sml:identifier");
	 * 
	 * }
	 * 
	 * public String getDOMModel(Element firstPElement, String elem) { NodeList
	 * list = firstPElement.getElementsByTagName(elem); Element element =
	 * (Element) list.item(0); // System.out.println("I'm in: " +
	 * element.getNodeName()); NodeList identifierList = element
	 * .getElementsByTagName("sml:IdentifierList"); element = (Element)
	 * identifierList.item(0); String model = ""; list =
	 * element.getElementsByTagName("sml:identifier"); element = (Element)
	 * list.item(0); try { String n = getDOMAttribute(element, "name"); if
	 * (n.equalsIgnoreCase("Model_Number")) { NodeList list1 =
	 * element.getElementsByTagName("sml:Term"); for (int j = 0; j <
	 * list1.getLength(); j++) { element = (Element) list1.item(j); //
	 * System.out.println("I'm in: " + element.getNodeName()); NodeList list2 =
	 * element.getElementsByTagName("sml:value"); for (int k = 0; k <
	 * list2.getLength(); k++) { element = (Element) list2.item(k); //
	 * System.out.println("I'm in: " // + element.getNodeName()); /*
	 * System.out.println("Value for Model Number: " +
	 * element.getFirstChild().getNodeValue());
	 */
	/*
	 * model = element.getFirstChild().getNodeValue(); } } } else {
	 * System.out.println("Error getting model!"); return
	 * "Error getting model!"; } } catch (Exception e) { e.printStackTrace();
	 * return "Error getting model!"; } return model; }
	 * 
	 * public Map<String, List<String>> getDOMSMLIdentifier(Element element,
	 * String el) { List<String> bsinfo = new ArrayList<String>(); List<String>
	 * zoneinfo = new ArrayList<String>(); Map<String, List<String>> toreturn =
	 * new HashMap<String, List<String>>();
	 * 
	 * NodeList list = element.getElementsByTagName(el); for (int i = 0; i <
	 * list.getLength(); i++) { element = (Element) list.item(i); try { String n
	 * = getDOMAttribute(element, "name"); if
	 * (n.equalsIgnoreCase("Model_Number")) { /* NodeList list1 =
	 * element.getElementsByTagName("sml:Term"); for (int j = 0; j <
	 * list1.getLength(); j++) { element = (Element) list1.item(j);
	 * //System.out.println("I'm in: " + element.getNodeName()); NodeList list2
	 * = element .getElementsByTagName("sml:value"); for (int k = 0; k <
	 * list2.getLength(); k++) { element = (Element) list2.item(k);
	 * //System.out.println("I'm in: " //+ element.getNodeName());
	 * /*System.out.println("Value for Model Number: " +
	 * element.getFirstChild().getNodeValue());
	 */
	/*
	 * model = element.getFirstChild().getNodeValue(); } }
	 */
	/*
	 * }
	 * 
	 * if (n.equalsIgnoreCase("NetParams")) { NodeList list1 =
	 * element.getElementsByTagName("sml:Term"); for (int j = 0; j <
	 * list1.getLength(); j++) { element = (Element) list1.item(j); //
	 * System.out.println("I'm in: " + // element.getNodeName());
	 * 
	 * if (element.getAttribute("definition") .equalsIgnoreCase("zone")) { //
	 * System.out.println("I'm in zone fields"); NodeList list2 = element
	 * .getElementsByTagName("zone"); element = (Element) list2.item(0);
	 * 
	 * zoneinfo.add(getDOMAttribute(element, "gml:id"));
	 * zoneinfo.add(getDOMSimpleElement(element, "gml:description"));
	 * zoneinfo.add(getDOMSimpleElement(element, "gml:name"));
	 * zoneinfo.add(getDOMSimpleElement(element, "edificio"));
	 * zoneinfo.add(getDOMSimpleElement(element, "piano")); }
	 * 
	 * else if (element.getAttribute("definition")
	 * .equalsIgnoreCase("base_station")) { //
	 * System.out.println("I'm in base station fields");
	 * 
	 * NodeList list2 = element.getElementsByTagName("bs"); element = (Element)
	 * list2.item(0);
	 * 
	 * bsinfo.add(getDOMAttribute(element, "gml:id"));
	 * bsinfo.add(getDOMSimpleElement(element, "gml:description"));
	 * bsinfo.add(getDOMSimpleElement(element, "gml:name"));
	 * bsinfo.add(getDOMSimpleElement(element, "gml:localip")); } } } } catch
	 * (IOException e) { e.printStackTrace(); } }
	 * 
	 * toreturn.put("bsinfo", bsinfo); toreturn.put("zoneinfo", zoneinfo);
	 * return toreturn; }
	 * 
	 * public String getDOMAttribute(Element firstPElement, String elem) throws
	 * IOException { return firstPElement.getAttribute(elem); //
	 * System.out.println("Element " + elem + ": " + s); }
	 * 
	 * public String getDOMSimpleElement(Element firstPElement, String elem) {
	 * NodeList list = firstPElement.getElementsByTagName(elem); Element element
	 * = (Element) list.item(0); NodeList textList = element.getChildNodes(); //
	 * System.out.println("Element " + elem + ": " // + ((Node)
	 * textList.item(0)).getNodeValue().trim()); return ((Node)
	 * textList.item(0)).getNodeValue().trim(); }
	 */
	@Override
	public Map<String, List<String>> getBSReference(XPath xpath,
			Document description) {
		Map<String, List<String>> netParams = new HashMap<String, List<String>>();
		String expr = "//Component/identification/IdentifierList/identifier/@name";
		netParams = getNetReferences(xpath, description, expr);
		// System.out.println("[SENSOR] BS REF: "+netParams.toString());
		return netParams;
	}

	@Override
	public Map<String, List<String>> getCapabilities(XPath xpath,
			Document description) {
		Map<String, List<String>> capabilities = new HashMap<String, List<String>>();
		String expr = "//Component/capabilities/DataRecord/field/@name";
		capabilities = getComplex(xpath, description, expr, "capabilities");
		return capabilities;
	}

	@Override
	public Map<String, List<String>> getInputList(XPath xpath,
			Document description) {
		Map<String, List<String>> il = new HashMap<String, List<String>>();
		String expr = "//Component/inputs/InputList/input/@name";
		il = getComplex(xpath, description, expr, "inputList");
		return il;
	}

	@Override
	public Map<String, List<String>> getOutputList(XPath xpath,
			Document description) {
		Map<String, List<String>> ol = new HashMap<String, List<String>>();
		String expr = "//Component/outputs/OutputList/output/@name";
		ol = getComplex(xpath, description, expr, "outputList");
		return ol;
	}

	@Override
	public String getDescription(XPath xpath, Document document) {
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

	@Override
	public String getName(XPath xpath, Document document) {
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

	@Override
	public String getState(XPath xpath, Document document) {
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

	@Override
	public String getNature(XPath xpath, Document document) {
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

	@Override
	public String getType(XPath xpath, Document document) {
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

	@Override
	public String getIdentifier(XPath xpath, Document document, String type) {
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

	@Override
	public Map<String, String> getPosition(XPath xpath, Document document) {
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
			return null;

		}
		return coord;
	}

	@Override
	public String getId(XPath xpath, Document document) {
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

	@Override
	public Map<String, List<String>> getNetReferences(XPath xpath,
			Document document, String ex) {
		Map<String, List<String>> netinfo = new HashMap<String, List<String>>();
		List<String> values = new ArrayList<String>();
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
						values = getValues(xpath, document, n.item(j)
								.getNodeValue(), "netParams");
						if (values == null)
							return null;
						netinfo.put(n.item(j).getNodeValue(), values);
					}
				}
			}
			// System.out.println(capab.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return netinfo;
	}

	@Override
	public Map<String, List<String>> getComplex(XPath xpath, Document document,
			String ex, String tag) {
		Map<String, List<String>> capab = new HashMap<String, List<String>>();
		List<String> values = new ArrayList<String>();
		try {
			XPathExpression expr = xpath.compile(ex);
			Object result = expr.evaluate(document, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				values = getValues(xpath, document, nodes.item(i)
						.getNodeValue(), tag);
				if (values.isEmpty())
					return null;
				capab.put(nodes.item(i).getNodeValue(), values);
			}
			// System.out.println(capab.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return capab;
	}

	@Override
	public List<String> getValues(XPath xpath, Document document, String node,
			String tag) {
		List<String> values = new ArrayList<String>();
		XPathExpression ex;
		Object result;
		NodeList nodes;
		String value;
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
					value = nodes.item(k).getNodeValue();
					values.add(value);
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
					String id = getZoneId(xpath, document, node);
					String description = getZoneFields(xpath, document, node,
							"description");
					String name = getZoneFields(xpath, document, node, "name");
					String edificio = getZoneFields(xpath, document, node,
							"edificio");
					String piano = getZoneFields(xpath, document, node, "piano");

					if (id.equals("") || description.equals("")
							|| name.equals("") || edificio.equals("")
							|| piano.equals(""))
						return null;

					values.add(id);
					values.add(description);
					values.add(name);
					values.add(edificio);
					values.add(piano);

					return values;
				} else if (node.equalsIgnoreCase("base_station")) {
					String id = getBSId(xpath, document, node);
					String description = getBSFields(xpath, document, node,
							"description");
					String name = getBSFields(xpath, document, node, "name");
					String localip = getBSFields(xpath, document, node,
							"localip");

					if (id.equals("") || description.equals("")
							|| name.equals("") || localip.equals(""))
						return null;

					values.add(id);
					values.add(description);
					values.add(name);
					values.add(localip);
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

	@Override
	@SuppressWarnings("unused")
	public String getZoneFields(XPath xpath, Document document, String node,
			String what) throws XPathExpressionException {
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

	@Override
	@SuppressWarnings("unused")
	public String getZoneId(XPath xpath, Document document, String node)
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

	@Override
	@SuppressWarnings("unused")
	public String getBSFields(XPath xpath, Document document, String node,
			String what) throws XPathExpressionException {
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

	@Override
	public String getBSId(XPath xpath, Document document, String node)
			throws XPathExpressionException {
		XPathExpression ex = xpath
				.compile("//Component/identification/IdentifierList/identifier[@name='NetParams']/Term[@definition='"
						+ node + "']/bs/@id");
		Object result = ex.evaluate(document, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		// System.out.println("SELECTED FOR "+node+": "+nodes.item(0).getNodeValue());
		return nodes.item(0).getNodeValue();
	}

	@Override
	public boolean writeSML(Document description) {

		/**
		 * Recupero i dati di interesse relativi al sensore
		 */
		XPath xpath = XPathFactory.newInstance().newXPath();
		Map<String, List<String>> netParams = new HashMap<String, List<String>>();
		netParams = getBSReference(xpath, description);
		String BsId = netParams.get("node_id").get(0);
		String SensorId = getId(xpath, description);
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

	@Override
	public void printXmlDocument(Document document) throws TransformerException {
		TransformerFactory fact = TransformerFactory.newInstance();
		Transformer transformer = null;
		transformer = fact.newTransformer();
		Source source = new DOMSource(document);
		StreamResult result = new StreamResult(System.out);
		transformer.transform(source, result);
	}

	// public void genDescription(List<String> slist, SensHybrid hb){
	@Override
	public Document genDescription(List<Sensor> slist, String Cid,
			String Cdesc, String Cname, String Ctype, String Cstate)
			throws TransformerException {
		try {
			Document newXmlDocument = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();

			Attr attr;

			Element root = newXmlDocument.createElement("sml:SensorML");

			// Phase 1: create ns
			createNS(newXmlDocument, root);

			// Phase 2: Generic Infos
			Element el1 = newXmlDocument.createElement("sml:Component");
			attr = newXmlDocument.createAttribute("gml:id");
			attr.setValue(Cid);
			el1.setAttributeNode(attr);

			// Elementi Semplici..
			createSimpleElement(newXmlDocument, el1, "gml:description", Cdesc);

			createSimpleElement(newXmlDocument, el1, "gml:name", Cname);

			createSimpleElement(newXmlDocument, el1, "gml:type", Ctype);

			createSimpleElement(newXmlDocument, el1, "gml:state", Cstate);

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
				addSensorElement(newXmlDocument, sensList, "sml:component",
						s.getID());
			}

			// Phase 4: TODO Add template

			// Phase 5: Close document and print!
			root.appendChild(el1);
			// printXmlDocument(newXmlDocument);
			return newXmlDocument;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void createSimpleElement(Document newXmlDocument, Element prev,
			String current, String text) {
		Element el = newXmlDocument.createElement(current);
		el.appendChild(newXmlDocument.createTextNode(text));
		prev.appendChild(el);
	}

	@Override
	public void createNS(Document newXmlDocument, Element root) {
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

	@Override
	public void addSensorElement(Document newXmlDocument, Element prev,
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
	
	public ArrayList<String> getSensorsFromHybrid (XPath xpath, Document document){
		
		ArrayList<String> sensors = new ArrayList<String>();
		
 		String expr = "//Component/components/ComponentList/component/@id";
		
		NodeList nodeList;
		try {
			nodeList = (NodeList) xpath.compile(expr).evaluate(document, XPathConstants.NODESET);
			for(int i = 0; i < nodeList.getLength(); i++){
				sensors.add(nodeList.item(i).getFirstChild().getNodeValue());
			}
			
		
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		return sensors;
		
	}

	@Override
	public ABComponent parse(Document description)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {

		System.out.println("Start parsing...");
		XPath xpath = XPathFactory.newInstance().newXPath();
		// PARSING A SENSOR..

		// STEP 1: GET INFORMATIONS (META+OTHERS)
		System.out.println("[STEP 1]");
		String id = getId(xpath, description);
		System.out.println("[SENSOR] id: " + id);

		String name = getName(xpath, description);
		System.out.println("[SENSOR] name: " + name);

		String model = getIdentifier(xpath, description, "Model_Number");
		System.out.println("[SENSOR] Model: " + model);

		String desc = getDescription(xpath, description);
		System.out.println("[SENSOR] description: " + desc);

		String type = getType(xpath, description);
		System.out.println("[SENSOR] TYPE: " + type);

		String state = getState(xpath, description);
		System.out.println("[SENSOR] STATE: " + state);

		String nature = getNature(xpath, description);
		System.out.println("[SENSOR] Nature: " + nature);

		// STEP 2: GET INPUT LIST
		System.out.println("[STEP 2]");
		Map<String, List<String>> il = new HashMap<String, List<String>>();
		il = getInputList(xpath, description);
		if (il == null || il.isEmpty()) {
			System.out.println("[SENSOR] Input List: [MISSING]");
		} else {
			System.out.println("[SENSOR] Input List: " + il.toString());
		}

		// STEP 3: GET OUTPUT LIST
		System.out.println("[STEP 3]");
		Map<String, List<String>> ol = new HashMap<String, List<String>>();
		ol = getOutputList(xpath, description);
		if (ol == null || ol.isEmpty()) {
			System.out.println("[SENSOR] Output List: [MISSING]");
		} else {
			System.out.println("[SENSOR] Output List: " + ol.toString());
		}
		// STEP 4: GET BS REFERENCE
		System.out.println("[STEP 4]");
		Map<String, List<String>> netParams = new HashMap<String, List<String>>();
		netParams = getBSReference(xpath, description);
		if (netParams == null || netParams.isEmpty()) {
			System.out.println("[SENSOR] BS REF: ");
			return null;
		} else {
			System.out.println("[SENSOR] BS REF: " + netParams.toString());
		}

		// STEP 5: GET CAPABILITIES
		System.out.println("[STEP 5]");
		Map<String, List<String>> capabilities = new HashMap<String, List<String>>();
		capabilities = getCapabilities(xpath, description);
		if (capabilities == null || capabilities.isEmpty()) {
			System.out.println("[SENSOR] Capabilities: ");
			return null;
		} else {
			System.out.println("[SENSOR] Capabilities: "
					+ capabilities.toString());
		}
		// STEP 6: GET POSITION (X,Y)
		System.out.println("[STEP 6]");
		Map<String, String> position = getPosition(xpath, description);
		if (position == null || position.isEmpty()) {
			System.out.println("[SENSOR] Position: ");
			return null;
		} else {
			System.out.println("[SENSOR] Position: " + position.toString());
		}
		if (id.equals("") || name.equals("") || model.equals("")
				|| type.equals("") || state.equals("") || desc.equals("")
				|| nature.equals("") || netParams.isEmpty()
				|| capabilities.isEmpty())
			return null;

		// FINAL STAGE

		if (!type.equalsIgnoreCase("DETECTOR_COMPOSED")) {
			Sensor sens = new Sensor(id, name, model, type, state, desc,
					nature, il, ol, netParams, capabilities, position);
			return sens;
		} else {
			ArrayList<String> sensors = new ArrayList<String>();
			sensors = getSensorsFromHybrid(xpath, description);
			SensHybrid sensH = new SensHybrid(id, name, model, type, state, desc, nature, sensors);
			sensH.setExpression(ol.get("MathFunctionOutput").get(0));
			sensH.setCapabilities(capabilities);
			sensH.setINPUT_LIST(il);
			sensH.setOUTPUT_LIST(ol);
			sensH.setPosition(position);
			sensH.setNetParams(netParams);
			return sensH;
		}
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

	}

	@Override
	public Document getDocument(String path) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// description = builder.parse(new File("sensorDescription.xml"));
			return builder.parse(new File(path));
		} catch (Exception e) {
			System.out.println("[CML:Alert]-> SensorML Malformed or Missing");
			System.out.println(e.getMessage());
			return null;
		}
	}

	/*
	 * @Override public String DOMSID(Document doc) { // TODO Auto-generated
	 * method stub return null; }
	 * 
	 * @Override public Document getDOMDocument(String path) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * @Override public Sensor parse(String path) { // TODO Auto-generated
	 * method stub return null; }
	 * 
	 * @Override public Sensor DOMparse(Document document) { // TODO
	 * Auto-generated method stub return null; }
	 */
}
