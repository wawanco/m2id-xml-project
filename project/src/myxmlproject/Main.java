package myxmlproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {

		Bank bank = new Bank();
		Customer customer = new Customer();

		System.out.println("Welcome. Are you a new customer? Yes/No");
		BufferedReader stdinp = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			String input = stdinp.readLine();
			//while (input != "Yes" && input != "No") {
			//	System.out
			//			.println("Invalid answer. Are you a new customer? Yes/No");
			//	stdinp = new BufferedReader(new InputStreamReader(System.in));
			//}
			
			if (input.equals("Yes")) {
				System.out.println("Please, choose your bank." + "Type 1 for LCL"
						+ "Type 2 for BNP Paribas" + "Type 3 for Societe Gnerale");
				BufferedReader stdinp2 = new BufferedReader(new InputStreamReader(
						System.in));
				String input2 = stdinp2.readLine();
				bank.setId(Integer.parseInt(input2));
				bank.setName(bank.getId());
				System.out.println("Please type your name.");
				BufferedReader stdinp3 = new BufferedReader(new InputStreamReader(
						System.in));
				customer.setName(stdinp3.readLine());
				System.out.println("Please type your firstname.");
				BufferedReader stdinp4 = new BufferedReader(new InputStreamReader(
						System.in));
				customer.setFirstname(stdinp4.readLine());
				System.out.println("Thank you. You have been registered.");
			} else if (input.equals("No")) {
				System.out.println("Customer already exists");
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
