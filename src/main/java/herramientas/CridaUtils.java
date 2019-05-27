package herramientas;

import estructurasDatos.DominioDelProblema.Sector;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CridaUtils {
    public static final String STRING_DESCANSO = "111";
    public static final String STRING_NO_TURNO = "000";
    public static final int LONGITUD_CADENAS = 3; // Es decir, el tamaño de cada unidad (aaa, 111, 000,...)
    public static final String STRING_SEPARADOR_CSV = ";";

    public static boolean containsIgnoreCase(Collection<String> collection, String s) {
        for (String element : collection) {
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


    public static Sector findSectorById(Collection<Sector> listaSectores, String id) {
        for (Sector sector : listaSectores) {

            if (CridaUtils.class.desiredAssertionStatus()) {
                for (char c : sector.getId().toCharArray()) {
                    assert Character.isLowerCase(c);
                }
            }

            if (sector.getId().equals/*IgnoreCase*/(id))
                return sector;
        }
        throw new RuntimeException("ERROR: NO EXISTE NINGUN SECTOR CON IDENTIFICADOR '" + id + "'");
    }

    public static boolean esAfin(String sectorCerrado, String sectorAbierto, Map<String, Set<String>> mapaAfinidad) {
        return mapaAfinidad.get(sectorCerrado).contains(sectorAbierto);
    }

    public static Sector findSectorByName(String nombre, List<Sector> sectores) {
        for (Sector sector : sectores) {
            if (sector.getNombre().equalsIgnoreCase(nombre))
                return sector;
        }
        throw new RuntimeException("MATRIZ DE AFINIDAD INCORRECTA: NO EXISTE SECTOR DE NOMBRE '" + nombre + "'");
    }

}
