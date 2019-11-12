package herramientas;

import main.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static herramientas.CridaUtils.STRING_SEPARADOR_CSV;

public class Log {


    private static Logger log = Logger.getLogger("ProyectoSalomon");
    private static Logger logFile = Logger.getLogger("ProyectoSalomonFile");
//    private static Logger logCsv = Logger.getLogger("ProyectoSalomonCSV");

    /**
     * En caso de utilizarse como pomparacion, permite elegir cada cuantas iteraciones
     * se hace el log
     */
    private static final int trazaCadaTantasIteraciones = 60;

    /**
     * Decide si el log por consola está habilitado o no
     */
    private static final boolean ON = false;

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
//        if (FICHERO)
        try {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");

            String[] nameFiles = new File(Main.carpetaTrazas).list();
            int number;
            if (nameFiles == null)
                number = 1;
            else
                number = nameFiles.length + 1;

            String name = Main.carpetaTrazas +/* dateFormat.format(date) */"output" + number + ".csv";
            File f = new File(name);
            f.getParentFile().mkdirs();
            f.createNewFile();
            fh = new BufferedWriter((new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8)));

            //                fh = new FileHandler(name);
//                fh.setFormatter(new SimpleFormatter());
//                fh.setFormatter(new SimpleFormatter() {
//                    @Override
//                    public String format(LogRecord record) {
//                        return String.format("%s %n", formatMessage(record));
//                    }
//                });
//                logFile.addHandler(fh);

            if (FICHERO)
                fh.write(
                        // cabecera del CSV
                        "iteracion" + STRING_SEPARADOR_CSV +
                                "tiempo (ms)" + STRING_SEPARADOR_CSV +
                                "fitness total" + STRING_SEPARADOR_CSV +
                                "fitness 1" + STRING_SEPARADOR_CSV +
                                "fitness 2" + STRING_SEPARADOR_CSV +
                                "fitness 3" + STRING_SEPARADOR_CSV +
                                "fitness 4" + STRING_SEPARADOR_CSV +
                                "tamaño" + STRING_SEPARADOR_CSV +
                                "porcentajeMejora" + STRING_SEPARADOR_CSV +
                                "vecindad" + STRING_SEPARADOR_CSV +
                                "mejor fitness" + STRING_SEPARADOR_CSV +
                                "distancia" + "\n"
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

    public static void csvLog(Object... elem) {
        if (FICHERO /*&& checkIter(iteracion)*/) {
            csvWriter(elem);
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

    /**
     * Imprime por consola esté activo o no el Logger
     */
    public static void debug(String s) {
        log.info(s);
    }

    /**
     * Imprime por fichero esté activo o no el csv
     */
    public static void csvDebug(Object... elem) {
        csvWriter(elem);
    }

    private static void csvWriter(Object... elem) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < elem.length; i++) {
            s.append(elem[i]);
            if (i < elem.length - 1) s.append(STRING_SEPARADOR_CSV);
        }
        s.append("\n");
        try {
            fh.write(s.toString()
                        /*iteracion + STRING_SEPARADOR_CSV
                                + tiempo + STRING_SEPARADOR_CSV
                                + fitness + STRING_SEPARADOR_CSV
                                + size + STRING_SEPARADOR_CSV
                                + numeroIteracionesSinMejora + STRING_SEPARADOR_CSV
                                + vecindad + "\n"*/
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Integer value = -1;

    public static Integer retrieveValue() {
        return value;
    }

    public static void saveValue(Integer val) {
        value = val;
    }
//    public static boolean isCsv() {
//        return CSV;
//    }

}
