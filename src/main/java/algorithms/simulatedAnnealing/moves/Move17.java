package algorithms.simulatedAnnealing.moves;

import static herramientas.CridaUtils.STRING_NO_TURNO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import estructurasDatos.Movimiento;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import herramientas.random.SplitRnd;
import patrones.Patrones;
import patrones.Restricciones;

public class Move17 {

	/*Traza de movimiento*/
	/* Las posiciones son:
	 * 0 - C�digo del movimiento
	 * 1 - Posici�n inicial del intervalo que se mueve finalmente
	 * 2 - Posici�n final del intervalo que se mueve finalmente
	 * 3 - Controlador 1 elegido para el movimiento
	 * 4 - Controlador 2 elegido para el movimiento
	 * 5 - N�mero de movimientos que son descartados
	 * 6 - gMin
	 * 7 - gMax
	 */ 
	private static Object [] traza = {"M17", null, null, null, null, null, null, null};
	private static String format_traza = "{};{};{};{};{};{};{};{}";
	
	private static ArrayList<Integer> grid = null;

	// Par�metros para controlar las refinaciones
	public static int iteracion = 0;
	public static boolean puede_refinarse = true;

	public static Solucion movimiento(Solucion sol, int gMax, int gMin, Patrones patrones, Entrada entrada,
			Parametros par, ParametrosAlgoritmo par_alg) {
		
		iteracion = iteracion + 1;
		if (iteracion == par_alg.SA.getCicloRefinarGrid()) {
			iteracion = 0;
			refine_grid(par, par_alg, entrada);
		}
		
		gMin = par_alg.SA.getMove15_min();
		gMax = par_alg.SA.getMove15_max();
		
		traza[5] = 0;
		traza[6] = gMin;
		traza[7] = gMax;

		Solucion sol2 = sol.clone();
		ArrayList<String> asignacion = sol.getTurnos();

		int[] exc_ctrl1 = {}; // Lista de controladores 1 seleccionados, para no repetir elecci�n
		mainloop:
		while (exc_ctrl1.length < asignacion.size()) {
			// Seleccionamos una asignaci�n al azar, no puede repetirse
			int ctrl1 = SplitRnd.nextInt(asignacion.size() - 1, exc_ctrl1);
			exc_ctrl1 = append(exc_ctrl1, ctrl1);
			
			int[] exc_n_slots = {};
			while (exc_n_slots.length < gMax - gMin + 1) {
				// Seleccionamos un intervalo al azar:
				// Primero seleccionamos la cantidad de intervalos que elegiremos
				int n_slots = SplitRnd.nextInt(gMin, gMax, exc_n_slots);
				exc_n_slots = append(exc_n_slots, n_slots);
				// Despu�s seleccionamos un intervalo al azar
				int slot = SplitRnd.nextInt(grid.size() - 1 - n_slots);
				int[] intervalo = {grid.get(slot), grid.get(slot + n_slots)};

				// Seleccionamos el segundo controlador
				int[] exc_ctrl2 = exc_ctrl1.clone();
				while (exc_ctrl2.length < asignacion.size()) {
					traza[5] = (int) traza[5] + 1;

					int ctrl2 = SplitRnd.nextInt(asignacion.size() - 1, exc_ctrl2);
					exc_ctrl2 = append(exc_ctrl2, ctrl2);

					Movimiento mov = new Movimiento(ctrl1, ctrl2, intervalo[0], intervalo[1]);
					if (intervalo[0]<entrada.getSlotMomentoActual()) {
						System.out.println("Error: "+ ctrl1 +", "+ ctrl2 +", "+ intervalo[0]+", "+ intervalo[1]);
					}
					// comprueba que sea factible (devuelve 2 string cambiados o null)
					ArrayList<String> dosInd = ChangeCont(sol, mov, patrones, entrada, par, par_alg);
					if (dosInd != null) {
						sol2 = DoChange1(dosInd, sol, mov, par_alg);
						break mainloop;
					}
				}
			}
			
		}
		
		return sol2;

	}
	
	public static ArrayList<Integer> init_grid(Solucion sol, Parametros par, ParametrosAlgoritmo par_alg, Entrada entrada) {
		
		ArrayList<Integer> aux = new ArrayList<Integer>();
		aux.add(entrada.getSlotMomentoActual()*3);
		aux.add(sol.getTurnos().get(0).length());
		
		for (String s : sol.getTurnos()) {
			for (int i = (entrada.getSlotMomentoActual()*3)+3; i < s.length() - 1; i += 3) {
				if (!s.substring(i - 3, i).equals(s.substring(i, i + 3))) {
					if (!aux.contains(i)) {
						aux.add(i);
					}
				}
			}
		}
		
		Collections.sort(aux);

		grid = aux;
		if (par_alg.SA.isMove17_adapt_max())
			adapt_gMax(par, par_alg);
		
		return grid;
	}

	public static ArrayList<Integer> refine_grid(Parametros par, ParametrosAlgoritmo par_alg, Entrada entrada) {
		
		if (!puede_refinarse)
			return grid;
		
		// Calculamos los puntos que hay que excluir
		Set<Integer> exclusions_set = new HashSet<Integer>();
		
		int ini = grid.get(0) / 3;
		int fin = grid.get(grid.size() - 1) / 3;
		for (int i : grid) {
			int point = i / 3;
			
			if (point - 2 > ini)
				exclusions_set.add(point - 2);
			if (point - 1 > ini)
				exclusions_set.add(point - 1);
			
			exclusions_set.add(point);
			
			if (point + 1 < fin)
				exclusions_set.add(point + 1);
			if (point + 2 < fin)
				exclusions_set.add(point + 2);
		}

		// Comprobamos que quedan puntos que se puedan escoger
		if (exclusions_set.size() >= fin + 1 -ini) {
			// Ya no se puede refinar m�s
			puede_refinarse = false;
			return grid;
		}
		
		// Transformamos Set -> int[]
		int [] exclusions = new int[exclusions_set.size()];
		Iterator<Integer> it = exclusions_set.iterator();
		int i = 0;
		while (it.hasNext())
			exclusions[i++] = it.next().intValue();
		
		// Seleccionamos un nuevo punto
		int new_point = SplitRnd.nextInt(ini,fin, exclusions); //TODO: entrada.getSlotMomentoActual() pero bien
		
		
		ArrayList<Integer> aux = grid;
		aux.add(new_point * 3);
		Collections.sort(aux);

		grid = aux;
		if (par_alg.SA.isMove17_adapt_max())
			adapt_gMax(par, par_alg);
		
		return grid;
	}
	
	public static void reset_grid() {
		grid = null;
	}

	/**
	 * Ajustamos el par�metro move15_max para que sea coherente con gMax de acuerdo
	 * al tama�o de la grid. Para ello calculamos la media de longitudes de
	 * intervalos de la grid y lo usamos como base
	 */
	public static void adapt_gMax(Parametros par, ParametrosAlgoritmo par_alg) {
		// Granularidad m�xima en slots
		int gMax = (par_alg.SA.getTamañoMaxMov() / par.getTamanoSlots()) * 3;
		// Media del tama�o de cada secci�n de la grid (en slots)
		double media = grid.get(grid.size() - 1) / (grid.size() - 1.);
		par_alg.SA.setMove15_max(Math.max((int)Math.round(gMax / media), par_alg.SA.getMove15_min()));
	}

	/** Crea un nuevo array de tama�o arr.length + 1 y con el valor v en la �ltima posici�n. El resto de posiciones tendr� el mismo valor que arr.
	 * @param arr Array al que se quiere a�adir el valor v. Si es null se crear� un nuevo array s�lo con el valor v
	 * @param v Valor que se quiere a�adir
	 * @return Un nuevo array con los mismos valores que arr y v a�adido en la �ltima posici�n.
	 */
	public static int[] append(int[] arr, int v) {
		int[] res;
		if (arr != null) {
			res = Arrays.copyOf(arr, arr.length + 1);
			res[arr.length] = v;
		} else {
			res = new int[] { v };
		}

		return res;
	}
	
	/** Crea un nuevo array de tama�o arr1.length + arr2.length y con los valores de arr1 y arr2.
	 * Si alguno de los argumentos es null se devuelve una copia del otro. Si los dos son null, se devuelve un array vac�o.
	 * @param arr1 Primer array
	 * @param arr2 Segundo array
	 * @return Un nuevo array con los valores de arr1 y arr2 concatenados y en este orden
	 */
	public static int[] append(int[] arr1, int[] arr2) {
		
		int l1 = arr1 == null ? 0 : arr1.length;
		int l2 = arr2 == null ? 0 : arr2.length;
		
		if (l1 + l2 == 0) return new int[] { };

		int[] res = new int[l1 + l2];
		
		if (arr1 == null) {
			for (int i = 0; i < arr2.length; i++) {
				res[i] = arr2[i];
			}
		} 
		if (arr2 == null) {
			for (int i = 0; i < arr1.length; i++) {
				res[i] = arr1[i];
			}
		}
		if (arr1 != null && arr2 != null) {
			for (int i = 0; i < arr1.length; i++) {
				res[i] = arr1[i];
			}
			for (int i = 0; i < arr2.length; i++) {
				res[l1 + i] = arr2[i];
			}
		}
		
		return res;
		
	}

	/** Comprueba que se pueda hacer un transpaso de trabajo entre dos controladores,
	 * si es posible devuelve los dos individuos con el cambio ya hecho, si no devuelve null
	 */
	private static ArrayList<String> ChangeCont(Solucion ind, Movimiento mov, Patrones patrones, Entrada entrada,
			Parametros parametros, ParametrosAlgoritmo parametrosAlg) {

		ArrayList<String> individuo = ind.getTurnos();
		String controladorVago = individuo.get(mov.getDador());
		String controladorP = individuo.get(mov.getReceptor());
		String cadena = controladorVago.substring(mov.getInicio(), mov.getFin());
		String cadenaP = controladorP.substring(mov.getInicio(), mov.getFin());
		String controladorVago2 = controladorVago.substring(0, mov.getInicio()) + cadenaP + controladorVago.substring(mov.getFin(), controladorVago.length());
		String controladorP2 = controladorP.substring(0, mov.getInicio()) + cadena + controladorP.substring(mov.getFin(), controladorP.length());
		if(cadena.contains(STRING_NO_TURNO)||cadenaP.contains(STRING_NO_TURNO)) {
        	return null;
        } 
		boolean t = false;// comprobarLibre(individuo, mov, cadena);
		boolean m = comprobarNucleos(cadena, controladorP, patrones);
		boolean m1 = comprobarNucleos(controladorVago, cadenaP, patrones);
		if (t || !m || !m1) {
			return null;
		}

		ArrayList<String> dosInd = new ArrayList<String>();
		dosInd.add(controladorVago2);
		dosInd.add(controladorP2);

		return dosInd;
	}

	/** Realiza el transpaso de trabajo entre dos controladores,
	 * y comprueba que el controlador que entrega la carga de trabajo, tenga algo mas (no sea todo ceros), si no se elimina
	 */
	private static Solucion DoChange1(ArrayList<String> dosInd, Solucion individuo, Movimiento mov,
			ParametrosAlgoritmo pa) {
		@SuppressWarnings("unchecked")
		ArrayList<String> turnos = (ArrayList<String>) individuo.getTurnos().clone();
		turnos.set(mov.getDador(), dosInd.get(0));
		turnos.set(mov.getReceptor(), dosInd.get(1));
		if (individuo.getControladores().size() != individuo.getTurnos().size()) {
			boolean descanso = true;
			for (int i = 0; i < dosInd.get(0).length(); i++) {
				if (dosInd.get(0).charAt(i) != '1') {
					descanso = false;
				}
			}
			ArrayList<Controlador> cs = new ArrayList<Controlador>();
			for (Controlador c : individuo.getControladores()) {
				cs.add((Controlador) c.clone());
			}
			if (descanso) {
				for (int i = 0; i < cs.size(); i++) {
					if (cs.get(i).getTurnoAsignado() == mov.getDador()) {
						cs = asignarControlador(i, mov.getDador(), cs);
						break;
					}
				}
				turnos.remove(mov.getDador());
				Solucion individuo2 = individuo.clone();
				individuo2.setControladores(cs);
				individuo2.setTurnos(turnos);
				return individuo2;
			}
		}
		Solucion individuo2 = individuo.clone();
		individuo2.setTurnos(turnos);
		return individuo2;
	}

	private static boolean comprobarNucleos(String cad, String conP, Patrones patrones) {
		ArrayList<String> nuc = Patrones.nuc;
		String[] cadenas = { cad, conP };
		for (int i = 0; i < cadenas.length; i++) {
			boolean correcto = false;
			String cadena = cadenas[i];
			for (int j = 0; j < nuc.size(); j++) {
				String posibles[] = patrones.getArray()[5 + j].toString().split(";");
				boolean bn = true;
				for (int l = 0; l < cadena.length(); l += 3) {
					if (!cadena.substring(l, l + 3).equalsIgnoreCase("111")) {
						boolean sectorPos = false;
						for (int k = 0; k < posibles.length; k++) {
							if (cadena.substring(l, l + 3).equalsIgnoreCase(posibles[k])) {
								sectorPos = true;
								break;
							}
						}
						if (!sectorPos) {
							bn = false;
							break;
						}
					}
				}
				if (bn) {
					correcto = true;
					break;
				}
			}
			if (!correcto) {
				return false;
			}
		}
		return true;
	}

	private static ArrayList<Controlador> asignarControlador(int posC, int dador, ArrayList<Controlador> cs) {
		for (int i = 0; i < cs.size(); i++) {
			if (cs.get(i).getTurnoAsignado() > dador) {
				cs.get(i).setTurnoAsignado(cs.get(i).getTurnoAsignado() - 1);
			}
		}
		ArrayList<Integer> aux = new ArrayList<Integer>();
		for (int i = 0; i < cs.size(); i++) {
			aux.add(cs.get(i).getTurnoAsignado());
		}
		for (int j = 0; j < cs.size(); j++) {
			boolean esta = false;
			for (int i = 0; i < aux.size(); i++) {
				if (aux.get(i) == j) {
					esta = true;
				}
			}
			if (!esta) {
				cs.get(posC).setTurnoAsignado(j);
				break;
			}
		}

		return cs;
	}
	
}
