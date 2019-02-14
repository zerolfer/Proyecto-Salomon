package trazas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import Main.Main;

public class TrazasAntiguo {

private static ArrayList<Traza> trazas = new ArrayList<Traza>();
private static ArrayList<String> trazas2 = new ArrayList<String>();/*Contiene las trazas completas de la evolucion del fitness (trazasFit) y otras trazas del ratio de aceptacion cada L iteracciones (trazasL)*/
private static Properties propOpciones = new Properties();
private static int nTrazas=0;
private static int nTmnsIntervalos=12;


	public static void addTraza(Traza traza){
		trazas.add(traza);
	}
	public static void archivarYLimpiarTrazas(ArrayList<Solucion> poblacionReducirControladores,String propFileOptions, ParametrosAlgoritmo_SA pa) {
		String c1="resultados/"+Main.entradaPath+Main.entradaId+"/";
		File f = new File(c1);
        f.mkdir();
        c1 = c1 + "Trazas/";
		File f1 = new File(c1);
        f1.mkdir(); 
		escribirTrazasFityL();
		getPropertiesO(propFileOptions);
		int[] titulos = obtenerTrazasDeInteres();
		String fichero = inicioTitulos(titulos);
		for(int i=0;i<trazas.size();i++){
			String[] info = ordenarTrazas(i,trazas.get(i).getOrden(), trazas.get(i).getInfo(), trazas.get(i).getTmn(),titulos,fichero,trazas.get(i).getTime(), pa);
			rwFiles.Escritura.escrituraCompleta(info[0], info[1]);
		}
		nTrazas = trazas.size();
		limpiarTrazas();
	}
	
	public static void limpiarTrazas() {
		trazas = new ArrayList<Traza>();
		trazas2 = new ArrayList<String>();
		
	}
	private static void escribirTrazasFityL() {
		/*Contiene las trazas completas de la evolucion del fitness (trazasFit) y otras trazas del ratio de aceptacion cada L iteracciones (trazasL)*/
		for (int i = 0; i < trazas2.size(); i+=2) {
			rwFiles.Escritura.escrituraCompleta(trazas2.get(i),trazas2.get(i+1));
		}
	}
	
	public static void trazasAExcel() {
		File f = new File(Main.carpetaTrazas);
        f.mkdir();
		ArrayList<ArrayList<ArrayList<String>>> info = recogidaTrazas();
		rwFiles.EscrituraExcel.escribirTrazasEnExcel(info);	
	}
	
	private static ArrayList<ArrayList<ArrayList<String>>> recogidaTrazas() {
		int e=0;
		ArrayList<String> resultado = new ArrayList<String>();
		ArrayList<ArrayList<String>> mismosParametros = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> todo = new ArrayList<ArrayList<ArrayList<String>>>();
		for(int i=0;i<nTrazas;i++){
			resultado = leerTrazas(Main.carpetaTrazas+"/TrazaN"+i+"_"+0+".txt");
			mismosParametros.add(resultado);
			e++;
			//if(i==6){
			//}
		}
		todo.add(mismosParametros);
		mismosParametros = new ArrayList<ArrayList<String>>();
		e=0;
		return todo;
	}
	
	private static String[] ordenarTrazas(int name, int n, ArrayList<ArrayList<String>> trazas,int tmn, int[] titulos, String fichero, long time, ParametrosAlgoritmo_SA pa) {
		System.out.println("Escribiendo traza "+name+"...");
		double mej=0,cam=0,nomej=0;
		double mPorcent=0,mediaIntentosTotal=0, mediaC1Intentos=0, mediaIntervalosIntentos=0;
		double maxDif =0,minDif=1000000,dif=0;
		
		//ArrayList<String> tempDatos = new ArrayList<String>();
		int[] tamanosIntervalos = new int[nTmnsIntervalos];//TODO: ESTA LONGITUD DEBE SER VARIABLE Y AQUI ALGO NO CUDRA SI SE CAMBIA
		for(int i=0;i<trazas.size();i++){
			ArrayList<String> iteracionX = trazas.get(i);
			if(iteracionX.size()!=0){
				fichero = ponerFila(fichero,iteracionX,titulos);
				if(iteracionX.get(11).equalsIgnoreCase("1")){
					mej++;
				}else{
					nomej++;
					mPorcent=mPorcent+Double.parseDouble(iteracionX.get(12).replace(",", "."));
					if(iteracionX.get(13).equalsIgnoreCase("1")){
						cam++;
					}
				}
				dif = Double.parseDouble(iteracionX.get(8));
				if(maxDif<dif){
					maxDif =dif;
				}if(minDif>dif){
					minDif =dif;
				}
				mediaIntentosTotal = mediaIntentosTotal+Double.parseDouble(iteracionX.get(4));
				mediaC1Intentos = mediaC1Intentos+Double.parseDouble(iteracionX.get(3));
				mediaIntervalosIntentos = mediaIntervalosIntentos+Double.parseDouble(iteracionX.get(2));
				tamanosIntervalos[(Integer.parseInt(iteracionX.get(1))/3)] = tamanosIntervalos[(Integer.parseInt(iteracionX.get(1))/3)]+1;
			}
		}
		cam = cam/nomej;
		mPorcent = mPorcent/nomej;
		mej = mej/(mej+nomej);
		String mediasTotales ="Controladores: "+ tmn+ ";;Media de Intentos Totales: "+(mediaIntentosTotal/trazas.size())+"\nMedia de Intentos de Intervalo: "+(mediaIntervalosIntentos/trazas.size())+"\nMedia de Intentos de trabajador1: "+(mediaC1Intentos/trazas.size())+"\nPorcentaje de mejora: "+(mej*100)+"%\nPorcentaje de cambios a peor: "+(cam*100)+"%\nMedia de probabilidad de cambio: "+(mPorcent)+"\n";
		mediasTotales += "\n";
		
		String bucle="";
		if(titulos[1]==1){bucle = "Soluciones validas obtenidas con los distintos tamaños de movimiento\n";}else{bucle="\n";}
			for(int i=0;i<tamanosIntervalos.length;i++){
				if(titulos[1]==1){
					bucle +=tamanosIntervalos[i]+"\n";
				}else{bucle+="\n";}
				
			}
		
		String info = "Temp: "+pa.getTemperaturaInicial()+" Alpha: "+pa.getDescensoTemperatura()+" L: "+pa.getIteracionesTemperaturaL()+" CPporcent: "+pa.getCondicionParadaPorcent()+" CPciclos: "+pa.getCondicionParadaCiclos()+ "FitnessFunction: "+pa.getFuncionFitnessFase2() +"\n"+"Tiempo de ejecución: "+ ((time/1000.0)/60.0)+"\n";
		info = info + "\n"+mediasTotales+"\n"+bucle+"\n"+fichero;
		String[] datosEscritura = {Main.carpetaTrazas+"TrazaN"+name+"_"+0+".txt",info};
		return datosEscritura;
	}
	
	private static String inicioTitulos(int[] titulos) {
		String fichero ="Nº Iteraciones;";
		for(int i=1;i<titulos.length;i++){
			if(titulos[i]==2){fichero += "Nº Controladores1 Probados;";}
			else if(titulos[i]==3){fichero += "Nº Intervalos Probados;";}
			else if(titulos[i]==4){fichero += "Nº Controladores2 Probados;";}
			else if(titulos[i]==5){fichero += "Fitness Individuo Actual;";}
			else if(titulos[i]==6){fichero += "Fitness f1;";}
			else if(titulos[i]==7){fichero += "Fitness f2;";}
			else if(titulos[i]==8){fichero += "Diferencia de Fitness;";}
			else if(titulos[i]==9){fichero += "Fitness Mejor Individuo (f*);";}
			else if(titulos[i]==10){fichero += "Temperatura;";}
			else if(titulos[i]==11){fichero += "Mejora;";}
			else if(titulos[i]==12){fichero += "Probabilidad de Elección;";}
			else if(titulos[i]==13){fichero += "Cambio Realizado;";}
		}
		fichero +="\n";
		return fichero;
	}

	private static int[] obtenerTrazasDeInteres(){
		int[] tt =  new int[15];tt[0] = 0;
		int cont=1;
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("tamanoIntervaloRealizado"))){tt[cont] = 1;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("numControladores1Probados"))){tt[cont] = 2;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("numIntervalosProbados"))){tt[cont] = 3;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("numControladores2Probados"))){tt[cont] = 4;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("fitnessIndividuoActual"))){tt[cont] = 5;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("fitnessf1"))){tt[cont] = 6;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("fitnessf2"))){tt[cont] = 7;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("diferenciaFitness"))){tt[cont] = 8;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("fitnessMejorIndividuo"))){tt[cont] = 9;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("temperatura"))){tt[cont] = 10;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("mejora"))){tt[cont] = 11;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("probabilidadEleccion"))){tt[cont] = 12;cont++;}
		if("TRUE".equalsIgnoreCase(propOpciones.getProperty("cambioRealizado"))){tt[cont] = 13;cont++;}
		return tt;
	}
	private static String ponerFila(String fichero, ArrayList<String> iteracionX, int[] titulos) {
		fichero +=iteracionX.get(0)+";";
		for(int i=1;i<titulos.length;i++){
			if(titulos[i]==2){fichero +=iteracionX.get(2).replace(".", ",")+";";}
			else if(titulos[i]==3){fichero +=iteracionX.get(3).replace(".", ",")+";";}
			else if(titulos[i]==4){fichero +=iteracionX.get(4).replace(".", ",")+";";}
			else if(titulos[i]==5){fichero +=iteracionX.get(5).replace(".", ",")+";";}
			else if(titulos[i]==6){fichero +=iteracionX.get(6).replace(".", ",")+";";}
			else if(titulos[i]==7){fichero +=iteracionX.get(7).replace(".", ",")+";";}
			else if(titulos[i]==8){fichero +=iteracionX.get(8).replace(".", ",")+";";}
			else if(titulos[i]==9){fichero +=iteracionX.get(9).replace(".", ",")+";";}
			else if(titulos[i]==10){fichero +=iteracionX.get(10).replace(".", ",")+";";}
			else if(titulos[i]==11){fichero +=iteracionX.get(11).replace(".", ",")+";";}
			else if(titulos[i]==12){fichero +=iteracionX.get(12).replace(".", ",")+";";}
			else if(titulos[i]==13){fichero +=iteracionX.get(13).replace(".", ",")+";";}
		}
		fichero +="\n";
		return fichero;
	}
	
	private static ArrayList<String> leerTrazas(String path) {
		/*TRATAMIENTO DE TRAZAS*/
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> resultado = new ArrayList<String>();
		try {
			archivo = new File(path);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			String linea;
			while((linea = br.readLine()) != null){
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
		TrazasAntiguo.trazas = trazas;
	}
	public static Properties getPropOpciones() {
		return propOpciones;
	}
	public static void setPropOpciones(Properties propOpciones) {
		TrazasAntiguo.propOpciones = propOpciones;
	}
	public static int getnTmnsIntervalos() {
		return nTmnsIntervalos;
	}
	public static void setnTmnsIntervalos(int nTmnsIntervalos) {
		TrazasAntiguo.nTmnsIntervalos = nTmnsIntervalos;
	}
	public static ArrayList<String> getTrazas2() {
		return trazas2;
	}
	public static void setTrazas2(ArrayList<String> trazas2) {
		TrazasAntiguo.trazas2 = trazas2;
	}

	
	
}
