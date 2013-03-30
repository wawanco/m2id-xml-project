package myxmlproject;

import java.util.ArrayList;
import java.util.List;

public class Company {

	private int id;
	private String name;
	private int idBank;
	private int min_amount;
	private String currency;
	private ArrayList<Product> productList;

	public Company(int id, String name, int idBank, int min_amount,
			String currency, ArrayList<Product> productList) {

		this.id = id;
		this.name = name;
		this.idBank = idBank;
		this.min_amount = min_amount;
		this.currency = currency;
		this.productList = productList;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Product> getProductList() {
		return productList;
	}

	public void setProductList(ArrayList<Product> productList) {
		this.productList = productList;
	}

	public int getMin_amount() {
		return min_amount;
	}

	public void setMin_amount(int min_amount) {
		this.min_amount = min_amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
