package myxmlproject;

public class Bank {
	static String pathToBase;
	private int id;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(int id) {
		if (id == 1){
			this.name = "lcl";
		}else if (id == 2){
			this.name = "bnp";
		}else{
			this.name = "sg";
		}
	}

	private String buildPath() {
		// path/to/base + "bank" + id + ".xml"
		return "";
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
