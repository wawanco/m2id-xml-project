package myxmlproject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class Check {
	private static String PATH_TO_XSD = "check.xsd";
	
	public enum Currency {
		dollars, euros
	}

	private boolean  filled;
	private int 	 id;
	private int      idBank;
	private int      idCustomer;
	private int      amount;
	private String   pathToXml;
	private Date     issueDate;
	private Currency currency;
	private Document doc;
	
	// Constructor
	public Check(int id, int idBank, Customer customer, Currency currency){
		this.id         = id;
		this.idBank     = idBank;
		idCustomer      = customer.getId();
		this.currency   = currency;
		filled = false;
		pathToXml = customer.getDirectory() + "/check_" + id + ".xml";
		amount = 0;
		
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
	
	public static Check getInstanceFromNode(Node eCheck, Customer c) {
		int idCheck     = Integer.parseInt(((Element) eCheck).getElementsByTagName("idCheck").item(0).getTextContent());
		int idBank      = Integer.parseInt(((Element) eCheck).getElementsByTagName("idBank" ).item(0).getTextContent());
		Node tmp = ((Element) eCheck).getElementsByTagName("amount").item(0);
		Currency currency = Currency.valueOf(((Element) tmp).getAttribute("currency"));
		return new Check(idCheck, idBank, c, currency);
	}
	
	public int getId() {
		return id;
	}

	public int getIdBank() {
		return idBank;
	}

	public int getIdCustomer() {
		return idCustomer;
	}
	
	public String getPathToXml() {
		return pathToXml;
	}

	public Currency getCurrency() {
		return currency;
	}
	
	public boolean isFilled() {
		return filled;
	}

	public Element createCheckNode() {
		Element root = doc.createElement("check");
		root.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "noNamespaceSchemaLocation", PATH_TO_XSD);
		Element e1 = doc.createElement("idCheck"   ); e1.setTextContent("" + id);
		Element e2 = doc.createElement("idBank"    ); e2.setTextContent("" + idBank);
		Element e3 = doc.createElement("idCustomer"); e3.setTextContent("" + idCustomer);
		Element e4 = doc.createElement("amount"); e4.setTextContent("" + amount);
		e4.setAttribute("currency", currency.toString());
		root.appendChild(e1);
		root.appendChild(e2);
		root.appendChild(e3);
		root.appendChild(e4);
		return root;
	}
	
	public void fillCheck(double amount, Date date) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			// On remplit le montant
			NodeList amountList = doc.getDocumentElement().getElementsByTagName("amount");
			((Element) amountList.item(0)).setTextContent(Double.toString(amount));
			// On remplit la date
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			Element eDate = doc.createElement("date");
			doc.getDocumentElement().appendChild(eDate);
			eDate.setAttribute("day"  , "" + calendar.get(Calendar.DAY_OF_MONTH));
			eDate.setAttribute("month", new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH) - 1]);
			eDate.setAttribute("year" , "" + calendar.get(Calendar.YEAR));
			filled = true;
			writeXml();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	// Getters and setters
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	
	public void delete() {
		try {
			Files.delete(Paths.get(pathToXml));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeXml(){
		try {
			// write the content into xml file
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(pathToXml));
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
