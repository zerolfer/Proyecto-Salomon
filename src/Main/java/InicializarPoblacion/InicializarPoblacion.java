package InicializarPoblacion;
import java.util.ArrayList;

import main.MainPruebas;
import estructurasDatos.*;
import estructurasDatos.Auxiliares.ObjAux1;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Nucleo;
import estructurasDatos.DominioDelProblema.Sector;
import patrones.*;
/**
 * Clase utilizada para la inicializacion de un conjunto de soluciones iniciales.
 * @author Tino
 *
 */
public class InicializarPoblacion {

	/**
	 * Metodo de inicializacion principal.
	 * @param entrada Entrada del problema.
	 * @param p Parametros del problema.
	 * @param patrones Patrones para la comprobacion de restricciones.
	 * @return Conjunto de soluciones iniciales (No necesariamente factibles).
	 */
	public static ArrayList<Solucion> inicializarPoblacion(Entrada entrada, Parametros p, Patrones patrones) {
		ArrayList<Solucion> poblacion = new ArrayList<Solucion>();

		int maxD = (p.getTiempoTrabMax()/p.getTamanoSlots())/2;
		int minD = p.getTiempoDesMin()/p.getTamanoSlots();
		int maxT = p.getTiempoTrabMax()/p.getTamanoSlots();
		int minT = p.getTiempoTrabMin()/p.getTamanoSlots();

		for(int descanso=minD; descanso<=maxD; descanso++){
			Solucion individuo = inicializarIndividuo(descanso,maxT,minT,minD,entrada,p, patrones);
			poblacion = comprobarCondicionesEntorno(individuo,poblacion, entrada, patrones,p);			
		}
		System.out.println("La poblacion inicial es de: " + poblacion.size()+" individuos");
		MainPruebas.problema +=("La poblacion inicial es de: " + poblacion.size()+" individuos"+"\n");
		return poblacion;
	}
	/**
	 * Metodo para devolver solo las soluciones factibles (Se deben descomentar las partes comentadas, en caso contrario el metodo devuelve todas las soluciones).
	 * @param individuo Solucion.
	 * @param poblacion Conjunto de soluciones.
	 * @param entrada Entrada del problema.
	 * @param patrones Patrones para la comprobacion de restricciones.
	 * @param parametros Paramtros del problema.
	 * @return Poblacion con el nuevo individuo. 
	 */
	private static ArrayList<Solucion> comprobarCondicionesEntorno(Solucion individuo,ArrayList<Solucion> poblacion, Entrada entrada, Patrones patrones, Parametros parametros) {
//		/*En el caso de querer introducir solo soluciones factibles descomentar el codigo*/
//		if(Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros)==0){
//			System.out.println(); 
		poblacion.add(individuo);
//		}
		return poblacion;
	}
	/**
	 * Metodo para la creacion de una solucion inicial con descansos de un tamaño determinado.
	 * @param descanso Tamaño de los descansos de la plantilla usada para la creacion de soluciones.
	 * @param maxT Tiempo de trabajo maximo.
	 * @param minT Tiempo de trabajo minimo.
	 * @param minD Tiempo de descanso minimo.
	 * @param entrada Entrada del problema.
	 * @param p Parametros del problema.
	 * @param patrones Patrones para la comprobacion de las restricciones.
	 * @return Solucion creada mediante plantillas 3x1.
	 */
	private static Solucion inicializarIndividuo(int descanso, int maxT, int minT, int minD, Entrada entrada, Parametros p, Patrones patrones) {
		//FASE 1
		ArrayList<ArrayList<String>> cadenasDeTurnos = introduccionPlantillas(entrada, descanso, maxT, minT, minD);		
		//FASE 2
		cadenasDeTurnos = reparacionSoluciones(entrada, p, cadenasDeTurnos,minT, patrones);
		//FASE 3
		ArrayList<String> turnos = transformacionSoluciones(cadenasDeTurnos);
		Solucion individuo = asignacionControladores(turnos,entrada, descanso);
		//FASE 4
		return individuo;
	}
	/**
	 * Metodo utilizado para modificar el formato de la matriz de turnos (Parse).
	 * @param cadenasDeTurnos Lista con los turnos de trabajo de los controladores.
	 * @return Lista con los turnos de trabajo de los controladores (con diferente formato).
	 */
	public static ArrayList<String> transformacionSoluciones(ArrayList<ArrayList<String>> cadenasDeTurnos) {
		ArrayList<String> turnos = new ArrayList<String>();
		for(int i=0;i<cadenasDeTurnos.size();i++){
			ArrayList<String>cad = cadenasDeTurnos.get(i); 
			String c = "";
			for(int j=0;j<cad.size();j++){
				c = c + cad.get(j);
			}
			turnos.add(c);
		}
		for(int i=0;i<turnos.size();i++){
			String t = turnos.get(i).replaceAll("1","");
			if(t.equalsIgnoreCase("")){
				turnos.remove(i);
				i--;
			}
		}
		return turnos;
	}
	/**
	 * Metodo para realizar una asignacion optima de los controladores aereos dispnibles a los distintos turnos.
	 * @param turnos Turnos de trabajo.
	 * @param entrada Entrada del problema.
	 * @param descanso Longitud de los descansos de los turnos en cuestion.
	 * @return Solucion con los controladores disponibles asignados a los turnos. 
	 */
	public static Solucion asignacionControladores(ArrayList<String> turnos, Entrada entrada, int descanso) {
		ArrayList<String> controladores = new ArrayList<String>();
		ArrayList<Controlador> c = new ArrayList<Controlador>();
		for(int i=0;i<entrada.getControladores().size();i++){
			Controlador cntl = entrada.getControladores().get(i);
			c.add(new Controlador(cntl.getId(),cntl.getTurno(),cntl.getNucleo(),cntl.isPTD(),cntl.isCON(),cntl.isImaginario()));
		}
		/*INICIAR LISTA CONTORLADORES*/
		ArrayList<ObjAux1> clasificacionTurnos = new ArrayList<ObjAux1>();
		for(int i=0;i<turnos.size();i++){
			boolean turnoLargo = comprobarTurno(turnos.get(i), entrada);
			boolean ptd = comprobarSector(turnos.get(i), entrada);
			ArrayList<String> nucleo = comprobarNucleo(turnos.get(i), entrada);
			if(nucleo.size()==1){
				clasificacionTurnos.add(0,new ObjAux1(turnoLargo, ptd, nucleo, i));
			}else{
				clasificacionTurnos.add(new ObjAux1(turnoLargo, ptd, nucleo, i));
			}
		}
		for(int i=0;i<clasificacionTurnos.size();i++){//asignarControlador TL Y PTD
			ObjAux1 o = clasificacionTurnos.get(i);
			boolean encontrado= false;
			if(o.isTurnoLargo()&& o.isPtd()){
				for(int j=0;j<c.size();j++){
					if(c.get(j).getTurno().equalsIgnoreCase("TL") && c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1 && !encontrado){
						for(int k=0;k<o.getNucleo().size();k++){
							if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
								c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
								controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
								clasificacionTurnos.remove(i);i--;
								encontrado = true;
							}
						}
					}
				}
			}
		}
		for(int i=0;i<clasificacionTurnos.size();i++){//asignarControlador TC Y PTD
			ObjAux1 o = clasificacionTurnos.get(i);
			boolean encontrado= false;
			if(!o.isTurnoLargo() && o.isPtd()){
				for(int j=0;j<c.size();j++){
					if(c.get(j).getTurno().equalsIgnoreCase("TC") && c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1 && !encontrado){
						for(int k=0;k<o.getNucleo().size();k++){
							if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
								c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
								controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
								clasificacionTurnos.remove(i);i--;
								encontrado = true;
							}
						}
					}
				}
				if(!encontrado){
					for(int j=0;j<c.size();j++){
						if(c.get(j).getTurno().equalsIgnoreCase("TL") && c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1 && !encontrado){
							for(int k=0;k<o.getNucleo().size();k++){
								if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
									c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
									controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
									clasificacionTurnos.remove(i);i--;
									encontrado = true;
								}
							}
						}
					}
				}
			}
		}
		for(int i=0;i<clasificacionTurnos.size();i++){//asignarControlador TL Y RUTA
			ObjAux1 o = clasificacionTurnos.get(i);
			boolean encontrado= false;
			if(o.isTurnoLargo() && !o.isPtd()){
				for(int j=0;j<c.size();j++){
					if(c.get(j).getTurno().equalsIgnoreCase("TL") && !c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1  && !encontrado){
						for(int k=0;k<o.getNucleo().size();k++){
							if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
								c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
								controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
								clasificacionTurnos.remove(i);i--;
								encontrado = true;
							}
						}
					}
				}
				if(!encontrado){
					for(int j=0;j<c.size();j++){
						if(c.get(j).getTurno().equalsIgnoreCase("TL") && c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1  && !encontrado){
							for(int k=0;k<o.getNucleo().size();k++){
								if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
									c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
									controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
									clasificacionTurnos.remove(i);i--;
									encontrado = true;
								}
							}
						}
					}
				}
			}
		}
		for(int i=0;i<clasificacionTurnos.size();i++){//asignarControlador TC Y RUTA
			ObjAux1 o = clasificacionTurnos.get(i);
			boolean encontrado= false;
			if(!o.isTurnoLargo() && !o.isPtd()){
				for(int j=0;j<c.size();j++){
					if(c.get(j).getTurno().equalsIgnoreCase("TC") && !c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1 && !encontrado){
						for(int k=0;k<o.getNucleo().size();k++){
							if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
								c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
								controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
								clasificacionTurnos.remove(i);i--;
								encontrado = true;
							}
						}
					}
				}
				if(!encontrado){
					for(int j=0;j<c.size();j++){
						if(c.get(j).getTurno().equalsIgnoreCase("TL") && !c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1 && !encontrado){
							for(int k=0;k<o.getNucleo().size();k++){
								if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
									c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
									controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
									clasificacionTurnos.remove(i);i--;
									encontrado = true;
								}
							}
						}
					}
				}
				if(!encontrado){
					for(int j=0;j<c.size();j++){
						if(c.get(j).getTurno().equalsIgnoreCase("TC") && c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1 && !encontrado){
							for(int k=0;k<o.getNucleo().size();k++){
								if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
									c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
									controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
									clasificacionTurnos.remove(i);i--;
									encontrado = true;
								}
							}
						}
					}
				}
				if(!encontrado){
					for(int j=0;j<c.size();j++){
						if(c.get(j).getTurno().equalsIgnoreCase("TL") && c.get(j).isPTD() && c.get(j).getTurnoAsignado()==-1 && !encontrado){
							for(int k=0;k<o.getNucleo().size();k++){
								if(c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado){
									c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
									controladores.add("Turno: "+clasificacionTurnos.get(i).getPosicion()+" Controlador: "+c.get(j).getId());
									clasificacionTurnos.remove(i);i--;
									encontrado = true;
								}
							}
						}
					}
				}
			}
		}
		/*FIN LISTA CONTROLADORES*/
		@SuppressWarnings("unchecked")
		ArrayList<Controlador> controladores1 = (ArrayList<Controlador>) c.clone();
		Solucion ind = new Solucion(turnos, controladores1, descanso);
		return ind;
	}
	/**
	 * Metodo utilizado para devolver el nucleo o lista de nucleos al que pertenecen los sectores de un turno (Unicamente es valido para turnos formados por un unico sector).
	 * @param turno Turnos de trabajo
	 * @param entrada Entrada del problema.
	 * @return Lista de nucleos 
	 */
	private static ArrayList<String> comprobarNucleo(String turno, Entrada entrada) {
		/*
		 * Solo funciona con soluciones iniciales: Solo funciona con turnos que contengan un sector*/
		ArrayList<String> nucleo = new ArrayList<String>();
		ArrayList<Nucleo> nucleos = entrada.getNucleos();
		for(int i=0;i<turno.length();i+=3){
			if(!turno.substring(i, i+3).equalsIgnoreCase("111")){
				for(int j=0;j<nucleos.size();j++){	
					ArrayList<Sector> sctNucleo = nucleos.get(j).getSectores();
					for(int k=0;k<sctNucleo.size();k++){	
						if(turno.substring(i, i+3).equalsIgnoreCase(sctNucleo.get(k).getId())){
							String nuc = nucleos.get(j).getNombre();
							boolean dentro = false;
							for(int l=0;l<nucleo.size();l++){
								if(nuc.equalsIgnoreCase(nucleo.get(l))){
									dentro = true;
								}
							}
							if(!dentro){
								nucleo.add(nuc);
							}
						}			
					}
				}
				return nucleo;
			}
		}
		return null;
	}
	/**
	 * Metodo utilizado para comprobar si los sectores de un turno son PDT (Unicamente es valido para turnos formados por un unico sector).
	 * @param turno Turnos de trabajo
	 * @param entrada Entrada del problema.
	 * @return True: En caso de que los sectores del turno sean PDT. False en caso que sean CON.
	 */
	private static boolean comprobarSector(String turno, Entrada entrada) {
		ArrayList<Sector> listaSec = entrada.getListaSectores();
		for(int i=0;i<turno.length();i+=3){
			for(int j=0;j<listaSec.size();j++){	
				if(turno.substring(i, i+3).equalsIgnoreCase(listaSec.get(j).getId())){
					if(listaSec.get(j).isPDT()){
						return true;
					}
				}
			}			
		}
		return false;
	}
	/**
	 * Comprobar si el turno de trabajo es corto o es largo.
	 * @param turno Turnos de trabajo
	 * @param entrada Entrada del problema.
	 * @return True: En caso de que el turno sea Corto. False en caso que el turno sea Largo.
	 */
	private static boolean comprobarTurno(String turno, Entrada entrada) {
		int nSlot=0;
		int[] tc = entrada.getTurno().getTc();
		for(int i=0; i<turno.length(); i +=3){
			String slot = turno.substring(i, i+3);	
			if(!slot.equalsIgnoreCase("111") &&  (tc[0] > nSlot || tc[1] < nSlot)){
				return true;
			}
			
			nSlot++;
		}
		return false;
	}
	/**
	 * Variable auxiliar que indica si la reparacion de ciertas restricciones incumplidas por las soluciones es posible.
	 */
	public static boolean posible = true;
	/**
	 * Metodo utilizado para la reparacion de soluciones, entendiendo esto, como la modificacion de las mismas para reducir el numero de restricciones incumplidas.
	 * @param entrada Entrada del problema
	 * @param p Parametros del problema.
	 * @param cadenasDeTurnos Lista de los turnos de trabajo.
	 * @param minT Tiempo de trabajo minimo.
	 * @param patrones Patrones para comprobacion de restricciones.
	 * @return Lista de los turnos de trabajo con las modificaciones pertenentes reducir la infactibilidad.
	 */
	public static ArrayList<ArrayList<String>> reparacionSoluciones(Entrada entrada, Parametros p, ArrayList<ArrayList<String>> cadenasDeTurnos, int minT, Patrones patrones) {
		for(int i=0;i<cadenasDeTurnos.size();i++){
				ArrayList<String> turno = cadenasDeTurnos.get(i);
				int cont = 0;
				for(int l=0;l<turno.size();l++){
					if(!turno.get(l).equals("111")){
						String ant = turno.get(l);
						if(l+1<turno.size() && ant.equals(turno.get(l+1))){
							cont++;
						}else{
							if(cont<minT-1){ //No se cumple el trabajo minimo
							cadenasDeTurnos = ArreglarSoluciones.arregloTrabajoMinimo(entrada, p, cadenasDeTurnos,i,turno,l,ant, patrones);
								if(posible==false){
									posible=true;
									return cadenasDeTurnos;
								} //No es posible su arreglo
								turno = cadenasDeTurnos.get(i);
								l=-1;cont=0;//Reseteamos l=-1 (al iniciar el bucle de nuevo l++ =0) para volver a comprobar el turno despues de los cambios
								 
							}
							cont=0;
						}
					}
				}
			}
		return cadenasDeTurnos;
	}
	/**
	 * Metodo para la generacion de soluciones con plantillas de 3x1.
	 * @param entrada Entrada del problema.
	 * @param descanso Tamaño del descanso para la plantilla.
	 * @param maxT Tiempo de trabajo maximo.
	 * @param minT Tiempo de trabajo minimo.
	 * @param minD Tiempo de descanso minimo.
	 * @return Lista de turnos de trabajo.
	 */
	private static ArrayList<ArrayList<String>> introduccionPlantillas(Entrada entrada, int descanso, int maxT, int minT, int minD) {
		ArrayList<ArrayList<String>> turnos = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> sectorizacion = entrada.getSectorizacion();
		ArrayList<Sector> sectores = entrada.getListaSectoresAbiertos();
		ArrayList<Integer> secNoc = new ArrayList<Integer>();
		for(int i=0;i<sectores.size();i++){
			ArrayList<ArrayList<String>> plantilla = new ArrayList<ArrayList<String>>();// agrupacion de 3 controladores (usando plantilla 3x1)
			ArrayList<String> c1 = new ArrayList<String>();
			ArrayList<String> c2 = new ArrayList<String>();
			ArrayList<String> c3 = new ArrayList<String>();
			plantilla.add(c1);plantilla.add(c2);plantilla.add(c3);
			if(sectores.get(i).getNoche()==0){
				for(int j=0;j<sectorizacion.size();j++){
					ArrayList<String> sectoresAbiertos = sectorizacion.get(j);
					boolean open = false;
					for(int k=0;k<sectoresAbiertos.size();k++){
						if(sectores.get(i).getId().equalsIgnoreCase(sectoresAbiertos.get(k))){
							open = true;
							plantilla = introducirSector(plantilla,descanso,sectores.get(i).getId());
						}
					}
					if(!open){
						plantilla = introducirDescanso(plantilla);
					}
				}
			}else{
				boolean yaIntroducido = false;
				for (int j = 0; j < secNoc.size(); j++) {
					if(secNoc.get(j)==sectores.get(i).getNoche()){
						yaIntroducido = true;
					}
				}
				if (!yaIntroducido) {
					secNoc.add(sectores.get(i).getNoche());
					plantilla = introducirSectorNoche(plantilla,sectores,sectores.get(i).getNoche(),sectorizacion);					
				}
			}
			turnos = introducirEnTurnos(plantilla,turnos);
		}
		return turnos;
	}
	/**
	 * Introduccion de las plantillas utilizadas para los sectores de noche.
	 * @param plantilla Lista de turnos de trabajo.
	 * @param sectores Lista de sectores.
	 * @param noche Indicador de los sectores marcados como turno de noche.
	 * @param sectorizacion Sectorizacion del problema.
	 * @return Lista de turnos de trabajo, con los turnos para cubrir los sectores nocturos incluidos, si estos existen.
	 */
	private static ArrayList<ArrayList<String>> introducirSectorNoche(ArrayList<ArrayList<String>> plantilla,ArrayList<Sector> sectores, int noche, ArrayList<ArrayList<String>> sectorizacion) {
		ArrayList<String> c4 = new ArrayList<String>();
		plantilla.add(c4);
		/*Lista de sectores noche = int noche*/
		ArrayList<Sector> sectoresNocturnos = new ArrayList<Sector>();
		for (int j = 0; j < sectores.size(); j++) {
			if (sectores.get(j).getNoche()==noche) {
				sectoresNocturnos.add(sectores.get(j));
			}
		}
		for(int j=0;j<sectorizacion.size();j++){
			ArrayList<String> sectoresAbiertos = sectorizacion.get(j);
			for(int k=0;k<sectoresAbiertos.size();k++){
				for(int i=0;i<sectoresNocturnos.size();i++){
					if(sectoresNocturnos.get(i).getId().equalsIgnoreCase(sectoresAbiertos.get(k))){
						plantilla = introducirSlotSectorNocturno(plantilla,sectoresNocturnos.get(i).getId());
					}
				}
			}
		}
		
		return plantilla;
	}
	/**
	 * Metodo para la introduccion de los sectores nocturnos especificos.
	 * @param plantilla Plantilla de trabajo para sectores nocturnos.
	 * @param id ID del sector nocturno.
	 * @return Devuelve la plantilla compuesta por 4 turnos.
	 */
	private static ArrayList<ArrayList<String>> introducirSlotSectorNocturno(ArrayList<ArrayList<String>> plantilla, String id) {
		int desc = 9; //Por ejemplo, este descanso/trabajo siempre es el mismo al ser turno de noche "el tiempo en posicion optimo son 45mins"
		ArrayList<String> c1= plantilla.get(plantilla.size()-4);
		ArrayList<String> c2= plantilla.get(plantilla.size()-3);
		ArrayList<String> c3= plantilla.get(plantilla.size()-2);
		ArrayList<String> c4= plantilla.get(plantilla.size()-1);
		int j = c1.size();
		while(j >= (desc*4)){
			j = j - desc*4;
		}
		if(j<desc){
			c1.add(id.toLowerCase());
			c2.add(id.toUpperCase());
			c3.add("111");
			c4.add("111");
		}else if(j<desc*2){
			c1.add("111");
			c2.add("111");
			c3.add(id.toUpperCase());
			c4.add(id.toLowerCase());
		}else if(j<desc*3){
			c1.add(id.toUpperCase());
			c2.add(id.toLowerCase());
			c3.add("111");
			c4.add("111");
		}else if(j<desc*4){
			c1.add("111");
			c2.add("111");
			c3.add(id.toLowerCase());
			c4.add(id.toUpperCase());
		}
		plantilla.set(plantilla.size()-4, c1);
		plantilla.set(plantilla.size()-3, c2);
		plantilla.set(plantilla.size()-2, c3);
		plantilla.set(plantilla.size()-1, c4);
		return plantilla;
	}
	/**
	 * Metodo utilizado para la introduccion de plantillas 3x1 asociadas a un sector al conjunto de turnos de trabajo.
	 * @param plantilla Plantilla 3x1.
	 * @param turnos Conjunto de turnos de trabajo en construccion.
	 * @return Conjunto de turnos de trabajo en construccion con la nueva plantilla incorporada
	 */
	private static ArrayList<ArrayList<String>> introducirEnTurnos(ArrayList<ArrayList<String>> plantilla,	ArrayList<ArrayList<String>> turnos) {
		if(plantilla.get(0).size()!=0){
			for(int i=0;i<plantilla.size();i++){
				turnos.add(plantilla.get(i));
			}
		}
		return turnos;
	}
	/**
	 * Metodo utilizado para rellenar las plantillas incompletas con descansos.
	 * @param plantilla Plantilla 3x1.
	 * @return Plantilla con un slot de descanso mas en los 3 turnos.
	 */
	private static ArrayList<ArrayList<String>> introducirDescanso(ArrayList<ArrayList<String>> plantilla) {
		ArrayList<String> c1= plantilla.get(plantilla.size()-3);
		ArrayList<String> c2= plantilla.get(plantilla.size()-2);
		ArrayList<String> c3= plantilla.get(plantilla.size()-1);
		c1.add("111");c2.add("111");c3.add("111");
		plantilla.set(plantilla.size()-3, c1);
		plantilla.set(plantilla.size()-2, c2);
		plantilla.set(plantilla.size()-1, c3);
		return plantilla;
	}
	/**
	 * Metodo utilizado para la introduccion en las plantillas de trabajo el ID del sector que se cubrira con la plantilla.
	 * @param plantilla Plantilla 3x1.
	 * @param desc Longitud del periodo de descanso.
	 * @param id ID del sector
	 * @return Plantilla completa.
	 */
	private static ArrayList<ArrayList<String>> introducirSector(ArrayList<ArrayList<String>> plantilla, int desc, String id) {
		ArrayList<String> c1= plantilla.get(plantilla.size()-3);
		ArrayList<String> c2= plantilla.get(plantilla.size()-2);
		ArrayList<String> c3= plantilla.get(plantilla.size()-1);
		int j = c1.size();
		while(j >= (desc*3)){
			j = j - desc*3;
		}
		if(j<desc){
			c1.add(id.toLowerCase());
			c2.add(id.toUpperCase());
			c3.add("111");
		}else if(j<desc*2){
			c1.add(id.toUpperCase());
			c2.add("111");
			c3.add(id.toLowerCase());
		}else if(j<desc*3){
			c1.add("111");
			c2.add(id.toLowerCase());
			c3.add(id.toUpperCase());
		}
		plantilla.set(plantilla.size()-3, c1);
		plantilla.set(plantilla.size()-2, c2);
		plantilla.set(plantilla.size()-1, c3);
		return plantilla;
	}
	public static ArrayList<String> reparacionSoluciones2(Entrada entrada, Parametros parametros, ArrayList<String> turnos, int minT, Patrones patrones) {
		String turno="";
		ArrayList<String> turno2 = new ArrayList<String>();
		ArrayList<ArrayList<String>> turnos2 = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < turnos.size(); i++) {
			turno = turnos.get(i);
			for (int j = 0; j < turno.length(); j+=3) {
				turno2.add(turno.substring(j, j+3));
			}
			turnos2.add(turno2);
			turno2 = new ArrayList<String>();
		}
		ArrayList<ArrayList<String>> cadenasDeTurnos = reparacionSoluciones(entrada, parametros, turnos2, minT, patrones);
		return transformacionSoluciones(cadenasDeTurnos);
	}
	
}
