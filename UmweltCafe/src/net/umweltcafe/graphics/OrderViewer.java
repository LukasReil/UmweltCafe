package net.umweltcafe.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.umweltcafe.ordering.Order;

public class OrderViewer {

	public Order currentOrder;

	int x, y;

	public static final int width = (int) (200 * 1.5);
	public static final int height = 20;

	public static final int counterCenter = (int) (width / 1.5 + (width - width / 1.5) / 2);
	public static final int increaseCenter = (int) (counterCenter + (width - width / 1.5) / 4);
	public static final int decreaseCenter = (int) (counterCenter - (width - width / 1.5) / 4);

	HashMap<String, IncreaseCountButton> icbs;
	HashMap<String, DecreaseCountButton> dcbs;

	public OrderViewer(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setCurrentOrder(Order currentOrder) {
		this.currentOrder = currentOrder;

		icbs = new HashMap<String, OrderViewer.IncreaseCountButton>();
		dcbs = new HashMap<String, OrderViewer.DecreaseCountButton>();
	}

	Point mousePos;
	boolean buttonState;

	public void update(Point mousePos, boolean buttonState) {

		this.buttonState = buttonState;
		this.mousePos = mousePos;

		for (String key : currentOrder.order.keySet()) {
			if (!icbs.containsKey(key)) {
				icbs.put(key, new IncreaseCountButton(height, height, (int) (height / 4), new Button.ButtonAction() {

					@Override
					public void perform() {
						currentOrder.order.put(key, currentOrder.order.get(key) + 1);
					}
				}));

				dcbs.put(key, new DecreaseCountButton(height, height, (int) (height / 4), new Button.ButtonAction() {

					@Override
					public void perform() {
						currentOrder.order.put(key, currentOrder.order.get(key) - 1);
						if (currentOrder.order.get(key) == 0) {
							icbs.remove(key);
							dcbs.remove(key);
						}
					}
				}));

				icbs.get(key).visible = true;
				dcbs.get(key).visible = true;
			}
		}

	}

	public void render(Graphics g) {
		HashMap<String, Integer> order = currentOrder.order;

		int h = 0;
		
		ArrayList<String> toRemove = new ArrayList<String>();

		for (Entry<String, Integer> o : order.entrySet()) {
			String key = o.getKey();
			Integer i = o.getValue();

			icbs.get(key).update(mousePos, buttonState, x + increaseCenter - height / 2, y + h * height);
			dcbs.get(key).update(mousePos, buttonState, x + decreaseCenter - height / 2, y + h * height);
			
			if(!icbs.containsKey(key)) {
				toRemove.add(key);
				continue;
			}
			
			g.setColor(Color.gray);
			g.drawRect(x, y + h * height, (int) (width / 1.5f), height);
			g.setColor(Color.black);
			renderTextCentered(g, key, 0, h * height, (int) (width / 1.5), height);
			renderTextCentered(g, i + "", counterCenter - 10, h * height, 20, height);

			icbs.get(key).render(g, x + increaseCenter - height / 2, y + h * height);
			dcbs.get(key).render(g, x + decreaseCenter - height / 2, y + h * height);

			h++;
		}
		
		for(String key : toRemove) {
			currentOrder.order.remove(key);
		}
	}

	private void renderTextCentered(Graphics g, String _text, int xOffset, int yOffset, int width, int height) {

		String[] lines = _text.split("\n");

		FontMetrics fm = g.getFontMetrics();

		int allHeight = fm.getHeight() * lines.length;

		for (int i = 0; i < lines.length; i++) {
			int xOff = (width - fm.stringWidth(lines[i])) / 2;
			int yOff = (height - allHeight) / 2 + i * fm.getHeight() + fm.getAscent();
			g.drawString(lines[i], x + xOff + xOffset, y + yOff + yOffset);
		}

	}

	private static class IncreaseCountButton extends Button {

		int stroke;

		public IncreaseCountButton(int width, int height, int stroke, ButtonAction action) {
			super(0, 0, width, height, Color.GREEN, "", action);
			this.stroke = stroke;
		}

		public void render(Graphics g, int x, int y) {
			g.setColor(getColor());
			g.fillRect(x + width / 2 - stroke / 2, y, stroke, height);
			g.fillRect(x, y + height / 2 - stroke / 2, width, stroke);
		}

	}

	private static class DecreaseCountButton extends Button {

		int stroke;

		public DecreaseCountButton(int width, int height, int stroke, ButtonAction action) {
			super(0, 0, width, height, Color.RED, "", action);
			this.stroke = stroke;
		}

		public void render(Graphics g, int x, int y) {
			g.setColor(getColor());
			g.fillRect(x, y + height / 2 - stroke / 2, width, stroke);
		}
	}

}
