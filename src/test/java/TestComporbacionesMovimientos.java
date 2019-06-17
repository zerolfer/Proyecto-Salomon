import algorithms.variableNeighborhoodSearch.impl.moves.Move0;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Propiedades;
import estructurasDatos.Solucion;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestComporbacionesMovimientos {

    @Test
    static void testComprobarTrabajoMinimo() {
//        assertFalse(MetaheuristicUtil.comprobarTrabajoMinimo("aaabbbccc111111111aaa111aaa111"));
//        assertTrue(MetaheuristicUtil.comprobarTrabajoMinimo("aaabbbccc111111111AAABBBCCC111"));
    }

    @Test
    static void testMovimiento1() {
        Move0 mov = new Move0(null, null, null, null);

        ArrayList<String> turnos = new ArrayList<>();
        turnos.add("111111111aaaaaaaaaaaa111111111aaaaaaaaa");
        turnos.add("111111111111aaaaaa111aaa111aaaaaaaaaaaa");


        ArrayList<Controlador> controladores = new ArrayList<>();
        controladores.add(new Controlador(0, "largo", "N", true, false, false, Propiedades.ALTA, -1));
        controladores.get(controladores.size() - 1).setTurnoAsignado(0);
        controladores.add(new Controlador(1, "largo", "N", true, false, false, Propiedades.ALTA, -1));
        controladores.get(controladores.size() - 1).setTurnoAsignado(1);

        Solucion x = mov.firstImprovement(new Solucion(turnos, controladores, 3));

        assertEquals("111111111aaaaaaaaa111111111111aaaaaaaaa", x.getTurnos().get(0));
        assertEquals("111111111111aaaaaaaaaaaa111aaaaaaaaaaaa", x.getTurnos().get(1));

    }

    public static void main(String[] args) {
        testMovimiento1();
    }
}
