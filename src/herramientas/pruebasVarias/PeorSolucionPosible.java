package herramientas.pruebasVarias;

import java.util.ArrayList;

import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.Parametros;
import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Entrada;
import patrones.Patrones;

public class PeorSolucionPosible {
	
	public static void testes() {
		for (int i = 0; i < 32; i++) {
			System.out.print("111"+"aaz"+"AAQ");
		}System.out.println();for (int i = 0; i < 32; i++) {
			System.out.print("aaq"+"111"+"AAZ");
		}System.out.println();for (int i = 0; i < 32; i++) {
			System.out.print("111"+"AAZ"+"aaq");
		}System.out.println();for (int i = 0; i < 32; i++) {
			System.out.print("AAQ"+"111"+"aaz");
		}System.out.println();for (int i = 0; i < 32; i++) {
			System.out.print("AAZ"+"aaq"+"111");
		}System.out.println();for (int i = 0; i < 32; i++) {
			System.out.print("aaz"+"AAQ"+"111");
		}
		
		ArrayList<String> turnos = new ArrayList<String>();
		turnos.add("111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ");
		turnos.add("AAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaak");
		turnos.add("aaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAK");
		turnos.add("AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111");
		turnos.add("aajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaa");
		turnos.add("AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111");
		turnos.add("aakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aaj");
		turnos.add("111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA");
		turnos.add("aaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZ");
		turnos.add("111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq");
		turnos.add("AAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aaz");
		turnos.add("AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111");
		turnos.add("aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111");
		turnos.add("111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ");
		
	}
	
	public static ArrayList<Solucion> CrearPeorSolucionCaso3(Entrada entrada, Parametros p, Patrones patrones) {
		ArrayList<String> turnos = new ArrayList<String>();
		turnos.add("111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ111aaaAAAAAJ");
		turnos.add("AAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaakAAAAAJaaaaak");
		turnos.add("aaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAKaaaAAAAAJAAK");
		turnos.add("AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111AAJaajAAK111");
		turnos.add("aajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaaaajaak111aaa");
		turnos.add("AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111AAK111aak111");
		turnos.add("aakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aajaakAAK111aaj");
		turnos.add("111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA111111aajAAA");
		turnos.add("aaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZ");
		turnos.add("111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq");
		turnos.add("AAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aaz");
		turnos.add("AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111AAZaaq111");
		turnos.add("aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111");
		turnos.add("111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ111aazAAQ");
		
		Solucion ind = InicializarPoblacion.asignacionControladores(turnos, entrada,0);
		ArrayList<Solucion> poblacion = new ArrayList<Solucion>();
		poblacion.add(ind);
		return poblacion;
	}
}