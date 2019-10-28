package herramientas.lecturaSoluciones;

import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Solucion;
import rwFiles.Lectura;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase para la lectura de soluciones de ficheros .txt.
 *
 * @author Tino
 */
public class LecturaSoluciones {
    /**
     * Metodo para la lectura de soluciones de ficheros .txt. Ademas de leer la cadena de turnos guardada en el txt, realiza una asignacion de los controladores disponibles para la resolucion del problema.
     *
     * @param path    Ruta del fichero.
     * @param entrada Entrada del problema.
     * @return Solucion.
     */
    public static Solucion leerSoluciones(String path, Entrada entrada) {
        /*Crea a partir de un fichero txt una solucion y le asigna los controladores disponibles (el formato de entrada es el mismo que se usa para la salida)*/
        String lst = Lectura.Listar(path).get(0);
        lst = lst.replaceAll(" ", "");
        String[] split = lst.split(",");
        ArrayList<String> matriz = new ArrayList<>();
        Collections.addAll(matriz, split);

        Solucion s = InicializarPoblacion.asignacionControladores(matriz, entrada, 0);
        //	Solucion s = asignacionManualControladores(matriz, entrada, 0);
        return s;
    }

    private static Solucion asignacionManualControladores(ArrayList<String> matriz, Entrada entrada, int descanso) {
        ArrayList<String> controladores = new ArrayList<>();
        ArrayList<Controlador> c = new ArrayList<>();
        for (int i = 0; i < entrada.getControladores().size(); i++) {
            Controlador cntl = entrada.getControladores().get(i);
            c.add(new Controlador(cntl.getId(), cntl.getTurno(), cntl.getNucleo(), cntl.isPTD(), cntl.isCON(), cntl.isImaginario(), cntl.getBajaAlta(), cntl.getSlotBajaAlta()));
        }
        c.get(0).setTurnoAsignado(6);
        c.get(1).setTurnoAsignado(1);
        c.get(2).setTurnoAsignado(7);
        c.get(3).setTurnoAsignado(2);
        c.get(4).setTurnoAsignado(0);
        c.get(5).setTurnoAsignado(10);
        c.get(6).setTurnoAsignado(13);
        c.get(7).setTurnoAsignado(9);
        c.get(8).setTurnoAsignado(8);
        c.get(9).setTurnoAsignado(4);
        c.get(10).setTurnoAsignado(5);
        c.get(11).setTurnoAsignado(14);
        c.get(12).setTurnoAsignado(12);
        c.get(13).setTurnoAsignado(3);
        c.get(14).setTurnoAsignado(11);

        @SuppressWarnings("unchecked")
        ArrayList<Controlador> controladores1 = (ArrayList<Controlador>) c.clone();
        Solucion ind = new Solucion(matriz, controladores1, descanso);
        return ind;
    }

}
