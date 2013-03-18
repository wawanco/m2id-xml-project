package myxmlproject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
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
		pathToBase = buildPath();
		// Initialize parser
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature("http://xml.org/sax/features/validation", true);
			dbf.setValidating(true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			customerBase = db.parse(pathToBase);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
/*		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature("http://xml.org/sax/features/validation", true);
			dbf.setValidating(true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document check= db.newDocument();
			check.createElement("")
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	
	// Private methods
	private String buildPath() {
		// Build path to xml file containing the customer database
		String path = baseDir + "/customer-base_" + id + ".xml";
		File f = new File(path);
		if(! f.isFile()) {
			try {
				f.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(f));
				out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				out.write("<!DOCTYPE customerList SYSTEM \"customer-base.dtd\" >\n");
				out.write("<customerList>\n");
				out.write("</customerList>\n");
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return path;
	}

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

}
