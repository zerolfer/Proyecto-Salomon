package algorithms.variableNeighborhoodSearch.impl.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import patrones.Patrones;

import java.util.ArrayList;

import static algorithms.MetaheuristicUtil.esDescanso;
import static algorithms.MetaheuristicUtil.esTrabajo;
import static herramientas.CridaUtils.*;

public class Move1 extends AbstractNeighborStructure {


    private static final int LIMITE_LONGITUD_INTERVALO = 18;

    Move1(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    @Override
    public Solucion generarSolucionAleatoria(Solucion solucionInicial) {
        Solucion x = solucionInicial.clone();
        XoRoShiRo128PlusRandom random = new XoRoShiRo128PlusRandom();
        ArrayList<String> turnos = x.getTurnos();

        int idx1, idx2;
        do {
            idx1 = random.nextInt(turnos.size());
            idx2 = random.nextInt(turnos.size());
        } while (!doMovement(x, turnos, idx1, idx2)); //TODO: probar que el movimiento se hace


        return x;
    }

    @Override
    public Solucion buscarSolucion(Solucion x) {
//        Solucion x = solucionInicial.clone();
        ArrayList<String> turnos = x.getTurnos();

////         En la busqueda local, iteramos repetidas veces hasta que no haya mejora
//        while (super.esMejorQue(x, solucionInicial)) {
//
        for (int idx1 = 0; idx1 < turnos.size(); idx1++) // TODO probar que esto funciona
            for (int idx2 = idx1 + 1; idx2 < turnos.size(); idx2++) // debe estar por debajo
                if (doMovement(x, turnos, idx1, idx2)) return x;

//        }
        return x;
    }

    /**
     * @return true si se produce el intercambio, false en caso contrario
     */
    private boolean doMovement(Solucion x, ArrayList<String> turnos, int idx1, int idx2) {
        String turnoA = turnos.get(idx1);
        String turnoB = turnos.get(idx2);

        // Calculamos la longitud
        for (int desde = getSlotMomentoActual() * LONGITUD_CADENAS; desde <= turnos.get(idx1).length() - LONGITUD_CADENAS; desde += LONGITUD_CADENAS) {
            int hasta = desde + LONGITUD_CADENAS;
            while (hasta < turnos.get(idx1).length() && esTrabajo(turnoA.substring(desde, hasta))
                    && esDescanso(turnoB.substring(desde, hasta)) && hasta - desde < LIMITE_LONGITUD_INTERVALO)
                hasta += LONGITUD_CADENAS;

            // en este punto ya tenemos calculado el inicio y fin del intervalo
            // ahora realizamos el cambio si es posible
            if (comprobarRestricciones(turnoA, turnoB, idx2, desde, hasta)) {
                doChange(x, turnoA, turnoB, desde, hasta, idx1, idx2);
                return true;
            }
        }
        return false;
    }

    private boolean comprobarRestricciones(String turnoA, String turnoB, int idx2, int desde, int hasta) {
        // en este caso la única restricción es que el trabajo anterior del segundo
        // controlador sea del mismo sector y posicion que el nuevo a incorporar

        String sector = turnoA.substring(desde, desde + LONGITUD_CADENAS);

        if (sector.equals(STRING_NO_TURNO)) return false;
        if (sector.equals(STRING_DESCANSO)) return false;

        // si estamos al principio, solo podemos mirar el final
        if (desde < LONGITUD_CADENAS)
            return checkFinal(turnoB, hasta, sector);

        // si estamos el final, solo podemos mirar el principio
        if (hasta + LONGITUD_CADENAS >= turnoB.length())
            return checkInicio(turnoB, desde, sector);

        // Si estamos en el medio, cualquiera de ellos nos sirve
        return checkInicio(turnoB, desde, sector) || checkFinal(turnoB, hasta, sector);
    }

    private boolean checkInicio(String turnoB, int desde, String sector) {
        return turnoB.substring(desde - LONGITUD_CADENAS, desde).equals(sector);
    }

    private boolean checkFinal(String turnoB, int hasta, String sector) {
        return turnoB.substring(hasta, hasta + LONGITUD_CADENAS).equals(sector);
    }

}
