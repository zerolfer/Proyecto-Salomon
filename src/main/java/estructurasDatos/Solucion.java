package estructurasDatos;

import estructurasDatos.DominioDelProblema.Controlador;

import java.util.ArrayList;
import java.util.Objects;

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

    private boolean yaOrdenado = true;

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
        ArrayList<String> turnos = (ArrayList<String>) this.getTurnos().clone();
        ArrayList<Controlador> cnew = new ArrayList<>();
        this.getControladores().forEach(c -> cnew.add(c.clone()));
        return new Solucion(turnos, cnew, this.getLongdescansos()).setYaOrdenado(this.yaOrdenado);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solucion solucion = (Solucion) o;
        return longdescansos == solucion.longdescansos &&
                controladores.equals(solucion.controladores) &&
                turnos.equals(solucion.turnos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(controladores, turnos, longdescansos);
    }

    public boolean isYaOrdenado() {
        return yaOrdenado;
    }

    public Solucion setYaOrdenado(boolean yaOrdenado) {
        this.yaOrdenado = yaOrdenado;
        return this;
    }
}
