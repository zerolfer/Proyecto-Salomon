package algorithms.simulatedAnnealing.moves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import InicializarPoblacion.ArreglarSoluciones;
import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.*;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Nucleo;
import estructurasDatos.DominioDelProblema.Sector;
import estructurasDatos.DominioDelProblema.Turno;
import fitnessFunction.DeciderFitnessFunction;
import fitnessFunction.Fitness;
import patrones.*;

public class GreedyMove2 {
	public static boolean posibleM=true;
	public static int contM=0;
	public static Solucion movimientoGready(Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg){
		int minT = parametros.getTiempoTrabMin()/parametros.getTamanoSlots();
		ArrayList<String> turnos = individuo.getTurnos();
		for(int i=0;i<turnos.size();i++){
			String turno = turnos.get(i);
			int cont = 0;
			for(int l=0;l<turno.length();l+=3){
				String ant = turno.substring(l, l+3);
				if(!ant.equals("111")){
					if(l+3<turno.length() && ant.equals(turno.substring(l+3, l+6))){
						cont++;
					}else{
						if(cont<minT-1){ //No se cumple el trabajo minimo
							turnos = arregloTrabajoMinimo(entrada, parametros, turnos,i,turno,l,ant, patrones);
								if(posibleM==false || contM > 200){
									posibleM=true;
									contM=0;
									individuo.setTurnos(turnos);
									return individuo;
								}
								turno = turnos.get(i);
								contM++;
								i-=1;cont=0;//Reseteamos l=-1 (al iniciar el bucle de nuevo l++ =0) para volver a comprobar el turno despues de los cambios
								break;
							}
							cont=0;
					}
				}
			}			
		}
		contM=0;
		return individuo;
	}
	private static ArrayList<String> arregloTrabajoMinimo(Entrada entrada, Parametros parametros, ArrayList<String> turnos, int posTurno, String turno, int posChar, String ant, Patrones patrones) {
		int[] posicionTras = busquedaTraspaso(entrada, parametros, turnos,posTurno,turno,posChar,ant,patrones);
		if(posicionTras[0]== -1){//No encontrado -  Intentar donacion
			int[] posicionAdq = busquedaAdquisicion(entrada, parametros, turnos,posTurno,turno,posChar,ant,patrones);
			if(posicionAdq[0] == -1){
				//Sin solucion!! Solucion no valida
 				posibleM=false;
			}else{
				turnos = realizarAdquisicion(posicionAdq,turnos,posTurno,turno,posChar,ant);
				
			}
		}else{
			turnos = realizarTranspaso(posicionTras,turnos,posTurno,turno,posChar,ant);
			
		}
		return turnos;
	}
	private static ArrayList<String> realizarAdquisicion(int[] posicionAdq, ArrayList<String> turnos, int posTurno,
			String turno, int posChar, String ant) {
		String AceptaTrabajo = turnos.get(posicionAdq[0]);
		String DaTrabajo = turno;
		String cadena = AceptaTrabajo.substring(posicionAdq[1],posicionAdq[2]);
		//Cadena de transpaso 2
		String cadenaP = DaTrabajo.substring(posicionAdq[1],posicionAdq[2]);
		
		/*---Realizar transpaso 1*/
		String AT2 = AceptaTrabajo.substring(0,posicionAdq[1])+cadenaP+AceptaTrabajo.substring(posicionAdq[2], AceptaTrabajo.length());
		/*---Realizar transpaso 2*/
		String DT2 = DaTrabajo.substring(0,posicionAdq[1])+cadena+DaTrabajo.substring(posicionAdq[2], DaTrabajo.length());
		
		
		turnos.set(posicionAdq[0],AT2);
		turnos.set(posTurno, DT2);
		return turnos;
	}
	private static ArrayList<String> realizarTranspaso(int[] posicionTras, ArrayList<String> turnos, int posTurno, String turno, int posChar, String ant) {
		String AceptaTrabajo = turnos.get(posicionTras[0]);
		String DaTrabajo = turno;
		String cadena = AceptaTrabajo.substring(posicionTras[1],posicionTras[2]);
		//Cadena de transpaso 2
		String cadenaP = DaTrabajo.substring(posicionTras[1],posicionTras[2]);
		
		/*---Realizar transpaso 1*/
		String AT2 = AceptaTrabajo.substring(0,posicionTras[1])+cadenaP+AceptaTrabajo.substring(posicionTras[2], AceptaTrabajo.length());
		/*---Realizar transpaso 2*/
		String DT2 = DaTrabajo.substring(0,posicionTras[1])+cadena+DaTrabajo.substring(posicionTras[2], DaTrabajo.length());
		
		
		turnos.set(posicionTras[0],AT2);
		turnos.set(posTurno, DT2);
		return turnos;
	}
	private static int[] busquedaAdquisicion(Entrada entrada, Parametros parametros, ArrayList<String> turnos,
			int posTurno, String turno, int finCad, String ant, Patrones patrones) {
		
		int[] re = {-1,-1,-1};
		int iniCad = finCad;
		ArrayList<String> dosTurnos = new ArrayList<String>();
		ArrayList<String> dosTurnosViejos = new ArrayList<String>();
		Turno t = entrada.getTurno();
		while(iniCad>=0 && turno.substring(iniCad,iniCad+3).equals(ant)){
			iniCad-=3;
		}iniCad+=3;
		
		String cadenaSlots = turno.substring(iniCad, finCad+3);
		
		int minT = parametros.getTiempoTrabMin()/parametros.getTamanoSlots();
		int slotsNecesarios = (minT - cadenaSlots.length()/3)*3;
		for(int i=0;i<turnos.size();i++){
			int num1 =-1;
			int num2 =-1;
			boolean prev = false;
			/*Comprobar:
			 * 	Trabajo Max 24 (no)
			 * 	Trabajo Min 6 del donante
			 * 	Descanso Min 6 propio
			 * 	1 Turno propio
			 * 	Maximo 96 slots propio
			 * 	48 o 72 slots trabajo no consecutivo propio
			 * 	*/
			String turno2 =  turnos.get(i);
			int slots = t.getTl()[1];
			if(iniCad>0 && turno2.substring(iniCad-3, iniCad).equals(ant)){ //Busqueda previa
				int[] tmp = {i,iniCad-slotsNecesarios,iniCad};
				ArrayList<String> cambio = comprobarAdquisicion(tmp, turnos, posTurno, turno, finCad, ant);
				dosTurnos.add(cambio.get(i));
				dosTurnos.add(cambio.get(posTurno));
				dosTurnosViejos.add(turnos.get(i));
				dosTurnosViejos.add(turnos.get(posTurno));
				num1 = comprobarRestriccionesArregloSoluciones(dosTurnosViejos, patrones, parametros);
				num2 = comprobarRestriccionesArregloSoluciones(dosTurnos, patrones, parametros);
				dosTurnos = new ArrayList<String>();
				prev = true;
			}else if(finCad<(slots-1)*3 && turno2.substring(finCad+3, finCad+6).equals(ant)) {//Busqueda posterior
				int[] tmp = {i,finCad,finCad+slotsNecesarios};
				ArrayList<String> cambio = comprobarAdquisicion(tmp, turnos, posTurno, turno, finCad, ant);
				dosTurnos.add(cambio.get(i));
				dosTurnos.add(cambio.get(posTurno));
				dosTurnosViejos.add(turnos.get(i));
				dosTurnosViejos.add(turnos.get(posTurno));
				num1 = comprobarRestriccionesArregloSoluciones(dosTurnosViejos, patrones, parametros);
				num2 = comprobarRestriccionesArregloSoluciones(dosTurnos, patrones, parametros);
				dosTurnos = new ArrayList<String>();
				prev = false;
			}
			if(num1>=num2 && num1!=-1){
				if( prev ){
					re[0]=i;re[1]=iniCad-slotsNecesarios;re[2]=iniCad+3;//Posicion turno + Posicion inicio + Posicion fin
				}else{
					re[0]=i;re[1]=finCad;re[2]=finCad+slotsNecesarios+3;//Posicion turno + Posicion inicio + Posicion fin
				}
				return re;
			}
		}
		return re;
	}
	
	private static int[] busquedaTraspaso(Entrada entrada, Parametros parametros, ArrayList<String> turnos,
			int posTurno, String turno, int finCad, String ant, Patrones patrones) {

		int[] re = {-1,-1,-1};
		int iniCad = finCad;
		ArrayList<String> dosTurnos = new ArrayList<String>();
		ArrayList<String> dosTurnosViejos = new ArrayList<String>();
		
		Turno t = entrada.getTurno();
		while(iniCad>=0 && ant.equals(turno.substring(iniCad, iniCad+3))){
			iniCad-=3;
		}iniCad+=3;
	
		for(int i=0;i<turnos.size();i++){
			if(i!=posTurno){
				int num1 =-1, num2=-1;
				boolean prev = false;

				String turno2 =  turnos.get(i);
				int slots = t.getTl()[1];
				if(iniCad>0 && turno2.substring(iniCad-3, iniCad).equals(ant)){ //Busqueda previa
					int[] tmp = {i,iniCad,finCad+3};
					ArrayList<String> cambio = comprobarTranspaso(tmp, turnos, posTurno, turno, finCad, ant);
					dosTurnos.add(cambio.get(i));
					dosTurnos.add(cambio.get(posTurno));
					dosTurnosViejos.add(turnos.get(i));
					dosTurnosViejos.add(turnos.get(posTurno));
					num1 = comprobarRestriccionesArregloSoluciones(dosTurnosViejos, patrones, parametros);
					num2 = comprobarRestriccionesArregloSoluciones(dosTurnos, patrones, parametros);
					dosTurnos = new ArrayList<String>();
					prev = true;
				}else if(finCad/3<(slots-1) && turno2.substring(finCad+3, finCad+6).equals(ant)){//Busqueda posterior
					int[] tmp = {i,iniCad,finCad+3};
					ArrayList<String> cambio = comprobarTranspaso(tmp, turnos, posTurno, turno, finCad, ant);
					dosTurnos.add(cambio.get(i));
					dosTurnos.add(cambio.get(posTurno));
					dosTurnosViejos.add(turnos.get(i));
					dosTurnosViejos.add(turnos.get(posTurno));
					num1 = comprobarRestriccionesArregloSoluciones(dosTurnosViejos, patrones, parametros);
					num2 = comprobarRestriccionesArregloSoluciones(dosTurnos, patrones, parametros);
					dosTurnos = new ArrayList<String>();
					prev = false;
				}
				if(num1>=num2 && num1 != -1){
					if( prev ){
						re[0]=i;re[1]=iniCad;re[2]=finCad+3;//Posicion turno + Posicion inicio + Posicion fin
					}else{
						re[0]=i;re[1]=iniCad;re[2]=finCad+3;//Posicion turno + Posicion inicio + Posicion fin
					}
					return re;
				}
			}
		}
		
		return re;
	}
	
	private static ArrayList<String> comprobarTranspaso(int[] posicionTras, ArrayList<String> cadenasDeTurnos, int posTurno, String turno, int posChar, String ant) {
		String AceptaTrabajo = cadenasDeTurnos.get(posicionTras[0]);
		String DaTrabajo = turno;
		//Cadena de transpaso 1
		String cadena = AceptaTrabajo.substring(posicionTras[1],posicionTras[2]);
		//Cadena de transpaso 2
		String cadenaP = DaTrabajo.substring(posicionTras[1],posicionTras[2]);
		
		/*---Realizar transpaso 1*/
		String AT2 = AceptaTrabajo.substring(0,posicionTras[1])+cadenaP+AceptaTrabajo.substring(posicionTras[2], AceptaTrabajo.length());
		/*---Realizar transpaso 2*/
		String DT2 = DaTrabajo.substring(0,posicionTras[1])+cadena+DaTrabajo.substring(posicionTras[2], DaTrabajo.length());
		
		@SuppressWarnings("unchecked")
		ArrayList<String> tmp = (ArrayList<String>) cadenasDeTurnos.clone();
		tmp.set(posicionTras[0],AT2);
		tmp.set(posTurno, DT2);
		return tmp;
	}
	private static ArrayList<String> comprobarAdquisicion(int[] posicionAdq,ArrayList<String> turnos, int posTurno, String turno, int posChar, String ant) {
		String AceptaTrabajo = turnos.get(posicionAdq[0]);
		String DaTrabajo = turno;
		//Cadena de transpaso 1
		String cadena = AceptaTrabajo.substring(posicionAdq[1]-3,posicionAdq[2]);
		//Cadena de transpaso 2
		String cadenaP = DaTrabajo.substring(posicionAdq[1]-3,posicionAdq[2]);
		/*---Realizar transpaso 1*/
		/*---Realizar transpaso 1*/
		String AT2 = AceptaTrabajo.substring(0,posicionAdq[1]-3)+cadenaP+AceptaTrabajo.substring(posicionAdq[2], AceptaTrabajo.length());
		/*---Realizar transpaso 2*/
		String DT2 = DaTrabajo.substring(0,posicionAdq[1]-3)+cadena+DaTrabajo.substring(posicionAdq[2], DaTrabajo.length());
		
		@SuppressWarnings("unchecked")
		ArrayList<String> tmp = (ArrayList<String>) turnos.clone();
		tmp.set(posicionAdq[0],AT2);
		tmp.set(posTurno, DT2);
		return tmp;
	}
	
	public static int comprobarRestriccionesArregloSoluciones(ArrayList<String> turnos, Patrones patrones,  Parametros parametros){
		int p=0;
		
		p += Restricciones.comprobarTrabajoMaximoConsecutivo(turnos, parametros);
		p += Restricciones.comprobarVentanaTrabajoDescanso(turnos, parametros);
		p += Restricciones.comprobarDescansoMinimoConsecutivo(turnos, parametros);
		p += Restricciones.comprobarTrabajoMinimoConsecutivo(turnos, parametros);
		return p;
	}
	
}