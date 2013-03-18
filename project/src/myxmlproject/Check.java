package myxmlproject;

import sun.util.calendar.BaseCalendar.Date;

public class Check {
	public enum Currency {
		Dollars, Euros
	}

	private int 	 id;
	private int      idBank;
	private int      idCustomer;
	private int      amount;
	private Date     issueDate;
	private Currency currency;
	
	// Constructor
	public Check(int id, int idBank, int idCustomer, Currency currency){
		this.id         = id;
		this.idBank     = idBank;
		this.idCustomer = idCustomer;
		this.currency   = currency;
	}
	
	// Getters and setters
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	// Public methods
	public void createXml(String path){
		
	}
}
