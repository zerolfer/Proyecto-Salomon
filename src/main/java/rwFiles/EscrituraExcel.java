package rwFiles;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Sector;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import main.Main;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import patrones.Patrones;
import patrones.Restricciones;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static herramientas.CridaUtils.STRING_DESCANSO;


/**
 * Clase utilizada para la escritura de soluciones y trazas en ficheros .xls y .xlsx (Excels) y en ficheros .txt (Texto).
 *
 * @author Tino
 */
public class EscrituraExcel {
    /**
     * Metodo utilizado para representar las soluciones generadas en ficheros excel y ficheros de texto. Ademas de crear el sistema de ficheros donde se guardan.
     *
     * @param titulo    Su contenido es utilizado para crear el titulo de los ficheros con las soluciones.
     * @param carpetaS  Ruta principal donde se guardan los ficheros.
     * @param poblacion Conjunto de soluciones.
     * @param entrada   Datos de la instancia del problema.
     * @param patrones  Clase donde se encuentran todos los patrones necesarios para la comprobacion de las restricciones.
     * @param p         Parametros del problema.
     */
    public static void EscrituraSoluciones(String titulo, String carpetaS, ArrayList<Solucion> poblacion, Entrada entrada, Patrones patrones, Parametros p, ParametrosAlgoritmo pa) {
        String c1 = "resultados/" + Main.entradaPath + Main.entradaId + "/";
        File f = new File(c1);
        f.mkdir();
        c1 = c1 + pa.getAlgoritmo() + "/";
        File f2 = new File(c1);
        f2.mkdir();
        c1 = c1 + "Soluciones/";
        File f1 = new File(c1);
        f1.mkdir();
        String ruta = escribirSolucionesEntxt(titulo, carpetaS, poblacion);
        escribirSolucionesEnExcel(titulo, ruta, poblacion, entrada, patrones, p);
    }

    /**
     * Metodo para escribir las soluciones en ficheros .txt y generar la ruta de las soluciones.
     *
     * @param titulo    Su contenido es utilizado para crear el titulo de los ficheros con las soluciones.
     * @param carpetaS  Ruta principal donde se guardan los ficheros.
     * @param poblacion Conjunto de soluciones.
     * @return Ruta donde se han guardado las soluciones.
     */
    private static String escribirSolucionesEntxt(String titulo, String carpetaS, ArrayList<Solucion> poblacion) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date date = new Date();
        String ruta = "";
        if (titulo == null) {
            ruta = carpetaS + "solucionesTxt_CurrentDate_" + dateFormat.format(date);
        } else {
            ruta = carpetaS + titulo + "_CurrentDate_" + dateFormat.format(date);
        }
        File f1 = new File(ruta);
        f1.mkdir();
        for (int i = 0; i < poblacion.size(); i++) {
            String sol = "";
            ArrayList<String> turnos = poblacion.get(i).getTurnos();
            for (int j = 0; j < turnos.size(); j++) {
                if (turnos.size() == j + 1) {
                    sol += turnos.get(j);
                } else {
                    sol += turnos.get(j) + ",";
                }
            }
            Escritura.escrituraCompleta(ruta + "/solucion" + (i + 1) + ".txt", sol);
            sol = "";
        }
        return ruta;
    }

    /**
     * Metodo utilizado para representar un conjunto de soluciones en un fichero excel.
     *
     * @param titulo    Su contenido es utilizado para crear el titulo de los ficheros con las soluciones.
     * @param carpetaS  Ruta principal donde se guardan los ficheros.
     * @param poblacion Conjunto de soluciones.
     * @param entrada   Datos de la instancia del problema.
     * @param patrones  Clase donde se encuentran todos los patrones necesarios para la comprobacion de las restricciones.
     * @param p         Parametros del problema.
     */
    private static void escribirSolucionesEnExcel(String titulo, String carpetaS, ArrayList<Solucion> poblacion, Entrada entrada, Patrones patrones, Parametros p) {
        FileInputStream file;
        //HSSFWorkbook
        XSSFWorkbook workbook = null;

        try {
            file = new FileInputStream(new File("src/main/resources/PlantillaSoluciones.xlsx"));
            workbook = new XSSFWorkbook(file);
        } catch (FileNotFoundException e1) {
            System.out.println("FileNotFoundException");
            e1.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
        ArrayList<Set<String>> sect = entrada.getSectorizacion();
        if(entrada.getSectorizacionModificada()!=null) {
            sect = entrada.getSectorizacionModificada();//.getSectorizacion();        	
        }
        ArrayList<List<String>> sectFinal = new ArrayList<>();
        ArrayList<Sector> sectAbiertos = entrada.getListaSectoresAbiertos();
        if(entrada.getListaNuevosSectoresAbiertosTrasMomentoActual()!=null) {
        	ArrayList<Sector> sectAbiertos1 = entrada.getListaNuevosSectoresAbiertosTrasMomentoActual();//.getListaSectoresAbiertos();
        	for (int i = 0; i < sectAbiertos1.size(); i++) {
				sectAbiertos.add(sectAbiertos1.get(i));
			}
        }
        /*MODIFICO LA SECTORIZACION PARA PASAR DE ID A NOMBRE*/
        for (Set<String> strings : sect) {
            List<String> slot = new ArrayList<>(strings);
            for (int j = 0; j < slot.size(); j++) {
                String s = slot.get(j);
                for (Sector sectAbierto : sectAbiertos) {
                    if (s.equalsIgnoreCase(sectAbierto.getId())) {
                        slot.set(j, sectAbierto.getNombre());
                        break;
                    }
                }
            }
            sectFinal.add(slot);
        }
        /*TERMINO DE MODIFICAR LA SECTORIZACION PARA PASAR DE ID A NOMBRE*/
        if (titulo == null) {
            titulo = Main.entradaPath + "_" + Main.entradaId;
        }
        int hoja = 1;
        CellStyle cstyle = workbook.createCellStyle();
        /*cstyle.setFillPattern(FillPatternType.BIG_SPOTS);
        cstyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());*/
        cstyle.setAlignment(HorizontalAlignment.CENTER);
        cstyle.setBorderBottom(BorderStyle.MEDIUM);
        cstyle.setBorderLeft(BorderStyle.MEDIUM);
        cstyle.setBorderRight(BorderStyle.MEDIUM);
        cstyle.setBorderTop(BorderStyle.MEDIUM);
        CellStyle cstyle1 = workbook.createCellStyle();
        /*cstyle1.setFillPattern(FillPatternType.BIG_SPOTS);
        cstyle1.setFillBackgroundColor(IndexedColors.RED.getIndex());*/
        cstyle1.setAlignment(HorizontalAlignment.CENTER);
        cstyle1.setBorderBottom(BorderStyle.MEDIUM);
        cstyle1.setBorderLeft(BorderStyle.MEDIUM);
        cstyle1.setBorderRight(BorderStyle.MEDIUM);
        cstyle1.setBorderTop(BorderStyle.MEDIUM);
        CellStyle center = workbook.createCellStyle();
        center.setAlignment(HorizontalAlignment.CENTER);
        center.setBorderBottom(BorderStyle.MEDIUM);
        center.setBorderLeft(BorderStyle.MEDIUM);
        center.setBorderRight(BorderStyle.MEDIUM);
        center.setBorderTop(BorderStyle.MEDIUM);

        for (int e = 0; e < poblacion.size(); e++) {
            XSSFSheet sheet = workbook.getSheet("Solucion " + hoja);
            hoja++;
            /*INICIO TITULO*/
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("" + titulo);
            /*FIN TITULO*/
            /*INICIO SECTORIZACION*/
            row = sheet.createRow(2);
            int cnt = 1, equ = 0;
            for (int i = 0; i < sectFinal.size(); i++) {
                if (cnt != 1) {
                    row.createCell(cnt).setCellStyle(center);
                } else {
                    row.createCell(cnt);
                }
                if (i == sectFinal.size() - 1) {
                    if (equ != 0) {
                        row.createCell(cnt + 1).setCellStyle(center);
                        sheet.addMergedRegion(new CellRangeAddress(2, 2, cnt - equ, cnt + 1));
                    }
                    String tmp = "";
                    for (int j = 0; j < sectFinal.get(i).size(); j++) {
                        if (j + 1 == sectFinal.get(i).size()) {
                            tmp += sectFinal.get(i).get(j);
                        } else {
                            tmp += sectFinal.get(i).get(j) + "-";
                        }
                    }
                    row.getCell(cnt - equ).setCellValue(tmp);
                    row.getCell(cnt - equ).setCellStyle(center);
                } else if (i == 0) {
                    if (sectFinal.get(i).equals(sectFinal.get(i + 1))) {
                        //equ++;
                    } else {
                        String tmp = "";
                        for (int j = 0; j < sectFinal.get(i).size(); j++) {
                            if (j + 1 == sectFinal.get(i).size()) {
                                tmp += sectFinal.get(i).get(j);
                            } else {
                                tmp += sectFinal.get(i).get(j) + "-";
                            }
                        }
                        row.getCell(cnt).setCellValue(tmp);
                        row.getCell(cnt).setCellStyle(center);
                    }
                } else if (sectFinal.get(i).equals(sectFinal.get(i - 1))) {
                    equ++;
                } else if (!sectFinal.get(i).equals(sectFinal.get(i - 1))) {
                    if (equ != 0) {
                        sheet.addMergedRegion(new CellRangeAddress(2, 2, cnt - equ, cnt));
                    }
                    String tmp = "";
                    for (int j = 0; j < sectFinal.get(i - 1).size(); j++) {
                        if (j + 1 == sectFinal.get(i - 1).size()) {
                            tmp += sectFinal.get(i - 1).get(j);
                        } else {
                            tmp += sectFinal.get(i - 1).get(j) + "-";
                        }
                    }
                    row.getCell(cnt - equ).setCellValue(tmp);
                    row.getCell(cnt - equ).setCellStyle(center);
                    equ = 0;
                }
                cnt++;
            }
            /*FIN SECTORIZACION*/
            /*INICIO SOLUCION*/
            ArrayList<String> sol = poblacion.get(e).getTurnos();
            ArrayList<Controlador> controladores = poblacion.get(e).getControladores();
            ArrayList<Sector> lstSect = entrada.getListaSectoresAbiertos();
            for (int i = 0; i < sol.size(); i++) {
                row = sheet.createRow(4 + i);
                String line = sol.get(i);
                cnt = 1;
                equ = 0;
                for (int k = 0; k < line.length(); k += 3) {
                    if (cnt != 1) {
                        row.createCell(cnt).setCellStyle(center);
                    } else {
                        row.createCell(cnt);
                    }
                    if (k == line.length() - 3) {
                        if (equ != 0) {
                            row.createCell(cnt + 1).setCellStyle(center);
                            sheet.addMergedRegion(new CellRangeAddress(4 + i, 4 + i, cnt - equ, cnt + 1));
                            boolean find = false;
                            for (int j = 0; j < lstSect.size(); j++) {
                                if (lstSect.get(j).getId().equalsIgnoreCase(line.substring(k, k + 3))) {
                                    if (line.substring(k, k + 3).toUpperCase().equals(line.substring(k, k + 3))) {
                                        row.getCell(cnt - equ).setCellValue(lstSect.get(j).getId().toUpperCase());
                                    } else {
                                        row.getCell(cnt - equ).setCellValue(lstSect.get(j).getId().toLowerCase());
                                    }
                                    find = true;
                                }
                            }
                            if (!find) {
                                row.getCell(cnt - equ).setCellValue(line.substring(k, k + 3));
                            }
                        } else {
                            /*Inicio Penultima celda*/
                            row.createCell(cnt).setCellStyle(center);
                            boolean find = false;
                            for (int j = 0; j < lstSect.size(); j++) {
                                if (lstSect.get(j).getId().equalsIgnoreCase(line.substring(k - 3, k))) {
                                    if (line.substring(k - 3, k).toUpperCase().equals(line.substring(k - 3, k))) {
                                        row.getCell(cnt - equ).setCellValue(lstSect.get(j).getId().toUpperCase());
                                    } else {
                                        row.getCell(cnt - equ).setCellValue(lstSect.get(j).getId().toLowerCase());
                                    }
                                    find = true;
                                }
                            }
                            if (!find) {
                                row.getCell(cnt - equ).setCellValue(line.substring(k - 3, k));
                            }
                            /*Inicio Ultima celda*/
                            row.createCell(cnt + 1);//.setCellStyle(center);
                            find = false;
                            for (int j = 0; j < lstSect.size(); j++) {
                                if (lstSect.get(j).getId().equalsIgnoreCase(line.substring(k, k + 3))) {
                                    if (line.substring(k, k + 3).toUpperCase().equals(line.substring(k, k + 3))) {
                                        row.getCell(cnt - equ + 1).setCellValue(lstSect.get(j).getId().toUpperCase());
                                    } else {
                                        row.getCell(cnt - equ + 1).setCellValue(lstSect.get(j).getId().toLowerCase());
                                    }
                                    find = true;
                                }
                            }
                            if (!find) {
                                row.getCell(cnt - equ + 1).setCellValue(line.substring(k, k + 3));
                            }
                            row.getCell(cnt + 1).setCellStyle(center);
                            /*Fin Ultima celda*/
                        }

                        if (line.substring(k - 3, k).equalsIgnoreCase(STRING_DESCANSO)) {
                            row.getCell(cnt - equ).setCellStyle(cstyle);
                        } else {
                            //row.getCell(cnt-equ).setCellValue("");
                            row.getCell(cnt - equ).setCellStyle(center);
                        }
                    } else if (k != 0 && !line.substring(k - 3, k).equals(line.substring(k, k + 3))) {
                        if (equ != 0) {
                            sheet.addMergedRegion(new CellRangeAddress(4 + i, 4 + i, cnt - equ, cnt));
                        }
                        boolean find = false;
                        for (int j = 0; j < lstSect.size(); j++) {
                            if (lstSect.get(j).getId().equalsIgnoreCase(line.substring(k - 3, k))) {
                                if (line.substring(k - 3, k).toUpperCase().equals(line.substring(k - 3, k))) {
                                    row.getCell(cnt - equ).setCellValue(lstSect.get(j).getId().toUpperCase());
                                } else {
                                    row.getCell(cnt - equ).setCellValue(lstSect.get(j).getId().toLowerCase());
                                }
                                find = true;
                            }
                        }
                        if (!find) {
                            row.getCell(cnt - equ).setCellValue(line.substring(k - 3, k));
                        }

                        if (line.substring(k - 3, k).equalsIgnoreCase(STRING_DESCANSO)) {
                            row.getCell(cnt - equ).setCellStyle(cstyle);
                        } else {
                            //row.getCell(cnt-equ).setCellValue("");
                            row.getCell(cnt - equ).setCellStyle(center);
                        }
                        equ = 0;
                    } else if (k != 0 && line.substring(k - 3, k).equals(line.substring(k, k + 3))) {
                        equ++;
                    } else if (k == 0) {
                        if (line.substring(k, k + 3).equals(line.substring(k + 3, k + 6))) {
                            //equ++;
                        } else {
                            boolean find = false;
                            for (int j = 0; j < lstSect.size(); j++) {
                                if (lstSect.get(j).getId().equalsIgnoreCase(line.substring(k, k + 3))) {
                                    if (line.substring(k, k + 3).toUpperCase().equals(line.substring(k, k + 3))) {
                                        row.getCell(cnt - equ).setCellValue(lstSect.get(j).getId().toUpperCase());
                                    } else {
                                        row.getCell(cnt - equ).setCellValue(lstSect.get(j).getId().toLowerCase());
                                    }
                                    find = true;
                                }
                            }
                            if (!find) {
                                row.createCell(cnt).setCellValue(line.substring(k, k + 3));
                            }

                            if (line.substring(k, k + 3).equalsIgnoreCase(STRING_DESCANSO)) {
                                row.getCell(cnt - equ).setCellStyle(cstyle);
                            } else {
                                //row.getCell(cnt-equ).setCellValue("");
                                row.getCell(cnt - equ).setCellStyle(center);
                            }
                        }
                    }
                    int id = 0;
                    for (int j = 0; j < controladores.size(); j++) {
                        if (controladores.get(j).getTurnoAsignado() == i) {
                            id = controladores.get(j).getId();
                        }
                    }
                    row.createCell(0).setCellStyle(center);
                    row.getCell(0).setCellValue("C" + id);
                    cnt++;
                }
            }
            /*FIN SOLUCION*/
            /*Inicio CONTROLADORES SOLUCION*/
            int n = 5 + sol.size();
            Row row1 = sheet.createRow(n);

            row1.createCell(2).setCellValue("Id");
            row1.createCell(3).setCellValue("Nuc");
            row1.createCell(4).setCellValue("Turno");
            row1.createCell(5).setCellValue("PTD");
            row1.createCell(6).setCellValue("CON");
            row1.createCell(7).setCellValue("T Asignado");
            row1.createCell(8).setCellValue("T Noche");

            for (int i = 0; i < controladores.size(); i++) {
                row = sheet.createRow(6 + i + sol.size());
                Controlador c = controladores.get(i);
                row.createCell(2).setCellValue("C" + c.getId());
                row.createCell(3).setCellValue(c.getNucleo());
                row.createCell(4).setCellValue(c.getTurno());
                row.createCell(5).setCellValue(c.isPTD());
                row.createCell(6).setCellValue(c.isCON());
                row.createCell(7).setCellValue(c.getTurnoAsignado());
                row.createCell(8).setCellValue(c.getTurnoNoche());
            }
            /*FIN CONTROLADORES SOLUCION*/

            Restricciones.penalizacionPorRestricciones(poblacion.get(e), patrones, entrada, p);
            double[] listaRestricciones = Restricciones.restriccionesNoCumplidas;
            row = sheet.createRow(9 + (sol.size() * 2));
            row1 = sheet.createRow(10 + (sol.size() * 2));
            for (int i = 2; i < listaRestricciones.length + 2; i++) {
                row.createCell(i).setCellValue("R" + (i - 1));
                row1.createCell(i).setCellValue((int) Math.floor(listaRestricciones[i - 2]));
            }
        }
        /*CERRAR FICHERO*/
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
            Date date = new Date();

            FileOutputStream out = new FileOutputStream(new File(carpetaS + "/Soluciones_" + titulo + "_CurrentDate_" + dateFormat.format(date) + ".xlsx"));

            workbook.write(out);
            workbook.close();
            out.close();

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
        /*FIN CERRAR FICHERO*/
    }

    /**
     * Metodo para representar las trazas generadas por el algoritmo en un fichero excel.
     *
     * @param info Contiene toda la informacion almacenada y estructurada para su escritura en excel y la generacion de graficos correspondientes.
     */
    public static void escribirTrazasEnExcel(ArrayList<ArrayList<ArrayList<String>>> info) {
        System.out.println("Escribiendo en Excel...");
        ArrayList<ArrayList<String>> mismaTraza = new ArrayList<>();
        ArrayList<String> traza = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            mismaTraza = info.get(i);
            FileInputStream file;
            HSSFWorkbook workbook = null;
            try {
                file = new FileInputStream(new File("src/main/resources/PlantillaTrazas.xlsx"));
                workbook = new HSSFWorkbook(file);
            } catch (FileNotFoundException e1) {

                e1.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            String titulo = "";

            for (int e = 0; e < mismaTraza.size(); e++) {
                traza = mismaTraza.get(e);
                //traza.remove(28);//ESPACIO EN BLANCO
                traza.remove(2);//ESPACIO EN BLANCO
                if (e == 0) {
                    titulo = traza.get(0);
                    titulo = titulo.replaceAll(":", "_");
                    titulo = titulo.replaceAll("\\.", ",");
                    titulo = titulo.replaceAll(" ", "");
                }

                HSSFSheet sheet = workbook.getSheet("Hoja" + e);
                //CABECERA
                for (int a = 0; a < 10; a++) {
                    Row row = sheet.createRow(a);
                    String linea = traza.get(a);
                    String[] div = linea.split(";");
                    for (int d = 0; d < div.length; d++) {
                        row.createCell(d).setCellValue(div[d]);
                    }
                }

                //TRAZAS LARGAS
                int fila = 10;
                if (traza.size() > 190000) {//Excel no permite escribir mas de 65535 filas, por lo que da una excepcion:Invalid row number (65536) outside allowable range (0..65535)
                    for (int a = 28; a < traza.size(); a += 6) {
                        Row row = sheet.createRow(fila);
                        String linea = traza.get(a);
                        String[] div = linea.split(";");
                        for (int d = 0; d < div.length; d++) {
                            if (a == 28) {
                                row.createCell(d).setCellValue(div[d]);
                            } else {
                                row.createCell(d).setCellValue(Double.parseDouble(div[d].replaceAll(",", "\\.")));
                            }
                        }
                        fila++;
                        if (fila > 65000) {
                            break;
                        }
                    }
                } else if (traza.size() > 65000) {//Excel no permite escribir mas de 65535 filas, por lo que da una excepcion:Invalid row number (65536) outside allowable range (0..65535)
                    for (int a = 28; a < traza.size(); a += 3) {
                        Row row = sheet.createRow(fila);
                        String linea = traza.get(a);
                        String[] div = linea.split(";");
                        for (int d = 0; d < div.length; d++) {
                            if (a == 28) {
                                row.createCell(d).setCellValue(div[d]);
                            } else {
                                row.createCell(d).setCellValue(Double.parseDouble(div[d].replaceAll(",", "\\.")));
                            }
                        }
                        fila++;
                        if (fila > 65000) {
                            break;
                        }
                    }
                } else {
                    for (int a = 28; a < traza.size(); a++) {
                        Row row = sheet.createRow(fila);
                        String linea = traza.get(a);
                        String[] div = linea.split(";");
                        for (int d = 0; d < div.length; d++) {
                            if (a == 28) {
                                row.createCell(d).setCellValue(div[d]);
                            } else {
                                row.createCell(d).setCellValue(Double.parseDouble(div[d].replaceAll(",", "\\.")));
                            }
                        }
                        fila++;
                    }
                }


                //LATERAL
                fila = 1;
                Row r = sheet.getRow(fila - 1);
                r.createCell(14).setCellValue(traza.get(10));//Titulo
                for (int a = 11; a < 25; a++) {
                    Row row = sheet.getRow(fila);
                    try {
                        row.createCell(14).setCellValue(Double.parseDouble(traza.get(a)));
                    } catch (Exception exception) {
                        row.createCell(14).setCellValue(0.0);
                    }
                    fila++;
                }

            }
            /*CERRAR FICHERO*/
            try {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
                FileOutputStream out =
                        new FileOutputStream(new File(Main.carpetaTrazas + "/Traza" + titulo + "_" + dateFormat.format(new Date()) + ".xlsx"));
                //System.out.println(titulo);
                workbook.write(out);
                workbook.close();
                out.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            /*FIN CERRAR FICHERO*/
        }
    }
}
