import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.FitnessFase3;
import patrones.Patrones;
import patrones.Restricciones;

import static fitnessFunction.Fitness.*;

public class test {

    public static double[] fitNUEVO(Solucion individuo, Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo paramAlg) {
        double fit = 0, f1 = 0, f2 = 0, f3 = 0, f4 = 0;
        double tmn = individuo.getTurnos().size();
        double nSlots = entrada.getSectorizacion().size();
        double maxRestricciones = 0;
        if (entrada.getTurno().getNombre().equalsIgnoreCase("Noche")) {
            maxRestricciones = 20 * tmn; //(16 * tmn)+ (4*(individuo.getTurnos().get(0).length()/3)*0.5*tmn);
        } else {
            maxRestricciones = 18 * tmn; //(14 * tmn)+ (4*(individuo.getTurnos().get(0).length()/3)*0.5*tmn);
        }


        //double r = Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros);
        double r = Restricciones.comprobarRestriccionesEnParalelo(individuo, patrones, entrada, parametros);

        //System.out.println(maxRestricciones);
        ///if(tempR<r){tempR=r;System.out.println(tempR);}
        double cReq = entrada.getControladores().size();
        if (cReq >= tmn) { // Cuando se alcanza el numero de controladores requerido, se deja de utilizar la funcion fitness f1.
            f2 = (maxRestricciones - r) / maxRestricciones;
            f3 = continuidadMomentoCambio(individuo, entrada);
            f4 = FitnessFase3.estadillos(individuo.getControladores().size(),
                    individuo.getTurnos().get(0).length() / 3,
                    individuo.getTurnos(),
                    entrada.getSlotMomentoActual());
            fit = 1 * paramAlg.getPonderacionFitness1() +
                    f2 * paramAlg.getPonderacionFitness2() +
                    f3 * paramAlg.getPonderacionFitness3() +
                    f4 * paramAlg.getPonderacionFitness4();

            if (fit < 0 || fit > 1) {
                System.out.println("Error, el fitness deberia ser un valor entre 0 y 1: " + fit);
            }
        } else {
            /*Inicio Normalizacion f1*/
            double cMax = entrada.getListaSectoresAbiertos().size() * 3;
            double f1Max = 0;
            double f1Min = 0;
            for (int i = 1; i <= tmn; i++) {
                double cc = Math.floor(getCtrlsCompletos());
                if (i < cc)
                    f1Min += nSlots * (i * 2);

                if (!(i <= (tmn - cc)))
                    f1Max += nSlots * (i * 2);

                int[] sum = slotsClassification(individuo.getTurnos().get(i));
                f1 += (sum[1] * (((i - 1) * 2) + 1));
            }
            f1Min = f1Min / (cMax * cMax);
            f1Max = f1Max / (cReq * cReq);
            /*Fin Normalizacion f1*/

            f1 = f1 / (tmn * tmn);
            f1 = (f1 - f1Min) / (f1Max - f1Min); //igual a: 1 - ((f1Max - f1) / (f1Max - f1Min));
            f2 = (maxRestricciones - r) / maxRestricciones;
            f3 = continuidadMomentoCambio(individuo, entrada);
            f4 = FitnessFase3.estadillos(individuo.getControladores().size(),
                    individuo.getTurnos().get(0).length() / 3,
                    individuo.getTurnos(),
                    entrada.getSlotMomentoActual());
            fit = f1 * paramAlg.getPonderacionFitness1() +
                    f2 * paramAlg.getPonderacionFitness2() +
                    f3 * paramAlg.getPonderacionFitness3() +
                    f4 * paramAlg.getPonderacionFitness4();
            if (fit < 0 || fit > 1) {
                System.out.println("Error, el fitness deberia ser un valor entre 0 y 1: " + fit + " (" + f1 + ", " + f2 + ", " + f3 + ", " + f4 + ")");
            }
        }

        return new double[]{fit, f1, f2, f3, f4};
    }

    public static double[] fitANTIGUO(Solucion individuo, Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo paramAlg) {
        double fit = 0, f1 = 0, f2 = 0, f3 = 0, f4 = 0;
        double tmn = individuo.getTurnos().size();
        double nSlots = entrada.getSectorizacion().size();
        double maxRestricciones = 0;
        if (entrada.getTurno().getNombre().equalsIgnoreCase("Noche")) {
            maxRestricciones = 20 * tmn; //(16 * tmn)+ (4*(individuo.getTurnos().get(0).length()/3)*0.5*tmn);
        } else {
            maxRestricciones = 18 * tmn; //(14 * tmn)+ (4*(individuo.getTurnos().get(0).length()/3)*0.5*tmn);
        }


        //double r = Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros);
        double r = Restricciones.comprobarRestriccionesEnParalelo(individuo, patrones, entrada, parametros);

        //System.out.println(maxRestricciones);
        ///if(tempR<r){tempR=r;System.out.println(tempR);}
        double cReq = entrada.getControladores().size();
        if (cReq >= tmn) { // Cuando se alcanza el numero de controladores requerido, se deja de utilizar la funcion fitness f1.
            f2 = (maxRestricciones - r) / maxRestricciones;
            f3 = continuidadMomentoCambio(individuo, entrada);
            f4 = FitnessFase3.estadillos(individuo.getControladores().size(),
                    individuo.getTurnos().get(0).length() / 3,
                    individuo.getTurnos(),
                    entrada.getSlotMomentoActual());
            fit =   1 * paramAlg.getPonderacionFitness1() +
                    f2 * paramAlg.getPonderacionFitness2() +
                    f3 * paramAlg.getPonderacionFitness3() +
                    f4 * paramAlg.getPonderacionFitness4();

            if (fit < 0 || fit > 1) {
                System.out.println("Error, el fitness deberia ser un valor entre 0 y 1: " + fit);
            }
        } else {
            /*Inicio Normalizacion f1*/
            double cMax = entrada.getListaSectoresAbiertos().size() * 3;
            double f1Max = 0;
            double f1Min = 0;
            for (int i = 1; i <= tmn; i++) {
                if (i < Math.floor(getCtrlsCompletos())) {
                    f1Min += nSlots * (i * 2);
                }
            }
            f1Min = f1Min / (cMax * cMax);
            for (int i = 1; i <= tmn; i++) {
                if (!(i <= (tmn - Math.floor(getCtrlsCompletos())))) {
                    f1Max += nSlots * (i * 2);
                }
            }
            f1Max = f1Max / (cReq * cReq);
            /*Fin Normalizacion f1*/
            for (int i = 0; i < tmn; i++) {
                int[] sum = slotsClassification(individuo.getTurnos().get(i));
                f1 += (sum[1] * ((i * 2) + 1));
            }
            f1 = f1 / (tmn * tmn);
            f1 = (f1 - f1Min) / (f1Max - f1Min); //igual a: 1 - ((f1Max - f1) / (f1Max - f1Min));
            f2 = (maxRestricciones - r) / maxRestricciones;
            f3 = continuidadMomentoCambio(individuo, entrada);
            f4 = FitnessFase3.estadillos(individuo.getControladores().size(),
                    individuo.getTurnos().get(0).length() / 3,
                    individuo.getTurnos(),
                    entrada.getSlotMomentoActual());
            fit =   f1 * paramAlg.getPonderacionFitness1() +
                    f2 * paramAlg.getPonderacionFitness2() +
                    f3 * paramAlg.getPonderacionFitness3() +
                    f4 * paramAlg.getPonderacionFitness4();
            if (fit < 0 || fit > 1) {
                System.out.println("Error, el fitness deberia ser un valor entre 0 y 1: " + fit + " (" + f1 + ", " + f2+ ", " + f3 + ", " + f4+")");
            }
        }

        return new double[]{fit, f1, f2, f3, f4};
    }


}
