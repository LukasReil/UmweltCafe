package net.umweltcafe.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class ComboBox {

	ArrayList<Button> buttons;

	int x, y;
	int width, height;

	Color baseColor, triangleColor;

	boolean hovered, pressed, collapsed;

	String text;

	public ComboBox(ArrayList<Button> buttons, int x, int y, int width, int height, String text, Color baseColor,
			Color triangleColor) {
		this.buttons = buttons;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.baseColor = baseColor;
		this.triangleColor = triangleColor;
		this.text = text;
		this.hovered = false;
		this.pressed = false;
		this.collapsed = true;
	}

	public int update(Point mousePos, boolean mouseButtonState) {

		int ret = 0;

		hovered = mousePos.x > x && mousePos.x < x + width && mousePos.y > y && mousePos.y < y + height;
		if (hovered && mouseButtonState && !pressed) {
			collapsed = !collapsed;
			for (Button b : buttons) {
				b.visible = !collapsed;
				ret += b.height;
			}
			ret *= collapsed ? -1 : 1;
		}
		pressed = mouseButtonState;

		if (!collapsed) {
			for (Button b : buttons) {
				b.update(mousePos, mouseButtonState, x, y + height);
			}
		}

		return ret;
	}

	public void render(Graphics g) {
		g.setColor(new Color((int) (baseColor.getRed() * (hovered ? pressed ? 0.5f : 0.8f : 1f)),
				(int) (baseColor.getGreen() * (hovered ? pressed ? 0.5f : 0.8f : 1f)),
				(int) (baseColor.getBlue() * (hovered ? pressed ? 0.5f : 0.8f : 1f))));
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		int yOff = (height + g.getFontMetrics().getAscent()) / 2;
		g.drawString(text, x + Renderer.BOX_HEIGHT, y + yOff);

		g.setColor(triangleColor);
		if (collapsed) {
			g.fillPolygon(new int[] {
					x + Renderer.BOX_HEIGHT / 4, x + Renderer.BOX_HEIGHT / 4, x + Renderer.BOX_HEIGHT / 4 * 3
			}, new int[] {
					y + Renderer.BOX_HEIGHT / 4, y + Renderer.BOX_HEIGHT / 4 * 3, y + Renderer.BOX_HEIGHT / 4 * 2
			}, 3);
			return;
		} else {
			g.fillPolygon(new int[] {
					x + Renderer.BOX_HEIGHT / 4, x + Renderer.BOX_HEIGHT / 4 * 2, x + Renderer.BOX_HEIGHT / 4 * 3
			}, new int[] {
					y + Renderer.BOX_HEIGHT / 4, y + Renderer.BOX_HEIGHT / 4 * 3, y + Renderer.BOX_HEIGHT / 4
			}, 3);
		}

		for (Button b : buttons) {
			b.render(g, x, y + height);
		}

	}

}
