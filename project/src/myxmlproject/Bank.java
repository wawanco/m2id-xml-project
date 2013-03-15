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
	private int id;
	private String name;
	private String pathToBase;
	private Document customerBase;
	
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

	public String getName() {
		return name;
	}

	private String buildPath() {
		// Build path to xml file containing the customer database
		String path = baseDir + "/customer-base_" + id + ".xml";
		File f = new File(path);
		if(! f.isFile()) {
			try {
				f.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(f));
				out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				out.write("<customerList>");
				out.write("</customerList>");
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return path;
	}

	private void GenerateCustomerId() {
		// parcours de l'arbre et incrementer l'ID
		;
	}

	private void GenerateCheck() {
		;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int RegisterCustomer(Customer c) {
		// parse xml
		// generer nell Id
		// ajouter client
		// generer cheque
		return 0;
	}

}
