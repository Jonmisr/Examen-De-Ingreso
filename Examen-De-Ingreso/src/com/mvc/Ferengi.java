package com.mvc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;

public class Ferengi extends Planeta {

	private Point movimientoDibujoFerengi;
	private Point movimientoFerengi;
	//Calculo De Las Revoluciones Por Dia En Base A La Velocidad Angular = 1 grado/dia
	private static double RPD = 0.017f;
	private long tiempoInicial;
	private double tiempoTranscurrido;

	public Ferengi() {
		super(500);
		this.tiempoInicial = System.currentTimeMillis();
		//Inicio El Planeta Con Su Respecto Radio En X e Y
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
		//Tomo El Tiempo Del Sistema, Lo Resto Con El Inicial Y Lo Divido En 1000 Por Estar En Milisegundos
		this.tiempoTranscurrido = (System.currentTimeMillis() - getTiempoInicial()) / 1000f;
		//Multiplico Por El RPD Para Que El MCU Sea Lento
		this.tiempoTranscurrido *= RPD;
		//Desplazo El Angulo En Base Al Tiempo Transcurrido
		this.desplazarAngulo(tiempoTranscurrido);
	}
	
	public void setMovimiento(Graphics g1) {
		//Re-Defino Su Trayectoria Cada Vez Que Cambia Su Movimiento
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

	//Trayectoria Que Se Fija En Que Posicion Se Encuentra
	//(Posible Problema) Los Calculos Son En Double Pero Para Instanciar El Point Lo Tengo Que Castear A Int
	public Point trayectoria() {

		return new Point((int) ((getRadio() * Math.cos(getAnguloInicial())) + getSol().getCentro().x),
				(int) ((getRadio() * Math.sin(getAnguloInicial())) + getSol().getCentro().y));
	}
	
	public Point trayectoriaDibujo() {
		
		return new Point((int) ((getRadio() * Math.cos(getAnguloInicial()) / 8) + getSol().getCentroDibujo().x),
				(int) ((getRadio() * Math.sin(getAnguloInicial()) / 8) + getSol().getCentroDibujo().y));
	}
}
