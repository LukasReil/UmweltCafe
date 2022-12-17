package net.umweltcafe;

import csvlib.CSV;
import net.umweltcafe.graphics.Window;

public class Cafe {
	 
	public static void main(String[] args) {
		Config.init(Config.class.getResourceAsStream("products.json"));
		Kasse kasse = Kasse.parseKasse(CSV.readCSVFile(Config.path));
		new Window(kasse);
	}
	
}
