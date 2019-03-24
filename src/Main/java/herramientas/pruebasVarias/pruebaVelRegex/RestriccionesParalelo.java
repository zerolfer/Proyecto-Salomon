package herramientas.pruebasVarias.pruebaVelRegex;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import estructurasDatos.Parametros;
import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Entrada;
import patrones.Patrones;
import patrones.Restricciones;
import patrones.RestricionesParalelizadas;

public class RestriccionesParalelo {

	public static long comprobarRestriccionesParalelo(Solucion individuo, Parametros parametros,Entrada entrada, Patrones patrones,int NumHilos) {
		long t1 = System.currentTimeMillis();
		/*Inicio Restricciones*/
		ArrayList<RestricionesParalelizadas> arrayRes = new ArrayList<RestricionesParalelizadas>();
		ExecutorService threadPool = Executors.newFixedThreadPool(NumHilos);  //El parñmetro es en numero de threads que quieres lanzar
		RestricionesParalelizadas restricciones = null;
		for(int i=1;i <= 14; i++){
			restricciones = new RestricionesParalelizadas(i, individuo, patrones, entrada, parametros, 0);
			arrayRes.add(restricciones);
			threadPool.submit(restricciones);
		}
		threadPool.shutdown();
		while(!threadPool.isTerminated()){}
			
		return System.currentTimeMillis()-t1;
	}

	public static long comprobarRestriccionesSecuencial(Solucion individuo, Parametros parametros,Entrada entrada, Patrones patrones) {
		long t1 = System.currentTimeMillis();
		/*Inicio Restricciones*/
		double p=0;
		//Restriccion 2
		p += Restricciones.comprobarNucleoTrabajo(individuo, patrones);
		//Restriccion 3: No se necesita comprobar, ya que la acreditacion PTD puede controlar todos los sectores
		//Restriccion 4 
		p += Restricciones.comprobarTipoSector(individuo,patrones);
		//Restriccion 5 y 6
		p += Restricciones.comprobarPorcentajeDescanso(individuo, entrada, entrada.getTurno(), parametros);
		//Restriccion 7
		p += Restricciones.comprobarSectoresAbiertosNoche(individuo.getTurnos(),individuo.getControladores(), patrones);
		//Restriccion 8
		p += Restricciones.comprobarTrabajoMaximoConsecutivo(individuo.getTurnos(), parametros);
		//Restriccion 9
		p += Restricciones.comprobarControladorTurnoCorto(individuo, entrada);
		//Restriccion 10
		p += Restricciones.comprobarVentanaTrabajoDescanso(individuo.getTurnos(),parametros);
		//Restriccion 11
		p += Restricciones.comprobarCambioPosicion(individuo.getTurnos(),entrada.getMatrizAfinidad(), entrada.getListaSectores());
		//Restriccion 12 --> Es una restriccion que ya esta implicita en otras y se comprueba antes.
		//Restriccion 13 
		p += Restricciones.comprobarTrabajoMinimoConsecutivo(individuo.getTurnos(), parametros);
		//Restriccion 14 
		p += Restricciones.comprobarDescansoMinimoConsecutivo(individuo.getTurnos(), parametros);
		//Restriccion 15
		p += Restricciones.comprobarTrabajoPosicionMinimoConsecutivoNoRegex(individuo.getTurnos(), parametros);
		//Restriccion 16 TODO:REVISAR CODIGO CRIDA
		p += Restricciones.comprobarNumMaximoSectores(individuo.getTurnos(), entrada, parametros);
		//Restriccion 17 --> Es una restriccion que ya esta implicita en otras y se comprueba antes.
		
		//Restriccion x: Todo controlador tiene que tener un turno asignado y a todo turno se le tiene que asignar un controlador
		p += Restricciones.comprobarControladorAsignado(individuo);
		//Restriccion x: Todo controlador debe trabajar. No puede existir un turno vacio
		p += Restricciones.comprobarTurnoVacio(individuo);
		
			
		return System.currentTimeMillis()-t1;
	}

}
