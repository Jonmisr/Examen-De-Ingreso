package com.galaxia.sistemasolar.PrediccionClima;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Planeta {

	private static RoundingMode RM = RoundingMode.HALF_EVEN;
	private static double MAXGRADOS = 360;
	private String nombrePlaneta;
	private BigDecimal radio;
	private double desplazamientoAngulo;
	private SistemaSolar sol;
	private double angulo;
	private BigDecimal movimientoEnX;
	private BigDecimal movimientoEnY;

	public Planeta(String nombrePlaneta, double radio, double desplazamientoAngulo) {
		this.nombrePlaneta = nombrePlaneta;
		this.radio = BigDecimal.valueOf(radio);
		this.desplazamientoAngulo = desplazamientoAngulo;
		this.movimientoEnX = BigDecimal.valueOf(0);
		this.movimientoEnY = BigDecimal.valueOf(0);
		// Angulo Con El Cual Inicia Posicionado El Planeta
		this.angulo = 90;
	}

	public void setMovimiento() {
		setMovimientoEnX(trayectoriaEnX());
		setMovimientoEnY(trayectoriaEnY());
	}

	public void setTiempoMovimiento() {
		this.desplazarAngulo();
	}

	public void setAnguloInicial(double angulo) {
		this.angulo = angulo;
	}

	public SistemaSolar getSol() {
		return sol;
	}

	public void setSol(SistemaSolar sol) {
		this.sol = sol;
	}

	public double getAnguloInicial() {
		return angulo;
	}

	public String getNombrePlaneta() {
		return nombrePlaneta;
	}

	public void setNombrePlaneta(String nombrePlaneta) {
		this.nombrePlaneta = nombrePlaneta;
	}

	public double getDesplazamientoAngulo() {
		return desplazamientoAngulo;
	}

	public void setDesplazamientoAngulo(double desplazamientoAngulo) {
		this.desplazamientoAngulo = desplazamientoAngulo;
	}

	public void setRadio(BigDecimal radio) {
		this.radio = radio;
	}

	// Desplazamiento Del Angulo Al Trasladarse
	public void desplazarAngulo() {
		this.angulo += this.getDesplazamientoAngulo();

		if (getDesplazamientoAngulo() > 0)
			this.angulo = angulo > 360 ? angulo - MAXGRADOS : angulo;
		else
			this.angulo = angulo <= 0 ? MAXGRADOS - angulo : angulo;
	}

	public BigDecimal getRadio() {
		return radio;
	}

	public BigDecimal getMovimientoEnX() {
		return movimientoEnX;
	}

	public void setMovimientoEnX(BigDecimal valorX) {
		this.movimientoEnX = valorX;
	}

	public BigDecimal getMovimientoEnY() {
		return movimientoEnY;
	}

	public void setMovimientoEnY(BigDecimal valorY) {
		this.movimientoEnY = valorY;
	}

	public BigDecimal trayectoriaEnY() {

		BigDecimal anguloSin = BigDecimal.valueOf(Math.sin(Math.toRadians(getAnguloInicial())));

		return getRadio().multiply(anguloSin).setScale(4, RM);
	}

	public BigDecimal trayectoriaEnX() {

		BigDecimal anguloCos = BigDecimal.valueOf(Math.cos(Math.toRadians(getAnguloInicial())));

		return getRadio().multiply(anguloCos).setScale(4, RM);
	}
}