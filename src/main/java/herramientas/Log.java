package herramientas;

import main.Main;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

import static herramientas.CridaUtils.STRING_SEPARADOR_CSV;

public class Log {


    private static Logger log = Logger.getLogger("ProyectoSalomon");
    private static Logger logFile = Logger.getLogger("ProyectoSalomonFile");
    private static Logger logCsv = Logger.getLogger("ProyectoSalomonCSV");

    /**
     * En caso de utilizarse como pomparacion, permite elegir cada cuantas iteraciones
     * se hace el log
     */
    private static final int trazaCadaTantasIteraciones = 10;

    /**
     * Decide si el log por consola está habilitado o no
     */
    private static final boolean ON = true;

    /**
     * Decide si el log por fichero está habilitado o no
     */
    private static final boolean FICHERO = !ON;

    private static final boolean CSV = true;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        log.setLevel(Level.INFO);

        if (FICHERO) {
            FileHandler fh;
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
            try {
                fh = new FileHandler(Main.carpetaTrazas + dateFormat.format(date) + ".log");
                fh.setFormatter(new SimpleFormatter());
                logFile.addHandler(fh);
                logFile.setUseParentHandlers(false);
                logFile.setLevel(Level.INFO);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fh = new FileHandler(Main.carpetaTrazas + dateFormat.format(date) + ".csv");
                fh.setFormatter(new SimpleFormatter() {
                    @Override
                    public String format(LogRecord record) {
                        return String.format("%s %n", formatMessage(record));
                    }
                });

                logCsv.addHandler(fh);
                logCsv.setUseParentHandlers(false);
                logCsv.setLevel(Level.INFO);
                logCsv.info(
                        // cabecera del CSV
                        "iteracion" + STRING_SEPARADOR_CSV +
                                "tiempo (ms)" + STRING_SEPARADOR_CSV +
                                "fitness"

                );
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static boolean isOn() {
        return ON || FICHERO || CSV;
    }

    public static boolean checkIter(int iter) {
        return iter % trazaCadaTantasIteraciones == 0;
    }

    public static void csvlog(int iteracion, long tiempo, double fitness) {
        if (CSV && checkIter(iteracion))
            logCsv.info(
                    iteracion + STRING_SEPARADOR_CSV
                            + tiempo + STRING_SEPARADOR_CSV +
                            fitness
            );
    }

    public static void info(String str) {
        if (ON) log.info(str);
        if (FICHERO) logFile.info(str);
    }

    public static void info(String str, int iter) {
        if (checkIter(iter)) info(str);
    }

    public static void warn(String str) {
        if (ON) log.warning(str);
        if (FICHERO) logFile.warning(str);
    }

    public static void turnOff() {
        LogManager.getLogManager().reset();
    }

    public static boolean isCsv() {
        return CSV;
    }

}
