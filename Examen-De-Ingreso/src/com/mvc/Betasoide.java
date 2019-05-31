package com.mvc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Betasoide extends Planeta {

	private Point movimientoDibujoBetasoide;
	private Point movimientoBetasoide;
	private static double RPD = 0.052f;
	private long tiempoInicial;
	private double tiempoTranscurrido;

	public Betasoide() {
		super(1000);
		this.tiempoInicial = System.currentTimeMillis();
		this.movimientoBetasoide = new Point(1000, 1000);
		this.movimientoDibujoBetasoide = new Point(1000, 1000);
		this.setAnguloInicial(270);
	}

	public Point getMovimiento() {
		return movimientoDibujoBetasoide;
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
		this.movimientoBetasoide = trayectoria();
		this.movimientoDibujoBetasoide = trayectoriaDibujo();
		g1.setColor(Color.BLUE);
		PintarPunto(movimientoDibujoBetasoide, 10, g1);
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
