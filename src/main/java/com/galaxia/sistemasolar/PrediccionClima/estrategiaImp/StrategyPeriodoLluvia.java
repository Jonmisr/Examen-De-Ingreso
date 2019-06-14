package com.galaxia.sistemasolar.PrediccionClima.estrategiaImp;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.galaxia.sistemasolar.PrediccionClima.Planeta;
import com.galaxia.sistemasolar.PrediccionClima.SistemaSolar;

public class StrategyPeriodoLluvia implements ICondicion{

	private SistemaSolar sol;
	private static RoundingMode RM = RoundingMode.HALF_EVEN;
	private int contadorLluvias;
	private boolean condicionCumplida;
	private BigDecimal maximoPerimetro;
	private long maximoDia;
	
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
	
	public boolean isCondicionCumplida() {
		return condicionCumplida;
	}

	public void setCondicionCumplida(boolean condicionCumplida) {
		this.condicionCumplida = condicionCumplida;
	}

	public void aumentarContadorLluvias() {
		this.contadorLluvias++;
	}
	
	public BigDecimal getMaximoPerimetro() {
		return maximoPerimetro;
	}

	public long getMaximoDia() {
		return maximoDia;
	}

	public void setMaximoDia(long diaSuceso) {
		this.maximoDia = diaSuceso;
	}

	public void setMaximoPerimetro(BigDecimal maximoPerimetro) {
		this.maximoPerimetro = maximoPerimetro;
	}
	
	public int getContadorLluvias() {
		return contadorLluvias;
	}
	
	public boolean sucesoPeriodo(long diaSuceso) {		
		return sucesoPeriodoDeLluvia(diaSuceso);	
	}
	
	// Algoritmo Para Calcular La Triangulacion Teniendo Dentro El
	// Sol------------------------------------------------------
	public boolean sucesoPeriodoDeLluvia(long diaSuceso) {

		// Esta Lista Es Para Los Valores De Segmento Entre Planetas Redondeado Ya Su
		// Lado
		ArrayList<BigDecimal> valoresOrientados = new ArrayList<>();

		Planeta planetaFerengi = getSol().getPlanetas().get(0);
		Planeta planetaBetasoide = getSol().getPlanetas().get(1);
		Planeta planetaVulcano = getSol().getPlanetas().get(2);

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
		boolean resultadoCondicion = valoresOrientados.stream().allMatch(valor -> tienenLaMismaOrientacion(orientacionTriangulo, valor));
		
		if (resultadoCondicion) {
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
		return resultadoCondicion;
	}
	
	/**
	 * Obtengo La Distancia Entre Los Puntos AB, AC, BC Siendo Este Cada Lado Del
	 * Triangulo Sumo Sus Lados Para Obtener El Perimetro
	 */

	private BigDecimal calcularPerimetroDelTriangulo(Planeta planetaA, Planeta planetaB, Planeta planetaC) {

		BigDecimal total = BigDecimal.valueOf(0);
		
		double planetaAX = planetaA.getMovimientoEnX().doubleValue();
		double planetaAY = planetaA.getMovimientoEnY().doubleValue();
		double planetaBX = planetaB.getMovimientoEnX().doubleValue();;
		double planetaBY = planetaB.getMovimientoEnY().doubleValue();;
		double planetaCX = planetaC.getMovimientoEnX().doubleValue();;
		double planetaCY = planetaC.getMovimientoEnY().doubleValue();;
		
		BigDecimal primerLado = BigDecimal.valueOf(Point2D.distance(planetaAX,
				planetaAY, planetaBX, planetaBY));
		total = total.add(primerLado).setScale(4, RM);

		BigDecimal segundoLado = BigDecimal.valueOf(Point2D.distance(planetaAX,
				planetaAY, planetaCX, planetaCY));
		total = total.add(segundoLado).setScale(4, RM);

		BigDecimal tercerLado = BigDecimal.valueOf(Point2D.distance(planetaBX,
				planetaBY, planetaCX, planetaCY));
		return total.add(tercerLado).setScale(4, RM);

	}

	// Funcion Para Comprobar Si El Punto Respecto Al Triangulo Tienen La Misma
	// Orientacion
	private boolean tienenLaMismaOrientacion(BigDecimal primeraOrientacion, BigDecimal segundaOrientacion) {
		
		BigDecimal comparoCero = new BigDecimal(0);
		
		return ((primeraOrientacion.compareTo(comparoCero) > 0) && (primeraOrientacion.compareTo(comparoCero) > 0))
				|| ((segundaOrientacion.compareTo(comparoCero) > 0) && (segundaOrientacion.compareTo(comparoCero) > 0));
	}

	/**
	 * Obtengo La Orientacion Del Triangulo ABC Con La Siguiente Formula (A1.x -
	 * A3.x) * (A2.y - A3.y) - (A1.y - A3.y) * (A2.x - A3.x)
	 */

	private BigDecimal calculoOrientacion(Planeta planetaA, Planeta planetaB, Planeta planetaC) {

		BigDecimal primero = planetaA.getMovimientoEnX()
				.subtract(planetaC.getMovimientoEnX()).setScale(4, RM);
		BigDecimal segundo = planetaB.getMovimientoEnY()
				.subtract(planetaC.getMovimientoEnY()).setScale(4, RM);
		BigDecimal tercero = (planetaA.getMovimientoEnY())
				.subtract(planetaC.getMovimientoEnY()).setScale(4, RM);
		BigDecimal cuarto = planetaB.getMovimientoEnX()
				.subtract(planetaC.getMovimientoEnX()).setScale(4, RM);

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

		BigDecimal primero = planetaA.getMovimientoEnX().subtract(BigDecimal.valueOf(getSol().getCentroX()))
				.setScale(4, RM);
		BigDecimal segundo = planetaB.getMovimientoEnY().subtract(BigDecimal.valueOf(getSol().getCentroY()))
				.setScale(4, RM);
		BigDecimal tercero = planetaA.getMovimientoEnY().subtract(BigDecimal.valueOf(getSol().getCentroY()))
				.setScale(4, RM);
		BigDecimal cuarto = planetaB.getMovimientoEnX().subtract(BigDecimal.valueOf(getSol().getCentroX()))
				.setScale(4, RM);

		BigDecimal primeraCuenta = primero.multiply(segundo).setScale(4, RM);
		BigDecimal segundaCuenta = tercero.multiply(cuarto).setScale(4, RM);

		return primeraCuenta.subtract(segundaCuenta).setScale(4, RM);
	}
}
