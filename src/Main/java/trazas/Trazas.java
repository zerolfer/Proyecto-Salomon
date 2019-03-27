package trazas;

import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import main.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class Trazas {

    private static ArrayList<Traza> trazas = new ArrayList<>();
    private static ArrayList<String> trazas2 = new ArrayList<>();/*Contiene las trazas completas de la evolucion del
    fitness (trazasFit) y otras trazas del ratio de aceptacion cada L iteracciones (trazasL)*/
    private static Properties propOpciones = new Properties();
    private static int nTrazas = 0;
    private static int nTmnsIntervalos = 13;


    public static void addTraza(Traza traza) {
        trazas.add(traza);
    }

    public static void archivarYLimpiarTrazas(ArrayList<Solucion> poblacionReducirControladores,
                                              String propFileOptions, ParametrosAlgoritmo pa) {
        String c1 = "resultados/" + Main.entradaPath + Main.entradaId + "/";
        File f = new File(c1);
        f.mkdir();
        c1 = c1 + pa.getAlgoritmo() + "/";
        File f2 = new File(c1);
        f2.mkdir();
        c1 = c1 + "Trazas/";
        File f1 = new File(c1);
        f1.mkdir();
        escribirTrazasFityL();
        getPropertiesO(propFileOptions);
        int[] titulos = obtenerTrazasDeInteres();
        String fichero = inicioTitulos(titulos);
        ArrayList<String> aExcel = new ArrayList<>();
        ArrayList<String[]> aTxt = new ArrayList<>();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < trazas.size(); i++) {
            String[] info = ordenarTrazas(i, trazas.get(i).getOrden(), trazas.get(i).getInfo(),
                    trazas.get(i).getTmn(), titulos, fichero, trazas.get(i).getTime(), pa);
            trazas.set(i, new Traza(i, i, null, i));
            aTxt.add(info);
            aExcel.add(info[1]);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("TiempoPreprocesadoEscritura: " + (t2 - t1) / (1000.0 * 60.0));
        nTrazas = trazas.size();
        trazasAExcel(aExcel);
        aExcel = new ArrayList<>();
        long t3 = System.currentTimeMillis();
        System.out.println("TiempoEscrituraExcel: " + (t3 - t2) / (1000.0 * 60.0));
        for (int i = 0; i < aTxt.size(); i++) {
            rwFiles.Escritura.escrituraCompleta(aTxt.get(i)[0], aTxt.get(i)[1]);
        }
        aTxt = new ArrayList<>();
        long t4 = System.currentTimeMillis();
        System.out.println("TiempoEscrituraTxt: " + (t4 - t3) / (1000.0 * 60.0));
        limpiarTrazas();
    }

    public static void limpiarTrazas() {
        trazas = new ArrayList<>();
        trazas2 = new ArrayList<>();

    }

    private static void escribirTrazasFityL() {
        /*Contiene las trazas completas de la evolucion del fitness (trazasFit) y otras trazas del ratio de
        aceptacion cada L iteracciones (trazasL)*/
        for (int i = 0; i < trazas2.size(); i += 2) {
            rwFiles.Escritura.escrituraCompleta(trazas2.get(i), trazas2.get(i + 1));
        }

        trazas2 = new ArrayList<>();
    }

    public static void trazasAExcel(ArrayList<String> info) {
        File f = new File(Main.carpetaTrazas);
        f.mkdir();
        ArrayList<String> uno = new ArrayList<>();
        ArrayList<ArrayList<String>> dos = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> tres = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            String[] x = info.get(i).split("\n");
            Collections.addAll(uno, x);
            dos.add(uno);
            uno = new ArrayList<>();
        }
        tres.add(dos);
        rwFiles.EscrituraExcel.escribirTrazasEnExcel(tres);
    }

    private static String[] ordenarTrazas(int name, int n, ArrayList<ArrayList<String>> trazas, int tmn,
                                          int[] titulos, String fichero, long time, ParametrosAlgoritmo pa) {
        System.out.println("Escribiendo traza " + name + "...");
        double mej = 0, cam = 0, nomej = 0;
        double mPorcent = 0, mediaIntentosTotal = 0, mediaC1Intentos = 0, mediaIntervalosIntentos = 0;

        //ArrayList<String> tempDatos = new ArrayList<String>();
        int[] tamanosIntervalos = new int[nTmnsIntervalos];
        ArrayList<String> iteracionX = new ArrayList<>();
        //DATOS
        int cnt = 1;
        while (trazas.size() > (65000 * cnt)) {
            cnt++;
        } //usamos 65k porque es el limite de filas de un excel y para que no tarde tanto en escribir las trazas

        for (int i = 1; i < trazas.size(); i++) {
            iteracionX = trazas.get(i);
            if ((i - 1) % cnt == 0) {
                fichero = ponerFila(fichero, iteracionX, titulos);
            }
            if (iteracionX.get(11).equalsIgnoreCase("1")) {
                mej++;
            } else {
                nomej++;
                mPorcent += Double.parseDouble(iteracionX.get(12).replace(",", "."));
                if (iteracionX.get(13).equalsIgnoreCase("1")) {
                    cam++;
                }
            }
            mediaIntentosTotal = mediaIntentosTotal + Double.parseDouble(iteracionX.get(4));
            mediaC1Intentos = mediaC1Intentos + Double.parseDouble(iteracionX.get(3));
            mediaIntervalosIntentos = mediaIntervalosIntentos + Double.parseDouble(iteracionX.get(2));
            //if(pa.SA.getMovimientosEntorno().equalsIgnoreCase("movimiento3")||pa.SA.getMovimientosEntorno()
            // .equalsIgnoreCase("movimiento2")){
            tamanosIntervalos[(Integer.parseInt(iteracionX.get(1)) / 3)] =
                    tamanosIntervalos[(Integer.parseInt(iteracionX.get(1)) / 3)] + 1;
            //}
        }

        cam = cam / nomej;
        mPorcent = mPorcent / nomej;
        mej = mej / (mej + nomej);
        //CABECERA
        String mediasTotales =
                "Controladores: " + tmn + ";;Media de Intentos Totales: " + (mediaIntentosTotal / trazas.size()) +
                        "\nMedia de Intentos de Intervalo: " + (mediaIntervalosIntentos / trazas.size()) + "\nMedia " +
                        "de Intentos de trabajador1: " + (mediaC1Intentos / trazas.size()) + "\nPorcentaje de mejora:" +
                        " " + (mej * 100) + "%\nPorcentaje de cambios a peor: " + (cam * 100) + "%\nMedia de " +
                        "probabilidad de cambio: " + (mPorcent) + "\n";
        mediasTotales += "\n";

        String bucle = "";
        if (titulos[1] == 1) {
            bucle = "Soluciones validas obtenidas con los distintos tamaños de movimiento\n";
        } else {
            bucle = "\n";
        }
        for (int i = 0; i < tamanosIntervalos.length; i++) {
            if (titulos[1] == 1) {
                bucle += tamanosIntervalos[i] + "\n";
            } else {
                bucle += "\n";
            }

        }

        String info =
                "Temp: " + pa.SA.getTemperaturaInicial() + " Alpha: " + pa.SA.getDescensoTemperatura() + " L: " + pa.SA.getIteracionesTemperaturaL() + " CPporcent: " + pa.SA.getCondicionParadaPorcent() + " CPciclos: " + pa.SA.getCondicionParadaCiclos() + "FitnessFunction: " + pa.getFuncionFitnessFase2() + " " + pa.SA.getMovimientosEntorno() + "\n" + "Tiempo de ejecuciñn: " + ((time / 1000.0) / 60.0) + "\n";
        info = info + "\n" + mediasTotales + "\n" + bucle + "\n" + fichero;
        String[] datosEscritura = {Main.carpetaTrazas + "TrazaN" + name + "_" + 0 + ".txt", info};
        return datosEscritura;
    }

    private static String inicioTitulos(int[] titulos) {
        String fichero = "Nñ Iteraciones;";
        for (int i = 1; i < titulos.length; i++) {
            if (titulos[i] == 2) {
                fichero += "Nñ Controladores1 Probados;";
            } else if (titulos[i] == 3) {
                fichero += "Nñ Intervalos Probados;";
            } else if (titulos[i] == 4) {
                fichero += "Nñ Controladores2 Probados;";
            } else if (titulos[i] == 5) {
                fichero += "Fitness Individuo Actual;";
            } else if (titulos[i] == 6) {
                fichero += "Fitness f1;";
            } else if (titulos[i] == 7) {
                fichero += "Fitness f2;";
            } else if (titulos[i] == 8) {
                fichero += "Diferencia de Fitness;";
            } else if (titulos[i] == 9) {
                fichero += "Fitness Mejor Individuo (f*);";
            } else if (titulos[i] == 10) {
                fichero += "Temperatura;";
            } else if (titulos[i] == 11) {
                fichero += "Mejora;";
            } else if (titulos[i] == 12) {
                fichero += "Probabilidad de Elecciñn;";
            } else if (titulos[i] == 13) {
                fichero += "Cambio Realizado;";
            }
        }
        fichero += "\n";
        return fichero;
    }

    private static int[] obtenerTrazasDeInteres() {
        int[] tt = new int[15];
        tt[0] = 0;
        int cont = 1;
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("tamanoIntervaloRealizado"))) {
            tt[cont] = 1;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("numControladores1Probados"))) {
            tt[cont] = 2;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("numIntervalosProbados"))) {
            tt[cont] = 3;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("numControladores2Probados"))) {
            tt[cont] = 4;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("fitnessIndividuoActual"))) {
            tt[cont] = 5;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("fitnessf1"))) {
            tt[cont] = 6;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("fitnessf2"))) {
            tt[cont] = 7;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("diferenciaFitness"))) {
            tt[cont] = 8;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("fitnessMejorIndividuo"))) {
            tt[cont] = 9;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("temperatura"))) {
            tt[cont] = 10;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("mejora"))) {
            tt[cont] = 11;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("probabilidadEleccion"))) {
            tt[cont] = 12;
            cont++;
        }
        if ("TRUE".equalsIgnoreCase(propOpciones.getProperty("cambioRealizado"))) {
            tt[cont] = 13;
            cont++;
        }
        return tt;
    }

    private static String ponerFila(String fichero, ArrayList<String> iteracionX, int[] titulos) {
        fichero += iteracionX.get(0) + ";";
        for (int i = 1; i < titulos.length; i++) {
            if (titulos[i] == 2) {
                fichero += iteracionX.get(2).replace(".", ",") + ";";
            } else if (titulos[i] == 3) {
                fichero += iteracionX.get(3).replace(".", ",") + ";";
            } else if (titulos[i] == 4) {
                fichero += iteracionX.get(4).replace(".", ",") + ";";
            } else if (titulos[i] == 5) {
                fichero += iteracionX.get(5).replace(".", ",") + ";";
            } else if (titulos[i] == 6) {
                fichero += iteracionX.get(6).replace(".", ",") + ";";
            } else if (titulos[i] == 7) {
                fichero += iteracionX.get(7).replace(".", ",") + ";";
            } else if (titulos[i] == 8) {
                fichero += iteracionX.get(8).replace(".", ",") + ";";
            } else if (titulos[i] == 9) {
                fichero += iteracionX.get(9).replace(".", ",") + ";";
            } else if (titulos[i] == 10) {
                fichero += iteracionX.get(10).replace(".", ",") + ";";
            } else if (titulos[i] == 11) {
                fichero += iteracionX.get(11).replace(".", ",") + ";";
            } else if (titulos[i] == 12) {
                fichero += iteracionX.get(12).replace(".", ",") + ";";
            } else if (titulos[i] == 13) {
                fichero += iteracionX.get(13).replace(".", ",") + ";";
            }
        }
        fichero += "\n";
        return fichero;
    }

    private static ArrayList<String> leerTrazas(String path) {
        /*TRATAMIENTO DE TRAZAS*/
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<String> resultado = new ArrayList<>();
        try {
            archivo = new File(path);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                resultado.add(linea);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static void getPropertiesO(String propFileName) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(propFileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            getPropOpciones().load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Traza> getTrazas() {
        return trazas;
    }

    public static void setTrazas(ArrayList<Traza> trazas) {
        Trazas.trazas = trazas;
    }

    public static Properties getPropOpciones() {
        return propOpciones;
    }

    public static void setPropOpciones(Properties propOpciones) {
        Trazas.propOpciones = propOpciones;
    }

    public static int getnTmnsIntervalos() {
        return nTmnsIntervalos;
    }

    public static void setnTmnsIntervalos(int nTmnsIntervalos) {
        Trazas.nTmnsIntervalos = nTmnsIntervalos;
    }

    public static ArrayList<String> getTrazas2() {
        return trazas2;
    }

    public static void setTrazas2(ArrayList<String> trazas2) {
        Trazas.trazas2 = trazas2;
    }


}
