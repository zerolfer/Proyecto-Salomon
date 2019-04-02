package pruebasCasos;

import main.Main;

/**
 * Clase con posibles casos por defecto incorporados
 *
 * @author Tino
 */
public class DeciderCase {
    /**
     * Metodo para introducir un caso nuevo
     *
     * @param caso    Nombre de la carpeta donde se almacena el caso, ej: Caso1
     * @param id      Id que se le asocia al caso, ej: Id1m-06-03-2017
     * @param entorno Aeropuerto donde se produce (Canarias o Barcelona)
     */
    public static void switchCase(String caso, String id, String entorno) {
        Main.entradaPath = caso;
        Main.entradaId = id;
        Main.entorno = entorno;
    }

    /**
     * Metodo para utilizar casos ya existentes
     *
     * @param caso Nombre de la carpeta donde se almacena el caso, ej: Caso1
     */
    public static void switchCase(String caso) {
        switch (caso) {
            case "Caso1":
                Main.entradaPath = "Caso1";
                Main.entradaId = "Id1m-01-01-2019";
                Main.entorno = "Barcelona";
                break;
            case "Caso2":
                Main.entradaPath = "Caso2";
                Main.entradaId = "xxx";
                Main.entorno = "Sevilla";
                break;
            case "Caso3":
                Main.entradaPath = "Caso3";
                Main.entradaId = "Id2m-01-01-2019";
                Main.entorno = "Barcelona";
                break;
            default:
                System.out.println("No es un caso predefinido, introducir ID y entorno");
                break;
        }

    }

}
