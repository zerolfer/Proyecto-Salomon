package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import patrones.Patrones;

import static herramientas.CridaUtils.LONGITUD_CADENAS;

public class Move1 extends AbstractNeighborStructure {

    @Override
    public Solucion encontrarSolucionEntorno(Solucion solActual, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
//        Solucion x = solActual.clone(); // partimos de la solucion actual
//        do {
//            StringBuilder sbA = new StringBuilder(x.getTurnos().get(0));
//            StringBuilder sbB = new StringBuilder(x.getTurnos().get(1));
//
//            for (int i = 3; i <= 7; i++) {
//                char charA = sbA.charAt(i);
//                char charB = sbB.charAt(i);
//                sbA.setCharAt(i, charB);
//                sbB.setCharAt(i, charA);
//            }
//
//        } while (fitness(x, e, pt, p, pa) <= fitness(solActual, e, pt, p, pa)); // hasta que haya mejora en el fitness
//        return x;
        Solucion x = solActual.clone();


//        XoRoShiRo128PlusRandom r = new XoRoShiRo128PlusRandom();
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


//            String turnoB;
//            int idx;

            // búsqueda de un controlador que le pueda dar trabajo
            for (idx2 = 0; idx2 < numMax; idx2++) {

                if (idx1 == idx2) continue;

                String turnoB = x.getTurnos().get(idx2);
                int idx = numSlotsNecesarios[1];
                String previoB = turnoB.substring(0, idx);
                String posteriorB = turnoB.substring(idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);

                if (!esPosibleAsignacion(previoB, posteriorB, numSlotsNecesarios[1])) continue;

                {
                    // obtenemos el resto de trozos
                    String medioB = turnoB.substring(idx, idx + numSlotsNecesarios[0]*LONGITUD_CADENAS);
                    String previoA = turnoA.substring(0, idx);
                    String medioA = turnoA.substring(idx, idx + numSlotsNecesarios[0]*LONGITUD_CADENAS);
                    String posteriorA = turnoA.substring(idx + numSlotsNecesarios[0]*LONGITUD_CADENAS);

                    // reconstruimos
                    String nuevoTurnoA = previoA + medioB + posteriorA;
                    String nuevoTurnoB = previoB + medioA + posteriorB;

                    // modificamos la matriz de turnos real
                    x.getTurnos().set(idx1, nuevoTurnoA);
                    x.getTurnos().set(idx2, nuevoTurnoB);
//        if (numMaxSlotsTrabajo[1] == numSlotsNecesarios[1])

                    return x;
                }

            }
        }
        return x;


//        do {
//            idx1 = r.nextInt(numMax);
//            turnoA = x.getTurnos().get(idx1);
//            numSlotsNecesarios = MetaheuristicUtil.comprobarTrabajoMinimo(turnoA);
//            num1++;
//        } while (numSlotsNecesarios[0] <= 0 || num1 < numMax);
//
//        if (num1 >= numMax) return x;
//        do {
//            idx2 = r.nextInt(numMax);
//            turnoB = x.getTurnos().get(idx2);
//            num2++;
//            idx = numSlotsNecesarios[1];
//            previoB = turnoB.substring(0, idx);
//            posteriorB = turnoB.substring(idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);
//        } while (idx1 == idx2 || !esPosibleAsignacion(previoB, posteriorB, numSlotsNecesarios[1]) || num2 < numMax);
//
//        if (num2 >= numMax) return x;

//        // obtenemos el resto de trozos
//        String medioB = turnoB.substring(idx, idx + numSlotsNecesarios[0]);
//        String previoA = turnoA.substring(0, idx);
//        String medioA = turnoA.substring(idx + numSlotsNecesarios[0]);
//        String posteriorA = turnoA.substring(idx, idx + numSlotsNecesarios[0]);
//
//        // reconstruimos
//        String nuevoTurnoA = previoA + medioB + posteriorA;
//        String nuevoTurnoB = previoB + medioA + posteriorB;
//
//        // modificamos la matriz de turnos real
//        x.getTurnos().set(num1, nuevoTurnoA);
//        x.getTurnos().set(num2, nuevoTurnoB);
////        if (numMaxSlotsTrabajo[1] == numSlotsNecesarios[1])
//
//        return x;
    }

    private boolean esPosibleAsignacion(String previo, String posterior, int idx) {
        if (MetaheuristicUtil.comprobarTrabajoMinimo(previo)[0] == 0
                && MetaheuristicUtil.comprobarTrabajoMinimo(posterior)[0] == 0)
            return true;
        return false;
//        while (turnoA.substring(idx-i)){
//
//        }
//
//
//        int numSlotsTrabajando = 0;
//        int maxCantidadSlotsTrabajando = 0;
//        for (int i = 0; i < turnoA.length(); i += 3) {
//            if (
//                    esTrabajo(turnoA.substring(i, i + 3))
//            ) {
//                numSlotsTrabajando++;
//                if (maxCantidadSlotsTrabajando < numSlotsTrabajando)
//                    maxCantidadSlotsTrabajando = numSlotsTrabajando;
//            } else {
//                if (numSlotsTrabajando > 0 && numSlotsTrabajando < 3) // 1 slot ~ 5min, trabajo minimo es 15 mim ~ 3 slots
//                    return new int[]{maxCantidadSlotsTrabajando, i};
//                else
//                    numSlotsTrabajando = 0;
//            }
//        }
//        return new int[]{-1, -1};

    }


}
