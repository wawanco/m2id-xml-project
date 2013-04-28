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
	private int idBank;
	private double minAmount;
	private String currency;
	private ArrayList<Product> productList;
	private Map<Product, Integer> sumProductQuantity;

	public Company(int id, String name, int idBank, double minAmount,
			String currency, ArrayList<Product> productList) {

		this.id = id;
		this.name = name;
		this.idBank = idBank;
		this.minAmount = minAmount;
		this.currency = currency;
		this.productList = productList;

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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void checkOrder() {
		boolean validate = false;
		try {
			try {
				File fXmlFile = new File("/project/mailBox/check.xml");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();

				int sumAmount = 0;
				NodeList nList = doc.getElementsByTagName("product");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						// calculate sum Amount
						sumAmount += (Integer.parseInt(eElement
								.getElementsByTagName("unitPrice").item(0)
								.getTextContent()));
						String name = eElement.getElementsByTagName("name")
								.item(0).getTextContent();
						// calculate sum Quantity
						if (sumProductQuantity.containsKey(name)) {
							int o = sumProductQuantity.get(name);
							sumProductQuantity.remove(name);
							for (int j = 0; j < productList.size(); j++) {
								if (productList.get(j).toString() == name) {
									sumProductQuantity.put(productList.get(j),
											o + 1);
								}
							}

						}
					}
				}
				// test minAmount
				if (sumAmount >= this.minAmount) {
					NodeList n2List = doc.getElementsByTagName("currency");
					for (int temp = 0; temp < n2List.getLength(); temp++) {
						Node nNode = n2List.item(temp);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							// test currency
							if (eElement.getAttribute("type") == this.currency) {
								// test stock
								for (int i = 0; i < productList.size(); i++) {
									if (sumProductQuantity.get(productList.get(i)) <= productList.get(i).getStock()) {
										validate = true;
									}
								}
							}
						}
					}
				}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (validate = true) {
			sendCheck();
		}
	}

	public void sendCheck() {
		// fonction in order to send the check to the companie's bank after the
		// validation of order's content
		Path source = Paths.get("/project/mailBox/check.xml");
		Path dest = Paths.get("/project/BankMailBox/check.xml");
		try {
			Files.copy(source, dest, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File("/project/mailBox/check.xml");
		file.delete();
	}
}
