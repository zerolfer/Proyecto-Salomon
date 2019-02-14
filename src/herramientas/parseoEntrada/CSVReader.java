package herramientas.parseoEntrada;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import estructurasDatos.Parametros;
import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Sector;
import patrones.Patrones;

public class CSVReader {
		/*DEAD CODE*/
	public static Solucion readSolution(Entrada entrada, Parametros parametros,
			Patrones patrones, String solucionFactible) {

        String csvFile = "zEntrada/"+solucionFactible;
        String line = "";
        
        int countLine = 0;
        ArrayList<String> turnos = new ArrayList<>();
        int countTurnos = 0;
        ArrayList<Integer> turnoAsignado = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
            	countLine++;
                String[] field = line.split(";");
                String previous = null;
                if(countLine>=5 & countLine<=17){
                	int c = 0;
                	String turn = "";
                	for(int i = 0;i<field.length;i++){
                		if(c>=2){
                			String str = field[c];
                			if(str.equals("")){
                				str = previous;
                			}
                			String id = "";
                			if(str.equals("111")){
                				turn+=str;
                			}
							else {
								for (Sector sector : entrada.getListaSectores()) {
									if (sector.getNombre().equalsIgnoreCase(str)) {
										if(str.equals(str.toUpperCase())){
											id = sector.getId().toUpperCase();
										}else{
											id = sector.getId();											
										}
										break;
									}
								}
								turn += id;
							}
                			previous = str;
                		}
                		c++;
                	}
                	turnos.add(countTurnos,turn);
                	countTurnos++;
                }
 
                else if(countLine>=20 & countLine<=32){
                	turnoAsignado.add(Integer.parseInt(field[7]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int iterate = 0;
        for(Controlador c:entrada.getControladores()){
        	c.setTurnoAsignado(turnoAsignado.get(iterate));
        	iterate++;
        }
        Solucion solucion = new Solucion(turnos, entrada.getControladores(),0);
        return solucion;
    }

}