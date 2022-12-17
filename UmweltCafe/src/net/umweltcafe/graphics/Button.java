package net.umweltcafe.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

public class Button {

	int x, y;
	int width, height;
	Color baseColor;
	String text;

	boolean hovered, pressed, visible;

	ButtonAction action;

	public Button(int x, int y, int width, int height, Color baseColor, String text, ButtonAction action) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.baseColor = baseColor;
		this.text = text;
		this.hovered = false;
		this.pressed = false;
		this.visible = false;
		this.action = action;
	}

	public void render(Graphics g) {
		render(g, 0, 0);
	}

	public void render(Graphics g, int xOffset, int yOffset) {
		if (!visible) {
			return;
		}
		g.setColor(getColor());
		g.fillRect(x + xOffset, y + yOffset, width, height);
		g.setColor(Color.black);
		renderTextCentered(g, text, xOffset, yOffset);
	}

	protected Color getColor() {
		return new Color((int) (baseColor.getRed() * (hovered ? pressed ? 0.5f : 0.8f : 1f)),
				(int) (baseColor.getGreen() * (hovered ? pressed ? 0.5f : 0.8f : 1f)),
				(int) (baseColor.getBlue() * (hovered ? pressed ? 0.5f : 0.8f : 1f)));
	}
	
	protected void renderTextCentered(Graphics g, String _text, int xOffset, int yOffset) {

		String[] lines = _text.split("\n");

		FontMetrics fm = g.getFontMetrics();

		int allHeight = fm.getHeight() * lines.length;

		for (int i = 0; i < lines.length; i++) {
			int xOff = (width - fm.stringWidth(lines[i])) / 2;
			int yOff = (height - allHeight) / 2 + i * fm.getHeight() + fm.getAscent();
			g.drawString(lines[i], x + xOff + xOffset, y + yOff + yOffset);
		}

	}

	public void update(Point mousePos, boolean mouseButtonState) {
		update(mousePos, mouseButtonState, 0, 0);
	}

	public void update(Point mousePos, boolean mouseButtonState, int xOffset, int yOffset) {
		if (!visible) {
			return;
		}

		hovered = mousePos.x > x + xOffset && mousePos.x < x + width + xOffset && mousePos.y > y + yOffset
				&& mousePos.y < y + yOffset + height;
		if (hovered && mouseButtonState && !pressed) {
			action.perform();
		}
		pressed = mouseButtonState;
	}

	public static abstract class ButtonAction {
		public abstract void perform();
	}
}
