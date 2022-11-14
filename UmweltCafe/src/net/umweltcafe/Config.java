package net.umweltcafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class Config {

	
	//Key: Identifier Value: Price
	public static HashMap<String, Float> prices;
	//Key: Identifier Value: Name
	public static HashMap<String, String> names;
	//Key: Identifier Value: All Identifiers contained in category
	public static HashMap<String, ArrayList<String>> categories;

	
	public static void init(InputStream stream) {

		prices = new HashMap<String, Float>();
		names = new HashMap<String, String>();

		categories = new HashMap<String, ArrayList<String>>();

		String jsonString = "";
		jsonString = readFile(stream);
		JSONObject obj = new JSONObject(jsonString);

		JSONArray json_categories = obj.getJSONArray("categories");

		for (int i = 0; i < json_categories.length(); i++) {
			JSONObject category = json_categories.getJSONObject(i);
			String categoryName = category.getString("name");
			JSONArray elements = category.getJSONArray("elements");
			ArrayList<String> categoryElements = new ArrayList<String>();
			for (int j = 0; j < elements.length(); j++) {
				JSONObject element = elements.getJSONObject(j);
				String elementIdentifier = element.getString("identifier");
				String elementName = element.getString("name");
				elementName = elementName.replaceAll("_", " ");
				float elementPrice = element.getFloat("price");
				categoryElements.add(element.getString("identifier"));
				names.put(elementIdentifier, elementName);
				prices.put(elementIdentifier, elementPrice);
			}
			categories.put(categoryName, categoryElements);
		}

		System.out.println(categories);
		System.out.println(names);
		System.out.println(prices);

	}
	
	/**
	 * Initializes the Config Class with given parameters
	 * @param path The path from which the Config is read
	 * @throws FileNotFoundException 
	 */	
	public static void init(String path) throws FileNotFoundException {
		init(new FileInputStream(new File(path)));
	}

	public static float price(String identifier) {
		return prices.get(identifier);
	}

	public static String name(String identifier) {
		return names.get(identifier);
	}

	public static ArrayList<String> category(String categoryName){
		return categories.get(categoryName);
	}
	
	public static Set<String> categoryNames(){
		return categories.keySet();
	}
	
	private static String readFile(InputStream stream) {
		Scanner sc = new Scanner(stream);
		String s = "";
		while (sc.hasNext()) {
			s += sc.next();
		}
		sc.close();
		return s;
	}
	
	private static String readFile(String path) throws FileNotFoundException {
		return readFile(new FileInputStream(new File(path)));
	}
}
