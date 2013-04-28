package myxmlproject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import sun.util.calendar.BaseCalendar.Date;

public class Check {
	private static String PATH_TO_XSD = "check.xsd";
	
	public enum Currency {
		Dollars, Euros
	}

	private int 	 id;
	private int      idBank;
	private int      idCustomer;
	private int      amount;
	private Date     issueDate;
	private Currency currency;
	private Document doc;
	
	// Constructor
	public Check(int id, int idBank, int idCustomer, Currency currency){
		this.id         = id;
		this.idBank     = idBank;
		this.idCustomer = idCustomer;
		this.currency   = currency;
		// Build xml DOM document
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setFeature("http://apache.org/xml/features/validation/schema", true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			Element root = createCheckNode();
			doc.appendChild(root);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public Element createCheckNode() {
		Element root = doc.createElement("check");
		// Attributes
		root.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "noNamespaceSchemaLocation", PATH_TO_XSD);
		HashMap<String, String> attrCurrency = new HashMap<String, String>();
		attrCurrency.put("type", currency.name());
		attrCurrency.put("type", currency.name());
		// Elements
		addElement("idCheck"   , root, "" + id, null);
		addElement("idBank"    , root, "" + idBank, null);
		addElement("idCustomer", root, "" + idCustomer, null);
		addElement("currency"  , root, "", attrCurrency);
		return root;
	}
	
	// Getters and setters
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	// Public methods
	private void addElement(String name, Element root, String msg, HashMap<String, String> attr){
		Element e = doc.createElement(name);
		e.setTextContent(msg);
		if(attr != null){
			for(Map.Entry<String, String> entry : attr.entrySet()){
				e.setAttribute(entry.getKey(), entry.getValue());
			}
		}
		root.appendChild(e);
	}
	
	public void createXml(String path){
		try {
			// write the content into xml file
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("test.xml"));
			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
			tFormer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
 	}
}
