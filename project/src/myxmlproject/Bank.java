package myxmlproject;

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
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Bank {
	static String baseDir = ".";
	
	private int      id;
	private String   name;
	private String   pathToBase;
	private Document customerBase;
	
	// Construtor
	public Bank(int id, String name) {
		this.id = id;
		this.name = name;
		pathToBase = baseDir + "/customer-base_" + id + ".xml";
		File f = new File(pathToBase);
		// Initialize parser
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature("http://xml.org/sax/features/validation", true);
			dbf.setValidating(true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			if(! f.isFile()) {
				customerBase = db.newDocument();
				Element root = customerBase.createElement("customerList"); 
				customerBase.appendChild(root);
			} else {
				customerBase = db.parse(f);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeXML();
	}
	
	// Getters and setters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	// Public methods
	public Customer registerCustomer(String firstname, String name, int amount){
		Customer customer = new Customer(
				firstname
			,	name
			, 	id
			, 	generateCustomerId()
			, 	amount
			);
		addCustomerToBase(customer);
		return customer;
	}
	
	public void generateChecks(Customer c, int nbEuros, int nbDollars) {
		for(int i = 0; i < nbEuros; i++) {
			Check check = new Check(0, id, c.getId(), Check.Currency.Euros);
			check.createXml(c.getDirectory());
		}
		
		for(int i = 0; i < nbDollars; i++) {
			Check check = new Check(0, id, c.getId(), Check.Currency.Dollars);
			check.createXml(c.getDirectory());
		}
	}

	// Private methods
	private int generateCustomerId() {
		// parcours de l'arbre et incrementer l'ID
		return 0;
	}
	
	private void addCustomerToBase(Customer c) {
		// parse xml
		// generer nell Id
		// ajouter client
		;
	}
	
	public void writeXML(){
		try {
			// write the content into xml file
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "customer-base.dtd");
			DOMSource source = new DOMSource(customerBase);
			StreamResult result = new StreamResult(new File(pathToBase));
			// Output to console for testing
			tFormer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
 	}


}
