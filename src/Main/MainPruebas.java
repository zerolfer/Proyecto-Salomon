package Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Entrada;
import fitnessFunction.DeciderFitnessFunction;
import herramientas.lecturaSoluciones.LecturaSoluciones;
import herramientas.pruebasVarias.PeorSolucionPosible;
import patrones.Patrones;
import patrones.Restricciones;
import pruebasCasos.DeciderCase;
import pruebasCasos.InicializacionCasos;
import rwFiles.Escritura;
/**
 * Clase creada para realizar pruebas sobre el codigo
 * @author Tino
 *
 */

public class MainPruebas {
	/**
	 * Pruebas1
	 * @param args Null
	 */
/* ESTO SOLO EXISTE EN EL MAIN
 * 	public static String propFileParameters = "recursos/problemParameters.properties";
	public static String propFileOptions= "recursos/options.properties";
	public static String propFileParametersAlgorithm = "recursos/algorithm.properties";
	
	public static String entradaPath = "Caso2";
	public static String entradaId = "Id1n-06-03-2017";
	public static String entorno = "Canarias";
	
	public static String carpetaSoluciones= "";
	public static String carpetaTrazas= "";
*/
	public static String problema = "";
	public static double[] TiemposResultadosproblema = {0,0,0,0,0,0,0};
	public static String[] LeyendaTRP= {"tiempoInicio","Tiempo1Factible","tiempoMedioSol","tiempoMaxSol","tiempoMinSol","tiempoTotal","tiempoMedioFactible"};
	public static double[] tiemposPorSolucionAux= {0,0,0,0,0,0,0,0,0,0};
	public static double tiempoFactibleAux = 0;
	public static int numFactibleAux=0;
	public static void mainPruebas1() {
		
		int nEjecuciones = 1;
		int[] casos = {65,69,7,13,22,30};
		
		String[] temperaturaInicial = {"BenAmeur", "Johnson", "krikpatric"};
		String[] Literaciones = {"CAdaptativas", "CEstaticas", "CutOff"};
		String[] DescensoTemperatura = {"krikpatric", "Adaptativa"/*, "Aarts"*/};
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		long t1 = System.currentTimeMillis();TiemposResultadosproblema[0]=t1;
		long t2 = System.currentTimeMillis();
		int cnt=0;
		for (int i = 0; i < nEjecuciones; i++) {
			for (int j = 0; j < casos.length; j++) {
				for (int l = 0; l < temperaturaInicial.length; l++) {
					for (int m = 0; m < Literaciones.length; m++) {
						for (int n = 0; n < DescensoTemperatura.length; n++) {
							Main.date = new Date();
							problema = "Ejecucion: "+ i+",Caso"+casos[j] + " - TMP: "+ temperaturaInicial[l]+ " // L: "+Literaciones[m] +" // DescT: "+DescensoTemperatura[n]; 
							System.out.println(problema);
							TiemposResultadosproblema[0]=t1;
							if(cnt>=0) {
							mainPruebasEvolucionTemperaturaSA(i,"Caso"+casos[j], temperaturaInicial[l], Literaciones[m],DescensoTemperatura[n]);
							}cnt++;
							
							t2= System.currentTimeMillis();
							double t = (((t2-t1)/1000.0)/60.0);
							System.out.println("____________________________"+t+" mins"+"_______________________________________");
							t1=System.currentTimeMillis();
							
							problema += t + "mins___\n";
							
							
							String path= Main.carpetaSoluciones +casos[j]+"_Trazas_CurrentDate_"+ dateFormat.format(Main.date)+".txt";
							Escritura.escrituraCompleta(path, problema);
						}
					}
				}
			}
		}	
	}
	
	public static void mainPruebasEvolucionTemperaturaSA (int ejecucion,String caso, String temperaturaInicial, String literaciones, String descensoTemperatura) {
		/*INICIALIZACION DE DATOS*/
		DeciderCase.switchCase(caso);//El caso4 no tiene solucion
		
		Parametros parametros = new Parametros(Main.propFileParameters, Main.propFileOptions);
		ParametrosAlgoritmo_SA parametrosAlgoritmo = new ParametrosAlgoritmo_SA(Main.propFileParametersAlgorithm);
		Entrada entrada = Entrada.leerEntrada(parametros, Main.entradaPath,Main.entradaId,Main.entorno);
		Patrones patrones = new Patrones(entrada, parametros);
		Main.carpetaSoluciones= "resultados/"+Main.entradaPath+Main.entradaId+"/"+parametrosAlgoritmo.getAlgoritmo()+"/Soluciones/";
		Main.carpetaTrazas= "resultados/"+Main.entradaPath+Main.entradaId+"/"+parametrosAlgoritmo.getAlgoritmo()+"/Trazas/";
		
		parametrosAlgoritmo = reajustarParametros(parametrosAlgoritmo,temperaturaInicial,literaciones,descensoTemperatura,entrada,patrones,parametros);
		switch (parametrosAlgoritmo.getAlgoritmo()) {
			case "SA":
				Main_SA.main_sa(parametros, parametrosAlgoritmo, entrada, patrones);
			break;
			case "VNS":
				Main_VNS.main_vns(parametros, parametrosAlgoritmo, entrada, patrones);
			break;
			default:
				System.out.println("Algoritmo"+parametrosAlgoritmo.getAlgoritmo()+" no encontrado." );
			break;
		}
		
		Patrones.nuc = new ArrayList<String>();
	}
	
	
			
			
			
	private static ParametrosAlgoritmo_SA reajustarParametros(ParametrosAlgoritmo_SA parametrosAlgoritmo,
			String temperaturaInicial, String literaciones, String descensoTemperatura,Entrada entrada, Patrones patrones, Parametros parametros) {
		MainPruebas.problema +=(problema+"\n");	
		switch (temperaturaInicial) {
			case "BenAmeur":
				parametrosAlgoritmo.setTemperaturaInicial(0.075);
				System.out.println("TEMP: BenAmeur "+ parametrosAlgoritmo.getTemperaturaInicial());
				MainPruebas.problema +=("TEMP: BenAmeur "+ parametrosAlgoritmo.getTemperaturaInicial()+"\n");	
			break;
			case "Johnson":
				double porcentajeAceptacion=0.95;
				parametrosAlgoritmo.setTemperaturaInicial(CalcularJohnson(porcentajeAceptacion,entrada, parametros, patrones,parametrosAlgoritmo));
				System.out.println("TEMP: Johnson "+ parametrosAlgoritmo.getTemperaturaInicial());
				MainPruebas.problema +=("TEMP: Johnson "+ parametrosAlgoritmo.getTemperaturaInicial()+"\n");
			break;
			case "krikpatric":
				parametrosAlgoritmo.setTemperaturaInicial(CalcularKrikpatric(entrada, parametros, patrones,parametrosAlgoritmo));
				System.out.println("TEMP: krikpatric "+ parametrosAlgoritmo.getTemperaturaInicial());
				MainPruebas.problema +=("TEMP: krikpatric "+ parametrosAlgoritmo.getTemperaturaInicial()+"\n");
			break;
		}
		switch (literaciones) {
			case "CAdaptativas":
				int cont=0;
				for (int i = 0; i < entrada.getSectorizacion().size(); i++) {
					cont += entrada.getSectorizacion().get(i).size();
				}
				int L  = 8 * cont; //tmb se puede usar el numero de controladores disponibles (menos real)
				parametrosAlgoritmo.setIteracionesTemperaturaL(L);
				System.out.println("L: CAdaptativas "+ parametrosAlgoritmo.getIteracionesTemperaturaL());
				MainPruebas.problema +=("L: CAdaptativas "+ parametrosAlgoritmo.getIteracionesTemperaturaL()+"\n");
			break;
			case "CEstaticas":
				parametrosAlgoritmo.setIteracionesTemperaturaL(3000);
				System.out.println("L: CEstaticas "+ 3000);
				MainPruebas.problema +=("L: CEstaticas "+ 3000+"\n");
			break;
			case "CutOff":
				parametrosAlgoritmo.setIteracionesTemperaturaL(-1); //Hay que tocar en el codigo (=El umbral esta en 1000 creo que es muy alto)
				System.out.println("L: CutOff "+ 1000);
				MainPruebas.problema +=("L: CutOff "+ 1000+"\n");
			break;
		}
		switch (descensoTemperatura) {
			case "krikpatric":
				parametrosAlgoritmo.setDescensoTemperatura(0.925);
				System.out.println("Desc: krikpatric "+ 0.9);
				MainPruebas.problema +=("Desc: krikpatric "+ 0.9+"\n");
			break;
			case "Aarts":
				parametrosAlgoritmo.setDescensoTemperatura(-1);//Hay que tocar en el codigo (tocado)
				System.out.println("Desc: Aarts ");
				MainPruebas.problema +=("Desc: Aarts "+"\n");
			break;
			case "Adaptativa":
				parametrosAlgoritmo.setDescensoTemperatura(-2); //Hay que tocar en el codigo (tocado)
				System.out.println("Desc: Adaptativa ");
				MainPruebas.problema +=("Desc: Adaptativa "+"\n");
			break;
		}
		return parametrosAlgoritmo;
	}

	private static double CalcularKrikpatric(Entrada entrada, Parametros parametros, Patrones patrones,
			ParametrosAlgoritmo_SA parametrosAlgoritmo) {
		ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);
		double tmp = 0;
		for (int j = 0; j < 200; j++) {
			for (int i = 0; i < poblacionInicial.size(); i++) {
				Solucion individuo2 = algorithms.simulatedAnnealing.moves.DeciderMove.switchMoves(poblacionInicial.get(i),12,2, 0, parametrosAlgoritmo, patrones, entrada, parametros, new ArrayList<String>());
				double f1= fitnessFunction.Fitness.fitPonderadoRestricYNumCtrls(poblacionInicial.get(i), entrada, patrones, parametros)[0];
				double f2= fitnessFunction.Fitness.fitPonderadoRestricYNumCtrls(individuo2, entrada, patrones, parametros)[0];
				if(tmp < Math.abs(f2-f1)) {
					tmp= Math.abs(f2-f1);
				}
				poblacionInicial.set(i, individuo2);
			}
		}
		
		return tmp;
	}

	private static double CalcularJohnson(double pAcp, Entrada entrada, Parametros parametros, Patrones patrones,ParametrosAlgoritmo_SA parametrosAlgoritmo) {
		ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);
		int cnt=0;
		double media=0;
		for (int j = 0; j < 500; j++) {
			for (int i = 0; i < poblacionInicial.size(); i++) {
				Solucion individuo2 = algorithms.simulatedAnnealing.moves.DeciderMove.switchMoves(poblacionInicial.get(i),12,2, 0, parametrosAlgoritmo, patrones, entrada, parametros, new ArrayList<String>());
				double f1= fitnessFunction.Fitness.fitPonderadoRestricYNumCtrls(poblacionInicial.get(i), entrada, patrones, parametros)[0];
				double f2= fitnessFunction.Fitness.fitPonderadoRestricYNumCtrls(individuo2, entrada, patrones, parametros)[0];
				if(f1<f2) {
					media +=(f2-f1);
					cnt++;
				}
				poblacionInicial.set(i, individuo2);
			}
		}
		
		media = media/cnt;
		double tmp = media / Math.log(pAcp);
		return Math.abs(tmp);
	}

	public static void mainPruebas (String[] args) {//PARA UNA PRUEBA
		DeciderCase.switchCase("Caso16");//El caso4 no tiene solucion
		Parametros parametros = new Parametros(Main.propFileParameters, Main.propFileOptions);
		ParametrosAlgoritmo_SA parametrosAlg = new ParametrosAlgoritmo_SA(Main.propFileParametersAlgorithm);
		Entrada entrada = Entrada.leerEntrada(parametros, Main.entradaPath,Main.entradaId,Main.entorno);
		Patrones patrones = new Patrones(entrada, parametros);
		Main.carpetaSoluciones= "resultados/"+Main.entradaPath+Main.entradaId+"/"+parametrosAlg.getAlgoritmo()+"/Soluciones/";
		Main.carpetaTrazas= "resultados/"+Main.entradaPath+Main.entradaId+"/"+parametrosAlg.getAlgoritmo()+"/Trazas/";
		ArrayList<Solucion> poblacionInicial = PeorSolucionPosible.CrearPeorSolucionCaso3(entrada, parametros, patrones);
		double r = Restricciones.penalizacionPorRestricciones(poblacionInicial.get(0), patrones, entrada, parametros);


		System.out.println("res: "+r);
		double m = (14 * poblacionInicial.get(0).getTurnos().size())+ (4*(poblacionInicial.get(0).getTurnos().get(0).length()/3)*0.5*poblacionInicial.get(0).getTurnos().size());
		System.out.println(m);
	}
	/**
	 * Pruebas de inicializacion
	 * @param args Null
	 */
	public static void mainInicializacion (String[] args){ //Inicializacion
		/*INICIALIZACION DE DATOS*/
		DeciderCase.switchCase("Caso3");
		Parametros parametros = new Parametros(Main.propFileParameters, Main.propFileOptions);
		ParametrosAlgoritmo_SA parametrosAlg = new ParametrosAlgoritmo_SA(Main.propFileParametersAlgorithm);
		Entrada entrada = Entrada.leerEntrada(parametros, Main.entradaPath,Main.entradaId,Main.entorno);
		Patrones patrones = new Patrones(entrada, parametros);
		Main.carpetaSoluciones= "resultados/"+Main.entradaPath+Main.entradaId+"/"+parametrosAlg.getAlgoritmo()+"/Soluciones/";
		Main.carpetaTrazas= "resultados/"+Main.entradaPath+Main.entradaId+"/"+parametrosAlg.getAlgoritmo()+"/Trazas/";
		/*INICIALIZACION DE SOLUCIONES FACTIBLES*/
		ArrayList<Solucion> poblacionInicial = InicializacionCasos.CrearIndFijoCaso3(entrada, parametros, patrones);
		//ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);
		//ArrayList<Solucion> poblacionReducir = new ArrayList<Solucion>();poblacionReducir.add(poblacionInicial.get(0));
		double res =0;
		ArrayList<Solucion> poblacionFactible = new ArrayList<Solucion>(); 
		for (int i = 0; i < poblacionInicial.size(); i++) {			
			if((res = Restricciones.penalizacionPorRestricciones(poblacionInicial.get(i), patrones, entrada, parametros))==0){
				poblacionFactible.add(poblacionInicial.get(i));
			}
			System.out.println(i+"-Restricciones incumplidas: "+res);
		}

		rwFiles.EscrituraExcel.EscrituraSoluciones("Caso3Referencia",Main.carpetaSoluciones, poblacionInicial , entrada, patrones, parametros, parametrosAlg);
		parametrosAlg.setFuncionFitnessFase2(parametrosAlg.getFuncionFitnessFase3());
		double[] fit;
		for (int i = 0; i < poblacionInicial.size(); i++) {			
			fit = DeciderFitnessFunction.switchFitnessF(poblacionInicial.get(i), patrones, entrada, parametros, parametrosAlg);
			System.out.print("Fitness inicial de "+i+ "--> ");
			for (int j = 0; j < fit.length; j++) {
				System.out.print("fit"+j+" = "+fit[j]+" | ");
			}System.out.println("");
		}
		
		/*FIN PRESENTACION DE RESULTADOS Y TRAZAS*/
	}
	/**
	 * Pruebas con soluciones definidas.
	 * @param args Null
	 */
	public static void mainSoluciones(String[] args) {//SolucionesAdan
		DeciderCase.switchCase("Caso16");
		String solucionALeer1 = "solucionMadrid.txt";
		Parametros parametros = new Parametros(Main.propFileParameters, Main.propFileOptions);
		ParametrosAlgoritmo_SA parametrosAlg = new ParametrosAlgoritmo_SA(Main.propFileParametersAlgorithm);
		Entrada entrada = Entrada.leerEntrada(parametros, Main.entradaPath,Main.entradaId,Main.entorno);
		Patrones patrones = new Patrones(entrada, parametros);
		Main.carpetaSoluciones= "resultados/"+Main.entradaPath+Main.entradaId+"/"+parametrosAlg.getAlgoritmo()+"/Soluciones/";
		Main.carpetaTrazas= "resultados/"+Main.entradaPath+Main.entradaId+"/"+parametrosAlg.getAlgoritmo()+"/Trazas/";
		Solucion s1 =LecturaSoluciones.leerSoluciones("zEntrada/"+solucionALeer1+"", entrada);
		//ArrayList<Solucion> poblacion = InicializacionCasos.CrearIndFijoCaso9(entrada,parametros,patrones); // new ArrayList<Solucion>();
		//ArrayList<Solucion> poblacion = InicializacionCasos.CrearIndFijoCaso7(entrada,parametros,patrones); // new ArrayList<Solucion>();
		ArrayList<Solucion> poblacion = new ArrayList<Solucion>();
		poblacion.add(s1);
		System.out.println("RES: "+Restricciones.penalizacionPorRestricciones(poblacion.get(0), patrones, entrada, parametros));
		parametrosAlg.setFuncionFitnessFase2(parametrosAlg.getFuncionFitnessFase3());

	/*	double[] fit;
		for (int i = 0; i < poblacion.size(); i++) {			
			fit = DeciderFitnessFunction.switchFitnessF(poblacion.get(i), patrones, entrada, parametros, parametrosAlg);
			System.out.print("Fitness inicial de "+i+ "--> ");
			for (int j = 0; j < fit.length; j++) {
				System.out.print("fit"+j+" = "+fit[j]+" | ");
			}System.out.println("");
			FitnessFase3Info.fitGlobal(poblacion.get(i), entrada, patrones, parametros);
		}
		*/
		
		//INICIO PROCESO DE  OPTIMIZACION
		//ArrayList<Solucion> poblacionOptimizada = SimulatedAnnealing.bucleSA(poblacion,parametrosAlg,parametros,patrones,entrada);
		
		//FIN PROCESO DE  OPTIMIZACION

		//PRESENTACION DE RESULTADOS Y TRAZAS
//		rwFiles.EscrituraExcel.EscrituraSoluciones("Solucion-Caso7-pablo", Main.carpetaSoluciones, poblacion, entrada, patrones, parametros);
		//trazas.Trazas.archivarYLimpiarTrazas(poblacionOptimizada, propFileOptions, parametrosAlg);
		///FIN PRESENTACION DE RESULTADOS Y TRAZAS
	}
}
