package InicializarPoblacion;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Turno;
import estructurasDatos.Parametros;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;

/**
 * Clase utilizada para minimizar el numero de restricciones violadas por las soluciones iniciales.
 *
 * @author Tino
 */
public class ArreglarSoluciones {
    /**
     * MEtodo utilizado para intentar cumplir la condicion de trabajo minimo en los distintos turnos. En caso que no fuera posible su reparacion utiliza la variable global "posible" para indicarlo.
     *
     * @param entrada         Entrada del problema.
     * @param p               Parametros del problema.
     * @param cadenasDeTurnos Conjunto de turnos de trabajo.
     * @param posTurno        Posicion del turno que queremos que cumpla esta condicion.
     * @param turno           El turno de trabajo en cuestion.
     * @param posChar         Posicion del turno donde se incumple (fin del intervalo).
     * @param ant             Contenido del slot en posChar.
     * @param patrones        Conjunto de patrones utilizados en la comprobacion de restricciones.
     * @return Conjunto de turnos de trabajo con las restricciones arregladas si esto ha sido posible. En caso de no serlo devuelve el mismo conjunto de turnos.
     */
    public static ArrayList<ArrayList<String>> arregloTrabajoMinimo(Entrada entrada, Parametros p, ArrayList<ArrayList<String>> cadenasDeTurnos, int posTurno, ArrayList<String> turno, int posChar, String ant, Patrones patrones) {
        //TODO: Revisar funcionamiento
        int[] posicionTras = busquedaTraspaso(entrada, p, cadenasDeTurnos, posTurno, turno, posChar, ant, patrones);
        if (posicionTras[0] == -1) {//No encontrado -  Intentar donacion
            int[] posicionAdq = busquedaAdquisicion(entrada, p, cadenasDeTurnos, posTurno, turno, posChar, ant, patrones);
            if (posicionAdq[0] == -1) {
                //Sin solucion!! Solucion no valida
                InicializarPoblacion.posible = false;
            } else {
                cadenasDeTurnos = realizarAdquisicion(posicionAdq, cadenasDeTurnos, posTurno, turno, posChar, ant);

            }
        } else {
            cadenasDeTurnos = realizarTranspaso(posicionTras, cadenasDeTurnos, posTurno, turno, posChar, ant);

        }
        return cadenasDeTurnos;
    }

    /**
     * Metodo utilizado para la busqueda de un turno de trabajo que acepte un intervalo de trabajo que no cumpla el tamaño minimo, uniendolo a uno propio que si lo cumpla.
     *
     * @param entrada         Entrada del problema.
     * @param p               Parametros del problema.
     * @param cadenasDeTurnos Conjunto de turnos de trabajo.
     * @param posTurno        Posicion del turno que queremos que cumpla esta condicion.
     * @param turno           El turno de trabajo en cuestion.
     * @param finCad          Posicion del turno donde se incumple (fin del intervalo).
     * @param ant             Contenido del slot en posChar.
     * @param patrones        Conjunto de patrones utilizados en la comprobacion de restricciones.
     * @return Array formado por: Posicion del turno con el que se realiza el intercambio + Posicion inicio del intervalo en el turno + Posicion fin del intervalo en el turno.
     */
    private static int[] busquedaTraspaso(Entrada entrada, Parametros p, ArrayList<ArrayList<String>> cadenasDeTurnos, int posTurno, ArrayList<String> turno, int finCad, String ant, Patrones patrones) {
        int[] re = {-1, -1, -1};
        int iniCad = finCad;
        ArrayList<ArrayList<String>> dosTurnos = new ArrayList<>();
        Turno t = entrada.getTurno();
        while (iniCad >= 0 && ant.equals(turno.get(iniCad))) {
            iniCad--;
        }
        iniCad++;
        ArrayList<String> slotsTras = new ArrayList<>();
        for (int z = iniCad; z <= finCad; z++) {
            slotsTras.add(turno.get(z));//cadena a transpasar
        }
        for (int i = 0; i < cadenasDeTurnos.size(); i++) {
            if (i != posTurno) {
                int num = -1;
                boolean prev = false;

                ArrayList<String> turno2 = cadenasDeTurnos.get(i);
                int slots = t.getTl()[1];
                if (iniCad > 0 && turno2.get(iniCad - 1).equals(ant)) { //Busqueda previa
                    int[] tmp = {i, iniCad, finCad + 1};
                    ArrayList<ArrayList<String>> cambio = comprobarTranspaso(tmp, cadenasDeTurnos, posTurno, turno, finCad, ant);
                    dosTurnos.add(cambio.get(i));
                    dosTurnos.add(cambio.get(posTurno));
                    num = Restricciones.comprobarRestriccionesArregloSoluciones(dosTurnos, patrones, p);
                    dosTurnos = new ArrayList<>();
                    prev = true;
                } else if (finCad < (slots - 1) && turno2.get(finCad + 1).equals(ant)) {//Busqueda posterior
                    int[] tmp = {i, iniCad, finCad + 1};
                    ArrayList<ArrayList<String>> cambio = comprobarTranspaso(tmp, cadenasDeTurnos, posTurno, turno, finCad, ant);
                    dosTurnos.add(cambio.get(i));
                    dosTurnos.add(cambio.get(posTurno));
                    num = Restricciones.comprobarRestriccionesArregloSoluciones(dosTurnos, patrones, p);
                    dosTurnos = new ArrayList<>();
                    prev = false;
                }
                if (num == 0) {
                    if (prev) {
                        re[0] = i;
                        re[1] = iniCad;
                        re[2] = finCad + 1;//Posicion turno + Posicion inicio + Posicion fin
                    } else {
                        re[0] = i;
                        re[1] = iniCad;
                        re[2] = finCad + 1;//Posicion turno + Posicion inicio + Posicion fin
                    }
                    return re;
                }
            }
        }
        return re;
    }

    /**
     * Metodo utilizado para la busqueda de un turno de trabajo que ceda un intervalo de trabajo para que el turno que lo recoge cumpla el tamaño minimo.
     *
     * @param entrada         Entrada del problema.
     * @param p               Parametros del problema.
     * @param cadenasDeTurnos Conjunto de turnos de trabajo.
     * @param posTurno        Posicion del turno que queremos que cumpla esta condicion.
     * @param turno           El turno de trabajo en cuestion.
     * @param finCad          Posicion del turno donde se incumple (fin del intervalo).
     * @param ant             Contenido del slot en posChar.
     * @param patrones        Conjunto de patrones utilizados en la comprobacion de restricciones.
     * @return Array formado por: Posicion del turno con el que se realiza el intercambio + Posicion inicio del intervalo en el turno + Posicion fin del intervalo en el turno.
     */
    private static int[] busquedaAdquisicion(Entrada entrada, Parametros p, ArrayList<ArrayList<String>> cadenasDeTurnos, int posTurno, ArrayList<String> turno, int finCad, String ant, Patrones patrones) {
        int[] re = {-1, -1, -1};
        int iniCad = finCad;
        ArrayList<ArrayList<String>> dosTurnos = new ArrayList<>();
        Turno t = entrada.getTurno();
        while (iniCad >= 0 && turno.get(iniCad).equals(ant)) {
            iniCad--;
        }
        iniCad++;

        ArrayList<String> cadenaSlots = new ArrayList<>();
        for (int z = iniCad; z <= finCad; z++) {
            cadenaSlots.add(turno.get(z));//cadena de fallo
        }

        int minT = p.getTiempoTrabMin() / p.getTamanoSlots();
        int slotsNecesarios = minT - cadenaSlots.size();
        for (int i = 0; i < cadenasDeTurnos.size(); i++) {
            int num = -1;
            boolean prev = false;
            /*Comprobar:
             * 	Trabajo Max 24 (no)
             * 	Trabajo Min 6 del donante
             * 	Descanso Min 6 propio
             * 	1 Turno propio
             * 	Maximo 96 slots propio
             * 	48 o 72 slots trabajo no consecutivo propio
             * 	*/
            ArrayList<String> turno2 = cadenasDeTurnos.get(i);
            int slots = t.getTl()[1];
            if (iniCad > 0 && turno2.get(iniCad - 1).equals(ant)) { //Busqueda previa
                int[] tmp = {i, iniCad - slotsNecesarios, iniCad};
                ArrayList<ArrayList<String>> cambio = comprobarAdquisicion(tmp, cadenasDeTurnos, posTurno, turno, finCad, ant);
                dosTurnos.add(cambio.get(i));
                dosTurnos.add(cambio.get(posTurno));
                num = Restricciones.comprobarRestriccionesArregloSoluciones(dosTurnos, patrones, p);
                dosTurnos = new ArrayList<>();
                prev = true;
            } else if (finCad < (slots - 1) && turno2.get(finCad + 1).equals(ant)) {//Busqueda posterior
                int[] tmp = {i, finCad, finCad + slotsNecesarios};
                ArrayList<ArrayList<String>> cambio = comprobarAdquisicion(tmp, cadenasDeTurnos, posTurno, turno, finCad, ant);
                dosTurnos.add(cambio.get(i));
                dosTurnos.add(cambio.get(posTurno));
                num = Restricciones.comprobarRestriccionesArregloSoluciones(dosTurnos, patrones, p);
                dosTurnos = new ArrayList<>();
                prev = false;
            }
            if (num == 0) {
                if (prev) {
                    re[0] = i;
                    re[1] = iniCad - slotsNecesarios;
                    re[2] = iniCad;//Posicion turno + Posicion inicio + Posicion fin
                } else {
                    re[0] = i;
                    re[1] = finCad;
                    re[2] = finCad + slotsNecesarios;//Posicion turno + Posicion inicio + Posicion fin
                }
                return re;
            }
        }
        return re;
    }

    /**
     * Metodo para realizar un intercambio concreto de slots.
     *
     * @param posicionAdq     Array formado por: Posicion del turno con el que se realiza el intercambio + Posicion inicio del intervalo en el turno + Posicion fin del intervalo en el turno.
     * @param cadenasDeTurnos Conjunto de turnos de trabajo.
     * @param posTurno        Posicion del segundo turno.
     * @param turno           El turno de trabajo en cuestion.
     * @param posChar         Posicion del turno donde se incumple (fin del intervalo).
     * @param ant             Contenido del slot en posChar.
     * @return Conjunto de turnos de trabajo con el intercambio realizado.
     */
    private static ArrayList<ArrayList<String>> realizarAdquisicion(int[] posicionAdq, ArrayList<ArrayList<String>> cadenasDeTurnos, int posTurno, ArrayList<String> turno, int posChar, String ant) {
        ArrayList<String> AceptaTrabajo = cadenasDeTurnos.get(posicionAdq[0]);
        ArrayList<String> DaTrabajo = turno;
        //Cadena transpaso 1
        ArrayList<String> cadena = new ArrayList<>();
        for (int z = posicionAdq[1]; z < posicionAdq[2]; z++) {
            cadena.add(AceptaTrabajo.get(z));
        }
        //Cadena transpaso 2
        ArrayList<String> cadenaP = new ArrayList<>();
        for (int z = posicionAdq[1]; z < posicionAdq[2]; z++) {
            cadenaP.add(DaTrabajo.get(z));
        }
        /*---Realizar transpaso 1*/
        int m = 0;
        ArrayList<String> AT2 = new ArrayList<>();
        for (int z = 0; z < posicionAdq[1]; z++) {
            AT2.add(AceptaTrabajo.get(z));
        }
        for (int z = posicionAdq[1]; z < posicionAdq[2]; z++) {
            AT2.add(cadenaP.get(m));
            m++;
        }
        for (int z = posicionAdq[2]; z < AceptaTrabajo.size(); z++) {
            AT2.add(AceptaTrabajo.get(z));
        }
        /*---Realizar transpaso 2*/
        m = 0;
        ArrayList<String> DT2 = new ArrayList<>();
        for (int z = 0; z < posicionAdq[1]; z++) {
            DT2.add(DaTrabajo.get(z));
        }
        for (int z = posicionAdq[1]; z < posicionAdq[2]; z++) {
            DT2.add(cadena.get(m));
            m++;
        }
        for (int z = posicionAdq[2]; z < DaTrabajo.size(); z++) {
            DT2.add(DaTrabajo.get(z));
        }

        cadenasDeTurnos.set(posicionAdq[0], AT2);
        cadenasDeTurnos.set(posTurno, DT2);
        return cadenasDeTurnos;
    }

    /**
     * Metodo para realizar un intercambio concreto de slots.
     *
     * @param posicionAdq     Array formado por: Posicion del turno con el que se realiza el intercambio + Posicion inicio del intervalo en el turno + Posicion fin del intervalo en el turno.
     * @param cadenasDeTurnos Conjunto de turnos de trabajo.
     * @param posTurno        Posicion del segundo turno.
     * @param turno           El turno de trabajo en cuestion.
     * @param posChar         Posicion del turno donde se incumple (fin del intervalo).
     * @param ant             Contenido del slot en posChar.
     * @return Conjunto de turnos de trabajo con el intercambio realizado.
     */
    private static ArrayList<ArrayList<String>> realizarTranspaso(int[] posicionTras, ArrayList<ArrayList<String>> cadenasDeTurnos, int posTurno, ArrayList<String> turno, int posChar, String ant) {
        ArrayList<String> AceptaTrabajo = cadenasDeTurnos.get(posicionTras[0]);
        ArrayList<String> DaTrabajo = turno;
        ArrayList<String> cadena = new ArrayList<>();
        //Cadena de transpaso 1
        for (int z = posicionTras[1]; z < posicionTras[2]; z++) {
            cadena.add(AceptaTrabajo.get(z));
        }
        //Cadena de transpaso 2
        ArrayList<String> cadenaP = new ArrayList<>();
        for (int z = posicionTras[1]; z < posicionTras[2]; z++) {
            cadenaP.add(DaTrabajo.get(z));
        }
        /*---*/
        /*---Realizar transpaso 1*/
        ArrayList<String> AT2 = new ArrayList<>();
        int m = 0;
        for (int z = 0; z < posicionTras[1]; z++) {
            AT2.add(AceptaTrabajo.get(z));
        }
        for (int z = posicionTras[1]; z < posicionTras[2]; z++) {
            AT2.add(cadenaP.get(m));
            m++;
        }
        for (int z = posicionTras[2]; z < AceptaTrabajo.size(); z++) {
            AT2.add(AceptaTrabajo.get(z));
        }
        /*---Realizar transpaso 2*/
        ArrayList<String> DT2 = new ArrayList<>();
        m = 0;
        for (int z = 0; z < posicionTras[1]; z++) {
            DT2.add(DaTrabajo.get(z));
        }
        for (int z = posicionTras[1]; z < posicionTras[2]; z++) {
            DT2.add(cadena.get(m));
            m++;
        }
        for (int z = posicionTras[2]; z < DaTrabajo.size(); z++) {
            DT2.add(DaTrabajo.get(z));
        }

        cadenasDeTurnos.set(posicionTras[0], AT2);
        cadenasDeTurnos.set(posTurno, DT2);
        return cadenasDeTurnos;
    }

    /**
     * Metodo para omprobar que no se geren nuevas infactibilidades con el intercambio de un intervalo.
     *
     * @param posicionAdq     Array formado por: Posicion del turno con el que se realiza el intercambio + Posicion inicio del intervalo en el turno + Posicion fin del intervalo en el turno.
     * @param cadenasDeTurnos Conjunto de turnos de trabajo.
     * @param posTurno        Posicion del segundo turno.
     * @param turno           El turno de trabajo en cuestion.
     * @param posChar         Posicion del turno donde se incumple (fin del intervalo).
     * @param ant             Contenido del slot en posChar.
     * @return Conjunto de turnos de trabajo con el intercambio realizado.
     */
    private static ArrayList<ArrayList<String>> comprobarAdquisicion(int[] posicionAdq, ArrayList<ArrayList<String>> cadenasDeTurnos, int posTurno, ArrayList<String> turno, int posChar, String ant) {
        ArrayList<String> AceptaTrabajo = cadenasDeTurnos.get(posicionAdq[0]);
        ArrayList<String> DaTrabajo = turno;
        //Cadena transpaso 1
        ArrayList<String> cadena = new ArrayList<>();
        for (int z = posicionAdq[1]; z < posicionAdq[2]; z++) {
            cadena.add(AceptaTrabajo.get(z));
        }
        //Cadena transpaso 2
        ArrayList<String> cadenaP = new ArrayList<>();
        for (int z = posicionAdq[1]; z < posicionAdq[2]; z++) {
            cadenaP.add(DaTrabajo.get(z));
        }
        /*---Realizar transpaso 1*/
        int m = 0;
        ArrayList<String> AT2 = new ArrayList<>();
        for (int z = 0; z < posicionAdq[1]; z++) {
            AT2.add(AceptaTrabajo.get(z));
        }
        for (int z = posicionAdq[1]; z < posicionAdq[2]; z++) {
            AT2.add(cadenaP.get(m));
            m++;
        }
        for (int z = posicionAdq[2]; z < AceptaTrabajo.size(); z++) {
            AT2.add(AceptaTrabajo.get(z));
        }
        /*---Realizar transpaso 2*/
        m = 0;
        ArrayList<String> DT2 = new ArrayList<>();
        for (int z = 0; z < posicionAdq[1]; z++) {
            DT2.add(DaTrabajo.get(z));
        }
        for (int z = posicionAdq[1]; z < posicionAdq[2]; z++) {
            DT2.add(cadena.get(m));
            m++;
        }
        for (int z = posicionAdq[2]; z < DaTrabajo.size(); z++) {
            DT2.add(DaTrabajo.get(z));
        }

        cadenasDeTurnos.set(posicionAdq[0], AT2);
        cadenasDeTurnos.set(posTurno, DT2);
        return cadenasDeTurnos;
    }

    /**
     * Metodo para omprobar que no se geren nuevas infactibilidades con el intercambio de un intervalo.
     *
     * @param posicionAdq     Array formado por: Posicion del turno con el que se realiza el intercambio + Posicion inicio del intervalo en el turno + Posicion fin del intervalo en el turno.
     * @param cadenasDeTurnos Conjunto de turnos de trabajo.
     * @param posTurno        Posicion del segundo turno.
     * @param turno           El turno de trabajo en cuestion.
     * @param posChar         Posicion del turno donde se incumple (fin del intervalo).
     * @param ant             Contenido del slot en posChar.
     * @return Conjunto de turnos de trabajo con el intercambio realizado.
     */
    private static ArrayList<ArrayList<String>> comprobarTranspaso(int[] posicionTras, ArrayList<ArrayList<String>> cadenasDeTurnos, int posTurno, ArrayList<String> turno, int posChar, String ant) {
        ArrayList<String> AceptaTrabajo = cadenasDeTurnos.get(posicionTras[0]);
        ArrayList<String> DaTrabajo = turno;
        ArrayList<String> cadena = new ArrayList<>();
        //Cadena de transpaso 1
        for (int z = posicionTras[1]; z < posicionTras[2]; z++) {
            cadena.add(AceptaTrabajo.get(z));
        }
        //Cadena de transpaso 2
        ArrayList<String> cadenaP = new ArrayList<>();
        for (int z = posicionTras[1]; z < posicionTras[2]; z++) {
            cadenaP.add(DaTrabajo.get(z));
        }
        /*---*/
        /*---Realizar transpaso 1*/
        ArrayList<String> AT2 = new ArrayList<>();
        int m = 0;
        for (int z = 0; z < posicionTras[1]; z++) {
            AT2.add(AceptaTrabajo.get(z));
        }
        for (int z = posicionTras[1]; z < posicionTras[2]; z++) {
            AT2.add(cadenaP.get(m));
            m++;
        }
        for (int z = posicionTras[2]; z < AceptaTrabajo.size(); z++) {
            AT2.add(AceptaTrabajo.get(z));
        }
        /*---Realizar transpaso 2*/
        ArrayList<String> DT2 = new ArrayList<>();
        m = 0;
        for (int z = 0; z < posicionTras[1]; z++) {
            DT2.add(DaTrabajo.get(z));
        }
        for (int z = posicionTras[1]; z < posicionTras[2]; z++) {
            DT2.add(cadena.get(m));
            m++;
        }
        for (int z = posicionTras[2]; z < DaTrabajo.size(); z++) {
            DT2.add(DaTrabajo.get(z));
        }
        @SuppressWarnings("unchecked")
        ArrayList<ArrayList<String>> tmp = (ArrayList<ArrayList<String>>) cadenasDeTurnos.clone();
        tmp.set(posicionTras[0], AT2);
        tmp.set(posTurno, DT2);
        return tmp;
    }

}
