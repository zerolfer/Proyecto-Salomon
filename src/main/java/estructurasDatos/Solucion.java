package estructurasDatos;

import estructurasDatos.DominioDelProblema.Controlador;

import java.util.ArrayList;

/**
 * Objeto Solucion, este objeto contiene toda la informacion necesaria para la representacion de una solucion del problema.
 *
 * @author Tino
 */
public class Solucion implements Cloneable {
    /**
     * Lista de los controladores utilizados para la resolucion del problema.
     */
    private ArrayList<Controlador> controladores;
    /**
     * Lista con todos los turnos que cubren los controladores pertenecientes a la lista "controladores".
     */
    private ArrayList<String> turnos;
    /**
     * Si la solucion se ha sido creada con el algoritmo de inicializacion propuesto, guarda la longitud de los descansos de la solucion inicial.
     * En caso contrario este valor permanece a cero.
     */
    private int longdescansos = 0;

    /**
     * Constructor
     *
     * @param turnos        Lista de turnos que componen la solucion.
     * @param controladores Lista de controladores asignados a los turnos.
     * @param longdescansos Longitud de los descansos asocioados a la solucion inicial (parametro de control).
     */
    public Solucion(ArrayList<String> turnos, ArrayList<Controlador> controladores, int longdescansos) {
        this.turnos = turnos;
        this.controladores = controladores;
        this.longdescansos = longdescansos;
    }

    /**
     * Metodo usado para la copia de una solucion.
     */
    @SuppressWarnings({"unchecked"})
    public Solucion clone() {
        return new Solucion((ArrayList<String>) this.turnos.clone(),
                (ArrayList<Controlador>) this.controladores.clone(), this.longdescansos);
    }

    /**
     * Metodo usado para la copia de una solucion.
     */
    public Solucion shallowClone() {
        return new Solucion(this.turnos, this.controladores, this.longdescansos);
    }


    public int getLongdescansos() {
        return longdescansos;
    }

    public void setLongdescansos(int longdescansos) {
        this.longdescansos = longdescansos;
    }

    public ArrayList<Controlador> getControladores() {
        return controladores;
    }

    public void setControladores(ArrayList<Controlador> controladores) {
        this.controladores = controladores;
    }

    public ArrayList<String> getTurnos() {
        return turnos;
    }

    public void setTurnos(ArrayList<String> turnos) {
        this.turnos = turnos;
    }

}
