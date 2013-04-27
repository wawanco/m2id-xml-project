package myxmlproject;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.util.calendar.BaseCalendar.Date;

public class Customer {
	// 1. Choisir le GdC (getter/setter idBank?)
	// 2. Envoyer les infos au GdC
	// 3. Modifier montant et date OK

	private int id;
	private int idBank;
	private String name;
	private String firstname;
	private int amount;
	private String directory;
	private ArrayList<Check> checkbook;
	private ArrayList<Product> cart;
	private ArrayList<Check> ckeckRevieved;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ArrayList<Product> getCart() {
		return cart;
	}

	public void setCart(ArrayList<Product> cart) {
		this.cart = cart;
	}

	public ArrayList<Check> getCkeckRevieved() {
		return ckeckRevieved;
	}

	public void setCkeckRevieved(ArrayList<Check> ckeckRevieved) {
		this.ckeckRevieved = ckeckRevieved;
	}

	// Constructor
	public Customer(String firstname, String name, int idBank, int id,
			int amount) {
		this.firstname = firstname;
		this.name = name;
		this.idBank = idBank;
		this.id = id;
		this.amount = amount;
		directory = "./Customer_" + id;
		File dir = new File(directory);
		dir.mkdir();
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public String getDirectory() {
		return directory;
	}

	// Public methods
	public void fillCheck(String amount, Date date) {
		// Customer will use this function to fill the amount and date of a
		// check
		if (amount != "0" && date != null) {
			throw new Error("Invalid amount or date.");
		}
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement("check");
			doc.appendChild(rootElement);
			
			Element money = doc.createElement("amount");
			money.appendChild(doc.createTextNode(amount));
			rootElement.appendChild(money);
			
			Element when = doc.createElement("date");
			rootElement.appendChild(when);
			
			Attr day = doc.createAttribute("day");
			day.setValue(Integer.toString(date.getDayOfMonth()));
			when.setAttributeNode(day);
			
			Attr month = doc.createAttribute("month");
			month.setValue(Integer.toString(date.getMonth()));
			when.setAttributeNode(month);
			
			Attr year = doc.createAttribute("year");
			year.setValue(Integer.toString(date.getYear()));
			when.setAttributeNode(year);		

			try {
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("/project/mailBox/check.xml"));
				try {
					transformer.transform(source, result);
				} catch (TransformerException e) {
					e.printStackTrace();
				}
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return "Customer name: " + name + "Customer surname: " + firstname
				+ "with customer id " + id + "Bank id" + idBank;
	}

}
