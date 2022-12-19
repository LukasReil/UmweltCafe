package net.umweltcafe.ordering;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;

import csvlib.CSV;
import net.umweltcafe.Config;

public class Order {

	public HashMap<String, Integer> order;


	String time;

	public Order(HashMap<String, Integer> order, String time) {
		this.order = order;
		this.time = time;
	}

	public Order() {
		this(new HashMap<String, Integer>(), "");
	}

	public void addItem(String identifier) {
		if (order.containsKey(identifier)) {
			order.put(identifier, order.get(identifier) + 1);
		} else {
			order.put(identifier, 1);
		}
	}

	public void finalize() {
		LocalDateTime time = LocalDateTime.now();
		this.time = time.getDayOfMonth() + "." + time.getMonthValue() + "." + time.getYear() + ";" + time.getHour()
				+ ":" + time.getMinute() + ":" + time.getSecond();
	}

	public float getPrice() {
		float sum = 0;

		for (Entry<String, Integer> entry : order.entrySet()) {
			sum += entry.getValue() * Config.price(entry.getKey());
		}

		return sum;
	}

	public void addToCSV(CSV csv) {
		csv.addRow(new String[] {
				"Order", "", "", String.format("%.2f", getPrice()), time
		});

		for (Entry<String, Integer> entry : order.entrySet()) {
			csv.addRow(new String[] {
					"Item",
					entry.getKey(),
					entry.getValue() + "",
					String.format("%.2f", entry.getValue() * Config.price(entry.getKey()))
			});
		}
	}

	public static Order parseOrder(CSV csv, int start, int end) {
		HashMap<String, Integer> order = new HashMap<String, Integer>();
		String time = csv.getCell(start, 4) + ";" + csv.getCell(start, 5);

		for (int i = start + 1; i <= end; i++) {
			order.put(csv.getCell(i, 1), Integer.parseInt(csv.getCell(i, 2)));
		}

		return new Order(order, time);
	}

	@Override
	public String toString() {
		String s = "Order: \t \t \t" + String.format("%.2f", getPrice()) + "\t" + time;

		for (Entry<String, Integer> entry : order.entrySet()) {
			s += "\nItem: \t" + entry.getKey() + "\t" + entry.getValue() + "\t"
					+ String.format("%.2f", entry.getValue() * Config.price(entry.getKey()));
		}
		return s;
	}

}
