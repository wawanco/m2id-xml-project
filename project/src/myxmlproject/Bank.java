package myxmlproject;

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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Bank {
	//===========================
	// Static attributes
	//===========================
	private static String PATH_TO_BASE_XSD = "customer-base.xsd";
	private static String PATH_TO_RC_XSD   = "received-check.xsd";

	//===========================
	// Private attributes
	//===========================
	private int      id;
	private String   name;
	private String   pathToBase;
	private Document customerBase;
	private Document receivedChecks;
	private String   pathToMailbox;
	private String   pathToReceivedCheck; 

	//===========================
	// Constructors
	//===========================
	public Bank(int id, String name) {
		this.id    = id;
		this.name  = name;
		pathToMailbox = Bank.getPathToMailbox(id);
		File mb = new File(pathToMailbox);
		mb.mkdirs();
		pathToBase = "./customer-base_" + id + ".xml";
		pathToReceivedCheck = "./received-checks_" + id + ".xml";
		customerBase   = InitializeXML("customerList", PATH_TO_BASE_XSD, pathToBase);
		receivedChecks = InitializeXML("checkList"   , PATH_TO_RC_XSD  , pathToReceivedCheck);
	}

	//===========================
	// Static methods
	//===========================
	private static String getPathToMailbox(int id) {
		return "./Bank_" + id;
	}
	
	//===========================
	// Public methods
	//===========================
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

	// Others
	public Customer registerCustomer(String firstname, String name, double deposit) {
		Customer customer = new Customer(firstname, name, this, generateCustomerId(), deposit);
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

	public Customer retrieveCustomer(int idCustomer) {
		Element eCustomer = retrieveCustomerElement(idCustomer);
		return Customer.getInstanceFromElement(eCustomer, this);
	}

	public void sendCheckToPayer(){
		NodeList nlChecks = receivedChecks.getDocumentElement().getElementsByTagName("check");
		for(int i = 0; i < nlChecks.getLength(); i++) {
			Element eCheck = (Element) nlChecks.item(i);
			int idBank = Check.readBankId(eCheck);
			String pathToMailBox = Bank.getPathToMailbox(idBank);
			Document checkDoc = createDoc("check", Check.PATH_TO_XSD);
			NodeList newNodes = eCheck.getChildNodes();
			for(int j = 0; j < newNodes.getLength(); j++) {
				Node n = newNodes.item(j).cloneNode(true);
				checkDoc.adoptNode(n);
				checkDoc.getDocumentElement().appendChild(n);
			}
			writeDocument(checkDoc, pathToMailBox + "/check_from_" + id + "_" + i + ".xml");
			receivedChecks.getDocumentElement().removeChild(eCheck);
		}
		writeDocument(receivedChecks, pathToReceivedCheck);
	}
	
	public void cashCheck(String pathToCheck) {
		File f = new File(pathToCheck);
		Node n = parseFile(f).getDocumentElement().cloneNode(true);
		receivedChecks.adoptNode(n);
		receivedChecks.getDocumentElement().appendChild(n);
		writeDocument(receivedChecks, pathToReceivedCheck);
		f.delete();
	}
	
	public void processChecks() {
		File[] xmls = pickupMails();
		for(File f: xmls){
			Element root = parseFile(f).getDocumentElement();
			Check.Currency currency = Check.readCurrency  (root);
			double amount           = Check.readAmount    (root);
			int idCustomer          = Check.readCustomerId(root);
			int idCheck             = Check.readCheckId   (root);
			Date date               = Check.readDate      (root);
			updateCustomerNode(idCustomer, idCheck, amount, currency, date);
			f.delete();
		}
	}


	//===========================
	// Private methods
	//===========================
	private File[] pickupMails() {
		return (new File(pathToMailbox)).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.toLowerCase().endsWith(".xml");
			}
		});		
	}
	
	private void updateCustomerNode(int idCustomer, int idCheck, double amount, Check.Currency currency, Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		Element cust = (Element) retrieveCustomerElement(idCustomer);
		Element eBalance = (Element) cust.getElementsByTagName("balance").item(0);
		eBalance.setTextContent(Double.toString(
				Double.parseDouble(eBalance.getTextContent()) - amount
		));
		Element check = retrieveCheckElement(idCustomer, idCheck);
		check.getElementsByTagName("amount").item(0).setTextContent(Double.toString(amount));
		Element eDate;
		if(check.getElementsByTagName("date").getLength() == 0) {
			eDate = customerBase.createElement("date");
			check.appendChild(eDate);
		} else {
			eDate = (Element) check.getElementsByTagName("date").item(0);
		}
		eDate.setAttribute("day"  , "" + cal.get(Calendar.DAY_OF_MONTH));
		eDate.setAttribute("month", Check.getEnglishMonth(date));
		eDate.setAttribute("year" , "" + cal.get(Calendar.YEAR));
		writeDocument(customerBase, pathToBase);
	}
	
	private Document parseFile(File f) {
		Document doc = null;
		// Initialize parser
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature("http://apache.org/xml/features/validation/schema", true);
			dbf.setValidating(true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			doc = db.parse(f);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	private Document createDoc(String rootName, String pathToXSD) {
		Document doc = null;
		// Initialize parser
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature("http://apache.org/xml/features/validation/schema", true);
			dbf.setValidating(true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			Element root = doc.createElement(rootName);
			root.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "noNamespaceSchemaLocation", pathToXSD);
			doc.appendChild(root);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return doc;
	}

	private Document InitializeXML(String rootName, String pathToXSD, String path) {
		Document doc = null;
		File f = new File(path);
		if(f.isFile())
			doc = parseFile(f);
		else
			doc = createDoc(rootName, pathToXSD);
		writeDocument(doc, path);
		return doc;
	}

	private Element retrieveCustomerElement(int idCustomer) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			String expr = "/customerList/customer[idCustomer = '" + idCustomer + "']";
			return (Element) xpath.evaluate(expr, customerBase, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			return null;
		}
	}
	
	private Element retrieveCheckElement(int idCustomer, int idCheck) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			String expr = 
				"/customerList/customer[idCustomer = '" + idCustomer + "']/checkList/check[idCheck = '"+ idCheck + "']";
			return (Element) xpath.evaluate(expr, customerBase, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			return null;
		}
	}
	
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
		Element balance  = customerBase.createElement("balance" );
		balance.setTextContent(String.valueOf(c.getBalance()));
		identite.setAttribute("firstname", c.getFirstname());
		identite.setAttribute("name", c.getName());
		Element idCustomer = customerBase.createElement("idCustomer");
		Text texte = customerBase.createTextNode("" + c.getId());
		idCustomer.appendChild(texte);
		Element listCheck = customerBase.createElement("checkList");
		customer.appendChild(identite);
		customer.appendChild(idCustomer);
		customer.appendChild(listCheck);
		customer.appendChild(balance);
		customerBase.getDocumentElement().appendChild(customer);
		writeDocument(customerBase, pathToBase);
	}
	
	private void addCheckToBase(Customer customer, Check check) {
		// Create check element
		Node checkNode = check.createCheckNode();
		customerBase.adoptNode(checkNode);
		// Add to checkList
		Element eCustomer = retrieveCustomerElement(customer.getId());
		Element eCheckLst = (Element) eCustomer.getElementsByTagName("checkList").item(0);
		eCheckLst.appendChild(checkNode);
		// Overwrite base
		writeDocument(customerBase, pathToBase);
	}
	
	private void writeDocument(Document doc, String path) {
		try {
			// write the content into xml file
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));
			// Output to console for testing
			tFormer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/*
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
	 */
}
