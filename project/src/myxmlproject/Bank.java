package myxmlproject;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import javax.xml.XMLConstants;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bank {
	private static String PATH_TO_XSD = "customer-base.xsd";

	private int      id;
	private String   name;
	private String   pathToBase;
	private Document customerBase;
	private String   pathToMailbox;

	// Construtor
	public Bank(int id, String name) {
		this.id    = id;
		this.name  = name;
		pathToMailbox = "./Bank_" + id;
		File mb = new File(pathToMailbox);
		mb.mkdirs();
		pathToBase    = "./customer-base_" + id + ".xml";
		File f = new File(pathToBase);
		// Initialize parser
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature("http://apache.org/xml/features/validation/schema", true);
			dbf.setValidating(true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			if (!f.isFile()) {
				customerBase = db.newDocument();
				Element root = customerBase.createElement("customerList");
				root.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "noNamespaceSchemaLocation", PATH_TO_XSD);
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
	
	public String getPathToMailbox() {
		return pathToMailbox;
	}

	// Public methods
	public Customer registerCustomer(String firstname, String name, int deposit) {
		// TODO gerer le deposit
		Customer customer = new Customer(firstname, name, this, generateCustomerId());
		addCustomerToBase(customer);
		return customer;
	}

	public void generateChecks(Customer c, int nbEuros, int nbDollars) {
		int checkId = 1;
		for (int i = 0; i < nbEuros; i++) {
			Check check = new Check(checkId++, id, c, Check.Currency.euros);
			check.writeXml();
			c.addToCheckBook(check);
			addCheckToBase(c, check);
		}

		for (int i = 0; i < nbDollars; i++) {
			Check check = new Check(checkId++, id, c, Check.Currency.dollars);
			check.writeXml();
			c.addToCheckBook(check);
			addCheckToBase(c, check);
		}
	}

	private Node retrieveCustomerNode(int idCustomer) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			String expr = "/customerList/customer[idCustomer = '" + idCustomer + "']";
			return (Node) xpath.evaluate(expr, customerBase, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			return null;
		}
	}
	
	public Customer retrieveCustomer(int idCustomer) {
		Node nCustomer = retrieveCustomerNode(idCustomer);
		return Customer.getInstanceFromNode(nCustomer, this);
	}

	// Private methods
	private int generateCustomerId() {
		// Cherche le dernier client dans la base et incremente l'id.
		XPath xpath = XPathFactory.newInstance().newXPath();
		Double dId = null;
		try {
			dId = (Double) xpath.evaluate("/customerList/customer[last()]/idCustomer/text()", customerBase, XPathConstants.NUMBER);
			if (dId.isNaN()) dId = 0.0;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		int id = dId.intValue();
		return ++id;
	}

	private void addCustomerToBase(Customer c) {
		Element customer = customerBase.createElement("customer");
		Element identite = customerBase.createElement("identity");
		identite.setAttribute("firstname", c.getFirstname());
		identite.setAttribute("name", c.getName());
		Element idCustomer = customerBase.createElement("idCustomer");
		Text texte = customerBase.createTextNode("" + c.getId());
		idCustomer.appendChild(texte);
		Element listCheck = customerBase.createElement("checkList");
		customer.appendChild(identite);
		customer.appendChild(idCustomer);
		customer.appendChild(listCheck);
		customerBase.getDocumentElement().appendChild(customer);
		writeXML();
	}
	
	private void addCheckToBase(Customer customer, Check check) {
		// Create check element
		Node checkNode = check.createCheckNode();
		customerBase.adoptNode(checkNode);
		// Add to checkList
		Element eCustomer = (Element) retrieveCustomerNode(customer.getId());
		Element eCheckLst = (Element) eCustomer.getElementsByTagName("checkList").item(0);
		eCheckLst.appendChild(checkNode);
		// Overwrite base
		writeXML();
	}
	
	public void writeXML() {
		try {
			// write the content into xml file
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
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

	public void demandChecks() {
		File fXmlFile = new File("/project/BankMailBox/check.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("check");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					int idBank = Integer.parseInt(eElement
							.getElementsByTagName("idBank").item(0)
							.getTextContent());
					Path source = Paths.get("/project/BankMailBox/check.xml");
					// if idBank==0
					Path dest = Paths.get("/project/MailBoxLCL/check.xml");
					if (idBank == 1) {
						dest = Paths.get("/project/MailBoxBNP/check.xml");
					} else if (idBank == 2) {
						dest = Paths.get("/project/MailBoxSG/check.xml");
					}
					Files.copy(source, dest, REPLACE_EXISTING);
					File file = new File("/project/BankMailBox/check.xml");
					file.delete();
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
	}

	public void manageChecks() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			// if this.id == 1
			Document doc = db.parse("/project/MailBoxLCL/check.xml");
			if (this.id == 2) {
				doc = db.parse("/project/MailBoxBNP/check.xml");
			} else if (this.id == 3) {
				doc = db.parse("/project/MailBoxSG/check.xml");
			}
			NodeList amountList = doc.getElementsByTagName("amount");
			NodeList dateList = doc.getDocumentElement().getElementsByTagName("date");
			NodeList bankList = doc.getDocumentElement().getElementsByTagName("idBank");
			NodeList idList = doc.getElementsByTagName("idCustomer");
			Element idElement = (Element) idList.item(0);
			int idCustomer = Integer.parseInt(idElement
					.getElementsByTagName("idCustomer").item(0)
					.getTextContent());
			Customer customer = retrieveCustomer(idCustomer);
			// test if amount et date exist
			if (amountList != null && dateList != null) {
				Element amElement = (Element) amountList.item(0);
				int amount = Integer.parseInt(amElement
						.getElementsByTagName("amount").item(0)
						.getTextContent());
				manageAmount(customer, amount);
			} else {
				// throw warning - send text to the bank (demandeur)

				// if this.id == 1;
				FileWriter fstream = new FileWriter("/project/MailBoxLCL/WrongCheck.txt");
				Path source = Paths.get("/project/MailBoxLCL/WrongCheck.txt");
				if (this.id == 2) {
					fstream = new FileWriter("/project/MailBoxBNP/WrongCheck.txt");
					source = Paths.get("/project/MailBoxBNP/WrongCheck.txt");
				} else if (this.id == 3) {
					fstream = new FileWriter("/project/MailBoxSG/WrongCheck.txt");
					source = Paths.get("/project/MailBoxSG/WrongCheck.txt");
				}
				BufferedWriter out = new BufferedWriter(fstream);
				out.write("The check for the client with id"
						+ idCustomer
						+ "that you sent has missing or wrong data. Please contact your client and send us a new one. ");
				out.close();
				// bank demandeur
				Element bankElement = (Element) bankList.item(0);
				int idBank = Integer.parseInt(bankElement.getElementsByTagName("idBank").item(0)
						.getTextContent());
				Path dest = Paths.get("/project/MailBoxLCL/WrongCheck.txt");
				if (idBank == 2) {
					dest = Paths.get("/project/MailBoxBNP/WrongCheck.txt");
				} else if (idBank == 3) {
					dest = Paths.get("/project/MailBoxSG/WrongCheck.txt");
				}
				Files.copy(source, dest, REPLACE_EXISTING);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void manageAmount(Customer customer, int amount) {
		if (customer.getAmount() > amount) {
			customer.setAmount(customer.getAmount() - amount);
		} else {
			// throw warning - send text to client
			warnClient(customer, amount);
		}
	}

	public void warnClient(Customer customer, int amount) {
		// throw warning - send text to client
		// if this.id == 1;
		FileWriter fstream;
		try {
			fstream = new FileWriter("/project/MailBoxLCL/AccountWarning.txt");
			Path source = Paths.get("/project/MailBoxLCL/AccountWarning.txt");
			if (this.id == 2) {
				fstream = new FileWriter("/project/MailBoxBNP/AccountWarning.txt");
				source = Paths.get("/project/MailBoxBNP/AccountWarning.txt");
			} else if (this.id == 3) {
				fstream = new FileWriter("/project/MailBoxSG/AccountWarning.txt");
				source = Paths.get("/project/MailBoxSG/AccountWarning.txt");
			}
				BufferedWriter out = new BufferedWriter(fstream);
				out.write("Dear client "
						+ customer.getName()
						+ " . We would like to inform you that your latest demand for a check of "
						+ amount
						+ " euros/dollars has been declined, as you have only "
						+ customer.getAmount()
						+ " euros/dollars left in your bank account. Please deposit money before you do another check deposit. ");
				out.close();
				Path dest = Paths.get("/project/Customer_0/AccountWarning.txt");
				Files.copy(source, dest, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
