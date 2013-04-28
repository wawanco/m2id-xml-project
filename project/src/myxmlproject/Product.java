package myxmlproject;

public class Product {

	private int stock;
	private double unitPrice;
	private String name;

	public Product(String name, int stock, double unitPrice) {
		this.stock = stock;
		this.name = name;
		this.unitPrice = unitPrice;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Product clone() {
		return new Product(name, stock, unitPrice);
	}

}
