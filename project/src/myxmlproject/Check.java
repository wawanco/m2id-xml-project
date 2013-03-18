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
	
	public Check(int id, int idBank, int idCustomer, Currency currency){
		this.id         = id;
		this.idBank     = idBank;
		this.idCustomer = idCustomer;
		this.currency   = currency;
	}
	
	public void createXml(String path){
		
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/*
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

	public int getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	public int getAmount() {
		return amount;
	}


	public Date getIssueDate() {
		return issueDate;
	}


	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	*/
}
