package com.mvc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;

public class Ferengi extends Planeta {

	private Point movimientoDibujoFerengi;
	private Point movimientoFerengi;
	private static double RPD = 0.017f;
	private long tiempoInicial;
	private double tiempoTranscurrido;

	public Ferengi() {
		super(500);
		this.tiempoInicial = System.currentTimeMillis();
		this.movimientoFerengi = new Point(500, 500);
		this.movimientoDibujoFerengi = new Point(500, 500);
		this.setAnguloInicial(270);
	}

	public Point getMovimiento() {
		return movimientoDibujoFerengi;
	}
	
	public long getTiempoInicial() {
		return tiempoInicial;
	}

	public void setTiempoMovimiento() {
		
		this.tiempoTranscurrido = (System.currentTimeMillis() - getTiempoInicial()) / 1000f;
		this.tiempoTranscurrido *= RPD;
		this.desplazarAngulo(tiempoTranscurrido);
	}
	
	public void setMovimiento(Graphics g1) {
		this.movimientoFerengi = trayectoria();
		this.movimientoDibujoFerengi = trayectoriaDibujo();
		g1.setColor(Color.WHITE);
		PintarPunto(movimientoDibujoFerengi, 10, g1);
	}

	public void dibujarRadio(Graphics g1) {

			g1.setColor(Color.WHITE);
			g1.drawOval(sol.getCentroDibujo().x - this.getRadio() / 8, sol.getCentroDibujo().y - this.getRadio() / 8,
					this.getRadio() / 4, this.getRadio() / 4);		
	}

	public Point trayectoria() {

		return new Point((int) ((getRadio() * Math.cos(getAnguloInicial())) + getSol().getCentro().x),
				(int) ((getRadio() * Math.sin(getAnguloInicial())) + getSol().getCentro().y));
	}
	
	public Point trayectoriaDibujo() {
		
		return new Point((int) ((getRadio() * Math.cos(getAnguloInicial()) / 8) + getSol().getCentroDibujo().x),
				(int) ((getRadio() * Math.sin(getAnguloInicial()) / 8) + getSol().getCentroDibujo().y));
	}
}
