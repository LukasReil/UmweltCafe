package net.umweltcafe;

import java.io.FileNotFoundException;

import csvlib.CSV;
import net.umweltcafe.graphics.Window;

public class Cafe {

	public static void main(String[] args) {

		if (Config.release) {
			try {
				Config.init("./products.json");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			Config.init(Cafe.class.getResourceAsStream("products.json"));
		}
		Kasse kasse = Kasse.parseKasse(CSV.readCSVFile(Config.path));
		new Window(kasse);
	}

}
