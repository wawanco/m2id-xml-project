package myxmlproject;

import java.util.ArrayList;
import java.util.List;

public class Company {

	private int id;
	private String name;
	private int idBank;
	private double minAmount;
	private String currency;
	private ArrayList<Product> productList;

	public Company(int id, String name, int idBank, double minAmount,
			String currency, ArrayList<Product> productList) {

		this.id = id;
		this.name = name;
		this.idBank = idBank;
		this.minAmount = minAmount;
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

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(int min_amount) {
		this.minAmount = minAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
