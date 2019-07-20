import algorithms.variableNeighborhoodSearch.impl.moves.Move0;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Propiedades;
import estructurasDatos.Solucion;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static algorithms.MetaheuristicUtil.esTrabajo;
import static herramientas.CridaUtils.LONGITUD_CADENAS;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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

        Solucion x = (Solucion) mov.firstImprovement(new Solucion(turnos, controladores, 3));

        assertEquals("111111111aaaaaaaaa111111111111aaaaaaaaa", x.getTurnos().get(0));
        assertEquals("111111111111aaaaaaaaaaaa111aaaaaaaaaaaa", x.getTurnos().get(1));

    }

    @Test
    static void testSplitIntervalos() {
        assertEquals(Arrays.asList(new int[]{60, 96}, new int[]{240, 192}, new int[]{216, 234}),
                getintervalos("ACYACYACYACYACYACYACYACYACYACYACYACYACYACYACYACYACYACYACYACYacyacyacyacyacyacyacyacyacyacyacyacy111111111111111111111111ADUADUADUADUADUADUADUADUADUADUADUADUaduaduaduaduaduaduaduaduadtadtadtadt111111111111111111111111ADCADCADCADCADCADCADC")
        );

    }

    public static void main(String[] args) {
        testSplitIntervalos();
    }


    private static List<int[]> getintervalos(String turno) {
        List<int[]> res = new ArrayList<>();

        // recorremos el turno
        int i = 20 * LONGITUD_CADENAS;

        while (i + LONGITUD_CADENAS <= turno.length() &&
                !esTrabajo(turno.substring(i, i + LONGITUD_CADENAS)))
            i += 3; // saltamos los descansos

        for (int f = i; f + LONGITUD_CADENAS <= turno.length(); f += LONGITUD_CADENAS) {
            if (!esTrabajo(turno.substring(f, f + LONGITUD_CADENAS))) {
                res.add(new int[]{i, f});
                i = f;

                while (i + LONGITUD_CADENAS <= turno.length() && !esTrabajo(turno.substring(i, i + LONGITUD_CADENAS)))
                    i += 3; // saltamos los descansos

                f = i;
            }
        }
        if (i < turno.length() - LONGITUD_CADENAS && esTrabajo(turno.substring(turno.length() - 3)))
            res.add(new int[]{i, turno.length()});

        return res;
    }

    public static List<String> split3In3(String string) {
        List<String> res = new ArrayList<>();//StringBuilder sb=new StringBuilder();
        for (int i = 0; i < string.length(); i += 3) {
            res.add(string.substring(i, i + LONGITUD_CADENAS));
        }
        return res;

    }


}
