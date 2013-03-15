package myxmlproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {

		Bank bank = new Bank(); // initialisation d'un objet Bank
		Customer customer = new Customer(); // objet Client

		// printf/scanf pour les infos du client
		System.out.println("Welcome. Are you a new customer? Yes/No");
		BufferedReader stdinp = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			String input = stdinp.readLine();
			while ((input.equals("Yes") == false)
					&& (input.equals("No") == false)) {
				System.out
						.println("Invalid answer. Are you a new customer? Yes/No");
				stdinp = new BufferedReader(new InputStreamReader(System.in));
				input = stdinp.readLine();
			}
			if (input.equals("Yes")) {
				System.out.println("Please, choose your bank."
						+ "Type 1 for LCL" + "Type 2 for BNP Paribas"
						+ "Type 3 for Societe Gnerale");
				stdinp = new BufferedReader(new InputStreamReader(System.in));
				String input2 = stdinp.readLine();
				bank.setId(Integer.parseInt(input2));
				bank.setName(bank.getId());
				System.out.println("Please type your name.");
				stdinp = new BufferedReader(new InputStreamReader(System.in));
				customer.setName(stdinp.readLine());
				System.out.println("Please type your firstname.");
				stdinp = new BufferedReader(new InputStreamReader(System.in));
				customer.setFirstname(stdinp.readLine());
				System.out.println("Thank you. You have been registered.");
			} else if (input.equals("No")) {
				System.out.println("Customer already exists"); // La on va
																// mettre
																// qqchose
																// d'autre...
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// while (System.in.toString() != "Yes" && System.in.toString() !=
		// "No"){
		// System.out.println("Invalid answer. Are you a new customer? Yes/No");
		// }

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
