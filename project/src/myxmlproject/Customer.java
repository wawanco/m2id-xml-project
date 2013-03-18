package myxmlproject;

import java.io.File;
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
	private int amount;
	private String directory;
	private ArrayList<Check> checkbook;

	// Constructor
	public Customer(String firstname, String name, int idBank, int id, int amount) {
		this.firstname = firstname;
		this.name = name;
		this.idBank = idBank;
		this.id = id;
		this.amount = amount;
		directory = "./Customer_" + id;
		File dir = new File(directory);
		dir.mkdir();
	}
	
	// Getters and setters
	public int getId() {
		return id;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	// Public methods
	public void fillCheck(int amount, Date date) {
		// Customer will use this function to fill the amount and date of a check
		if (amount != 0 && date != null) {
			throw new Error("Invalid amount or date.");
		}
		checkbook.get(0).setAmount(amount);
		checkbook.get(0).setIssueDate(date);
	}
	
	public String toString() {
		return "Customer name: " + name + "Customer surname: "
				+ firstname + "with customer id " + id + "Bank id"
				+ idBank;
	}

}
