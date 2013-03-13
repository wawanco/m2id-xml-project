package myxmlproject;

import java.util.Date;


public class Check {
	public enum Currency { Dollars, Euros }

	private int id;
	private int idBank;
	private int idCustomer;
	private int amount;
	private Date issueDate;
	private Currency currency;
}
