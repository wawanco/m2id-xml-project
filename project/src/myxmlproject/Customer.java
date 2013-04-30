package myxmlproject;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	private int amount;

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
		this.name      = name;
		this.idBank    = idBank;
		this.id        = id;
		checkbook = new ArrayList<Check>();
		directory = "./Bank_" + idBank + "/Customer_" + id;
		File dir = new File(directory);
		dir.mkdirs();
	}
	
	public static Customer getInstanceFromNode(Node nCustomer, int idBank) {
		Element eIdentity   = (Element) ((Element) nCustomer).getElementsByTagName("identity"  ).item(0);
		Element eIdCustomer = (Element) ((Element) nCustomer).getElementsByTagName("idCustomer").item(0);
		Customer c = new Customer(
			eIdentity.getAttribute("firstname")
		,	eIdentity.getAttribute("name")
		,	idBank
		,	Integer.parseInt(eIdCustomer.getTextContent())
		);
		Node checkList  = ((Element) nCustomer).getElementsByTagName("checkList").item(0);
		NodeList checks = ((Element) checkList).getElementsByTagName("check");
		for(int i = 0; i < checks.getLength(); i++) {
			c.addToCheckBook(Check.getInstanceFromNode(checks.item(i), c));
		}
		return c;
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public String getDirectory() {
		return directory;
	}

	// Public methods
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
		return "Customer name: " + name + "Customer surname: " + firstname + "with customer id " + id + "Bank id" + idBank;
	}

}
