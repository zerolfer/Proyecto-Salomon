package estructurasDatos.DominioDelProblema;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Objeto Sector, el cual contiene toda la informacion relativa a los sectores del espacio aereo de un aeropuerto
 *
 * @author Tino
 */
public class Sector implements Cloneable {
    /**
     * Nombre del sector
     */
    private String nombre;
    /**
     * ID que se le otorga al sector para la resolucion del problema. Este ID simpre consta de 3 letras.
     */
    private String id;
    /**
     * Indica si el sector es de tipo Aproximacion (En caso que el valor sea True).
     */
    private boolean pDT;
    /**
     * Indica si el sector es de tipo Ruta (En caso que el valor sea True).
     */
    private boolean ruta;
    /**
     * Indica si el sector es un sector nocturno, y debe ser cubierto por 4 controladores.
     * Si toma el valor 1, ES nocturno, sino NO ES nocturno
     */
    private int noche;
    /**
     * Lista de los sectores elementales que componen el sector.
     */
    private ArrayList<String> sectoresElementales;

    /**
     * Constructor
     *
     * @param nombre              Nobre del sector
     * @param id                  ID que se le otorga al sector para la resolucion del problema. Este ID simpre consta de 3 letras.
     * @param pDT                 Indica si el sector es de tipo Aproximacion (En caso que el valor sea True).
     * @param ruta                Indica si el sector es de tipo Ruta (En caso que el valor sea True).
     * @param noche               Indica si el sector es un sector nocturno, y debe ser cubierto por 4 controladores.
     * @param sectoresElementales Lista de los sectores elementales que componen el sector.
     */
    public Sector(String nombre, String id, boolean pDT, boolean ruta, int noche, ArrayList<String> sectoresElementales) {
        this.nombre = nombre;
        this.id = id;
        this.pDT = pDT;
        this.ruta = ruta;
        this.noche = noche;
        this.sectoresElementales = sectoresElementales;

    }

    public Sector clone() {
        return new Sector(this.nombre, this.id, this.pDT, this.ruta, this.noche, this.sectoresElementales);
    }

    public ArrayList<String> getSectoresElementales() {
        return sectoresElementales;
    }

    public void setSectoresElementales(ArrayList<String> sectoresElementales) {
        this.sectoresElementales = sectoresElementales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPDT() {
        return pDT;
    }

    public void setPDT(boolean pDT) {
        this.pDT = pDT;
    }

    public boolean isRuta() {
        return ruta;
    }

    public void setRuta(boolean ruta) {
        this.ruta = ruta;
    }

    public int getNoche() {
        return noche;
    }

    public void setNoche(int noche) {
        this.noche = noche;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sector sector = (Sector) o;
        return id.equals(sector.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sector{" +
                "nombre='" + nombre + '\'' +
                ", id='" + id + '\'' +
                ", pDT=" + pDT +
                ", ruta=" + ruta +
                ", noche=" + noche +
                ", sectoresElementales=" + sectoresElementales +
                '}';
    }
}
