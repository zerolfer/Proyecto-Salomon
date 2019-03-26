package algorithms.vnsPablo;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import patrones.Restricciones;

import java.util.List;

public class FillNeigbourhoodListTask implements Runnable {
    Solucion solFin;
    String turno;
    Controlador ctrl;
    Solucion solIni;
    List<Solucion> neighbourhoodList;
    int size;
    patrones.Patrones patrones;
    Entrada entrada;
    Parametros parametros;
    ParametrosAlgoritmo_SA parametrosAlg;
    int neigbourhood;
    int nTurno;

    public FillNeigbourhoodListTask(int neigbourhood, Solucion solIni, int nTurno, List<Solucion> neighbourhoodList, int size, patrones.Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg) {

        this.solIni = solIni;
        this.nTurno = nTurno;
        this.neighbourhoodList = neighbourhoodList;
        this.size = size;
        this.entrada = entrada;
        this.patrones = patrones;
        this.parametros = parametros;
        this.parametrosAlg = parametrosAlg;
        this.neigbourhood = neigbourhood;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            Solucion solFin = (Solucion) solIni.clone();
            turno = solIni.getTurnos().get(nTurno);
            //< For each block of the turn
            for (int i = 0; i < turno.length(); i = i + 3) {
                String firstCandidate = turno.substring(i, i + size);
                ///< If candidate block i'ts "111", look for the next block
                String candidate = "";
                for (int i2 = 0; i2 < size; i2++) {
                    candidate = candidate.concat("1");
                }
                //System.err.println(candidate);

                if (firstCandidate.equals(candidate) && (neigbourhood == 0 || neigbourhood == 1)) continue;
                if (firstCandidate.equals(candidate) && neigbourhood == 3) continue;

                //int hash=ctrl.hashCode();
                int ini = nTurno;
                if (solFin.getTurnos().size() == solFin.getControladores().size()) ini = 0;

                for (int nTurno2 = ini; nTurno2 < solFin.getTurnos().size(); nTurno2++) {
                    String turno2 = solIni.getTurnos().get(nTurno2);
                    // if (hash!=ctrl2.hashCode())
                    //{
                    String secondCandidate = turno2.substring(i, i + size);
                    if (!secondCandidate.equals(candidate) && (neigbourhood == 0 || neigbourhood == 1)) continue;
                    if (secondCandidate.equals(candidate) && (neigbourhood == 3 || neigbourhood == 2)) continue;
                    try {
                        boolean cond1 = false;
                        boolean cond2 = false;
                        if (neigbourhood == 1) {

                        }

                        if (i >= 3) cond1 = firstCandidate.substring(0, 3).equals(turno2.substring(i - 3, i));
                        if (i <= turno.length() - size - 3)
                            cond2 = firstCandidate.substring(size - 3, size).equals(turno2.substring(i + size, i + size + 3));

                        if (!firstCandidate.equals(secondCandidate) &&
                                ((neigbourhood == 3 && (cond1 || cond2)) || (neigbourhood == 0 && (cond1 || cond2))
                                        || (neigbourhood == 1 && (!cond1 && !cond2)) || (neigbourhood == 2 && (!cond1 && !cond2)))
                        ) {

                            String aux2 = turno2.substring(0, i) + firstCandidate + turno2.substring(i + size);
                            String aux = turno.substring(0, i) + secondCandidate + turno.substring(i + size);
                            solFin.getTurnos().set(nTurno2, aux2);
                            solFin.getTurnos().set(nTurno, aux);
                            ;
                            //VariableNeighborhood.fillTurnsWithControllers(solFin);


                            if (parametrosAlg.getFuncionFitnessFase2().equals("fitGlobal") == true) {
                                double restriccionesIncumplidas = Restricciones.penalizacionPorRestriccionesSinTiempos(solFin, patrones, entrada, parametros);

                                if (restriccionesIncumplidas == 0) {
                                    if (solFin != null) {
                                        neighbourhoodList.add((Solucion) solFin.clone());
                                    }
                                }
                                solFin = (Solucion) solIni.clone();
                            }
                            if (parametrosAlg.getFuncionFitnessFase2().equals("reduccionControladoresYRestricciones") == true) {
                                if (solFin != null) {
                                    neighbourhoodList.add((Solucion) solFin.clone());
                                } else {
                                    System.out.println("Nullzaco");
                                    ;
                                }
                                solFin = (Solucion) solIni.clone();

                            }


                        }
                    } catch (StringIndexOutOfBoundsException exception) {
                        //System.err.println("Out of bounds exception 1");
                        solFin = (Solucion) solIni.clone();
                        continue;
                    }

                    //System.out.println( "Global fitnes in localsearch: ["+globalTotalFitnes+"]");
                    //}
                }
            }
        } catch (StringIndexOutOfBoundsException excepcion) {
            solFin = (Solucion) solIni.clone();
            //System.err.println("Out of bounds exception 2");
        }
    }
}
