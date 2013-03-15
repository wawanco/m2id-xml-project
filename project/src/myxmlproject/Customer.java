package myxmlproject;

import java.util.ArrayList;

import sun.util.calendar.BaseCalendar.Date;

public class Customer {
	// 1. Choisir le GdC (getter/setter idBank?)
	// 2. Envoyer les infos au GdC
	// 3. Modifier montant et date OK

	private int id;
	private int idBank;
	private String name;
	private String firstname;
	private ArrayList<Check> checkbook;

	public Customer(String firstname, String name, int idBank) {
		this.firstname = firstname;
		this.name = name;
		this.idBank = idBank;
	}
	
	// Public functions
	public void fillCheck(int amount, Date date) {
		// Customer will use this function to fill the amount and date of a check
		if (amount != 0 && date != null) {
			throw new Error("Invalid amount or date.");
		}
		checkbook.get(0).setAmount(amount);
		checkbook.get(0).setIssueDate(date);
	}
	
	// Utilities
	public String toString() {
		return "Customer name: " + name + "Customer surname: "
				+ firstname + "with customer id " + id + "Bank id"
				+ idBank;
	}

}
