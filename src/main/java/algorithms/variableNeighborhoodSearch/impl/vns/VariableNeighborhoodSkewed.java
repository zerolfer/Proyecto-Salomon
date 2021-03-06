package algorithms.variableNeighborhoodSearch.impl.vns;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.impl.AbstractVariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.CridaUtils;
import herramientas.Log;
import patrones.Patrones;
import patrones.Restricciones;

import static herramientas.CridaUtils.LONGITUD_CADENAS;

public class VariableNeighborhoodSkewed extends AbstractVariableNeighborhoodSearch {


    private final double alpha;
    private final String funcionDistancia;

    private Solucion x_best;

    private double distancia;

    VariableNeighborhoodSkewed(Parametros parametros, Patrones patrones,
                               ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        super(parametros, patrones, parametrosAlgoritmo, entrada);
        this.alpha = parametrosAlgoritmo.VNS.getAlpha();
        this.funcionDistancia = parametrosAlgoritmo.VNS.getFuncionDistancia();
    }


    @Override
    public Solucion startExecution(Solucion x) {
        MetaheuristicUtil.reordenarYEliminarTurnos(x);
        initTime = System.currentTimeMillis();
        contadorIteraciones = 0;
        int contadorReinicios = 0;
//        double fitnessAnterior = -1;
//        double fitnessMejor = -1;
        double fitnessMejorCiclo = -1;
        long t = 0;

        do {
            while (super.checkCondicionParadaTiempo(t) && neighborhoodSet.hayEntornosSinUsar() &&
                    super.checkCondicionParadaPorcentajeMejora()) {

                if (Log.isOn() && Log.checkIter(contadorIteraciones)) {
                    double[] fit = fitness(x);
                    String s = "[VNS] tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s" +
                            "\t|\t" + "#Iteracion: " + contadorIteraciones +
                            "\t|\t" + String.format("Fitness actual: %.16f (%.4f, %.4f, %.4f, %.4f)", fitness(x)[0], fit[1], fit[2], fit[3], fit[4]) +
                            "\t|\t" + "vecindad actual: " + getCurrentNeighborhood() +
                            "\t|\t" + "porcentaje de mejora: " + porcentajeMejora;
                    Log.info(s);


                }


                Solucion x_prime_2 = vnsImplemetation(x);
                distancia = ((funcionDistancia.equals("fitness")) ? distanceFunctionA(x, x_prime_2) : distanceFunctionB(x, x_prime_2));

                // System.out.println("Funcion: " + funcionDistancia + "| valor: " + distancia);

                Solucion x_anterior = x;
                x = neighborhoodChange(x, x_prime_2);

                x_best = keepBest(x_best, x, x_prime_2);


                if (Log.isOn() && Log.checkIter(contadorIteraciones)) {
                    String s = "[SVNS] x_best: " + fitness(x_best)[0] +
                            "\t|\t" + "x anterior: " + fitness(x_anterior)[0] +
                            "\t|\t" + "x nueva: " + fitness(x)[0] +
                            "\t|\t" + "x tras BL: " + fitness(x_prime_2)[0] +
                            "\t|\t" + "distancia: " + distancia;
                    Log.info(s);
                }

                // se actualiza el tiempo (condición de parada)
                t = System.currentTimeMillis() - initTime;

                double[] fit = fitness(x);
                Log.csvLog(contadorIteraciones, t, fit[0],
                        fit[1], fit[2], fit[3], fit[4],
                        x.getTurnos().size(), porcentajeMejora,
                        neighborhoodSet.hayEntornosSinUsar()?getCurrentNeighborhood():"Reinicio",
                        /* fitness(x_anterior)[0], fitness(x_prime_2)[0],*/
                        fitness(x_best)[0], distancia, -1, -contadorReinicios);

                // solo se recalcula cada getNumIteracionesCiclo() iteraciones
                if (contadorIteraciones % super.getNumIteracionesCiclo() == 0) {
                    // calcular porcentaje mejora
                    if (fitnessMejorCiclo == -1)
                        super.porcentajeMejora = 100;
                    else{
                        if (fitnessMejorCiclo - fitness(x_best)[0] >= 0)
                            super.porcentajeMejora = Math.abs(fitnessMejorCiclo - fitness(x_best)[0]) * 100; //(fitness(x)[0] * 100 / fitnessAnterior) - 100;
                        else super.porcentajeMejora = 0;
                    }

                    // actualizar fitness anterior por el actual
//                    fitnessAnterior = fitness(x_best)[0];
                    fitnessMejorCiclo = fitness(x_best)[0];

                }
                // actualizar fitness mejor
//                if (fitness(x_best)[0] > fitnessMejor) // NOTE: Maximizacion
//                    fitnessMejor = fitness(x_best)[0];
                if (fit[0] > fitnessMejorCiclo) // NOTE: Maximizacion
                    fitnessMejorCiclo = fit[0];

                contadorIteraciones++;
                Log.info("", contadorIteraciones);
            }

            getNeighborSet().reset();
            contadorReinicios++;
            x = x_best;
        } while (checkCondicionParadaTiempo(t) && checkCondicionParadaPorcentajeMejora());

//        Log.debug
        double[] fit = fitness(x_best);
        double numRestricciones = Restricciones.penalizacionPorRestricciones(x_best, getPatrones(), getEntrada(), getParametros());
        Log.debug("[Fin VNS] Fitness final: " + fit[0] +
                "    |    " + "Fitness desglosado: [" + fit[1] + ", " + fit[2] + ", " + fit[3] + ", " + fit[4] + "]" +
                "    |    " + "iteraciones totales: " + contadorIteraciones +
                "    |    " + "porcentajeMejora: " + porcentajeMejora + " de " + getPorcentajeMinimoMejoria() +
                "    |    " + "tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s de " + getMaxTimeAllowed() / 1000 + "s" +
                "    |    " + "tamaño: " + x.getTurnos().size() +
                "    |    " + "Numero de reinicios: " + contadorReinicios +
                "    |    " + "Restricciones: " + numRestricciones + "\n");
        Log.csvLog(contadorIteraciones, t, fit[0], fit[1], fit[2], fit[3], fit[4], x_best.getTurnos().size(), porcentajeMejora,
                getNeighborSet().getCurrentNeighborhood(), fitness(x_best)[0], distancia, numRestricciones, contadorReinicios);

        return x_best;
    }

    @Override
    protected boolean checkCondicionReiniciarVecindad(Solucion x, Solucion x_prime) {

        return fitness(x_prime)[0] + alpha * distancia > fitness(x)[0];
    }

    @Override
    protected Solucion vnsImplemetation(Solucion x) {
        Solucion x_prime = getNeighborSet().getCurrentNeighborhood().generarSolucionAleatoria(x);
        Solucion x_prime_2 = getNeighborSet().getCurrentNeighborhood().bestImprovement(x_prime);

        if (Log.isOn() && Log.checkIter(super.contadorIteraciones)) {
            Log.info("[SVNS impl] fitness inicial: " + fitness(x)[0] + " | \t" +
                    "fitness sol aleatoria: " + fitness(x_prime)[0] + " | \t" +
                    "fitness sol aleatoria tras BL: " + fitness(x_prime_2)[0]);
        }

        return x_prime_2;
    }

    private double distanceFunctionA(Solucion x, Solucion x_prime) {
        return Math.abs(fitness(x)[0] - fitness(x_prime)[0]);
    }

    private double distanceFunctionB(Solucion x, Solucion x_prime) {
        double distancia = 0;
        for (int i = 0; i < x.getTurnos().size(); i++) {
            Controlador cA = CridaUtils.obtenerControladorTurno(i, x.getControladores());
            if (cA == null) continue;
            String turnoA = x.getTurnos().get(cA.getTurnoAsignado());
            int length = turnoA.length();

            Controlador cB = null;
            for (Controlador c : x_prime.getControladores()) {
                if (c.getId() == (cA.getId())) {
                    cB = c;
                    break;
                }
            }
            assert cB != null;
            String turnoB = x_prime.getTurnos().get(cB.getTurnoAsignado());

            for (int j = 0; j < length - LONGITUD_CADENAS; j += LONGITUD_CADENAS) {
                String slotA = turnoA.substring(j, j + LONGITUD_CADENAS);
                String slotB = turnoB.substring(j, j + LONGITUD_CADENAS);
                if (!slotA.equals(slotB)) distancia++;
            }
            // tratar el ultimo slot
            String slotA = turnoA.substring(length - LONGITUD_CADENAS);
            String slotB = turnoB.substring(length - LONGITUD_CADENAS);
            if (!slotA.equals(slotB)) distancia++;
        }
//        if (Log.retrieveValue() < distancia) Log.saveValue(distancia); FIXME
        return distancia / 400; // NOTE: numero estimado de el conjunto de instancias disponibles (normalización)
//         return distancia / ((float) x.getTurnos().get(0).length() / LONGITUD_CADENAS * x.getTurnos().size()); // normalizada
    }

    private Solucion keepBest(Solucion x, Solucion x_prime, Solucion x_prime_2) {

        double f_x = x == null ? -1 : fitness(x)[0], f_x_p = fitness(x_prime)[0], f_x_p_2 = fitness(x_prime_2)[0];

        return f_x >= f_x_p && f_x >= f_x_p_2 ?
                x : f_x_p >= f_x && f_x_p >= f_x_p_2 ?
                x_prime : x_prime_2;
//
//
//
//        if (x == null) {
//            if (f_x_p > f_x_p_2) return x_prime;
//            else return x_prime_2;
//        }
//        if (f_x > f_x_p && f_x > f_x_p_2)
//            return x;
//        if (f_x_p > f_x && f_x_p > f_x_p_2) return x_prime;
//        return x_prime_2;


//        if (x == null) return x_prime;
//        if (fitness(x_prime) > fitness(x))
//            return x_prime;
//        return x;
    }

    @Override
    public String toString() {
        return "SVNS";
    }
}