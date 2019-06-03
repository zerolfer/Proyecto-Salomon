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
            case "Caso4":
                Main.entradaPath = "Caso4";
                Main.entradaId = "Id3t-16-01-2019";
                Main.entorno = "Madrid";
                break;
            case "Caso5":
                Main.entradaPath = "Caso5";
                Main.entradaId = "Id5t-16-01-2019";
                Main.entorno = "Madrid";
                break;
            case "Caso6":
                Main.entradaPath = "Caso6";
                Main.entradaId = "Id4t-14-01-2019";
                Main.entorno = "Madrid";
                break;
            case "Caso7":
                Main.entradaPath = "Caso7";
                Main.entradaId = "Id6t-19-10-2018";
                Main.entorno = "Barcelona";
                break;
            case "Caso8":
                Main.entradaPath = "Caso8";
                Main.entradaId = "Id0m-13-09-2018";
                Main.entorno = "Palma";
                break;
            case "Caso9":
                Main.entradaPath = "Caso9";
                Main.entradaId = "Id1m-13-09-2018";
                Main.entorno = "Palma";
                break;
            default:
                System.err.println("No es un caso predefinido, introducir ID y entorno");
                break;
        }

    }

}
