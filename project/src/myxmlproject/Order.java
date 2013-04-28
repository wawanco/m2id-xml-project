package myxmlproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order {

	private int id;
	private int idCustomer;
	private HashMap<Product, Integer> cart;
	private String companyName;
	private int idCompany;
	private int idCompanyBank;
	private int orderSum = 0;

	public Order(int id, int idCustomer, int idCompany) {
		this.id = id;
		this.idCompany = idCompany;
		this.idCustomer = idCustomer;
		cart = new HashMap<Product, Integer>();
	}
	
	public int getIdCompany() {
		return idCompany;
	}

	public void addProduct(Product p, int quantity) {
		if(cart.containsKey(p)) {
			cart.put(p, cart.get(p) + quantity);
		} else {
			cart.put(p, quantity);			
		}
	}

	/*
	public void removeProduct(Product product) {
		for (int i = 0; i < cart.size(); i++) {
			if (cart.get(i).equals(product)) {
				cart.remove(i);
				quantities.remove(i);
			}
		}
	}
	*/

	public double calculateSum() {
		double sum = 0;
		for (Map.Entry<Product, Integer> e: cart.entrySet()) {
			sum += e.getKey().getUnitPrice() * e.getValue();
		}
		return sum;
	}
}
