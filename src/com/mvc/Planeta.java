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

	protected void setAnguloInicial(int radio) {	
		É∆ = Math.toDegrees(radio);
	}
	
	protected SistemaSolar getSol() {
		return sol;
	}

	protected void setSol(SistemaSolar sol) {
		this.sol = sol;
	}

	protected double getAnguloInicial() {
		return É∆;
	}

	//Desplazamiento Del Angulo Al Trasladarse
	protected void desplazarAngulo(double t) {
		double angulo = 2 * Math.PI * t;
		this.É∆ = this.redondearDecimales(angulo, 4);
	}

	protected int getRadio() {
		return radio;
	}
	
	protected double getMovimientoEnX() {
		return movimientoEnX;
	}

	protected void setMovimientoEnX(double valorX) {
		this.movimientoEnX = valorX;
	}

	protected double getMovimientoEnY() {
		return movimientoEnY;
	}

	protected void setMovimientoEnY(double valorY) {
		this.movimientoEnY = valorY;
	}
	
	public double trayectoriaEnY() {
		double radioY = getRadio() * Math.sin(getAnguloInicial()) + getSol().getCentroY();
		return this.redondearDecimales(radioY, 4);
	}
	
	public double trayectoriaEnX() {	
			double radioX = getRadio() * Math.cos(getAnguloInicial()) + getSol().getCentroX();
			return this.redondearDecimales(radioX, 4);
	}
	
	public double redondearDecimales(double valorInicial, int numeroDecimales) {
		double parteEntera, resultado;
		resultado = valorInicial;
		parteEntera = Math.floor(resultado);
		resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
		resultado = Math.round(resultado);
		resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
		if (resultado < parteEntera) return Math.ceil(resultado);
		return resultado;
	}
}
