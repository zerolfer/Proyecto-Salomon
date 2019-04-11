package herramientas;

import estructurasDatos.DominioDelProblema.Sector;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CridaUtils {
    public static final String STRING_DESCANSO = "111";
    public static final int LONGITUD_CADENAS = 3; // Es decir, el tamaño de cada unidad (aaa, 111, 000,...)

    public static boolean containsIgnoreCase(Collection<String> collection, String s){
        for (String element : collection ) {
            if (element.equalsIgnoreCase(s))
                return true;
        }
        return false;
    }

    public static boolean containsSectorByIdIngoreCase(List<Sector> sectoresAbiertos, String sct) {
        boolean esta = false;
        for (int l = 0; l < sectoresAbiertos.size(); l++) {
            if (sectoresAbiertos.get(l).getId().equalsIgnoreCase(sct)) {
                esta = true;
                break;
            }
        }
        return esta;
    }

    public static boolean containsSectorById(List<Sector> sectoresAbiertos, String sct) {
        boolean esta = false;
        for (int l = 0; l < sectoresAbiertos.size(); l++) {
            if (sectoresAbiertos.get(l).getId().equals(sct)) {
                esta = true;
                break;
            }
        }
        return esta;
    }


    public static Sector findSectorById(List<Sector> listaSectores, String id){
        for (Sector sector : listaSectores) {

            if(CridaUtils.class.desiredAssertionStatus()) {
                for (char c : sector.getId().toCharArray()) {
                    assert Character.isLowerCase(c);
                }
            }

            if (sector.getId().equals/*IgnoreCase*/(id))
                return sector;
        }
        return null;
    }
}
