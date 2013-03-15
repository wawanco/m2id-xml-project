package myxmlproject;

import java.nio.file.Path;

public class Bank {
	static String baseDir = ".";
	private int id;
	private String name;
	private String pathToBase;
	
	public Bank(int id, String name) {
		this.id = id;
		this.name = name;
		pathToBase = buildPath();
	}

	public String getName() {
		return name;
	}

	private String buildPath() {
		// Build path to xml file containing the customer database
		return baseDir + "/customer-base_" + id + ".xml";
	}

	private void GenerateCustomerId() {
		// parcours de l'arbre et incrementer l'ID
		;
	}

	private void GenerateCheck() {
		;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int RegisterCustomer(Customer c) {
		// parse xml
		// generer nell Id
		// ajouter client
		// generer cheque
		return 0;
	}

}
