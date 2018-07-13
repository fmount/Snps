package smlparser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.osgi.snps.base.common.ABComponent;
import org.osgi.snps.base.common.Sensor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public interface Parser {
	
	
	/*public String DOMSID(Document doc);
	
	public Document getDOMDocument(String path);
	
	public Sensor parse(String path);	
	
	public Sensor DOMparse(Document document);*/
	
	public Map<String,List<String>> getBSReference(XPath xpath, Document description);
	
	public Map<String,List<String>> getCapabilities(XPath xpath, Document description);
	
	public Map<String,List<String>> getInputList(XPath xpath, Document description);
	
	public Map<String,List<String>> getOutputList(XPath xpath, Document description);
	
	public String getDescription(XPath xpath, Document document);
	
	public String getName(XPath xpath, Document document);
	
	public String getState(XPath xpath, Document document);
	
	public String getNature(XPath xpath, Document document);
	
	public String getType(XPath xpath, Document document);
	
	public String getIdentifier(XPath xpath, Document document,String type);
	
	public Map<String,String> getPosition(XPath xpath, Document document);
	
	public String getId(XPath xpath, Document document);
	
	public Map<String,List<String>> getNetReferences(XPath xpath, Document document, String ex);
	
	public Map<String,List<String>> getComplex(XPath xpath, Document document, String ex,String tag);
	
	public List<String> getValues(XPath xpath, Document document,String node, String tag);
	
	public String getZoneFields(XPath xpath, Document document,String node,String what) throws XPathExpressionException;
	
	public String getZoneId(XPath xpath, Document document,String node) throws XPathExpressionException;
	
	public String getBSFields(XPath xpath, Document document,String node,String what) throws XPathExpressionException;
	
	public String getBSId(XPath xpath, Document document,String node) throws XPathExpressionException;
	
	public boolean writeSML(Document description);
	
	public void printXmlDocument(Document document) throws TransformerException;
	
	public Document genDescription(List<Sensor> slist
			, String Cid,String Cdesc, String Cname, 
			String Ctype,String Cstate) throws TransformerException;
	
	public void createSimpleElement(Document newXmlDocument,Element prev, 
			String current, String text);
	
	public void createNS(Document newXmlDocument, Element root);
	
	public void addSensorElement(Document newXmlDocument,Element prev, 
			String current, String id);
	
	public ABComponent parse(Document description) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException;
	
	public Document getDocument(String path);

	public String sayhello();
	

}
