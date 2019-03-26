package rwFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Clase para la escritura de archivos.
 *
 * @author Tino
 */
public class Escritura {

    static File archivo = null;
    static FileWriter fr = null;
    static BufferedWriter br = null;

    /**
     * Metodo para abrir un archivo, si este no existe lo crea.
     *
     * @param path Ruta del archivo.
     */
    public static void abrirFicheroEscritura(String path) {

        try {
            archivo = new File(path);
            archivo.createNewFile();
            fr = new FileWriter(archivo);
            br = new BufferedWriter(fr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo para escribir un archivo previamiente abierto mediante el metodo abrirFicheroEscritura.
     *
     * @param datos Texto que sera escrito en el fichero.
     */
    public static void escribirDatos(String datos) {
        try {
            br.write(datos);
            br.flush();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra un archivo previamiente abierto.
     *
     * @param path Ruta del archivo.
     */
    public static void cerrarFicheroEscritura(String path) {
        archivo.setExecutable(true, true);
        try {
            if (path.equalsIgnoreCase(archivo.getPath())) {
                br.close();
                fr.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * Metodo para crear o abrir un archivo, escribir los datos que se quiera y cerrar el archivo.
     *
     * @param path  Ruta del archivo.
     * @param datos Texto que sera escrito.
     */
    public static void escrituraCompleta(String path, String datos) {
        abrirFicheroEscritura(path);
        escribirDatos(datos);
        cerrarFicheroEscritura(path);
    }
}
