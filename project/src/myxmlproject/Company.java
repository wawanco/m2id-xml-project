package myxmlproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.nio.file.StandardCopyOption.*;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Company {

	private int id;
	private String name;
	private String pathToMailbox;
	private Bank bank;
	private double minAmount;
	private Check.Currency currency;
	private ArrayList<Product> productList;

	public Company(int id, String name, Bank bank, double minAmount, Check.Currency currency, ArrayList<Product> productList) {
		this.id          = id;
		this.name        = name;
		this.bank        = bank;
		this.minAmount   = minAmount;
		this.currency    = currency;
		this.productList = productList;
		pathToMailbox = "./Company_" + id;
		File dir = new File(pathToMailbox);
		dir.mkdirs();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Product> getProductList() {
		return productList;
	}

	public void setProductList(ArrayList<Product> productList) {
		this.productList = productList;
	}

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(int min_amount) {
		this.minAmount = minAmount;
	}

	public Check.Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Check.Currency currency) {
		this.currency = currency;
	}
	
	public String getPathToMailbox() {
		return pathToMailbox;
	}
	
	private Product getProductByName(String name){
		for(Product p: productList){
			if(p.getName().equals(name))
				return p;
		}
		return null;
	}

	public boolean checkOrder(String pathToOrder) {
		// TODO Utiliser un schema pour la validation?
		boolean validate = true;
		int sumAmount = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(pathToOrder));
			// Validate minimal amount and stock
			NodeList nList = doc.getElementsByTagName("item");
			for (int i = 0; i < nList.getLength(); i++) {
				Element e = (Element) nList.item(i);
				int qty = Integer.parseInt(e.getElementsByTagName("quantity").item(0).getTextContent());
				sumAmount += (
					Double.parseDouble(e.getElementsByTagName("unitPrice").item(0).getTextContent())
				*	qty
				);
				String name = e.getElementsByTagName("name").item(0).getTextContent();
				validate &= getProductByName(name).getStock() >= qty;
			}
			validate &= (sumAmount >= minAmount);
			// Validate currency
			Check.Currency oCurrency = Check.Currency.valueOf(
				((Element) doc.getElementsByTagName("currency").item(0)).getAttribute("type")
			);
			validate &= (oCurrency.equals(currency));
			//TODO Si la commande est validee il faudrait diminuer le stock.
			Files.delete(Paths.get(pathToOrder));
			return validate;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void sendCheck(String pathToCheck) {
		// Send the check to the companie's bank after the order content validation
		Path source = Paths.get(pathToCheck);
		Path dest   = Paths.get(bank.getPathToMailbox());
		try {
			Files.copy(source, dest, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		(new File(pathToCheck)).delete();
	}
}
