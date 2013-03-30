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

public class Main {

	// banks
	private static HashMap<Integer, String> banks = new HashMap<Integer, String>();
	static {
		banks.put(0, "LCL");
		banks.put(1, "BNP");
		banks.put(2, "Société Générale");
	}

	private static ArrayList<Company> companies = new ArrayList<Company>();

	public static void main(String[] args) {

		// Initializing
		HashMap<Integer, Bank> bankObj = new HashMap<Integer, Bank>();
		companies = InitialiseCompanies();
		BufferedReader stdinp = new BufferedReader(new InputStreamReader(
				System.in));
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
				System.out
						.println("How much money do you want to deposit on your account (in Euros)?");
				int amount = Integer.parseInt(stdinp.readLine());
				Customer c = bankObj.get(idBank).registerCustomer(firstname,
						name, amount);
				System.out.println("Thank you. You have been registered.");
				System.out.println("How many checks do you want :");
				System.out.println("In Euros ?");
				int nbEuros = Integer.parseInt(stdinp.readLine());
				System.out.println("In Dollars ?");
				int nbDollars = Integer.parseInt(stdinp.readLine());
				bankObj.get(idBank).generateChecks(c, nbEuros, nbDollars);
				System.out
						.println("Your "
								+ (nbDollars + nbEuros)
								+ " check have been generated in your customer directory:");
				System.out.println("--> " + c.getDirectory());
				// Choisir entreprise-produit
				System.out
						.println("Please choose the company you wish from the list below: ");
				for (int i = 0; i <= companies.size(); i++) {
					System.out.println(i + ") " + "Type -" + i + "- for "
							+ companies.get(i).getName());
				}
				int idCompany = Integer.parseInt(stdinp.readLine());
				int selection = 3;
				while (selection == 3) {
					System.out
							.println("Plesae choose a product from the list below: ");
					for (int i = 0; i <= companies.get(idCompany)
							.getProductList().size(); i++) {
						System.out.println(i
								+ ") "
								+ "Type -"
								+ i
								+ "- if you want to buy"
								+ companies.get(idCompany).getProductList()
										.get(i).getName());
					}
					Product product = companies.get(idCompany).getProductList()
							.get(Integer.parseInt(stdinp.readLine()));
					System.out.println("The price of the product selected is "
							+ product.getUnitPrice() + " "
							+ companies.get(idCompany).getCurrency());
					System.out
							.println("Select the quantity that you wish to buy: ");
					int quantity = Integer.parseInt(stdinp.readLine());
					float totalPrice = quantity * product.getUnitPrice();
					System.out.println("The total price of your selection is "
							+ totalPrice + " "
							+ companies.get(idCompany).getCurrency());
					System.out
							.println("If you want to validate your command, please type 1. "
									+ "If you want to cancel your command, please type 2."
									+ "If you want to buy another product, please type 3");
					selection = Integer.parseInt(stdinp.readLine());
				}
			} else if (input.equals("No")) {
				System.out.println("Customer already exists");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 1. Initialiser les objets GdC ??? ok
		// 2. print "Choix du Gdc?" ok
		// 3. scan ok
		// 4. print "Infos?" ok
		// 5. scan ok
		// 6. Remplir classe client : getter/setter ok
		// 7. Enregister le client chez le GdC
		// Attention gestion des clients existants avec login
	}

	private static ArrayList<Company> InitialiseCompanies() {
		// products
		Product burger = new Product("burger", 50, 4f);
		Product fries = new Product("fries", 100, 1.5f);
		Product drink = new Product("drink", 100, 1f);

		Product tshirt = new Product("tshirt", 40, 15.99f);
		Product pants = new Product("pants", 50, 39.99f);
		Product shoes = new Product("shoes", 20, 34.99f);

		Product travel1 = new Product("Hawaii", 10, 2499f);
		Product travel2 = new Product("Barcelone", 8, 899f);
		Product travel3 = new Product("Athens", 5, 499f);
		// products List
		ArrayList<Product> productMc = new ArrayList<Product>();
		ArrayList<Product> productGap = new ArrayList<Product>();
		ArrayList<Product> productTA = new ArrayList<Product>();
		ArrayList<Product> productZara = new ArrayList<Product>();
		// companiesLists
		productMc.add(burger);
		productMc.add(fries);
		productMc.add(drink);
		productGap.add(tshirt);
		productGap.add(pants);
		productGap.add(shoes);
		productZara.add(tshirt);
		productZara.add(pants);
		productZara.add(shoes);
		productTA.add(travel1);
		productTA.add(travel2);
		productTA.add(travel3);

		// initialise Companies
		Company McDonalds = new Company(0, "McDonalds", 0, 0, "dollars",
				productMc);
		Company GAP = new Company(1, "GAP", 0, 20, "dollars", productGap);
		Company TravelAir = new Company(2, "TravelAir", 1, 40, "euros",
				productTA);
		Company ZARA = new Company(3, "ZARA", 2, 10, "euros", productZara);

		companies.add(McDonalds);
		companies.add(GAP);
		companies.add(TravelAir);
		companies.add(ZARA);

		return companies;
	}
}
