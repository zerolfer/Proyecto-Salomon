package estructurasDatos;

/**
 * Objeto con la estructura del movimiento utilizado para la generacion de soluciones en el entorno de soluciones del problema.
 * @author Tino
 *
 */
public class Movimiento {
	/**
	 * Posicion del primer turno en la lista que intercambia el contenido de los slots en el movimiento.
	 */
	int dador =0;
	/**
	 * Posicion del segundo turno en la lista que intercambia el contenido de los slots en el movimiento.
	 */
	int receptor =0;
	/**
	 * Posicion del primer slot del intervalo a intercambiar entre ambos turnos para la generacion de una solucion del entorno.
	 */
	int inicio =0;
	/**
	 * Posicion del ultimo slot del intervalo a intercambiar entre ambos turnos para la generacion de una solucion del entorno.
	 */
	int fin =0;
	/**
	 * Contructor
	 * @param dador Posicion del primer turno en la lista que intercambia el contenido de los slots en el movimiento.
	 * @param receptor Posicion del segundo turno en la lista que intercambia el contenido de los slots en el movimiento.
	 * @param inicio Posicion del primer slot del intervalo a intercambiar entre ambos turnos para la generacion de una solucion del entorno.
	 * @param fin Posicion del ultimo slot del intervalo a intercambiar entre ambos turnos para la generacion de una solucion del entorno.
	 */
	public Movimiento(int dador, int receptor, int inicio, int fin){
		this.dador=dador;
		this.receptor=receptor;
		this.inicio=inicio;
		this.fin=fin;
	}
	public int getDador() {
		return dador;
	}
	public void setDador(int dador) {
		this.dador = dador;
	}
	public int getReceptor() {
		return receptor;
	}
	public void setReceptor(int receptor) {
		this.receptor = receptor;
	}
	public int getInicio() {
		return inicio;
	}
	public void setInicio(int inicio) {
		this.inicio = inicio;
	}
	public int getFin() {
		return fin;
	}
	public void setFin(int fin) {
		this.fin = fin;
	}
	
}
