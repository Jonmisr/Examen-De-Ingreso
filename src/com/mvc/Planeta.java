package com.mvc;

public abstract class Planeta {

	protected int radio;
	protected SistemaSolar sol;
	protected double É∆;
	protected double movimientoEnX;
	protected double movimientoEnY;

	public Planeta(int radio) {
		this.radio = radio;
		this.movimientoEnX = 0;
		this.movimientoEnY = 0;
		//Angulo Con El Cual Inicia Posicionado El Planeta
		this.É∆ = Math.toDegrees(90);
	}
	
	protected abstract void setMovimiento();
	protected abstract void setTiempoMovimiento();

	public void setAnguloInicial(int radio) {	
		É∆ = Math.toDegrees(radio);
	}
	
	public SistemaSolar getSol() {
		return sol;
	}

	public void setSol(SistemaSolar sol) {
		this.sol = sol;
	}

	public double getAnguloInicial() {
		return É∆;
	}

	//Desplazamiento Del Angulo Al Trasladarse
	public void desplazarAngulo(double t) {
		this.É∆ = 2 * Math.PI * t;
	}

	public int getRadio() {
		return radio;
	}
	
	public double getMovimientoEnX() {
		return movimientoEnX;
	}

	public void setMovimientoEnX(double valorX) {
		this.movimientoEnX = valorX;
	}

	public double getMovimientoEnY() {
		return movimientoEnY;
	}

	public void setMovimientoEnY(double valorY) {
		this.movimientoEnY = valorY;
	}
	
	public double trayectoriaEnY() {
		return getRadio() * Math.sin(getAnguloInicial()) + getSol().getCentroY();
	}
	
	public double trayectoriaEnX() {	
			return getRadio() * Math.cos(getAnguloInicial()) + getSol().getCentroX();
	}
}