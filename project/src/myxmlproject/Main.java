package myxmlproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {

		// Initializing
		Bank bank = new Bank();
		Customer customer = new Customer();
		BufferedReader stdinp = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Welcome. Are you a new customer? Yes/No");
		try {
			String input = stdinp.readLine();
			if (input.equals("Yes")) {
				System.out.println("Please, choose your bank." + "Type 1 for LCL"
						+ " Type 2 for BNP Paribas" + " Type 3 for Societe Gnerale");
				String input2 = stdinp.readLine();
				bank.setId(Integer.parseInt(input2));
				bank.setName(bank.getId());
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
		
		// 1. Initialiser les objets GdC ???
		// 2. print "Choix du Gdc?" ok
		// 3. scan ok
		// 4. print "Infos?" ok
		// 5. scan ok
		// 6. Remplir classe client : getter/setter ok
		// 7. Enregister le client chez le GdC
		// Attention gestion des clients existants avec login
	}
}
