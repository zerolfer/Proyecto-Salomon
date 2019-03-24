package pruebasCasos;
import main.Main;

/**
 * Clase con posibles casos por defecto incorporados
 * @author Tino
 *
 */
public class DeciderCase {
	/**
	 * Metodo para introducir un caso nuevo
	 * @param caso  Nombre de la carpeta donde se almacena el caso, ej: Caso1
	 * @param id Id que se le asocia al caso, ej: Id1m-06-03-2017
	 * @param entorno Aeropuerto donde se produce (Canarias o Barcelona)
	 */
	public static void switchCase (String caso, String id, String entorno){
		Main.entradaPath = caso;
		Main.entradaId= id;
		Main.entorno = entorno;
	}
	/**
	 * Metodo para utilizar casos ya existentes
	 * @param caso  Nombre de la carpeta donde se almacena el caso, ej: Caso1
	 */
	public static void switchCase (String caso){
		switch(caso){
			case "Caso1":
				Main.entradaPath = "Caso1";
				Main.entradaId= "Id1m-06-03-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso2":
				Main.entradaPath = "Caso2";
				Main.entradaId= "Id1n-06-03-2017";
				Main.entorno = "Canarias";
			break;
			case "Caso3":
				Main.entradaPath = "Caso3";
				Main.entradaId= "Id2m-08-06-2017";
				Main.entorno = "Canarias";
			break;
			case "Caso4"://El caso4 no tiene solucion con los controladores disponibles.
				Main.entradaPath = "Caso4";
				Main.entradaId= "Id3m-08-06-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso5":
				Main.entradaPath = "Caso5";
				Main.entradaId= "Id4m-08-06-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso6":
				Main.entradaPath = caso;
				Main.entradaId= "Id5m-08-06-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso7":
				Main.entradaPath = caso;
				Main.entradaId= "Id6m-01-11-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso8":
				Main.entradaPath = caso;
				Main.entradaId= "Id7m-01-11-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso9":
				Main.entradaPath = caso;
				Main.entradaId= "Id8m-01-11-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso10":
				Main.entradaPath = caso;
				Main.entradaId= "Id9m-01-11-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso11":
				Main.entradaPath = caso;
				Main.entradaId= "Id10m-07-09-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso12":
				Main.entradaPath = caso;
				Main.entradaId= "Id11m-23-09-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso13":
				Main.entradaPath = caso;
				Main.entradaId= "Id12m-11-10-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso14":
				Main.entradaPath = caso;
				Main.entradaId= "Id13m-16-10-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso15":
				Main.entradaPath = caso;
				Main.entradaId= "Id14m-25-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso16":
				Main.entradaPath = caso;
				Main.entradaId= "Id15m-27-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso17":
				Main.entradaPath = caso;
				Main.entradaId= "Id16m-01-11-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso19":
				Main.entradaPath = caso;
				Main.entradaId= "Id1t-06-11-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso20":
				Main.entradaPath = caso;
				Main.entradaId= "Id7t-06-11-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso21":
				Main.entradaPath = caso;
				Main.entradaId= "Id7m-29-11-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso22":
				Main.entradaPath = caso;
				Main.entradaId= "Id7m-10-09-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso23":
				Main.entradaPath = caso;
				Main.entradaId= "Id7m-10-09-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso24":
				Main.entradaPath = caso;
				Main.entradaId= "Id7m-29-09-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso25":
				Main.entradaPath = caso;
				Main.entradaId= "Id7m-29-09-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso26":
				Main.entradaPath = caso;
				Main.entradaId= "Id7t-31-10-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso27":
				Main.entradaPath = caso;
				Main.entradaId= "Id7t-31-10-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso28":
				Main.entradaPath = caso;
				Main.entradaId= "Id23t-30-10-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso29":
				Main.entradaPath = caso;
				Main.entradaId= "Id23t-30-10-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso30":
				Main.entradaPath = caso;
				Main.entradaId= "Id21m-28-10-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso31":
				Main.entradaPath = caso;
				Main.entradaId= "Id22m-28-10-2017";
				Main.entorno = "Barcelona";
			break;
			case "Caso60":
				Main.entradaPath = caso;
				Main.entradaId= "Id1m-07-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso61":
				Main.entradaPath = caso;
				Main.entradaId= "Id2m-07-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso62":
				Main.entradaPath = caso;
				Main.entradaId= "Id3m-12-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso63":
				Main.entradaPath = caso;
				Main.entradaId= "Id4m-12-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso64":
				Main.entradaPath = caso;
				Main.entradaId= "Id5t-01-02-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso65":
				Main.entradaPath = caso;
				Main.entradaId= "Id6t-01-02-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso66":
				Main.entradaPath = caso;
				Main.entradaId= "Id7m-25-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso67":
				Main.entradaPath = caso;
				Main.entradaId= "Id8m-25-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso68":
				Main.entradaPath = caso;
				Main.entradaId= "Id9t-03-02-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso69":
				Main.entradaPath = caso;
				Main.entradaId= "Id10t-03-02-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso70":
				Main.entradaPath = caso;
				Main.entradaId= "Id11t-01-11-2017";
				Main.entorno = "Madrid";
			break;
			case "Caso71":
				Main.entradaPath = caso;
				Main.entradaId= "Id12t-01-11-2017";
				Main.entorno = "Madrid";
			break;
			default:
				System.out.println("No es un caso predefinido, introducir ID y entorno");
			break;
		}
	
	}

}
