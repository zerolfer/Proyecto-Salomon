package rwFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Clase para la lectura de archivos.
 *
 * @author Tino
 */
public class Lectura {
    /**
     * Metodo para la lectura de un archivo de numeros separados por espacios (" "), este metodo devuelve los numeros
     * en forma de lista.
     *
     * @param path Ruta del archivo.
     * @return Lista con los numeros del archivo.
     */
    public static ArrayList<Double> Vector(String path) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Double> resultado = new ArrayList<>();

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).

            archivo = new File(path);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            linea = br.readLine();
            String[] rend = linea.split(" ");
            for (int i = 0; i < rend.length; i++) {
                resultado.add(Double.parseDouble(rend[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return resultado;

    }

    /**
     * Metodo para la lectura de un archivo, la lectura carga cada linea en un string de una lista.
     *
     * @param path Ruta del archivo.
     * @return Lista con las distintas lineas del archivo.
     */
    public static ArrayList<String> Listar(String path) {
        return Listar(path, false);
    }

    public static ArrayList<String> Listar(String path, boolean opcional) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<String> resultado = new ArrayList<>();

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).

            archivo = new File(path);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                resultado.add(linea);
            }

        } catch (Exception e) {
            if (!opcional)
                e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return resultado;

    }


    /**
     * Metodo para la lectura de un archivo de numeros separados por punto y coma (";"), este metodo devuelve los
     * numeros en forma de matriz, cada fila del documento corresponde a una fila de la matriz.
     *
     * @param path Ruta del archivo.
     * @return Matriz con los numeros del archivo.
     */
    public static double[][] array(String path) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Double> f1 = new ArrayList<>();
        ArrayList<Double> f2 = new ArrayList<>();
        double[][] array = null;
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).

            archivo = new File(path);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] rend = linea.split(";");
                f1.add(Double.parseDouble(rend[0]));
                f2.add(Double.parseDouble(rend[1]));
            }

            array = new double[2][f1.size()];
            for (int i = 0; i < f1.size(); i++) {
                array[0][i] = f1.get(i).doubleValue();
                array[1][i] = f2.get(i).doubleValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return array;

    }

    /**
     * Metodo para la lectura de un archivo de numeros separados por punto y coma (";"), este metodo devuelve los
     * numeros en forma de matriz, cada fila del documento corresponde a una fila de la matriz.
     *
     * @param path Ruta del archivo.
     * @return Matriz con los numeros del archivo (Dos listas anidadas).
     */
    public static ArrayList<ArrayList<Double>> Matriz(String path) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<ArrayList<Double>> resultado = new ArrayList<>();

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).

            archivo = new File(path);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                ArrayList<Double> fila = new ArrayList<>();
                String[] rend = linea.split(" ");

                for (int i = 0; i < rend.length; i++) {
                    fila.add(Double.parseDouble(rend[i]));
                }
                resultado.add(fila);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return resultado;

    }

    /**
     * Metodo para la impresion por pantalla de una lista de numeros separados por espacios.
     *
     * @param Vector Lista de numeros.
     */
    public static void ImprimirVector(ArrayList<Double> Vector) {
        for (int i = 0; i < Vector.size(); i++) {
            System.out.print(Vector.get(i) + " ");
        }
    }

    /**
     * Metodo para la impresion por pantalla de una matriz de numeros separados por espacios.
     *
     * @param Matriz Matriz de numeros.
     */
    public static void ImprimirMatriz(ArrayList<ArrayList<Double>> Matriz) {
        for (int i = 0; i < Matriz.size(); i++) {
            for (int j = 0; j < Matriz.get(i).size(); j++) {
                System.out.print(Matriz.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
