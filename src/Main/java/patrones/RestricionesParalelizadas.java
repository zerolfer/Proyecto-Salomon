package patrones;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.Solucion;

public class RestricionesParalelizadas implements Runnable {
    int numeroRestriccion;
    Solucion individuo;
    Patrones patrones;
    Entrada entrada;
    Parametros parametros;
    double n;

    public RestricionesParalelizadas(int numeroRestriccion, Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros, double n) {
        this.numeroRestriccion = numeroRestriccion;
        this.individuo = individuo;
        this.patrones = patrones;
        this.entrada = entrada;
        this.parametros = parametros;
        this.n = n;
    }

    @Override
    public void run() {
        switch (numeroRestriccion) {
            case 1:
                n = Restricciones.comprobarNucleoTrabajo(individuo, patrones);
                break;
            case 2:
                n = Restricciones.comprobarTipoSector(individuo, patrones);
                break;
            case 3:
                n = Restricciones.comprobarPorcentajeDescanso(individuo, entrada, entrada.getTurno(), parametros);
                break;
            case 4:
                n = Restricciones.comprobarSectoresAbiertosNoche(individuo.getTurnos(), individuo.getControladores(), patrones);
                break;
            case 5:
                n = Restricciones.comprobarTrabajoMaximoConsecutivo(individuo.getTurnos(), parametros);
                break;
            case 6:
                n = Restricciones.comprobarControladorTurnoCorto(individuo, entrada);
                break;
            case 7:
                n = Restricciones.comprobarVentanaTrabajoDescanso(individuo.getTurnos(), parametros);
                break;
            case 8:
                n = Restricciones.comprobarCambioPosicion(individuo.getTurnos(), entrada.getMatrizAfinidad(), entrada.getListaSectores());
                break;
            case 9:
                n = Restricciones.comprobarTrabajoMinimoConsecutivo(individuo.getTurnos(), parametros);
                break;
            case 10:
                n = Restricciones.comprobarDescansoMinimoConsecutivo(individuo.getTurnos(), parametros);
                break;
            case 11:
                n = Restricciones.comprobarTrabajoPosicionMinimoConsecutivoNoRegex(individuo.getTurnos(), parametros);
                break;
            case 12:
                n = Restricciones.comprobarNumMaximoSectores(individuo.getTurnos(), entrada, parametros);
                break;
            case 13:
                n = Restricciones.comprobarControladorAsignado(individuo);
                break;
            case 14:
                n = Restricciones.comprobarTurnoVacio(individuo);
                break;
            default:
                System.out.println("ERROR");
                break;
        }
    }
}
