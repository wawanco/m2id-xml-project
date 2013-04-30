package myxmlproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
				cList.add(new Company(i++, cName, bankObj[idBank], minAmount, Check.Currency.valueOf(currency), myProductList));
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

	private static int promptOptions(){
		try {
			BufferedReader stdinp = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Select on of the following options:");
			System.out.println("\tType 1 if want to buy items");
			System.out.println("\tType 2 if want to see your shopping cart");
			System.out.println("\tType 3 if want to exit without buying");
			return Integer.parseInt(stdinp.readLine());
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private static final String[] banks   = {"LCL", "BNP", "Société Générale"};
	private static Bank[] bankObj = new Bank[banks.length];
	private static ArrayList<Company> companies;

	public static void main(String[] args) {
		// Initializing
		for(int i = 0; i < banks.length; i++) {
			bankObj[i] = new Bank(i, banks[i]);
		}
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

		System.out.println("Welcome. Are you a new customer? (Yes/No)");
		try {
			String input = stdinp.readLine();
			Customer customer = null;
			if (input.substring(0,1).equalsIgnoreCase("y")) {
				customer = getInformationFromNewCustomer();
			} else {
				customer = getInformationFromExistingCustomer();
			}
			int selection = promptOptions();
			boolean goShopping = true;
			ArrayList<Order> myOrders = new ArrayList<Order>();
			while(goShopping){
				switch(selection) {
				case 1:
					// Choisir entreprise et produit
					System.out.println("Choose the company you wish from the list below: ");
					for (int i = 0; i < companies.size(); i++) {
						System.out.println("\tType " + i + " for " + companies.get(i).getName());
					}
					int idCompany = Integer.parseInt(stdinp.readLine());
					Company chosenCpny = companies.get(idCompany);
					System.out.println("Please choose a product from the list below: ");
					for (int i = 0; i < chosenCpny.getProductList().size(); i++) {
						System.out.println("\tType " + i + " if you want to buy " + chosenCpny.getProductList().get(i).getName());
					}
					Product product = chosenCpny.getProductList().get(Integer.parseInt(stdinp.readLine()));
					System.out.println("The price of the product selected is " + product.getUnitPrice() + " " + chosenCpny.getCurrency());
					System.out.println("Select the quantity that you wish to buy: ");
					int quantity = Integer.parseInt(stdinp.readLine());
					boolean isOrderAdded = false;
					for(Order o: myOrders){
						if(o.getIdCompany() == idCompany) {
							o.addProduct(product, quantity);
							isOrderAdded = true;
						}
					}
					if(!isOrderAdded){
						Order newOrder = new Order(0, customer.getId(), idCompany, chosenCpny.getCurrency());
						newOrder.addProduct(product, quantity);
						myOrders.add(newOrder);
					}
					selection = promptOptions();
					break;
				case 2:
					System.out.println("Your shopping cart contains " + myOrders.size() + " orders.");
					for(Order o: myOrders) {
						Company c = companies.get(o.getIdCompany());
						System.out.println("\tAt " + c.getName() + ": " + o.calculateSum() + " " + c.getCurrency());
					}
					System.out.println("Do you want to purchase the content of your shopping cart ? (Yes/No)");
					if (stdinp.readLine().substring(0,1).equalsIgnoreCase("y")) {
						goShopping = false;
						for(Order o: myOrders) {
							Company myCompany = companies.get(o.getIdCompany());
							Date date = new Date();
							int idCheck = customer.fillCheck(o.calculateSum(), date, myCompany.getCurrency());
							if(idCheck == -1) {
								System.out.println("Your order for " + myCompany.getName() + " cannot be completed");
							} else {
								String pathToCheck = customer.sendCheck(idCheck, myCompany);
								o.sendOrder(myCompany.getPathToMailbox());
								if(myCompany.checkOrder(myCompany.getPathToMailbox() + "/order.xml")){
									System.out.println("Your order for " + myCompany.getName() + " is completed");
									myCompany.sendCheck(pathToCheck);
								}
							}
						}
					} else {
						selection = promptOptions();
					}
					break;
				case 3:
					goShopping = false;
					System.exit(0);
					break;
				default:
					System.out.println("Unknown command, try again.");
					selection = promptOptions();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
