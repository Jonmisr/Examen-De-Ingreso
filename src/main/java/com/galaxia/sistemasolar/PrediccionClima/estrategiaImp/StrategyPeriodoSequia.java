package com.galaxia.sistemasolar.PrediccionClima.estrategiaImp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.galaxia.sistemasolar.PrediccionClima.Planeta;
import com.galaxia.sistemasolar.PrediccionClima.SistemaSolar;

public class StrategyPeriodoSequia implements ICondicion{

	private static RoundingMode RM = RoundingMode.HALF_EVEN;
	private SistemaSolar sol;
	private int contadorSequias;
	private boolean condicionCumplida;
	private BigDecimal valorCero;
	private BigDecimal limiteSuperiorSequia;
	private ArrayList<Long> diasCumplidosSequias = new ArrayList<>();
	
	public StrategyPeriodoSequia(SistemaSolar sol) {
		this.sol = sol;
		this.contadorSequias = 0;
		this.valorCero = new BigDecimal(0);
		this.limiteSuperiorSequia = new BigDecimal(0);
	}
	
	public SistemaSolar getSol() {	
		return this.sol;
	}
	
	public ArrayList<Long> getDiasOcurridosSequias() {

		return diasCumplidosSequias;
	}
	
	public boolean isCondicionCumplida() {
		return condicionCumplida;
	}

	public void setCondicionCumplida(boolean condicionCumplida) {
		this.condicionCumplida = condicionCumplida;
	}

	public BigDecimal getValorCero() {
		return valorCero;
	}
	
	public BigDecimal getLimiteSuperior() {
		return limiteSuperiorSequia;
	}
	
	public int getContadorSequias() {
		return contadorSequias;
	}
	
	public void aumentarContadorSequias() {
		this.contadorSequias++;
	}
	
	public boolean sucesoPeriodo(long diaSuceso) {
		
		return sucesoPeriodoDeSequia(diaSuceso);
	}
	
	// Algoritmo Para Calcular La Alineacion Total En El
	// Sistema------------------------------------------------------

	public boolean sucesoPeriodoDeSequia(long dia) {

		Planeta unPlaneta = sol.getPlanetas().get(2);

		BigDecimal pendiente = calcularPendiente(unPlaneta);
		BigDecimal ordenadaAlOrigen = calcularOrdenada(pendiente, unPlaneta);

		// Filtro La Lista De Planetas Para Que Al Comparar Con La Recta No Calcule El
		// Del Planeta Que Utilice
		List<Planeta> aux = sol.getPlanetas().stream().filter(planeta -> planeta.getNombrePlaneta() != unPlaneta.getNombrePlaneta())
				.collect(Collectors.toList());

		/**
		 * Recorro La Lista De Planetas Y Si Todos Pertenecen A La Recta Entonces
		 * Deberia Suceder La Sequia Si Uno No Cumple La Condicion El Resto No Lo
		 * Comprueba
		 */
		boolean resultadoCondicion = aux.stream().allMatch(planeta -> compararPuntoConRecta(pendiente, ordenadaAlOrigen, planeta, dia));
		
		if (resultadoCondicion) {
			getDiasOcurridosSequias().add(dia);
			this.aumentarContadorSequias();
		}
		
		return resultadoCondicion;
	}

//	 /**Esta Funcion Lo Que Hace Es Calcular La Diferencia Entre 2 Puntos Y Obtener
//	 Su DeltaX y DeltaY
//	 Al Dividirlos Me Debe Dar La Pendiente m De La Ecuacion -> y = m.x + b
//	 El getCentro() Es De La Posicion Del Sol En (0,0)*/

	private BigDecimal calcularPendiente(Planeta unPlaneta) {

		BigDecimal deltaX = unPlaneta.getMovimientoEnX().subtract(BigDecimal.valueOf(this.getSol().getCentroX())).setScale(4, RM);
		BigDecimal deltaY = unPlaneta.getMovimientoEnY().subtract(BigDecimal.valueOf(this.getSol().getCentroY())).setScale(4, RM);
		
		if(deltaX.compareTo(valorCero) == 0) { return this.getValorCero(); }
		
		BigDecimal total = deltaY.divide(deltaX, 4, RM);
		return total;
	}

	/**
	 * Una Vez Obtenido La Pendiente -> m Sacada Entre Un Planeta Y El Sol, Calculo
	 * Cual Es La Ordenada Al Origen -> b Resto La Posicion Y Del Planeta Con La
	 * Pendiente Calculada Por La Posicion X Del Planeta Quedando La Ecuacion b = y
	 * - m.x
	 */

	private BigDecimal calcularOrdenada(BigDecimal pendiente, Planeta unPlaneta) {

		BigDecimal centroCero = new BigDecimal(0);
		BigDecimal exponente = BigDecimal.valueOf(1e-5);

		BigDecimal ecuacionDerecha = pendiente.multiply(unPlaneta.getMovimientoEnX()).setScale(4, RM);
		BigDecimal ecuacionIzquierda = unPlaneta.getMovimientoEnY();

		BigDecimal ordenada = ecuacionIzquierda.subtract(ecuacionDerecha).setScale(4, RM);
		BigDecimal resultado = ordenada.abs();
		if (resultado.compareTo(exponente) > -1) { return centroCero; }
		return ordenada;
	}

	/**
	 * Esta Funcion Al Sacar La Pendiente Y La Ordenada Entre 1 Planeta Y El Sol En
	 * (0,0) Le Paso Los Demas Planetas Sus Puntos Y Reemplazo En La Ecuacion y =
	 * m.x + b Los Valores Y Si El Punto X == Y Quiere Decir Que El P(x,y) Pertenece
	 * A La Recta
	 */

	private boolean compararPuntoConRecta(BigDecimal pendiente, BigDecimal ordenadaAlOrigen, Planeta unPlaneta, long dia) {

		BigDecimal primerCalculo = pendiente.multiply(unPlaneta.getMovimientoEnX()).setScale(4, RM);

		BigDecimal calculoX = primerCalculo.add(ordenadaAlOrigen).setScale(4, RM);
		BigDecimal calculoY = unPlaneta.getMovimientoEnY().setScale(4, RM);

		BigDecimal resultado;

		if (((calculoX.compareTo(this.getValorCero()) > 0) && (calculoY.compareTo(this.getValorCero()) > 0))
				|| ((calculoX.compareTo(this.getValorCero()) < 0) && (calculoY.compareTo(this.getValorCero()) < 0))) {

			BigDecimal absCalculoX = calculoX.abs();
			BigDecimal absCalculoY = calculoY.abs();
			
			if (absCalculoX.compareTo(absCalculoY) > 0) {
				resultado = absCalculoX.subtract(absCalculoY).setScale(4, RM);
			} else {
				resultado = absCalculoY.subtract(absCalculoX).setScale(4, RM);
			}

			return (resultado.compareTo(this.getValorCero()) > 0) && (resultado.compareTo(this.getLimiteSuperior()) < 0);
		}

		return (calculoY.compareTo(calculoX) == 0);
	}
}