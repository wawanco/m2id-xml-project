package myxmlproject;

import java.util.ArrayList;
import java.util.Map;

public class Order {

	private int id;
	private int idCustomer;
	private ArrayList<Product> cart;
	private ArrayList<Integer> quantities;
	private String companyName;
	private int idCompany;
	private int idCompanyBank;
	private int orderSum = 0;

	public Order(int id, int idCustomer, int idCompany) {

		this.id = id;
		this.idCompany = idCompany;
		this.idCustomer = idCustomer;
	}

	public void addProduct(Product product, int quantity) {
		if (cart.contains(product)) {
			for (int i = 0; i < cart.size(); i++) {
				if (cart.get(i) == product) {
					this.quantities.set(i, this.quantities.get(i) + quantity);
				}
			}
		} else {
			for (int i = 0; i < cart.size(); i++) {
				if (cart.get(i).equals(null)) {
					cart.set(i, product);
					this.quantities.set(i, quantity);
				}
			}
		}
	}

	public void removeProduct(Product product) {
		for (int i = 0; i < cart.size(); i++) {
			if (cart.get(i).equals(product)) {
				cart.remove(i);
				quantities.remove(i);
			}
		}
	}

	public int calculateSum() {
		for (int i = 1; i < cart.size(); i++) {
			orderSum += quantities.get(i) * cart.get(i).getUnitPrice();
		}
		return orderSum;
	}
}
