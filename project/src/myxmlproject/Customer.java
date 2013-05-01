package myxmlproject;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Customer {
	//===========================
	// Private attributes
	//===========================
	private int                id;
	private Bank               bank;
	private String             name;
	private String             firstname;
	private String             directory;
	private ArrayList<Check>   checkbook;
	private double             balance;

	//===========================
	// Constructors
	//===========================
	public Customer(String firstname, String name, Bank bank, int id, double balance) {
		this.firstname = firstname;
		this.name      = name;
		this.bank      = bank;
		this.id        = id;
		this.balance   = balance;
		checkbook = new ArrayList<Check>();
		directory = bank.getPathToMailbox() + "/Customer_" + id;
		File dir  = new File(directory);
		dir.mkdirs();
		Path pathToXSD = Paths.get(Check.PATH_TO_XSD);
		try {
			Files.copy(
				pathToXSD
			, 	Paths.get(directory + "/" + pathToXSD.getFileName())
			, 	StandardCopyOption.REPLACE_EXISTING
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Customer getInstanceFromElement(Element eCustomer, Bank bank) {
		Element eIdentity   = (Element) eCustomer.getElementsByTagName("identity"  ).item(0);
		Element eIdCustomer = (Element) eCustomer.getElementsByTagName("idCustomer").item(0);
		Element eBalance    = (Element) eCustomer.getElementsByTagName("balance"   ).item(0);
		Customer c = new Customer(
			eIdentity.getAttribute("firstname")
		,	eIdentity.getAttribute("name")
		,	bank
		,	Integer.parseInt(eIdCustomer.getTextContent())
		,	Double.parseDouble(eBalance.getTextContent())
		);
		Node checkList  = eCustomer.getElementsByTagName("checkList").item(0);
		NodeList checks = ((Element) checkList).getElementsByTagName("check");
		for(int i = 0; i < checks.getLength(); i++) {
			c.addToCheckBook(Check.getInstanceFromElement((Element) checks.item(i), c));
		}
		return c;
	}

	//===========================
	// Public methods
	//===========================
	// Getters and setters
	public double getBalance() {
		return balance;
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

	public int getId() {
		return id;
	}

	public String getDirectory() {
		return directory;
	}

	// Other public methods
	public void addToCheckBook(Check check) {
		checkbook.add(check);
	}
	
	public int fillCheck(double amount, Date date, Check.Currency currency) {
		int i = 0;
		Check check;
		while(i < checkbook.size()) {
			check = checkbook.get(i++);
			if(currency.equals(check.getCurrency())){
				check.fillCheck(amount, date);
				return check.getId();
			}
		}
		System.out.println("There is no more check in " + currency + ".");
		return -1;
	}
	
	public String sendCheck(int idCheck, Company c) throws IOException {
		int i = 0;
		while(i < checkbook.size()) {
			Check check = checkbook.get(i);
			if(check.getId() == idCheck && check.isFilled()){
				String dest = c.getPathToMailbox() + "/Customer_" + id + "_Check_" + check.getId() + ".xml";
				Files.copy(
					Paths.get(check.getPathToXml())
				,	Paths.get(dest)
				, 	REPLACE_EXISTING
				);
				checkbook.remove(i);
				check.delete();
				return dest;
			}
			i++;
		}
		return null;
	}

	public String toString() {
		return "Customer name: " + name + "Customer surname: " + firstname + "with customer id " + id + "Bank id" + bank.getId();
	}
}
