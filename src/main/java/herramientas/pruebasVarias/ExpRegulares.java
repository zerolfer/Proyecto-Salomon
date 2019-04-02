package herramientas.pruebasVarias;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpRegulares {

    public static void main(String[] args) {
        pruebaconRegex();
    }

    public static void pruebaconRegex() {
        String cad = "111aawaawaawaaw111111111111111111111111111111111111111111111111111111111111AAYaazaazaazaazabeabeabeabeABAABAABAABA111111abcabcabcabc111111111111111111111111ABAABAABAABAaayaayaayaayaayaayaayaayabbabbabbabbABBABBABBABBaawaawaawaaw";
        int tMax = (24 * 3) + 1;
        int tMin = (3 * 3) - 1;
        int dMin = (3 * 3) - 1;

        Pattern p = Pattern.compile("[a-zA-Z]{" + tMax + ",}"); //tiempo trabajo maximo
        Pattern p1 = Pattern.compile("(1|^)[a-zA-Z]{1," + tMin + "}(1|$)"); // tiempo trabajo minimo
        Pattern p2 = Pattern.compile("[a-zA-Z]{1,}(1){1," + dMin + "}[a-zA-Z]{1,}"); // tiempo descanso minimo
        Pattern p3 = Pattern.compile("(1|^)([a-z]){1," + tMin + "}(1|$)"); //tiempo trabajo minimo pos1
        Pattern p4 = Pattern.compile("(1|^)(([A-Z][A-Z][A-Z])){1," + (3 - 1) + "}(1|$)"); //tiempo trabajo minimo pos2
        //p3 y p4 not working.


        Matcher mat = p4.matcher(cad);
        if (mat.find()) {
            System.out.println("Se cumple");
        }
    }
}
