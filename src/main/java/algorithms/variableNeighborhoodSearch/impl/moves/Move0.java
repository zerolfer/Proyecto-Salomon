package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import patrones.Patrones;

import static herramientas.CridaUtils.LONGITUD_CADENAS;

public class Move0 extends AbstractNeighborStructure {

    public Move0(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    @Override
    public Solucion generarSolucionAleatoria(Solucion solActual) {
        Solucion x = solActual.clone();
        XoRoShiRo128PlusRandom r = new XoRoShiRo128PlusRandom();
        int idx1, idx2, num1 = 0, num2 = 0;
        int[] numSlotsNecesarios;
        int numMax = x.getTurnos().size();
        String turnoA, turnoB;
        do {
            idx1 = r.nextInt(numMax);
            turnoA = x.getTurnos().get(idx1);
            numSlotsNecesarios = MetaheuristicUtil.comprobarTrabajoMinimo(turnoA);
            num1++;
        } while (numSlotsNecesarios[0] <= 0 || num1 < numMax);

        int idx;
        String previoB, posteriorB;
        if (num1 >= numMax) return x;
        do {
            idx2 = r.nextInt(numMax);
            turnoB = x.getTurnos().get(idx2);
            num2++;
            idx = numSlotsNecesarios[1];
            previoB = turnoB.substring(0, idx);
            posteriorB = turnoB.substring(idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);
        } while (idx1 == idx2 || !esPosibleAsignacion(previoB, posteriorB) || num2 < numMax);

        if (num2 >= numMax) return x;

        doChange(x, turnoA, turnoB, previoB, posteriorB, idx, numSlotsNecesarios, idx1, idx2);

        return x;

    }

    @Override
    public Object[] busquedaLocal(Solucion solActual) {
        Solucion x = solActual.clone();
//        int num1 = 0;
        int idx1, idx2;
        String turnoA;
        int[] numSlotsNecesarios;

        int numMax = x.getTurnos().size();

        // búsqueda de un controlador sin el trabajo mínimo
        for (idx1 = 0; idx1 < numMax; idx1++) {

            turnoA = x.getTurnos().get(idx1);
            numSlotsNecesarios = MetaheuristicUtil.comprobarTrabajoMinimo(turnoA);
            if (numSlotsNecesarios[0] <= 0) continue;

            // búsqueda de un controlador que le pueda dar trabajo
            for (idx2 = 0; idx2 < numMax; idx2++) {

                if (idx1 == idx2) continue;

                String turnoB = x.getTurnos().get(idx2);
                int idx = numSlotsNecesarios[1];
                String previoB = turnoB.substring(0, idx);
                String posteriorB = turnoB.substring(idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);

                if (!esPosibleAsignacion(previoB, posteriorB)) continue;

                doChange(x, turnoA, turnoB, previoB, posteriorB, idx, numSlotsNecesarios, idx1, idx2);

                return new Object[]{x, fitness(x)};

            }
        }
        return new Object[]{x, fitness(x)};
    }

    @Override
    protected Solucion buscarSolucion(Solucion x) {
        return null;
    }

    private boolean esPosibleAsignacion(String previo, String posterior) {
        if (MetaheuristicUtil.comprobarTrabajoMinimo(previo)[0] == 0
                && MetaheuristicUtil.comprobarTrabajoMinimo(posterior)[0] == 0)
            return true;
        return false;
    }

    private void doChange(Solucion x, String turnoA, String turnoB, String previoB, String posteriorB,
                          int idx, int[] numSlotsNecesarios, int idx1, int idx2) {
        // obtenemos el resto de trozos
        String medioB = turnoB.substring(idx, idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);
        String previoA = turnoA.substring(0, idx);
        String medioA = turnoA.substring(idx, idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);
        String posteriorA = turnoA.substring(idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);

        // reconstruimos
        String nuevoTurnoA = previoA + medioB + posteriorA;
        String nuevoTurnoB = previoB + medioA + posteriorB;

        // modificamos la matriz de turnos real
        x.getTurnos().set(idx1, nuevoTurnoA);
        x.getTurnos().set(idx2, nuevoTurnoB);
    }

    @Override
    void doChange(Solucion x, String turnoA, String turnoB, int desde, int hasta, int idx1, int idx2) {
    }
}