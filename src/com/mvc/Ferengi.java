package com.mvc;

public class Ferengi extends Planeta {
	
	// Calculo De Las Revoluciones Por Dia En Base A La Velocidad Angular = 1
	// grado/dia
	private static double RPD = 0.0175;
	private long tiempoInicial;
	private double tiempoTranscurrido;

	public Ferengi() {
		super(500);
		this.tiempoInicial = System.currentTimeMillis();	
		this.É∆ = Math.toDegrees(270);
	}

	public long getTiempoInicial() {
		return tiempoInicial;
	}

	public void setTiempoMovimiento() {
		// Tomo El Tiempo Del Sistema, Lo Resto Con El Inicial Y Lo Divido En 1000 Por
		// Estar En Milisegundos
		this.tiempoTranscurrido = (System.currentTimeMillis() - getTiempoInicial()) / 1000f;
		// Multiplico Por El RPD Para Que El MCU Sea Lento
		this.tiempoTranscurrido *= RPD;
		// Desplazo El Angulo En Base Al Tiempo Transcurrido
		this.desplazarAngulo(tiempoTranscurrido);
	}

	public void setMovimiento() {
		// Re-Defino Su Trayectoria Cada Vez Que Cambia Su Movimiento
		double valorX = trayectoriaEnX();
		setMovimientoEnX(valorX);
		double valorY = trayectoriaEnY();
		setMovimientoEnY(valorY);
	}
}
