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

	// getter/setter OK
	// printer ???

	public boolean fillCheck(int aumount) {
		return true;
	}

	public boolean setName(String name) {
		if (name != null) {
			this.name = name;
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return this.name;
	}

	public boolean setFirstname(String firstname) {
		if (firstname != null) {
			this.firstname = firstname;
			return true;
		} else {
			return false;
		}
	}

	public String getFirstname() {
		return this.firstname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdBank() {
		return idBank;
	}

	public void setIdBank(int idBank) {
		this.idBank = idBank;
	}

	public ArrayList<Check> getCheckbook() {
		return checkbook;
	}

	public void setCheckbook(ArrayList<Check> checkbook) {
		this.checkbook = checkbook;
	}

	public String printCustomer() {
		return "Customer name: " + this.name + "Customer surname: "
				+ this.firstname + "with customer id " + this.id + "Bank id"
				+ this.idBank;
	}

	public boolean createCheck(int amount, Date date) {
		if (amount != 0 && date != null) {
			checkbook.get(0).setAmount(amount);
			checkbook.get(0).setIssueDate(date);
			return true;
		} else {
			return false;
		}
	}

}
