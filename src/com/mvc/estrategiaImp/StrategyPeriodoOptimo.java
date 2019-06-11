package com.mvc.estrategiaImp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.mvc.Planeta;
import com.mvc.SistemaSolar;

public class StrategyPeriodoOptimo implements ICondicion{

	private static RoundingMode RM = RoundingMode.HALF_EVEN;
	private SistemaSolar sol;
	private int contadorOptimo;
	private BigDecimal valorCero;
	private BigDecimal limiteSuperiorOptimo;
	private ArrayList<Integer> diasCumplidosOptimos = new ArrayList<>();
	
	public StrategyPeriodoOptimo(SistemaSolar sol) {
		
		this.sol = sol;
		this.contadorOptimo = 0;
		this.valorCero = BigDecimal.valueOf(0);
		this.limiteSuperiorOptimo = BigDecimal.valueOf(0.045);
	}
	
	public SistemaSolar getSol() {	
		return this.sol;
	}
	
	public ArrayList<Integer> getDiasOcurridosOptimos() {

		return diasCumplidosOptimos;
	}
	
	public void aumentarContadorOptimo() {
		this.contadorOptimo++;
	}
	
	public int getContadorOptimo() {
		return contadorOptimo;
	}
	
	public BigDecimal getValorCero() {
		return valorCero;
	}

	public BigDecimal getLimiteSuperiorOptimo() {
		return limiteSuperiorOptimo;
	}

	public void sucesoPeriodo(int diaSuceso) {
		sucesoPeriodoOptimo(diaSuceso);
	} 
	
	// Algoritmo Para Calcular La Alineacion Solo Entre
	// Planetas------------------------------------------------------

	public void sucesoPeriodoOptimo(int dia) {

		Planeta planetaFerengi = getSol().getPlanetas().get(0);
		Planeta planetaBetasoide = getSol().getPlanetas().get(1);
		Planeta planetaVulcano = getSol().getPlanetas().get(2);

		BigDecimal puntoX = calcularSegmentoEntreDosPlanetasEnX(planetaFerengi, planetaVulcano);
		BigDecimal puntoY = calcularSegmentoEntreDosPlanetasEnY(planetaFerengi, planetaVulcano);

		boolean primeraCondicion = compararPuntosConLaRecta(planetaFerengi, planetaBetasoide, puntoX, puntoY, dia);
		boolean segundaCondicion = compararPuntosConLaRecta(planetaFerengi, puntoX, puntoY, dia);

		/**
		 * Si El Tercer Planeta (Ya Sacado El Segmento Con Los Dos Anteriores), Cumple
		 * Con La Condicion Y El Sol No, Quiere Decir Que El Planeta Esta Dentro De La
		 * Recta Y El Sol No Entonces Deberia Suceder La Condicion Optima De Presion Y
		 * Temperatura
		 */

		if (primeraCondicion && segundaCondicion) {
			this.aumentarContadorOptimo();
			getDiasOcurridosOptimos().add(dia);
		}
	}

	// Calculo El Segmento BA Entre Dos Planetas Para Obtener La Recta

	public BigDecimal calcularSegmentoEntreDosPlanetasEnX(Planeta planetaA, Planeta planetaB) {

		BigDecimal planBX = planetaB.getMovimientoEnX().setScale(4, RM);
		BigDecimal planAX = planetaA.getMovimientoEnX().setScale(4, RM);
		
		return planBX.subtract(planAX).setScale(4, RM) ;
	}

	public BigDecimal calcularSegmentoEntreDosPlanetasEnY(Planeta planetaA, Planeta planetaB) {

		BigDecimal planBY = planetaB.getMovimientoEnY().setScale(4, RM);
		BigDecimal planAY = planetaA.getMovimientoEnY().setScale(4, RM);
		
		return planBY.subtract(planAY).setScale(4, RM);
	}

	/**
	 * Entre Los Planetas A y B Y El Segmento puntoXY Calculado Anteriormente,
	 * Obtengo El Calculo De La Ecuacion De La Siguiente Forma (B.x - A.x) / x ==
	 * (B.y - A.y) / y
	 */

	public boolean compararPuntosConLaRecta(Planeta planetaA, Planeta planetaB, BigDecimal puntoX, BigDecimal puntoY, int dia) {

		BigDecimal resultado;
		
		BigDecimal planAX = planetaA.getMovimientoEnX().setScale(4, RM);
		BigDecimal planBX = planetaB.getMovimientoEnX().setScale(4, RM);
		BigDecimal primeroX = planBX.subtract(planAX).setScale(4, RM);	
		BigDecimal puntoEnX;
		if (puntoX.compareTo(valorCero) == 0) { puntoEnX = this.getValorCero(); }
		else { puntoEnX = primeroX.divide(puntoX, 4, RM); }
		
		BigDecimal planAY = planetaA.getMovimientoEnY().setScale(4, RM);
		BigDecimal planBY = planetaB.getMovimientoEnY().setScale(4, RM);
		BigDecimal primeroY = planBY.subtract(planAY).setScale(4, RM);	
		BigDecimal puntoEnY;
		if (puntoY.compareTo(valorCero) == 0) { puntoEnY = this.getValorCero(); }
		else { puntoEnY = primeroY.divide(puntoY, 4, RM); }

		if (tienenLaMismaOrientacion(puntoEnX, puntoEnY)) {
		
			BigDecimal valorX = puntoEnX.abs();
			BigDecimal valorY = puntoEnY.abs();

			if (valorX.compareTo(valorY) > 0) {
				resultado = valorX.subtract(valorY).setScale(4, RM);
			} else {
				resultado = valorY.subtract(valorX).setScale(4, RM);
			}
			//System.out.println("Planeta = " + resultado);
			return seEncuentraDentroDelRango(resultado);
		}
		return puntoEnX.compareTo(puntoEnY) == 0;
	}

	/**
	 * Entre El Planeta A, El Sol Y El Segmento puntoXY Calculado Anteriormente,
	 * Obtengo El Calculo De La Ecuacion De La Siguiente Forma (0 - A.x) / x == (0 -
	 * A.y) / y Si Los Resultados Son Distintos Quiere Decir Que El Sol No Se
	 * Encuentra En La Recta
	 */

	public boolean compararPuntosConLaRecta(Planeta planetaA, BigDecimal puntoX, BigDecimal puntoY, int dia) {

		BigDecimal resultado;
		BigDecimal operandoNegativo = BigDecimal.valueOf(-1);
		
		BigDecimal planAX = planetaA.getMovimientoEnX().multiply(operandoNegativo).setScale(4, RM);	
		BigDecimal planAY = planetaA.getMovimientoEnY().multiply(operandoNegativo).setScale(4, RM);
		
		BigDecimal puntoEnX;
		if (puntoX.compareTo(valorCero) == 0) { puntoEnX = this.getValorCero(); }
		else { puntoEnX = planAX.divide(puntoX, 4, RM); }
		
		BigDecimal puntoEnY;
		if (puntoY.compareTo(valorCero) == 0) { puntoEnY = this.getValorCero(); }
		else { puntoEnY = planAY.divide(puntoY, 4, RM); }

		if (tienenLaMismaOrientacion(puntoEnX, puntoEnY)) {
			
			BigDecimal valorX = puntoEnX.abs();
			BigDecimal valorY = puntoEnY.abs();

			if (valorX.compareTo(valorY) > 0) {
				resultado = valorX.subtract(valorY).setScale(4, RM);
			} else {
				resultado = valorY.subtract(valorX).setScale(4, RM);
			}
			//System.out.println("Sol = " + resultado);
			return seEncuentraDentroDelRango(resultado);
		}
		return puntoEnX.compareTo(puntoEnY) != 0;
	}
	
	private boolean tienenLaMismaOrientacion(BigDecimal puntoEnX, BigDecimal puntoEnY) {
		return (puntoEnX.compareTo(valorCero) > 0 && puntoEnY.compareTo(valorCero) > 0)
				|| (puntoEnX.compareTo(valorCero) < 0 && puntoEnY.compareTo(valorCero) < 0);
	}
	
	private boolean seEncuentraDentroDelRango(BigDecimal valor) {
		return valor.compareTo(valorCero) > 0 && valor.compareTo(limiteSuperiorOptimo) < 0;
	}
}
