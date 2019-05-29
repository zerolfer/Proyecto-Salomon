package estructurasDatos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Objeto donde se encuentran los pesos asociados a los objetivos y sub-objetivos que deben optimizarse en las solciones generadas
 *
 * @author Tino
 */
public class PesosObjetivos {
    /**
     * Fichero que contiene los parametros del problema.
     */
    private static Properties propParametros = new Properties();
    /**
     * Peso asociado al objetivo 1.
     */
    private double pesoObj1;
    /**
     * Peso asociado al objetivo 2.
     */
    private double pesoObj2;
    /**
     * Peso asociado al objetivo 3.
     */
    private double pesoObj3;
    /**
     * Peso asociado al objetivo 4.
     */
    private double pesoObj4;
    /**
     * Peso asociado al sub-objetivo 1.1.
     */
    private double pesoObj1Sub1;
    /**
     * Peso asociado al sub-objetivo 1.2.
     */
    private double pesoObj1Sub2;
    /**
     * Peso asociado al sub-objetivo 1.3.
     */
    private double pesoObj1Sub3;
    /**
     * Peso asociado al sub-objetivo 3.1.
     */
    private double pesoObj3Sub1;
    /**
     * Peso asociado al sub-objetivo 3.2.
     */
    private double pesoObj3Sub2;

    /**
     * @param nombreFicheroP Ruta del fichero con las propiedades del problema.
     */
    public PesosObjetivos(URL nombreFicheroP) {
        getPropertiesP(nombreFicheroP);
        this.pesoObj1 = Double.parseDouble(propParametros.getProperty("Obj1"));
        this.pesoObj2 = Double.parseDouble(propParametros.getProperty("Obj2"));
        this.pesoObj3 = Double.parseDouble(propParametros.getProperty("Obj3"));
        this.pesoObj4 = Double.parseDouble(propParametros.getProperty("Obj4"));
        this.pesoObj1Sub1 = Double.parseDouble(propParametros.getProperty("Obj1Sub1"));
        this.pesoObj1Sub2 = Double.parseDouble(propParametros.getProperty("Obj1Sub2"));
        this.pesoObj1Sub3 = Double.parseDouble(propParametros.getProperty("Obj1Sub3"));
        this.pesoObj3Sub1 = Double.parseDouble(propParametros.getProperty("Obj3Sub1"));
        this.pesoObj3Sub2 = Double.parseDouble(propParametros.getProperty("Obj3Sub2"));
    }

    public double getPesoObj1() {
        return pesoObj1;
    }

    public void setPesoObj1(double pesoObj1) {
        this.pesoObj1 = pesoObj1;
    }

    public double getPesoObj2() {
        return pesoObj2;
    }

    public void setPesoObj2(double pesoObj2) {
        this.pesoObj2 = pesoObj2;
    }

    public double getPesoObj3() {
        return pesoObj3;
    }

    public void setPesoObj3(double pesoObj3) {
        this.pesoObj3 = pesoObj3;
    }

    public double getPesoObj4() {
        return pesoObj4;
    }

    public void setPesoObj4(double pesoObj4) {
        this.pesoObj4 = pesoObj4;
    }

    public double getPesoObj1Sub1() {
        return pesoObj1Sub1;
    }

    public void setPesoObj1Sub1(double pesoObj1Sub1) {
        this.pesoObj1Sub1 = pesoObj1Sub1;
    }

    public double getPesoObj1Sub2() {
        return pesoObj1Sub2;
    }

    public void setPesoObj1Sub2(double pesoObj1Sub2) {
        this.pesoObj1Sub2 = pesoObj1Sub2;
    }

    public double getPesoObj1Sub3() {
        return pesoObj1Sub3;
    }

    public void setPesoObj1Sub3(double pesoObj1Sub3) {
        this.pesoObj1Sub3 = pesoObj1Sub3;
    }

    public double getPesoObj3Sub1() {
        return pesoObj3Sub1;
    }

    public void setPesoObj3Sub1(double pesoObj3Sub1) {
        this.pesoObj3Sub1 = pesoObj3Sub1;
    }

    public double getPesoObj3Sub2() {
        return pesoObj3Sub2;
    }

    public void setPesoObj3Sub2(double pesoObj3Sub2) {
        this.pesoObj3Sub2 = pesoObj3Sub2;
    }

    public static void getPropertiesP(URL propFileName) {
        InputStream input = null;
        try {
            input = propFileName.openStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            getPropParametros().load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getPropParametros() {
        return propParametros;
    }

    public static void setPropParametros(Properties propParametros) {
        PesosObjetivos.propParametros = propParametros;
    }
}
