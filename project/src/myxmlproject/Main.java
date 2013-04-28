//*** TODO ***
/// Comment setter l'id du customer
/// Est-ce que la classe check est necessaire

package myxmlproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
				cList.add(new Company(i++, cName, idBank, minAmount, currency, myProductList));
				pList.clear();
			}
		}

		public ArrayList<Company> getcList() {
			return cList;
		}
	}
	


	// banks
	private static HashMap<Integer, String> banks = new HashMap<Integer, String>();
	static {
		banks.put(0, "LCL");
		banks.put(1, "BNP");
		banks.put(2, "Société Générale");
	}

	private static ArrayList<Company> companies;

	public static void main(String[] args) {

		// Initializing
		HashMap<Integer, Bank> bankObj = new HashMap<Integer, Bank>();
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
		Iterator it = banks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			int id = (Integer) me.getKey();
			String name = (String) me.getValue();
			bankObj.put(id, new Bank(id, name));
		}

		System.out.println("Welcome. Are you a new customer? Yes/No");
		try {
			String input = stdinp.readLine();
			if (input.equals("Yes")) {
				System.out.println("Please, choose your bank.");
				it = bankObj.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry me = (Map.Entry) it.next();
					int id = ((Bank) me.getValue()).getId();
					String name = ((Bank) me.getValue()).getName();
					System.out.println("Type " + id + " for " + name + ".");
				}
				int idBank = Integer.parseInt(stdinp.readLine());
				System.out.println("Please type your name.");
				String name = stdinp.readLine();
				System.out.println("Please type your firstname.");
				String firstname = stdinp.readLine();
				System.out.println("How much money do you want to deposit on your account (in Euros)?");
				int amount = Integer.parseInt(stdinp.readLine());
				Customer c = bankObj.get(idBank).registerCustomer(firstname, name, amount);
				System.out.println("Thank you. You have been registered.");
				System.out.println("How many checks do you want :");
				System.out.println("In Euros ?");
				int nbEuros = Integer.parseInt(stdinp.readLine());
				System.out.println("In Dollars ?");
				int nbDollars = Integer.parseInt(stdinp.readLine());
				bankObj.get(idBank).generateChecks(c, nbEuros, nbDollars);
				System.out.println("Your " + (nbDollars + nbEuros) + " check have been generated in your customer directory:");
				System.out.println("--> " + c.getDirectory());
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
					System.out.println("If you want to validate your command, please type 1. "
									+ "If you want to cancel your command, please type 2."
									+ "If you want to buy another product, please type 3");
					selection = Integer.parseInt(stdinp.readLine());
					order.addProduct(product, quantity);
				}
				System.out.println("The total of your order is :" + order.calculateSum());
			} else if (input.equals("No")) {
				System.out.println("Customer already exists");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
