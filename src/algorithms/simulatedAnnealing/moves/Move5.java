package algorithms.simulatedAnnealing.moves;

import java.util.ArrayList;
import java.util.Random;

import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Controlador;


public class Move5 {

	public static Solucion movimientoControladores(Solucion individuo) {
		Solucion ind2 = (Solucion) individuo.clone();
		ArrayList<Controlador> c = ind2.getControladores();
		Random random = new Random();
		int r1 = random.nextInt(c.size());
		int r2 = random.nextInt(c.size());
		int t1 = c.get(r1).getTurnoAsignado();
		int t2 = c.get(r2).getTurnoAsignado();
		c.get(r1).setTurnoAsignado(t2);
		c.get(r2).setTurnoAsignado(t1);
		ind2.setControladores(c);		
		return ind2;
	}

}
