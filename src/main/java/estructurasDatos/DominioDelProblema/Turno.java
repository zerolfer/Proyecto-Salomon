package estructurasDatos.DominioDelProblema;

import estructurasDatos.Parametros;

/**
 * Objeto Turno, el cual contiene toda la informacion relacionada con el turno que se esta resolviendo (nombre, horarios, slots, slots de descanso necesarios... )
 *
 * @author Tino
 */
public class Turno {
    /**
     * Nombre del turno.
     */
    private String nombre;
    /**
     * Hora de inicio del turno largo, ej: "10:30".
     */
    private String inicioTL;
    /**
     * Hora de fin del turno largo, ej: "18:30".
     */
    private String finTL;
    /**
     * Hora de inicio del turno corto, ej: "12:30".
     */
    private String inicioTC;
    /**
     * Hora de inicio del turno corto, ej: "18:30".
     */
    private String finTC;
    /**
     * Contiene el slot de inicio y fin del turno corto.
     */
    private int[] tc = {0, 0};
    /**
     * Contiene el slot de inicio y fin del turno largo.
     */
    private int[] tl = {0, 0};
    /**
     * Numero de slots necesarios de descanso para cumplir las restricciones en turno corto.
     */
    private int slotsDesTC;
    /**
     * Numero de slots necesarios de descanso para cumplir las restricciones en turno largo.
     */
    private int slotsDesTL;

    /**
     * Constructor
     *
     * @param nombre     Nombre del turno.
     * @param inicioTL   Hora de inicio del turno largo, ej: "10:30".
     * @param finTL      Hora de fin del turno largo, ej: "18:30".
     * @param inicioTC   Hora de inicio del turno corto, ej: "12:30".
     * @param finTC      Hora de inicio del turno corto, ej: "18:30".
     * @param parametros Contiene los parametros del problema.
     */
    public Turno(String nombre, String inicioTL, String finTL, String inicioTC, String finTC, Parametros parametros) {
        this.nombre = nombre;
        this.inicioTL = inicioTL;
        this.finTL = finTL;
        this.inicioTC = inicioTC;
        this.finTC = finTC;
        int[] turnos = turnosSlots(inicioTL, finTL, inicioTC, finTC);
        int[] tll = {turnos[0], turnos[1]};
        int[] tcc = {turnos[2], turnos[3]};
        this.tl = tll;
        this.tc = tcc;
        double pDescanso = 0;
        if (nombre.equalsIgnoreCase("Noche")) {
            pDescanso = parametros.getPorcentDescansoNoche();
        } else {
            pDescanso = parametros.getPorcentDescansoDia();
        }
        this.slotsDesTC = ((int) Math.ceil((tc[1] - tc[0]) * pDescanso) + tc[0] + (tl[1] - tc[1]));
        this.slotsDesTL = ((int) Math.ceil((tl[1] - tl[0]) * pDescanso));

    }

    public static int[] turnosSlots(String inicioTL2, String finTL2, String inicioTC2, String finTC2) {
        int iTCh = Integer.parseInt(inicioTC2.split(":")[0]);
        int iTCm = Integer.parseInt(inicioTC2.split(":")[1]);
        int fTCh = Integer.parseInt(finTC2.split(":")[0]);
        int fTCm = Integer.parseInt(finTC2.split(":")[1]);
        int iTLh = Integer.parseInt(inicioTL2.split(":")[0]);
        int iTLm = Integer.parseInt(inicioTL2.split(":")[1]);
        int fTLh = Integer.parseInt(finTL2.split(":")[0]);
        int fTLm = Integer.parseInt(finTL2.split(":")[1]);
        int[] turnos = {-1, -1, -1, -1};//Primero largo luego corto
        if (iTCh == iTLh) {
            if (iTCm == iTLm) {
                turnos[0] = 0;
                turnos[2] = 0;
            } else if (iTCm < iTLm) {
                turnos[2] = 0;
                turnos[0] = (iTLm - iTCm) / 5; //TODO: UTILIZAR OPCIONES.PROPERTIES tmnSlots
            } else if (iTCm > iTLm) {
                turnos[0] = 0;
                turnos[2] = (iTCm - iTLm) / 5; //TODO: UTILIZAR OPCIONES.PROPERTIES tmnSlots
            }
        } else if (iTCh < iTLh) {
            turnos[0] = ((iTLh - iTCh) * 60 + (iTLm - iTCm)) / 5;//TODO: UTILIZAR OPCIONES.PROPERTIES tmnSlots
            turnos[2] = 0;
        } else if (iTCh > iTLh) {
            turnos[0] = 0;
            turnos[2] = ((iTCh - iTLh) * 60 + (iTCm - iTLm)) / 5;//TODO: UTILIZAR OPCIONES.PROPERTIES tmnSlots
        }
        int inicioH = 0;
        int inicioM = 0;
        if (turnos[2] >= turnos[0]) {
            inicioH = iTLh;
            inicioM = iTLm;
        } else {
            inicioH = iTCh;
            inicioM = iTCm;
        }
        if (iTLh <= fTLh) {
            turnos[1] = ((fTLh - inicioH) * 60 + (fTLm - inicioM)) / 5;
        } else {
            turnos[1] = (((24 - inicioH) * 60 + (00 - inicioM)) / 5) + (((fTLh - 0) * 60 + (fTLm - 0)) / 5);//TODO: UTILIZAR OPCIONES.PROPERTIES tmnSlots
        }
        if (iTCh <= fTCh) {
            turnos[3] = ((fTCh - inicioH) * 60 + (fTCm - inicioM)) / 5;
        } else {
            turnos[3] = (((24 - inicioH) * 60 + (00 - inicioM)) / 5) + (((fTCh - 0) * 60 + (fTCm - 0)) / 5);//TODO: UTILIZAR OPCIONES.PROPERTIES tmnSlots
        }

        return turnos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInicioTL() {
        return inicioTL;
    }

    public void setInicioTL(String inicioTL) {
        this.inicioTL = inicioTL;
    }

    public String getFinTL() {
        return finTL;
    }

    public void setFinTL(String finTL) {
        this.finTL = finTL;
    }

    public String getInicioTC() {
        return inicioTC;
    }

    public void setInicioTC(String inicioTC) {
        this.inicioTC = inicioTC;
    }

    public String getFinTC() {
        return finTC;
    }

    public void setFinTC(String finTC) {
        this.finTC = finTC;
    }

    public int[] getTc() {
        return tc;
    }

    public void setTc(int[] tc) {
        this.tc = tc;
    }

    public int[] getTl() {
        return tl;
    }

    public void setTl(int[] tl) {
        this.tl = tl;
    }

    public int getSlotsDesTC() {
        return slotsDesTC;
    }

    public void setSlotsDesTC(int slotsDesTC) {
        this.slotsDesTC = slotsDesTC;
    }

    public int getSlotsDesTL() {
        return slotsDesTL;
    }

    public void setSlotsDesTL(int slotsDesTL) {
        this.slotsDesTL = slotsDesTL;
    }


}
