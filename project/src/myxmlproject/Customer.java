package myxmlproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.DateFormatSymbols;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Customer {
	// 1. Choisir le GdC (getter/setter idBank?)
	// 2. Envoyer les infos au GdC
	// 3. Modifier montant et date OK

	private int id;
	private int idBank;
	private String name;
	private String firstname;
	private String directory;
	private ArrayList<Check> checkbook;
	private ArrayList<Product> cart;

	public ArrayList<Product> getCart() {
		return cart;
	}

	public void setCart(ArrayList<Product> cart) {
		this.cart = cart;
	}

	public String getName() {
		return name;
	}

	public String getFirstname() {
		return firstname;
	}
	
	public ArrayList<Check> getCheckbook() {
		return checkbook;
	}

	public int getIdBank() {
		return idBank;
	}

	// Constructor
	public Customer(String firstname, String name, int idBank, int id) {
		this.firstname = firstname;
		this.name = name;
		this.idBank = idBank;
		this.id = id;
		directory = "./Bank_" + idBank + "/Customer_" + id;
		File dir = new File(directory);
		dir.mkdirs();
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public String getDirectory() {
		return directory;
	}

	// Public methods
	public void fillCheck(double amount, Date date) {
		// Customer will use this function to fill the amount and date of a
		// check
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			// TODO donner la vrai adresse du check
			Document doc = db.parse("/project/check.xml");
			// On remplit le montant
			NodeList amountList = doc.getDocumentElement().getElementsByTagName("amount");
			Element eAmount;
			if(amountList == null) {
				eAmount = doc.createElement("amount");
				doc.getDocumentElement().appendChild(eAmount);
			} else {
				eAmount = ((Element) amountList.item(0));
			}
			eAmount.appendChild(doc.createTextNode(Double.toString(amount)));
			// On remplit la date
			NodeList dateList = doc.getDocumentElement().getElementsByTagName("date");
			Element eDate;
			if(dateList == null) {
				eDate = doc.createElement("date");
				doc.getDocumentElement().appendChild(eDate);
			} else {
				eDate = ((Element) dateList.item(0));
			}
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			eDate.setAttribute("day"  , "" + calendar.get(Calendar.DAY_OF_MONTH));
			eDate.setAttribute("month", new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH) - 1]);
			eDate.setAttribute("year" , "" + calendar.get(Calendar.YEAR));
			// Ecrire le nouveau xml
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("/project/mailBox/check.xml"));
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		return "Customer name: " + name + "Customer surname: " + firstname + "with customer id " + id + "Bank id" + idBank;
	}

}
