package herramientas;

import main.Main;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

import static herramientas.CridaUtils.STRING_SEPARADOR_CSV;

public class Log {


    private static Logger log = Logger.getLogger("ProyectoSalomon");
    private static Logger logFile = Logger.getLogger("ProyectoSalomonFile");
//    private static Logger logCsv = Logger.getLogger("ProyectoSalomonCSV");

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
    private static final boolean FICHERO = true;

//    private static final boolean CSV = true;

    private static BufferedWriter fh;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        log.setLevel(Level.INFO);
    }

    public static void open() {
        if (FICHERO)
            try {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
                String name = Main.carpetaTrazas + dateFormat.format(date) + ".csv";
                File f = new File(name);
                f.getParentFile().mkdirs();
                f.createNewFile();
                fh = new BufferedWriter(new FileWriter(f));
//                fh = new FileHandler(name);
//                fh.setFormatter(new SimpleFormatter());
//                fh.setFormatter(new SimpleFormatter() {
//                    @Override
//                    public String format(LogRecord record) {
//                        return String.format("%s %n", formatMessage(record));
//                    }
//                });
//                logFile.addHandler(fh);

                fh.write(
//                         cabecera del CSV
                        "iteracion" + STRING_SEPARADOR_CSV +
                                "tiempo (ms)" + STRING_SEPARADOR_CSV +
                                "fitness" + STRING_SEPARADOR_CSV +
                                "tamaño" + "\n"
                );

            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    public static boolean isOn() {
        return ON || FICHERO /*|| CSV*/;
    }

    public static boolean checkIter(int iter) {
        return iter % trazaCadaTantasIteraciones == 0;
    }

    public static void csvLog(int iteracion, long tiempo, double fitness, int size, int numeroIteracionesSinMejora) {
        if (FICHERO && checkIter(iteracion)) {
            try {
                fh.write(
                        iteracion + STRING_SEPARADOR_CSV
                                + tiempo + STRING_SEPARADOR_CSV
                                + fitness + STRING_SEPARADOR_CSV
                                + size + STRING_SEPARADOR_CSV
                                + numeroIteracionesSinMejora
                                + "\n"
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void info(String str) {
        if (ON) log.info(str);
//        if (FICHERO) logFile.info(str);
    }

    public static void info(String str, int iter) {
        if (checkIter(iter)) info(str);
    }

    public static void warn(String str) {
        if (ON) log.warning(str);
//        if (FICHERO) logFile.warning(str);
    }

    public static void turnOff() {
        LogManager.getLogManager().reset();

    }

    public static void close() {
        if (fh != null) {
            try {
                fh.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public static boolean isCsv() {
//        return CSV;
//    }

}
