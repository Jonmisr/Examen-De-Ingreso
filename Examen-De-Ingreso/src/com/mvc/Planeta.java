package com.mvc;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Planeta {

	private int radio;
	protected SistemaSolar sol;
	private double ��;

	public Planeta(int radio) {
		this.radio = radio;
		//Angulo Con El Cual Inicia Posicionado El Planeta
		this.�� = Math.toRadians(90);
	}

	public abstract Point trayectoria();

	public abstract void dibujarRadio(Graphics g1);

	public abstract void setMovimiento(Graphics g1);
	
	public abstract Point getMovimiento();

	public int getRadio() {
		return radio;
	}

	public void setAnguloInicial(int radio) {	
		�� = Math.toRadians(radio);
	}

	public SistemaSolar getSol() {
		return sol;
	}

	public void setSol(SistemaSolar sol) {
		this.sol = sol;
	}

	public double getAnguloInicial() {
		return ��;
	}

	//Desplazamiento Del Angulo Al Trasladarse
	protected void desplazarAngulo(double t) {
		this.�� = 2 * Math.PI * t;
	}

	void PintarPunto(Point c, int r, Graphics g) {
		g.fillOval(c.x - r / 2, c.y - r / 2, r, r);
	}

}
