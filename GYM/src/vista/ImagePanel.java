package vista;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private Image background;

	/**
	 * @param
	 */
	public ImagePanel(String resourcePath) {
		try {
			background = ImageIO.read(getClass().getResource(resourcePath));
		} catch (IOException | IllegalArgumentException e) {
			System.err.println("No se pudo cargar la imagen de fondo: " + resourcePath);
			e.printStackTrace();
		}
		setLayout(new java.awt.BorderLayout());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (background != null) {
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
