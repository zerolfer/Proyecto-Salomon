package algorithms.variableNeighborhoodSearch.impl;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.CridaUtils;
import herramientas.Log;
import patrones.Patrones;

import static herramientas.CridaUtils.LONGITUD_CADENAS;

public class VariableNeighborhoodSkewed extends AbstractVariableNeighborhoodSearch {


    private final double alpha;
    private final String funcionDistancia;

    private Solucion x_best;

    private double distancia;

    public VariableNeighborhoodSkewed(Parametros parametros, Patrones patrones,
                                      ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        super(parametros, patrones, parametrosAlgoritmo, entrada);
        this.alpha = parametrosAlgoritmo.VNS.getAlpha();
        this.funcionDistancia = parametrosAlgoritmo.VNS.getFuncionDistancia();
    }


    @Override
    public Solucion startExecution(Solucion x) {
        MetaheuristicUtil.orderByLazyCriteria(x);
        initTime = System.currentTimeMillis();
        contadorIteraciones = 1;
        double fitnessAnterior = -1;
        long t = 0;

        do {
            while (super.getCurrentNeighborhoodIndex() < neighborStructures.size() &&
                    (contadorIteraciones % getNumIteracionesCiclo() != 0 || porcentajeMejora > getPorcentajeMinimoMejoria())) {

                if (Log.isOn() && Log.checkIter(contadorIteraciones)) {
                    String s = "[VNS] tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s" +
                            "    |    " + "#Iteracion: " + contadorIteraciones +
                            "    |    " + "Fitness actual: " + fitness(x) +
                            "    |    vecindad actual: " + getCurrentNeighborhood() +
                            "    |    " + "numero de iteraciones sin mejora: " + porcentajeMejora;
                    Log.info(s);
                }


                Solucion x_prime_2 = vnsImplemetation(x);
                distancia = ((funcionDistancia.equals("fitness")) ? distanceFunctionA(x, x_prime_2) : distanceFunctionB(x, x_prime_2));
                Solucion x_anterior = x;
                x = neighborhoodChange(x, x_prime_2);

                x_best = keepBest(x_best, x, x_prime_2);

                Log.csvLog(contadorIteraciones++, System.currentTimeMillis() - initTime, fitness(x_best),
                        x_best.getTurnos().size(), porcentajeMejora,
                        getCurrentNeighborhoodIndex(), fitness(x_anterior), fitness(x_prime_2), distancia);

                if (Log.isOn() && Log.checkIter(contadorIteraciones)) {
                    String s = "[SVNS] x_best: " + fitness(x_best) +
                            "    |    x anterior: " + fitness(x_anterior) +
                            "    |    x nueva: " + fitness(x) +
                            "    |    x tras BL: " + fitness(x_prime_2) +
                            "    |    distancia: " + distancia + "\n";
                    Log.info(s);
                }


            }

            if (contadorIteraciones % super.getNumIteracionesCiclo() != 0) {
                // calcular porcentaje mejora
                if (fitnessAnterior == -1)
                    super.porcentajeMejora = 0;
                super.porcentajeMejora = (fitness(x) * 100 / fitnessAnterior) - 100;

                // actualizar fitness anterior
                if (fitness(x) > fitnessAnterior) // NOTE: Maximizacion
                    fitnessAnterior = fitness(x);
            }


            // se actualiza el tiempo (condición de parada)
            t = System.currentTimeMillis() - initTime;
            setCurrentNeighborhoodIndex(0);
            x = x_best;
        } while (t < getMaxTimeAllowed() &&
                (contadorIteraciones % getNumIteracionesCiclo() != 0 || porcentajeMejora > getPorcentajeMinimoMejoria()));

//        Log.debug
        Log.info("[Fin VNS] Fitness final: " + fitness(x) +
                "    |    " + "numeroIteracionesSinMejora: " + porcentajeMejora + " de " + getPorcentajeMinimoMejoria() +
                "    |    " + "tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s de " + getMaxTimeAllowed() / 1000 + "s" +
                "    |    " + "tamaño: " + x.getTurnos().size());
        Log.csvLog(contadorIteraciones, t, fitness((x_best)), x_best.getTurnos().size(), porcentajeMejora,
                getCurrentNeighborhoodIndex());

        return x_best;
    }

    @Override
    boolean checkCondicionReiniciarVecindad(Solucion x, Solucion x_prime) {

        return fitness(x_prime) + alpha * distancia > fitness(x);
    }

    @Override
    protected Solucion vnsImplemetation(Solucion x) {
        Solucion x_prime = super.getCurrentNeighborhood().generarSolucionAleatoria(x);
        Solucion x_prime_2 = super.getCurrentNeighborhood().bestImprovement(x_prime);

        if (Log.isOn() && Log.checkIter(super.contadorIteraciones)) {
            Log.info("[SVNS impl] fitness inicial: " + fitness(x) + " | \t" +
                    "fitness sol aleatoria: " + fitness(x_prime) + " | \t" +
                    "fitness sol aleatoria tras BL: " + fitness(x_prime_2));
        }

        return x_prime_2;
    }

    private double distanceFunctionA(Solucion x, Solucion x_prime) {
        return Math.abs(fitness(x) - fitness(x_prime));
    }

    private double distanceFunctionB(Solucion x, Solucion x_prime) {
        int distancia = 0;
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
        if (Log.retrieveValue() < distancia) Log.saveValue(distancia);
        return distancia / (x.getTurnos().get(0).length() / LONGITUD_CADENAS * x.getTurnos().size()); // normalizada
    }

    private Solucion keepBest(Solucion x, Solucion x_prime, Solucion x_prime_2) {

        double f_x = x == null ? -1 : fitness(x), f_x_p = fitness(x_prime), f_x_p_2 = fitness(x_prime_2);

        Solucion res = f_x >= f_x_p && f_x >= f_x_p_2 ?
                x : f_x_p >= f_x && f_x_p >= f_x_p_2 ?
                x_prime : x_prime_2;
        return res;
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