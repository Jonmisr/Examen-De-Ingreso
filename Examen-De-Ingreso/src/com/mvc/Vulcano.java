package com.mvc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Vulcano extends Planeta{

	private Point movimientoDibujoVulcano;
	private Point movimientoVulcano;
	private static double RPD = 0.088f;
	private long tiempoInicial;
	private double tiempoTranscurrido;

	public Vulcano() {
		super(2000);
		this.tiempoInicial = System.currentTimeMillis();
		this.movimientoVulcano = new Point(2000, 2000);
		this.movimientoDibujoVulcano = new Point(2000, 2000);
		
	}

	public Point getMovimiento() {
		return movimientoDibujoVulcano;
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
		this.movimientoVulcano = trayectoria();
		this.movimientoDibujoVulcano = trayectoriaDibujo();
		g1.setColor(Color.GREEN);
		PintarPunto(movimientoDibujoVulcano, 10, g1);
	}

	public void dibujarRadio(Graphics g1) {

		g1.setColor(Color.WHITE);
		g1.drawOval(sol.getCentroDibujo().x - this.getRadio() / 8, sol.getCentroDibujo().y - this.getRadio() / 8,
				this.getRadio() / 4, this.getRadio() / 4);
	}

	public Point trayectoria() {

		return new Point(
				(int) ((getRadio() * Math.cos(getAnguloInicial()) / 8) + getSol().getCentro().x),
				(int) ((-getRadio() * Math.sin(getAnguloInicial()) / 8) + getSol().getCentro().y));
	}
	
	public Point trayectoriaDibujo() {

		return new Point((int) ((getRadio() * Math.cos(getAnguloInicial()) / 8) + getSol().getCentroDibujo().x),
				(int) ((-getRadio() * Math.sin(getAnguloInicial()) / 8) + getSol().getCentroDibujo().y));
	}
}
