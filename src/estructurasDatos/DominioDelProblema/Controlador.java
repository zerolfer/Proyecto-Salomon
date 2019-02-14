package estructurasDatos.DominioDelProblema;

/**
 * Objeto Controlador, el cual contiene toda la informacion de interes del mismo.
 * @author Tino
 *
 */
public class Controlador implements Cloneable{
	/**
	 * ID asignado a un controlador para su identificacion.
	 */
	private int id;
	/**
	 * Turno de trabajo en el que el controlador realiza su jornada (Ma�ana Largo, Tarde Corto, Noche...).
	 */
	private String turno;
	/**
	 * Nucleo que el controldor tiene asignado para realizar su trabajo.
	 */
	private String nucleo;
	/**
	 *  Acreditacion del controlador (Si el controlador tiene esta acreditacion su valor sera true).
	 */
	private boolean PTD;
	/**
	 *  Acreditacion del controlador (Si el controlador tiene esta acreditacion su valor sera true).
	 */
	private boolean CON;
	/**
	 * Posicion de la fila en la matriz de turnos que el controlador tiene asignada para realizar su jornada.
	 * Este valor se inicia como -1 hasta que se le asigne un turno.
	 */
	private int turnoAsignado;
	/**
	 * Si el controlador esta cubriendo un sector en un turno de noche, es posible que solo pueda cubrir este unico sector, para indicar si esto se produce (lo cual depende del sector) se utiliza esta variable.
	 */
	private int turnoNoche;
	/**
	 *  Parametro para marcar cuando se utilizan mas controladores de los disponibles. "Fase pruebas"
	 */
	private boolean imaginario;
	/**
	 * Constructor
	 * @param id ID asignado a un controlador para su identificacion.
	 * @param turno Turno de trabajo en el que el controlador realiza su jornada (Ma�ana Largo, Tarde Corto, Noche...).
	 * @param nucleo Nucleo que el controldor tiene asignado para realizar su trabajo.
	 * @param pTD Acreditacion del controlador (Si el controlador tiene esta acreditacion su valor sera true).
	 * @param cON Acreditacion del controlador (Si el controlador tiene esta acreditacion su valor sera true).
	 * @param imaginario Parametro para marcar cuando se utilizan mas controladores de los disponibles. "Fase pruebas"
	 */
	public Controlador(int id, String turno, String nucleo, boolean pTD, boolean cON, boolean imaginario) {
		this.id = id;
		this.turno = turno;
		this.nucleo = nucleo;
		this.PTD = pTD;
		this.CON = cON;
		this.turnoAsignado= -1;
		this.setTurnoNoche(0);
		this.imaginario = imaginario;
	}
    public Object clone() {
        Controlador con = new Controlador(this.id,this.turno,this.nucleo,this.PTD,this.CON, this.imaginario);
        con.setTurnoAsignado(this.turnoAsignado);
        con.setTurnoNoche(this.turnoNoche);
        return con;
    }
	public boolean isImaginario() {
		return imaginario;
	}
	public void setImaginario(boolean imaginario) {
		this.imaginario = imaginario;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTurno() {
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	public String getNucleo() {
		return nucleo;
	}
	public void setNucleo(String nucleo) {
		this.nucleo = nucleo;
	}
	public boolean isPTD() {
		return PTD;
	}
	public void setPTD(boolean pTD) {
		PTD = pTD;
	}
	public boolean isCON() {
		return CON;
	}
	public void setCON(boolean cON) {
		CON = cON;
	}

	public int getTurnoAsignado() {
		return turnoAsignado;
	}

	public void setTurnoAsignado(int turnoAsignado) {
		this.turnoAsignado = turnoAsignado;
	}
	public int getTurnoNoche() {
		return turnoNoche;
	}
	public void setTurnoNoche(int turnoNoche) {
		this.turnoNoche = turnoNoche;
	}
	

}
