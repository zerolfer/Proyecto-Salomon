package herramientas.random;

import java.util.SplittableRandom;

public class SplitRnd {

	private static SplittableRandom rnd = null;
	
	private SplitRnd() {
		// Esta clase no se puede instanciar
	}
	
	public static void init(long seed) {
		rnd = new SplittableRandom(seed);
	}
	
	public static void init() {
		rnd = new SplittableRandom();
	}
	
	public static int nextInt() {
		return rnd.nextInt();
	}

	/** Devuelve un entero aleatorio en el intervalo [ini, fin] */
	public static int nextInt(int ini, int fin) {
		return ini + nextInt(fin - ini);
	}

	/** Devuelve un entero aleatorio en el intervalo [0, fin] */
	public static int nextInt(int fin) {
		return rnd.nextInt(fin + 1);
	}
	
	/** Devuelve un double aleatorio en el intervalo [0, 1) */
	public static double nextDouble() {
		return rnd.nextDouble();
	}

	/** Devuelve un double aleatorio en el intervalo [0, fin) */
	public static double nextDouble(double fin) {
		return fin * nextDouble();
	}

	/** Devuelve un double aleatorio en el intervalo [ini, fin) */
	public static double nextDouble(double ini, double fin) {
		return ini + nextDouble(fin - ini);
	}

	/** Devuelve un entero aleatorio en el intervalo [ini, fin] que no est�
	 * presente en exclusions. Exclusions no puede tener valores repetidos.
	 * Si exclusions est� ordenado de forma ascendente funciona m�s r�pido.
	 */
	public static int nextInt(int ini, int fin, int[] exclusions) {
		
		if (exclusions == null) return nextInt(ini, fin);
		int exclusions_in_range = 0;
		for (int i = 0; i < exclusions.length; i++) {
			if (ini <= exclusions[i] && exclusions[i] <= fin) {
				exclusions_in_range++;
			}
		}
		
		int c = nextInt(fin - exclusions_in_range - ini);
		
		int res = ini - 1;
		do {
			res++;
			boolean otra_vuelta = false;
			do {
				otra_vuelta = false;
				for (int i = 0; i < exclusions.length; i++) {
					if (res == exclusions[i]) {
						res++;
						otra_vuelta = true;
					}
				}
			} while(otra_vuelta);
			c--;
		} while (c >= 0);

		return res;
	}

	/** Devuelve un entero aleatorio en el intervalo [0, fin] que no est�
	 * presente en exclusions.
	 */
	public static int nextInt(int fin, int[] exclusions) {
			return nextInt(0, fin, exclusions);
	}
	
	/** Muestrea la distribuci�n de pesos.
	 * @param pesos Peso de cada una de las opciones
	 * @return Devuelve uno de los �ndices. Si sum(pesos) == 0 devuelve un �ndice al azar.
	 */
	public static int sample(double[] pesos) {
		
		double suma = 0;
		int i;
		
		for (i = 0; i < pesos.length; suma += pesos[i++]);
		
		if (suma == 0) return nextInt(pesos.length - 1);
		
		double rnd = nextDouble(suma);
		double ini = 0;
		double fin = pesos[0];
		for (i = 0; i < pesos.length - 1; i++) {
			if (ini <= rnd && rnd < fin) break;
			ini = fin;
			fin += pesos[i + 1];
		}
		
		return i;
		
	}

	/** Muestrea la distribuci�n de pesos.
	 * @param pesos Peso de cada una de las opciones
	 * @param exclusions �ncides exclu�dos del array de pesos. No pueden haber valores repetidos. Puede ser null
	 * @return Devuelve uno de los �ndices. Si sum(pesos) == 0 devuelve un �ndice al azar.
	 */
	public static int sample(double[] pesos, int[] exclusions) {

		if (exclusions == null) return sample(pesos);
		
		int exclusions_in_range = 0;
		for (int i = 0; i < exclusions.length; i++) {
			if (0 <= exclusions[i] && exclusions[i] < pesos.length) {
				exclusions_in_range++;
			}
		}
		
		if (exclusions_in_range == 0) return sample(pesos);
		
		int[] idxs = new int[pesos.length - exclusions_in_range];
		double[] exc_pesos = new double[pesos.length - exclusions_in_range];
		
		int k = 0;
		for (int i = 0; i < pesos.length; i++) {
			boolean excluir = false;
			for (int j = 0; j < exclusions.length; j++) {
				if (i == exclusions[j]) {
					excluir = true;
					break;
				}
			}
			if (!excluir) {
				idxs[k] = i;
				exc_pesos[k] = pesos[i];
				k++;
			}
		}
		
		int res = sample(exc_pesos);
		return idxs[res];
		
	}
	
	/** Devuelve un intervalo [a, b], conteniendo como m�ximo dif enteros, dentro del intervalo [ini, end].
	 * Siempre se cumplir� que a = ini � b = end. Todos los intervalos tienen igual probabilidad de ser devueltos.
	 * @param max_dif es la cantidad de enteros que habr� como m�ximo en el intervalo devuelto
	 */
	public static int[] get_extreme_interval(int ini, int end, int max_dif) {
		return get_extreme_interval(ini, end, 1, max_dif);
	}
	
	/** Devuelve un intervalo [a, b], conteniendo como m�ximo dif enteros, dentro del intervalo [ini, end].
	 * Todos los intervalos tienen igual probabilidad de ser devueltos.
	 * Siempre se cumplir� que a = ini � b = end y adem�s que <em>min_dif <= b - a + 1 <= max_dif</em>.
	 * @param ini extremo inferior del intervalo
	 * @param end extremo superior del intervalo
	 * @param min_dif la m�nima cantidad de enteros que ser� devuelto
	 * @param max_dif es la cantidad de enteros que habr� como m�ximo en el intervalo devuelto
	 */
	public static int[] get_extreme_interval(int ini, int end, int min_dif, int max_dif) {
		int[] res = {ini, end};
		double rnd = nextDouble();
		double correccion = 0;
		
		if (max_dif >= end - ini + 1) {
			max_dif = end - ini + 1;
			correccion = 0.5; // Esto sirve para ajustar el caso en que se puede seleccionar el intervalo completo
		}
		if (min_dif == 0) min_dif = 1;
		if(max_dif < min_dif) min_dif = max_dif;
		
		// Si rnd < 0.5 ser� un intervalo conteniendo el extremo inicial, si no, el extremo final
		if (rnd < 0.5) {
			res[1] = ini + min_dif - 1 + (int) Math.floor(rnd * 2 * (max_dif - correccion - min_dif + 1));
		} else {
			res[0] = end - min_dif + 1 - (int) Math.floor((1 - rnd) * 2 * (max_dif - correccion - min_dif + 1));
		}
		
		// TODO Esto es muy ineficiente. Tendr�a que hacerlo 
		if (res[1] - res[0] + 1 < min_dif) {
			return get_extreme_interval(ini, end, min_dif, max_dif);
		}
		return res;
	}
	
	/** Tests unitarios de los m�todos de la clase SplitRnd. Las diferencias entre valores
	 * obtenidos y esperados deben hacerse de forma manual
	 * @param args
	 */
	public static void main(String [] args) {
		/* Utilizamos el main para ejecutar tests unitarios */
		SplitRnd.init();
		int n_tests = 1000000;
		
		if (args.length < 1000) {
			for (int i = 0; i < 10; i++) {
			int[] resre = get_extreme_interval(1, 7, 3, 4);
			System.out.format("[%d, %d]%n", resre[0], resre[1]);
			}
//			return;
		}
		
		// TEST nextInt(int)
		int error = 0;
		int i;
		int rnd;
		double drnd;
		
		for (i = 0; i < n_tests; i++) {
			rnd = nextInt(10);
			if (rnd < 0 || rnd > 10) {
				error++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: nextInt(int): %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextInt(int)");
		}
		
		// TEST nextInt(int, int)
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = nextInt(1, 10);
			if (rnd < 1 || rnd > 10) {
				error++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: nextInt(int, int): %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextInt(int, int)");
		}
		
		// TEST nextDouble()
		error = 0;
		for (i = 0; i < n_tests; i++) {
			drnd = nextDouble();
			if (drnd < 0  || drnd >= 1) {
				error++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: nextDouble(): %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextDouble()");
		}
		
		// TEST nextDouble(double)
		error = 0;
		for (i = 0; i < n_tests; i++) {
			drnd = nextDouble(5.5);
			if (drnd < 0  || drnd >= 5.5) {
				error++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: nextDouble(double): %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextDouble(double)");
		}
		
		// TEST nextDouble(double, double)
		error = 0;
		for (i = 0; i < n_tests; i++) {
			drnd = nextDouble(3.4, 6.7);
			if (drnd < 3.4  || drnd >= 6.7) {
				error++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: nextDouble(double, double): %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextDouble(double, double)");
		}
		
		// TEST 1 nextInt(int, int, int[])
		int [] exclusions = {1,2,3};
		double [] res = {0,0,0,0,0,0};
		double [] esp = {1/6., 1/6., 1/6., 1/6., 1/6., 1/6.}; // esperado
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = nextInt(3, 8, null);
			if (rnd < 3 || rnd > 8) error++;
			else res[rnd - 3]++;
		}
		if (error > 0) {
			System.out.format("ERROR: nextInt(int, int, int[]) [1]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextInt(int, int, int[]) [1]. Distribuci�n resultante:");
			for (i = 0; i < res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 3, res[i] / n_tests, esp[i]);
			}
		}
		
		// TEST 2 nextInt(int, int, int[])
		exclusions[0] = 1; exclusions[1] = 2; exclusions[2] = 3;
		res[0] = 0; res[1] = 0; res[2] = 0; res[3] = 0; res[4] = 0; res[5] = 0;
		esp[0] = 0; esp[1] = 1/5.; esp[2] = 1/5.; esp[3] = 1/5.; esp[4] = 1/5.; esp[5] = 1/5.;
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = nextInt(3, 8, exclusions);
			if (esp[rnd - 3] == 0) error++;
			else res[rnd - 3]++;
		}
		if (error > 0) {
			System.out.format("ERROR: nextInt(int, int, int[]) [2]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextInt(int, int, int[]) [2]. Distribuci�n resultante:");
			for (i = 0; i < res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 3, res[i] / n_tests, esp[i]);
			}
		}
		
		// TEST 3 nextInt(int, int, int[])
		exclusions[0] = 1; exclusions[1] = 5; exclusions[2] = 3;
		res[0] = 0; res[1] = 0; res[2] = 0; res[3] = 0; res[4] = 0; res[5] = 0;
		esp[0] = 0; esp[1] = 1/4.; esp[2] = 0; esp[3] = 1/4.; esp[4] = 1/4.; esp[5] = 1/4.;
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = nextInt(3, 8, exclusions);
			if (esp[rnd - 3] == 0) error++;
			else res[rnd - 3]++;
		}
		if (error > 0) {
			System.out.format("ERROR: nextInt(int, int, int[]) [3]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextInt(int, int, int[]) [3]. Distribuci�n resultante:");
			for (i = 0; i < res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 3, res[i] / n_tests, esp[i]);
			}
		}
		
		// TEST 4 nextInt(int, int, int[])
		exclusions[0] = 6; exclusions[1] = 4; exclusions[2] = 3;
		res[0] = 0; res[1] = 0; res[2] = 0; res[3] = 0; res[4] = 0; res[5] = 0;
		esp[0] = 0; esp[1] = 0; esp[2] = 1/3.; esp[3] = 0; esp[4] = 1/3.; esp[5] = 1/3.;
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = nextInt(3, 8, exclusions);
			if (esp[rnd - 3] == 0) error++;
			else res[rnd - 3]++;
		}
		if (error > 0) {
			System.out.format("ERROR: nextInt(int, int, int[]) [4]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextInt(int, int, int[]) [4]. Distribuci�n resultante:");
			for (i = 0; i < res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 3, res[i] / n_tests, esp[i]);
			}
		}
		
		// TEST 5 nextInt(int, int, int[])
		exclusions[0] = 2; exclusions[1] = 4; exclusions[2] = 8;
		res[0] = 0; res[1] = 0; res[2] = 0; res[3] = 0; res[4] = 0; res[5] = 0;
		esp[0] = 1/4.; esp[1] = 0; esp[2] = 1/4.; esp[3] = 1/4.; esp[4] = 1/4.; esp[5] = 0;
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = nextInt(3, 8, exclusions);
			if (esp[rnd - 3] == 0) error++;
			else res[rnd - 3]++;
		}
		if (error > 0) {
			System.out.format("ERROR: nextInt(int, int, int[]) [5]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextInt(int, int, int[]) [5]. Distribuci�n resultante:");
			for (i = 0; i < res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 3, res[i]/n_tests, esp[i]);
			}
		}
		
		// TEST 6 nextInt(int, int, int[])
		exclusions[0] = 2; exclusions[1] = 4; exclusions[2] = 9;
		res[0] = 0; res[1] = 0; res[2] = 0; res[3] = 0; res[4] = 0; res[5] = 0;
		esp[0] = 1/5.; esp[1] = 0; esp[2] = 1/5.; esp[3] = 1/5.; esp[4] = 1/5.; esp[5] = 1/5.;
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = nextInt(3, 8, exclusions);
			if (esp[rnd - 3] == 0) error++;
			else res[rnd - 3]++;
		}
		if (error > 0) {
			System.out.format("ERROR: nextInt(int, int, int[]) [6]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: nextInt(int, int, int[]) [6]. Distribuci�n resultante:");
			for (i = 0; i < res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 3, res[i]/n_tests, esp[i]);
			}
		}
		
		// TEST 1 sample(double[])
		double[] dist = {0,0,1,0,0};
		double[] sample_res = {0,0,0,0,0};
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = sample(dist);
			if (rnd != 2) error++;
			else sample_res[rnd]++;
		}
		if (error > 0) {
			System.out.format("ERROR: sample(double[]) [1]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: sample(double[]) [1]. Distribuci�n resultante:");
			for (i = 0; i < dist.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, sample_res[i]/n_tests, dist[i]/1.);
			}
		}
		
		// TEST 2 sample(double[])
		dist[0] = 0;dist[1] = 0;dist[2] = 0;dist[3] = 0;dist[4] = 0;
		sample_res[0] = 0;sample_res[1] = 0;sample_res[2] = 0;sample_res[3] = 0;sample_res[4] = 0;
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = sample(dist);
			if (rnd < 0 || rnd > 4) error++;
			else sample_res[rnd]++;
		}
		if (error > 0) {
			System.out.format("ERROR: sample(double[]) [2]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: sample(double[]) [2]. Distribuci�n resultante:");
			for (i = 0; i < dist.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, sample_res[i]/n_tests, 1/5.);
			}
		}
		
		// TEST 3 sample(double[])
		dist[0] = 1;dist[1] = 0;dist[2] = 0;dist[3] = 0;dist[4] = 1;
		sample_res[0] = 0;sample_res[1] = 0;sample_res[2] = 0;sample_res[3] = 0;sample_res[4] = 0;
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = sample(dist);
			if (rnd != 0 && rnd != 4) error++;
			else sample_res[rnd]++;
		}
		if (error > 0) {
			System.out.format("ERROR: sample(double[]) [3]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: sample(double[]) [3]. Distribuci�n resultante:");
			for (i = 0; i < dist.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, sample_res[i]/n_tests, dist[i]/2.);
			}
		}
		
		// TEST 4 sample(double[])
		dist[0] = 1;dist[1] = 1;dist[2] = 4;dist[3] = 1;dist[4] = 1;
		sample_res[0] = 0;sample_res[1] = 0;sample_res[2] = 0;sample_res[3] = 0;sample_res[4] = 0;
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = sample(dist);
			if (rnd < 0 || rnd > 4) error++;
			else sample_res[rnd]++;
		}
		if (error > 0) {
			System.out.format("ERROR: sample(double[]) [4]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: sample(double[]) [4]. Distribuci�n resultante:");
			for (i = 0; i < dist.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, sample_res[i]/n_tests, dist[i]/8.);
			}
		}
		
		// TEST 1 sample(double[], int[])
		dist = new double[] {0,1,1,0,0};
		sample_res = new double[] {0,0,0,0,0};
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = sample(dist, null);
			if (rnd != 1 && rnd != 2) error++;
			else sample_res[rnd]++;
		}
		if (error > 0) {
			System.out.format("ERROR: sample(double[], int[]) [1]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: sample(double[], int[]) [1]. Distribuci�n resultante:");
			for (i = 0; i < dist.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, sample_res[i]/n_tests, dist[i]/2.);
			}
		}
		
		// TEST 2 sample(double[], int[])
		dist = new double[] {0,1,1,1,0};
		sample_res = new double[] {0,0,0,0,0};
		double[] sesp = {0,1/2.,1/2.,0,0};
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = sample(dist, new int[] {3});
			if (sesp[rnd]==0) error++;
			else sample_res[rnd]++;
		}
		if (error > 0) {
			System.out.format("ERROR: sample(double[], int[]) [2]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: sample(double[], int[]) [2]. Distribuci�n resultante:");
			for (i = 0; i < dist.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, sample_res[i]/n_tests, sesp[i]);
			}
		}
		
		// TEST 3 sample(double[], int[])
		dist = new double[] {0,1,1,1,0};
		sample_res = new double[] {0,0,0,0,0};
		sesp = new double[] {0,1/2.,1/2.,0,0};
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = sample(dist, new int[] {3, -1, 8, 9});
			if (sesp[rnd]==0) error++;
			else sample_res[rnd]++;
		}
		if (error > 0) {
			System.out.format("ERROR: sample(double[], int[]) [3]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: sample(double[], int[]) [3]. Distribuci�n resultante:");
			for (i = 0; i < dist.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, sample_res[i]/n_tests, sesp[i]);
			}
		}
		
		// TEST 4 sample(double[], int[])
		dist = new double[] {0,1,1,1,0};
		sample_res = new double[] {0,0,0,0,0};
		sesp = new double[] {0,1/2.,1/2.,0,0};
		error = 0;
		for (i = 0; i < n_tests; i++) {
			rnd = sample(dist, new int[] {3, -1, 8, 4});
			if (sesp[rnd]==0) error++;
			else sample_res[rnd]++;
		}
		if (error > 0) {
			System.out.format("ERROR: sample(double[], int[]) [4]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: sample(double[], int[]) [4]. Distribuci�n resultante:");
			for (i = 0; i < dist.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, sample_res[i]/n_tests, sesp[i]);
			}
		}
		
		// TEST 1 get_random_extreme(int, int, int)
		error = 0;
		for (i = 0; i < n_tests; i++) {
			int [] b = get_extreme_interval(1, 7, 4);
			if (b[1] - b[0] + 1 > 4 || !(b[0] == 1 || b[1] == 7)) error++;
		}
		if (error > 0) {
			System.out.format("ERROR: get_random_extreme(int, int, int) [1]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: get_random_extreme(int, int, int) [1]");
		}
		
		// TEST 2 get_random_extreme(int, int, int)
		error = 0;
		double [] gre_res = {0, 0};
		for (i = 0; i < n_tests; i++) {
			int [] b = get_extreme_interval(1, 2, 1);
			if (b[1] - b[0] + 1 > 1 || !(b[0] == 1 || b[1] == 2)) error++;
			else {
				if (b[0] == 1) gre_res[0]++;
				else gre_res[1]++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: get_random_extreme(int, int, int) [2]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: get_random_extreme(int, int, int) [2]. Distribuci�n resultante:");
			for (i = 0; i < gre_res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, gre_res[i]/n_tests, 1/2.);
			}
		}
		
		// TEST 3 get_random_extreme(int, int, int)
		error = 0;
		gre_res = new double[]{0, 0, 0};
		for (i = 0; i < n_tests; i++) {
			int [] b = get_extreme_interval(1, 2, 2);
			if (b[1] - b[0] + 1 > 2 || !(b[0] == 1 || b[1] == 2)) error++;
			else {
				if (b[0] == b[1]) {
					if (b[0] == 1) gre_res[0]++;
					else gre_res[1]++;
				} else gre_res[2]++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: get_random_extreme(int, int, int) [3]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: get_random_extreme(int, int, int) [3]. Distribuci�n resultante:");
			for (i = 0; i < gre_res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, gre_res[i]/n_tests, 1/3.);
			}
		}

		// TEST 1 get_random_extreme(int, int, int, int)
		error = 0;
		for (i = 0; i < n_tests; i++) {
			int [] b = get_extreme_interval(1, 7, 3, 4);
			if (!((b[0] == 1 && b[1] == 3) || (b[0] == 1 && b[1] == 4) || (b[0] == 4 && b[1] == 7) || (b[0] == 5 && b[1] == 7))) error++;
		}
		if (error > 0) {
			System.out.format("ERROR: get_random_extreme(int, int, int, int) [1]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: get_random_extreme(int, int, int, int) [1]");
		}
		
		// TEST 2 get_random_extreme(int, int, int, int)
		error = 0;
		gre_res = new double[]{0, 0, 0, 0};
		for (i = 0; i < n_tests; i++) {
			int [] b = get_extreme_interval(1, 7, 3, 4);
			if (!((b[0] == 1 && b[1] == 3) || (b[0] == 1 && b[1] == 4) || (b[0] == 4 && b[1] == 7) || (b[0] == 5 && b[1] == 7))) error++;
			else {
				if (b[0] == 1) {
					if (b[1] == 3) gre_res[0]++;
					else gre_res[1]++;
				} else {
					if (b[0] == 4) gre_res[2]++;
					else gre_res[3]++;
				}
			}
		}
		if (error > 0) {
			System.out.format("ERROR: get_random_extreme(int, int, int, int) [2]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: get_random_extreme(int, int, int, int) [2]. Distribuci�n resultante:");
			for (i = 0; i < gre_res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, gre_res[i]/n_tests, 1/4.);
			}
		}
		
		// TEST 3 get_random_extreme(int, int, int, int)
		error = 0;
		gre_res = new double[]{0, 0, 0};
		for (i = 0; i < n_tests; i++) {
			int [] b = get_extreme_interval(1, 2, 0, 2);
			if (b[1] - b[0] + 1 > 2 || !(b[0] == 1 || b[1] == 2)) error++;
			else {
				if (b[0] == b[1]) {
					if (b[0] == 1) gre_res[0]++;
					else gre_res[1]++;
				} else gre_res[2]++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: get_random_extreme(int, int, int, int) [3]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: get_random_extreme(int, int, int, int) [3]. Distribuci�n resultante:");
			for (i = 0; i < gre_res.length; i++) {
				System.out.format("  Elemento %d: Obtenido=%1.3f  Esperado=%1.3f%n", i + 1, gre_res[i]/n_tests, 1/3.);
			}
		}
		
		// TEST Generaci�n aleatoria id�ntica para enteros
		error = 0;
		int [] muestra = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		for (i = 0; i < n_tests; i++) {
			SplitRnd.init(i);
			for (int j = 0; j < muestra.length; j++) {
				muestra[j] = SplitRnd.nextInt();
			}
			SplitRnd.init(i);
			for (int j = 0; j < muestra.length; j++) {
				if (muestra[j] != SplitRnd.nextInt()) error++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: inicializaci�n con la misma semilla [int]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: inicializaci�n con la misma semilla [int]");
		}
		
		// TEST Generaci�n aleatoria id�ntica para double
		error = 0;
		double [] muestra2 = {0., 0., 0., 0., 0., 0., 0., 0., 0., 0.};
		
		for (i = 0; i < n_tests; i++) {
			SplitRnd.init(i);
			for (int j = 0; j < muestra.length; j++) {
				muestra2[j] = SplitRnd.nextDouble();
			}
			SplitRnd.init(i);
			for (int j = 0; j < muestra.length; j++) {
				if (muestra2[j] != SplitRnd.nextDouble()) error++;
			}
		}
		if (error > 0) {
			System.out.format("ERROR: inicializaci�n con la misma semilla [double]: %d errores.%n", error);
		} else {
			System.out.println("TEST OK: inicializaci�n con la misma semilla [double]");
		}
		
	}
	
}
