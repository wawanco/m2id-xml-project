package myxmlproject;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.util.calendar.BaseCalendar.Date;

public class Check {
	public enum Currency {
		Dollars, Euros
	}

	private int 	 id;
	private int      idBank;
	private int      idCustomer;
	private int      amount;
	private Date     issueDate;
	private Currency currency;
	
	// Constructor
	public Check(int id, int idBank, int idCustomer, Currency currency){
		this.id         = id;
		this.idBank     = idBank;
		this.idCustomer = idCustomer;
		this.currency   = currency;
	}
	
	// Getters and setters
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	// Public methods
	public void createXml(String path){
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			Element root = doc.createElement("check");
			doc.appendChild(root);
			// write the content into xml file
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "check.dtd");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("test.xml"));
	 
			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
	 
			tFormer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
 	}
}
