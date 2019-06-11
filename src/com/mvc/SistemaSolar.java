package com.mvc;

import java.util.ArrayList;
import com.mvc.estrategiaImp.ICondicion;

public class SistemaSolar {

	private int centroX;
	private int centroY;
	private static SistemaSolar instanciaSistema;
	private ArrayList<Planeta> planetas;
	private ICondicion estrategia;

	private ArrayList<ICondicion> condicionesStrategy = new ArrayList<>();

	private SistemaSolar() {
		planetas = new ArrayList<>();
		this.centroX = 0;
		this.centroY = 0;
	}

	// Patron Singleton
	public static SistemaSolar getInstanceSistemaSolar() {

		if (instanciaSistema == null) {

			instanciaSistema = new SistemaSolar();
		}

		return instanciaSistema;
	}

	public void agregarStrategy(ICondicion concreteStrategy) {	
		this.condicionesStrategy.add(concreteStrategy);
	}
	
	public void setConcreteStrategy(ICondicion concreteStrategy) {
		this.estrategia = concreteStrategy;
	}
	
	public ICondicion getConcreteStrategy() {
		return estrategia;
	}

	public ArrayList<ICondicion> getCondicionesStrategy() {
		return condicionesStrategy;
	}

	public ArrayList<Planeta> getPlanetas() {
		return planetas;
	}

	public void agregarPlaneta(Planeta unPlaneta) {
		unPlaneta.setSol(this);
		planetas.add(unPlaneta);
	}

	public int getCentroX() {
		return centroX;
	}

	public void setCentroX(int centroX) {
		this.centroX = centroX;
	}

	public int getCentroY() {
		return centroY;
	}

	public void setCentroY(int centroY) {
		this.centroY = centroY;
	}
	
	public void aplicarCondiciones(ICondicion estrategia, int dia) {
		this.setConcreteStrategy(estrategia);
		this.getConcreteStrategy().sucesoPeriodo(dia);
	}
}