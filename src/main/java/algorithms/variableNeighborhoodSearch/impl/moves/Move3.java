package algorithms.variableNeighborhoodSearch.impl.moves;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

import static herramientas.CridaUtils.LONGITUD_CADENAS;
import static herramientas.CridaUtils.STRING_DESCANSO;

public class Move3 extends AbstractNeighborStructure {
    public Move3(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    /**
     * case "movimiento3": Intervalo aleatorio entre max y min con multiplos de 3 slots
     * <br/>
     * Este movimiento genera una nueva solucion a partir de un cambio de un intervalo de trabajo entre
     * dos controladores,
     * a la hora de escoger el intervalo se utiliza una granularidad aleatoria que sea multiplo de 3
     * slots (15 mins) entre un maximo y minimo simpre y si esto no genera un individuo valido,
     * se prueba a modificar el segundo controlador (el que acepta el intervalo), si sigue sin generar un
     * individuo valido,
     * se reduce en uno el tamaño del intervalo, asi hasta llegar a una granularidad minima, despues de
     * esto se modifica el primer controlador.
     * <p>
     * Cuando se consigue hacer un intercambio, se retorna la nueva solucion
     */
    @Override
    protected Solucion buscarSolucion(Solucion x) {
        Controlador c1 = x.getControladores().get(random.nextInt(x.getControladores().size()));
        Controlador c2 = x.getControladores().get(random.nextInt(x.getControladores().size()));

        List<Integer[]> trabajosC1 = getintervalos(x.getTurnos().get(c1.getTurnoAsignado()));

        return x;
    }

    private List<Integer[]> getintervalos(String turno) {
        List<Integer[]> res = new ArrayList<>();

        // recorremos el turno
        int i = 0;
        while (turno.substring(i, i + LONGITUD_CADENAS).equals(STRING_DESCANSO)) i += 3; // saltamos los descansos
        for (; i < turno.length(); i += 3) {

            
            res.add(turno.substring(i, i + LONGITUD_CADENAS));
        }

        return new ArrayList<>();
    }

    @Override
    public Solucion generarSolucionAleatoria(Solucion x) {
        return null;
    }

}
