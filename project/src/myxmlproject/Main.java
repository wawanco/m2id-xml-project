package myxmlproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {
	private static HashMap<Integer, String> banks = new HashMap<Integer, String>();
	static {
		banks.put(0, "LCL");
		banks.put(1, "BNP");
		banks.put(2, "Société Générale");
	}
	
	public static void main(String[] args) {

		// Initializing
		HashMap<Integer, Bank> bankObj = new HashMap<Integer, Bank>(); 
		Customer customer = new Customer();
		BufferedReader stdinp = new BufferedReader(new InputStreamReader(System.in));
		Iterator it = banks.entrySet().iterator();
		while(it.hasNext()) {
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
				while(it.hasNext()) {
					Map.Entry me = (Map.Entry) it.next();
					int id = ((Bank) me.getValue()).getId();
					String name = ((Bank) me.getValue()).getName();
					System.out.println("Type " +  id + " for " + name + ".");
				}
				String input2 = stdinp.readLine();
				customer.setIdBank(Integer.parseInt(input2));
				System.out.println("Please type your name.");
				customer.setName(stdinp.readLine());
				System.out.println("Please type your firstname.");
				customer.setFirstname(stdinp.readLine());
				System.out.println("Thank you. You have been registered.");
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
}
