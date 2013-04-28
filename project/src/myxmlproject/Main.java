//*** TODO ***
/// Comment setter l'id du customer
/// Est-ce que la classe check est necessaire

package myxmlproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sun.util.calendar.BaseCalendar;
import sun.util.calendar.BaseCalendar.*;

public class Main {
	
	static class CompanyInitializer extends DefaultHandler {
		private String vCurrent, bCurrent;
		private String cName, pName, currency;
		private int stock;
		private double price, minAmount;
		private ArrayList<Product> pList;
		private ArrayList<Company> cList;
		static int i = 0;
		static int idBank = 0;
		
        @Override
		public void startDocument() throws SAXException {
        	pList = new ArrayList<Product>();
        	cList = new ArrayList<Company>();
		}
		
        @Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
			if(qName.equals("product") || qName.equals("company"))
				bCurrent = qName;
		}
			
		@Override
		public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
			vCurrent = new String(arg0, arg1, arg2);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if(qName.equals("name")){
				if(bCurrent.equals("company"))
					cName = vCurrent;
				if(bCurrent.equals("product"))
					pName = vCurrent;
			}
			if(qName.equals("stock"))
				stock = Integer.parseInt(vCurrent);
			if(qName.equals("price"))
				price = Double.parseDouble(vCurrent);
			if(qName.equals("min_amount"))
				minAmount = Double.parseDouble(vCurrent);
			if(qName.equals("currency"))
				currency = vCurrent;
			if(qName.equals("product")) {
				pList.add(new Product(pName, stock, price));
			}
			if(qName.equals("company")) {
				ArrayList<Product> myProductList = new ArrayList<Product>();
				for(Product p: pList) myProductList.add(p.clone());
				cList.add(new Company(i++, cName, idBank, minAmount, currency, myProductList));
				pList.clear();
			}
		}

		public ArrayList<Company> getcList() {
			return cList;
		}
	}
	
	private static Customer getInformationFromExistingCustomer() {
		try {
			BufferedReader stdinp = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("To which bank have been registered?");
			for(int i = 0; i < bankObj.length; i++) {
				System.out.println("Type " + i + " for " + bankObj[i].getName() + ".");
			}
			int idBank = Integer.parseInt(stdinp.readLine());
			System.out.println("What is your identification number?");
			int id = Integer.parseInt(stdinp.readLine());
			Customer customer = bankObj[idBank].retrieveCustomer(id);
			while(customer == null){
				System.out.println("Your id has not been found, try it again..");
				id = Integer.parseInt(stdinp.readLine());
				customer = bankObj[idBank].retrieveCustomer(id);
			}
			System.out.println("Hello " + customer.getFirstname() + " " + customer.getName() + "!");
			return customer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Customer getInformationFromNewCustomer() {
		try {
			BufferedReader stdinp = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Please, choose your bank.");
			for(int i = 0; i < bankObj.length; i++) {
				System.out.println("Type " + i + " for " + bankObj[i].getName() + ".");
			}
			int idBank = Integer.parseInt(stdinp.readLine());
			System.out.println("Please type your name.");
			String name = stdinp.readLine();
			System.out.println("Please type your firstname.");
			String firstname = stdinp.readLine();
			System.out.println("How much money do you want to deposit on your account (in Euros)?");
			int amount = Integer.parseInt(stdinp.readLine());
			Customer newCustomer = bankObj[idBank].registerCustomer(firstname, name, amount);
			System.out.println("Thank you. You have been registered.");
			System.out.println("How many checks do you want :");
			System.out.println("In Euros ?");
			int nbEuros = Integer.parseInt(stdinp.readLine());
			System.out.println("In Dollars ?");
			int nbDollars = Integer.parseInt(stdinp.readLine());
			bankObj[idBank].generateChecks(newCustomer, nbEuros, nbDollars);
			System.out.println("Your " + (nbDollars + nbEuros) + " check have been generated in your customer directory:");
			System.out.println("--> " + newCustomer.getDirectory());
			return newCustomer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// banks
	//private static HashMap<Integer, String> banks = new HashMap<Integer, String>();
	private static final String[] banks   = {"LCL", "BNP", "Société Générale"};
	private static Bank[] bankObj = new Bank[banks.length];

	private static ArrayList<Company> companies;

	public static void main(String[] args) {

		// Initializing
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			spf.setFeature("http://xml.org/sax/features/validation", true);
			spf.setValidating(true);
			spf.setNamespaceAware(true);
			javax.xml.parsers.SAXParser sp;
			sp = spf.newSAXParser();
			CompanyInitializer ci = new CompanyInitializer();
			sp.parse("companies.xml", ci);
			companies = ci.getcList();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader stdinp = new BufferedReader(new InputStreamReader(System.in));
		for(int i = 0; i < banks.length; i++) {
			bankObj[i] = new Bank(i, banks[i]);
		}

		System.out.println("Welcome. Are you a new customer? Yes/No");
		try {
			String input = stdinp.readLine();
			Customer customer = null;
			if (input.substring(0,1).equalsIgnoreCase("y")) {
				customer = getInformationFromNewCustomer();
			} else {
				customer = getInformationFromExistingCustomer();
			}
				// Choisir entreprise-produit
				System.out.println("Please choose the company you wish from the list below: ");
				for (int i = 0; i < companies.size(); i++) {
					System.out.println(i + ") " + "Type -" + i + "- for " + companies.get(i).getName());
				}
				int idCompany = Integer.parseInt(stdinp.readLine());
				int selection = 3;
				Order order = new Order(1,1,idCompany);
				while (selection == 3) {
					Company chosenCpny = companies.get(idCompany);
					System.out.println("Please choose a product from the list below: ");
					for (int i = 0; i < chosenCpny.getProductList().size(); i++) {
						System.out.println(i + ") "	+ "Type -" + i + "- if you want to buy " + chosenCpny.getProductList().get(i).getName());
					}
					Product product = chosenCpny.getProductList().get(Integer.parseInt(stdinp.readLine()));
					System.out.println("The price of the product selected is "
							+ product.getUnitPrice() + " "
							+ companies.get(idCompany).getCurrency());
					System.out.println("Select the quantity that you wish to buy: ");
					int quantity = Integer.parseInt(stdinp.readLine());
					double totalPrice = quantity * product.getUnitPrice();
					System.out.println("The total price of your selection is "
							+ totalPrice + " "
							+ companies.get(idCompany).getCurrency());
					System.out.println("If you want to validate your command, please type 1.");
					System.out.println("If you want to cancel your command, please type 2.");
					System.out.println("If you want to buy another product, please type 3");
					selection = Integer.parseInt(stdinp.readLine());
					if (selection == 1){
						order.addProduct(product, quantity);
					}	
				}
				System.out.println("The total of your order is : " + order.calculateSum() + " euros.");
				System.out.println("If you want to validate your command and write a check, please type 1. If you want to cancel your command, please type 2. ");
				selection = Integer.parseInt(stdinp.readLine());
				if (selection == 1){
					
					BaseCalendar.Date date;
						try {
							date = BaseCalendar.Date.class.newInstance();
							date.setDate(2013, 4, 28);
							customer.fillCheck(order.calculateSum(),date);
						} 	catch (TransformerConfigurationException e) {
							e.printStackTrace();
						}	catch (InstantiationException e) {
							e.printStackTrace();
						} 	catch (IllegalAccessException e) {
							e.printStackTrace();
						}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
