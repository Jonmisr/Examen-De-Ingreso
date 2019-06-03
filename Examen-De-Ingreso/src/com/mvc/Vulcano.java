package com.mvc;

public class Vulcano extends Planeta{

	//Calculo De Las Revoluciones Por Dia En Base A La Velocidad Angular = 5 grado/dia
	private static double RPD = 0.08726646;
	private long tiempoInicial;
	private double tiempoTranscurrido;

	public Vulcano() {
		super(2000);
		this.tiempoInicial = System.currentTimeMillis();
		//Inicio El Planeta Con Su Respecto Radio En X e Y
		//this.movimientoDibujoVulcano = new Point(2000, 2000);
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
	
	public void setMovimiento() {
		//Re-Defino Su Trayectoria Cada Vez Que Cambia Su Movimiento
		setMovimientoEnX(trayectoriaEnX());
		setMovimientoEnY(trayectoriaEnY());
	}

	//Trayectoria Que Se Fija En Que Posicion Se Encuentra
	//(Posible Problema) Los Calculos Son En Double Pero Para Instanciar El Point Lo Tengo Que Castear A Int
	@Override
	public double trayectoriaEnY() {
		return -(getRadio() * Math.sin(getAnguloInicial()) + getSol().getCentroY());
	}
}