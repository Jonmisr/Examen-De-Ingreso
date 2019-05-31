package com.mvc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import javax.swing.JFrame;

public class Demos {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		
		SistemaSolar sol = SistemaSolar.getInstanceSistemaSolar();
		Ferengi ferengi = new Ferengi();
		Betasoide beta = new Betasoide();
		Vulcano vulcano = new Vulcano();

		sol.agregarPlaneta(ferengi);
		sol.agregarPlaneta(beta);
		sol.agregarPlaneta(vulcano);
		
		JFrame ventanaMovimiento = new JFrame() {

			{
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				setSize(1280, 720);
				setLocationRelativeTo(null);
				setVisible(true);
			}

			@Override
			public void paint(Graphics g1) {
				BufferedImage fotograma = new BufferedImage(getBounds().width, getBounds().height,
						BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = fotograma.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getBounds().width, getBounds().height);

				sol.setCentroDibujo(new Point(getBounds().width / 2, getBounds().height / 2));
				g.setStroke(new BasicStroke(1.2f ,BasicStroke.CAP_SQUARE ,BasicStroke.JOIN_MITER ,10.0f ,new float[]{4, 5} ,0.0f));
				sol.setPosicionSistemaSolar(g);

				ferengi.dibujarRadio(g);
				beta.dibujarRadio(g);
				vulcano.dibujarRadio(g);

				ferengi.setMovimiento(g);
				beta.setMovimiento(g);
				vulcano.setMovimiento(g);

				sol.dibujarLineasEntrePuntos(g);

				g1.drawImage(fotograma ,0, 0, null);
			} 
		};

		System.out.println("Ingrese La Cantidad De Anios A Predecir:");
		int anios = scanner.nextInt();
		
		for (int dias = 0; dias < (365 * anios); dias++) {

			ferengi.setTiempoMovimiento();
			beta.setTiempoMovimiento();
			vulcano.setTiempoMovimiento();
			sol.sucesoPeriodoDeSequia();
			sol.sucesoPeriodoDeLluvia(dias);
			sol.sucesoPeriodoOptimo();
			ventanaMovimiento.repaint();
			
			try {
				Thread.sleep(1000 / sol.getFPS());
			} catch (Exception e) {
			}
		}
		
		scanner.close();
		System.out.println("Cantidad Sucesos Sequias = " + sol.getContadorSequias());
		System.out.println("Cantidad Sucesos Lluvias = " + sol.getContadorLluvias());
		System.out.println("Dia = " + sol.getMaximoDia() + " - Perimetro = " + sol.getMaximoPerimetro());
		System.out.println("Cantidad Sucesos Optimos = " + sol.getContadorOptimo());
	}
}