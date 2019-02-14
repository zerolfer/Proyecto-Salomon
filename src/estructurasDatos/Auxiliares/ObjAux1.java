package estructurasDatos.Auxiliares;

import java.util.ArrayList;

/**
 * Objeto auxiliar utilizado para facilitar la asignacion de controladores a sus turnos de trabajo en la construccion de soluciones iniciales.
 * @author Tino
 *
 */
public class ObjAux1 {

	private boolean turnoLargo;
	private boolean ptd;
	private ArrayList<String> nucleo;
	private int posicion;
	/**
	 * Contructor
	 * @param turnoLargo Boolean que indica si un controlador puede trabaja en turno largo (true) o corto (false).
	 * @param ptd Boolean que indica si un controlador tiene acreditacion PTD (true) o CON (false).
	 * @param nucleo Nucleo que el controlador tiene permitido trabajar.
	 * @param posicion Posicion en la lista de turnos que el controlador tiene asignado.
	 */
	public ObjAux1(boolean turnoLargo, boolean ptd, ArrayList<String> nucleo, int posicion){
		this.turnoLargo = turnoLargo;
		this.ptd = ptd;
		this.nucleo = nucleo;
		this.posicion = posicion;
	}
	
	public boolean isTurnoLargo() {
		return turnoLargo;
	}
	public void setTurnoLargo(boolean turnoLargo) {
		this.turnoLargo = turnoLargo;
	}
	public boolean isPtd() {
		return ptd;
	}
	public void setPtd(boolean ptd) {
		this.ptd = ptd;
	}
	public ArrayList<String> getNucleo() {
		return nucleo;
	}
	public void setNucleo(ArrayList<String> nucleo) {
		this.nucleo = nucleo;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

}
