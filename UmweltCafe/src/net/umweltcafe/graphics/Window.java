package net.umweltcafe.graphics;

import javax.swing.JFrame;

import net.umweltcafe.Kasse;

public class Window {
	
	JFrame window;
	Renderer renderer;
	Kasse kasse;
	
	public Window(Kasse kasse) {
		this.kasse = kasse;
		renderer = new Renderer(kasse);
		window = new JFrame("Umwelt Café");
		window.add(renderer);
		window.setSize(600, 600);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
}
