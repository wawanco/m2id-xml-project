package myxmlproject;

public class Product {

	private int stock;
	private float unitPrice;
	private String name;

	public Product(String name, int stock, float unitPrice) {
		this.stock = stock;
		this.name = name;
		this.unitPrice = unitPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

}
