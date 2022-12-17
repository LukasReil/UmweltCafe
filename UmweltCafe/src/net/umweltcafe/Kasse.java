package net.umweltcafe;

import java.io.IOException;
import java.util.ArrayList;

import csvlib.CSV;
import net.umweltcafe.ordering.Order;

public class Kasse {
	
	ArrayList<Order> orders;
	
	public Order currentOrder;
	
	public Kasse() {
		this.currentOrder = new Order();
		this.orders = new ArrayList<Order>();
	}
	
	public void addItemToOrder(String identifier) {
		currentOrder.addItem(identifier);
	}
	
	public void finishOrder() {
		currentOrder.finalize();
		orders.add(currentOrder);
		currentOrder = new Order();
		export(Config.path);
	}
	
	public static Kasse parseKasse(CSV csv) {
		Kasse kasse = new Kasse();
		
		
		int startIndex = 0;
		int endIndex = 0;
		
		for(int i = 1; i < csv.getRowCount(); i++) {
			String c = csv.getCell(i, 0);
			if(c.equals("Order")) {
				endIndex = i-1;
				kasse.orders.add(Order.parseOrder(csv, startIndex, endIndex));
				startIndex = i;
			} else if(c.equals("Summe")) {
				endIndex = i-1;
				kasse.orders.add(Order.parseOrder(csv, startIndex, endIndex));
			}
		}
		
		for(Order o : kasse.orders) {
			System.out.println(o);
		}
		
		return kasse;
	}
	
	public void export(String path) {
		CSV csv = new CSV();
		float price = 0;
		for(Order order : orders) {
			order.addToCSV(csv);
			price += order.getPrice();
		}
			
		
		csv.addRow(new String[] {"Summe", "", "", String.format("%.2f", price)});
		try {
			csv.writeCSVFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		
		float price = 0;
		for(Order order : orders) {
			price += order.getPrice();
		}
		
		for(Order o : orders) {
			s += o + "\n";
		}
		s += "Summe: \t \t \t" + String.format("%.2f", price);
		return s;
	}
	
}
