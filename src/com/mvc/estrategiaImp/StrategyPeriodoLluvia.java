package com.mvc.estrategiaImp;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.mvc.Betasoide;
import com.mvc.Ferengi;
import com.mvc.Planeta;
import com.mvc.SistemaSolar;
import com.mvc.Vulcano;

public class StrategyPeriodoLluvia implements ICondicion{

	private SistemaSolar sol;
	private static RoundingMode RM = RoundingMode.HALF_EVEN;
	private int contadorLluvias;
	private BigDecimal maximoPerimetro;
	private int maximoDia;
	
	public StrategyPeriodoLluvia(SistemaSolar sol) {	
		this.sol = sol;
		this.contadorLluvias = 0;
		// Maximo Dia En Base Al Perimetro Maximo
		this.maximoDia = 0;
		// Maximo Perimetro Para Guardar
		this.maximoPerimetro = new BigDecimal(0);
	}
	
	public SistemaSolar getSol() {	
		return this.sol;
	}
	
	public void aumentarContadorLluvias() {
		this.contadorLluvias++;
	}
	
	public BigDecimal getMaximoPerimetro() {
		return maximoPerimetro;
	}

	public int getMaximoDia() {
		return maximoDia;
	}

	public void setMaximoDia(int maximoDia) {
		this.maximoDia = maximoDia;
	}

	public void setMaximoPerimetro(BigDecimal maximoPerimetro) {
		this.maximoPerimetro = maximoPerimetro;
	}
	
	public int getContadorLluvias() {
		return contadorLluvias;
	}
	
	public void sucesoPeriodo(int diaSuceso) {		
		sucesoPeriodoDeLluvia(diaSuceso);	
	}
	
	// Algoritmo Para Calcular La Triangulacion Teniendo Dentro El
	// Sol------------------------------------------------------
	public void sucesoPeriodoDeLluvia(int diaSuceso) {

		// Esta Lista Es Para Los Valores De Segmento Entre Planetas Redondeado Ya Su
		// Lado
		ArrayList<BigDecimal> valoresOrientados = new ArrayList<>();

		Ferengi planetaFerengi = (Ferengi) getSol().getPlanetas().get(0);
		Betasoide planetaBetasoide = (Betasoide) getSol().getPlanetas().get(1);
		Vulcano planetaVulcano = (Vulcano) getSol().getPlanetas().get(2);

		// Calculo La Orientacion De ABC
		BigDecimal orientacionTriangulo = calculoOrientacion(planetaFerengi, planetaBetasoide, planetaVulcano);

		// Calculo La Orientacion De ABP, BCP, CBP
		BigDecimal orientacionTrianguloPunto1 = calculoOrientacion(planetaFerengi, planetaBetasoide);
		BigDecimal orientacionTrianguloPunto2 = calculoOrientacion(planetaBetasoide, planetaVulcano);
		BigDecimal orientacionTrianguloPunto3 = calculoOrientacion(planetaVulcano, planetaFerengi);

		// Los Agrego A La Lista
		valoresOrientados.add(orientacionTrianguloPunto1);
		valoresOrientados.add(orientacionTrianguloPunto2);
		valoresOrientados.add(orientacionTrianguloPunto3);

		/**
		 * Recorro La Lista De Planetas Y Si Todos Tienen La Misma Orientacion Que ABC
		 * Sucede El De Lluvia Si Uno No Cumple La Condicion El Resto No Lo Comprueba Y
		 * Significa Que El Punto Esta Fuera Del Triangulo
		 */

		if (valoresOrientados.stream().allMatch(valor -> tienenLaMismaOrientacion(orientacionTriangulo, valor))) {
			this.aumentarContadorLluvias();
			BigDecimal perimetroTriangulo = calcularPerimetroDelTriangulo(planetaFerengi, planetaBetasoide,
					planetaVulcano);

			if (perimetroTriangulo.compareTo(this.getMaximoPerimetro()) > 0
			 || perimetroTriangulo.compareTo(this.getMaximoPerimetro()) == 0) {
				//System.out.println("Dia: " + diaSuceso + " Nuevo Perimetro Maximo = " + perimetroTriangulo);
				this.setMaximoPerimetro(perimetroTriangulo);
				this.setMaximoDia(diaSuceso);
			}
		}
	}
	
	/**
	 * Obtengo La Distancia Entre Los Puntos AB, AC, BC Siendo Este Cada Lado Del
	 * Triangulo Sumo Sus Lados Para Obtener El Perimetro
	 */

	private BigDecimal calcularPerimetroDelTriangulo(Planeta planetaA, Planeta planetaB, Planeta planetaC) {

		BigDecimal total = BigDecimal.valueOf(0);

		BigDecimal primerLado = BigDecimal.valueOf(Point2D.distance(planetaA.getMovimientoEnX(),
				planetaA.getMovimientoEnY(), planetaB.getMovimientoEnX(), planetaB.getMovimientoEnY()));
		total = total.add(primerLado).setScale(4, RM);

		BigDecimal segundoLado = BigDecimal.valueOf(Point2D.distance(planetaA.getMovimientoEnX(),
				planetaA.getMovimientoEnY(), planetaC.getMovimientoEnX(), planetaC.getMovimientoEnY()));
		total = total.add(segundoLado).setScale(4, RM);

		BigDecimal tercerLado = BigDecimal.valueOf(Point2D.distance(planetaB.getMovimientoEnX(),
				planetaB.getMovimientoEnY(), planetaC.getMovimientoEnX(), planetaC.getMovimientoEnY()));
		return total.add(tercerLado).setScale(4, RM);

	}

	// Funcion Para Comprobar Si El Punto Respecto Al Triangulo Tienen La Misma
	// Orientacion
	private boolean tienenLaMismaOrientacion(BigDecimal primeraOrientacion, BigDecimal segundaOrientacion) {

		BigDecimal comparoCero = new BigDecimal(0);

		return ((primeraOrientacion.compareTo(comparoCero) > 0) && (segundaOrientacion.compareTo(comparoCero) > 0))
				|| ((primeraOrientacion.compareTo(comparoCero) > 0) && (segundaOrientacion.compareTo(comparoCero) > 0));
	}

	/**
	 * Obtengo La Orientacion Del Triangulo ABC Con La Siguiente Formula (A1.x -
	 * A3.x) * (A2.y - A3.y) - (A1.y - A3.y) * (A2.x - A3.x)
	 */

	private BigDecimal calculoOrientacion(Planeta planetaA, Planeta planetaB, Planeta planetaC) {

		BigDecimal primero = (BigDecimal.valueOf(planetaA.getMovimientoEnX()))
				.subtract(BigDecimal.valueOf(planetaC.getMovimientoEnX())).setScale(4, RM);
		BigDecimal segundo = (BigDecimal.valueOf(planetaB.getMovimientoEnY()))
				.subtract(BigDecimal.valueOf(planetaC.getMovimientoEnY())).setScale(4, RM);
		BigDecimal tercero = (BigDecimal.valueOf(planetaA.getMovimientoEnY()))
				.subtract(BigDecimal.valueOf(planetaC.getMovimientoEnY())).setScale(4, RM);
		BigDecimal cuarto = (BigDecimal.valueOf(planetaB.getMovimientoEnX()))
				.subtract(BigDecimal.valueOf(planetaC.getMovimientoEnX())).setScale(4, RM);

		BigDecimal primeraCuenta = primero.multiply(segundo).setScale(4, RM);
		BigDecimal segundaCuenta = tercero.multiply(cuarto).setScale(4, RM);

		return primeraCuenta.subtract(segundaCuenta).setScale(4, RM);
	}

	/**
	 * Obtengo La Orientacion Que Forma El Punto P Con Los Vertices Del Triangulo
	 * ABC. El Punto P Para Esta Situacion Es El Sol En (0,0) Calculo Para Obtener
	 * Las Orientaciones De ABP, BCP, CBP
	 */

	private BigDecimal calculoOrientacion(Planeta planetaA, Planeta planetaB) {

		BigDecimal primero = (BigDecimal.valueOf(planetaA.getMovimientoEnX())).subtract(BigDecimal.valueOf(getSol().getCentroX()))
				.setScale(4, RM);
		BigDecimal segundo = (BigDecimal.valueOf(planetaB.getMovimientoEnY())).subtract(BigDecimal.valueOf(getSol().getCentroY()))
				.setScale(4, RM);
		BigDecimal tercero = (BigDecimal.valueOf(planetaA.getMovimientoEnY())).subtract(BigDecimal.valueOf(getSol().getCentroY()))
				.setScale(4, RM);
		BigDecimal cuarto = (BigDecimal.valueOf(planetaB.getMovimientoEnX())).subtract(BigDecimal.valueOf(getSol().getCentroX()))
				.setScale(4, RM);

		BigDecimal primeraCuenta = primero.multiply(segundo).setScale(4, RM);
		BigDecimal segundaCuenta = tercero.multiply(cuarto).setScale(4, RM);

		return primeraCuenta.subtract(segundaCuenta).setScale(4, RM);
	}
}
