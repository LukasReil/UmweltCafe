package net.umweltcafe.ordering;

import java.time.LocalDateTime;
import java.util.ArrayList;

import csvlib.CSV;
import net.umweltcafe.Config;

public class Order {

	ArrayList<String> ids;

	String time;

	public Order(ArrayList<String> ids, String time) {
		this.ids = ids;
		this.time = time;
	}

	public Order() {
		this(new ArrayList<String>(), "");
	}

	public void addItem(String identifier) {
		ids.add(identifier);
	}

	public void finalize() {
		LocalDateTime time = LocalDateTime.now();
		this.time = time.getDayOfMonth() + "." + time.getMonthValue() + "." + time.getYear() + " " + time.getHour()
				+ ":" + time.getMinute() + ":" + time.getSecond();
	}

	public float getPrice() {
		float sum = 0;

		for (String id : ids) {
			sum += Config.price(id);
		}

		return sum;
	}

	public void addToCSV(CSV csv) {
		csv.addRow(new String[] { "Order", "", String.format("%.2f", getPrice()), time });
		for (String id : ids) {
			csv.addRow(new String[] { "Item", id, String.format("%.2f", Config.price(id)) });
		}
	}

	public static Order parseOrder(CSV csv, int start, int end) {
		ArrayList<String> ids = new ArrayList<String>();
		String time = csv.getCell(start, 3);
		
		for(int i = start; i <= end; i++) {
			ids.add(csv.getCell(i, 1));
		}
		
		return new Order(ids, time);
	}

}
