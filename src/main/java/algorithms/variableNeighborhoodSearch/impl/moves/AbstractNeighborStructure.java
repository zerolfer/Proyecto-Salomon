package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import fitnessFunction.Fitness;
import patrones.Patrones;

abstract class AbstractNeighborStructure implements NeighborStructure {
    private Entrada entrada;
    private Patrones patrones;
    private Parametros parametros;
    private ParametrosAlgoritmo parametrosAlgoritmo;

    AbstractNeighborStructure(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        this.entrada = entrada;
        this.patrones = patrones;
        this.parametros = parametros;
        this.parametrosAlgoritmo = parametrosAlgoritmo;
    }

    double fitness(Solucion solucion) {
        return DeciderFitnessFunction.switchFitnessF(solucion, patrones, entrada, parametros, parametrosAlgoritmo)[0];
    }

    @Override
    public Object[] busquedaLocal(Solucion solucionInicial) {
        Solucion x = solucionInicial.clone();
        Solucion x_prime = x;
        // En la busqueda local, iteramos repetidas veces hasta que no haya mejora
        while (true) {
            x_prime = buscarSolucion(x);
            double f_x = fitness(x);
            double f_x_prime = fitness(x_prime);
            if (f_x_prime < f_x) // si no es mejor... NOTE: maximización
                return new Object[]{x, f_x};
            x = x_prime;
        }
    }

    protected abstract Solucion buscarSolucion(Solucion x);

    //    @Override
//    public Solucion buscarSolucion(Solucion solActual, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
//        Solucion x = solActual.clone();
//
//        XoRoShiRo128PlusRandom r = new XoRoShiRo128PlusRandom();
//
//        int idx1, idx2, num1 = 0, num2 = 0;
//
//        int[] numSlotsNecesarios;
//        int numMax = x.getTurnos().size();
//        String turnoA, turnoB;
//        do {
//            idx1 = r.nextInt(numMax);
//            turnoA = x.getTurnos().get(idx1);
//            numSlotsNecesarios = MetaheuristicUtil.comprobarTrabajoMinimo(turnoA);
//            num1++;
//        } while (numSlotsNecesarios[0] <= 0 || num1 < numMax);
//
//        int idx;
//        String previoB, posteriorB;
//        if (num1 >= numMax) return x;
//        do {
//            idx2 = r.nextInt(numMax);
//            turnoB = x.getTurnos().get(idx2);
//            num2++;
//            idx = numSlotsNecesarios[1];
//            previoB = turnoB.substring(0, idx);
//            posteriorB = turnoB.substring(idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);
//        } while (idx1 == idx2 || !esPosibleAsignacion(previoB, posteriorB) || num2 < numMax);
//
//        if (num2 >= numMax) return x;
//
//        doChange(x, turnoA, turnoB, previoB, posteriorB, idx, numSlotsNecesarios, idx1, idx2);
//
//        return x;
//
//    }

//    protected abstract void doChange(int[] idxs);
//
//    @Override
//    public Solucion busquedaLocal(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
//        return null;
//    }

    void doChange(Solucion x, String turnoA, String turnoB,
                  int desde, int hasta, int idx1, int idx2) {

        // obtenemos el resto de trozos
        String previoB = turnoB.substring(0, desde);
        String posteriorB = turnoB.substring(hasta);
        String medioB = turnoB.substring(desde, hasta);

        String previoA = turnoA.substring(0, desde);
        String medioA = turnoA.substring(desde, hasta);
        String posteriorA = turnoA.substring(hasta);

        // reconstruimos
        String nuevoTurnoA = previoA + medioB + posteriorA;
        String nuevoTurnoB = previoB + medioA + posteriorB;

        // modificamos la matriz de turnos real
        x.getTurnos().set(idx1, nuevoTurnoA);
        x.getTurnos().set(idx2, nuevoTurnoB);
    }

    protected boolean esMejorQue(Solucion x, Solucion solucionInicial) {
        return Fitness.esMejorQue(patrones, entrada, parametros, parametrosAlgoritmo, x, solucionInicial);
    }
}
