package myxmlproject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Order {

	private int id;
	private int idCustomer;
	private HashMap<Product, Integer> cart;
	private String companyName;
	private int idCompany;
	private int idCompanyBank;
	private Check.Currency currency;
	private int orderSum = 0;

	public Order(int id, int idCustomer, int idCompany, Check.Currency currency) {
		this.id = id;
		this.idCompany  = idCompany;
		this.idCustomer = idCustomer;
		this.currency   = currency;
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

	public void sendOrder(String where) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature("http://apache.org/xml/features/validation/schema", true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			Element order = (Element) doc.createElement("order");
			doc.appendChild(order);
			order.setAttribute("id", Integer.toString(id));
			Element eIdCustomer = doc.createElement("idCustomer");
			eIdCustomer.setTextContent(Integer.toString(idCustomer));
			order.appendChild(eIdCustomer);
			Element eIdCompany = doc.createElement("idCompany");
			eIdCompany.setTextContent(Integer.toString(idCompany));
			order.appendChild(eIdCompany);
			Element eCurrency = doc.createElement("currency");
			eCurrency.setAttribute("type", currency.toString());
			order.appendChild(eCurrency);
			Element eCart = doc.createElement("cart");
			for (Map.Entry<Product, Integer> e: cart.entrySet()) {
				Element q = doc.createElement("quantity");
				q.setTextContent(Integer.toString(e.getValue()));
				Element p = doc.createElement("product");
				Element name = doc.createElement("name");
				Element up   = doc.createElement("unitPrice");
				name.setTextContent(e.getKey().getName());
				up  .setTextContent(Double.toString(e.getKey().getUnitPrice()));
				p.appendChild(name);
				p.appendChild(up);
				Element item = doc.createElement("item");
				item.appendChild(p);
				item.appendChild(q);
				eCart.appendChild(item);
			}
			order.appendChild(eCart);
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(where + "/order.xml"));
			tFormer.transform(source, result);			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
			e1.printStackTrace();
		} catch (TransformerException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public double calculateSum() {
		double sum = 0;
		for (Map.Entry<Product, Integer> e: cart.entrySet()) {
			sum += e.getKey().getUnitPrice() * e.getValue();
		}
		return sum;
	}
}
