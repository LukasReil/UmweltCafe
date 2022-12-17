package net.umweltcafe.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;

import javafx.scene.shape.Box;
import net.umweltcafe.Config;
import net.umweltcafe.Kasse;
import net.umweltcafe.graphics.Button.ButtonAction;
import net.umweltcafe.ordering.Order;

public class Renderer extends JComponent {

	// Rendering Configuration
	public static final int BOX_WIDTH = 200;
	public static final int BOX_HEIGHT = 20;

	ArrayList<ComboBox> boxes;

	OrderViewer orderViewer;

	Button finalizer;

	Point mousePos;
	boolean mouseState;

	public Renderer(Kasse kasse) {

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1)
					mouseState = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1)
					mouseState = false;
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePos = e.getPoint();
			}
		});

		mousePos = new Point();
		mouseState = false;

		boxes = new ArrayList<ComboBox>();

		for (int i = 0; i < Config.categoryNames().size(); i++) {
			String categoryName = (String) Config.categoryNames().toArray()[i];
			ArrayList<Button> buttons = new ArrayList<Button>();
			ArrayList<String> buttonIds = Config.category(categoryName);
			for (int j = 0; j < buttonIds.size(); j++) {
				String buttonId = buttonIds.get(j);
				ButtonAction action = new ButtonAction() {

					@Override
					public void perform() {

						kasse.addItemToOrder(buttonId);

					}
				};

				Button button = new Button(0, j * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, Color.WHITE, buttonId,
						action);
				buttons.add(button);
			}
			ComboBox box = new ComboBox(buttons, 0, i * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, categoryName,
					Color.LIGHT_GRAY, Color.DARK_GRAY);
			boxes.add(box);
		}

		finalizer = new Button(getWidth() - BOX_WIDTH, getHeight() - BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, Color.green,
				"Bestellung Abschließen", new ButtonAction() {

					@Override
					public void perform() {
						kasse.finishOrder();
						System.out.println(kasse);
						orderViewer.setCurrentOrder(kasse.currentOrder);
					}
				});
		finalizer.visible = true;

		orderViewer = new OrderViewer(0, 0);
		orderViewer.setCurrentOrder(kasse.currentOrder);
	}

	@Override
	public void paint(Graphics g) {
		for (int i = 0; i < boxes.size(); i++) {
			int m = boxes.get(i).update(mousePos, mouseState);
			if (m == 0) {
				continue;
			}
			for (int j = i + 1; j < boxes.size(); j++) {
				boxes.get(j).y += m;
			}

		}
		finalizer.x = getWidth() - BOX_WIDTH;
		finalizer.y = getHeight() - BOX_HEIGHT;
		finalizer.update(mousePos, mouseState);

		orderViewer.x = getWidth() - (int) (BOX_WIDTH * 1.5);
		orderViewer.y = 20;
		
		orderViewer.update(mousePos, mouseState);

		orderViewer.render(g);

		for (ComboBox box : boxes) {
			box.render(g);
		}

		finalizer.render(g);

		repaint();
	}

}
