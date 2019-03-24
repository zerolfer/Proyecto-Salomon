package estructurasDatos.DominioDelProblema;

import java.util.ArrayList;
/**
 * Objeto Nucleo, el cual contiene toda la informacion de los Nucleos en los que se divide un aeropuerto.
 * @author Tino
 *
 */
public class Nucleo {

	/**
	 * Nombre del nucleo.
	 */
	private String nombre;
	/**
	 * ID del nucleo.
	 */
	private String id;
	/**
	 * Lista de sectores que forman el nucleo.
	 */
	private ArrayList<Sector> sectores =  new ArrayList<Sector>();
	/**
	 * Constructor
	 * @param nombre Nombre del nucleo.
	 * @param id ID del nucleo.
	 * @param sectores Lista de sectores que forman el nucleo.
	 */
	public Nucleo(String nombre, String id, ArrayList<Sector> sectores) {
		this.nombre = nombre;
		this.id = id;
		this.sectores = sectores;
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
	public ArrayList<Sector> getSectores() {
		return sectores;
	}
	public void setSectores(ArrayList<Sector> sectores) {
		this.sectores = sectores;
	}
	
	
}
