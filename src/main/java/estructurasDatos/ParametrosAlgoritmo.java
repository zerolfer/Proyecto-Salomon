package estructurasDatos;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveFactory;
import estructurasDatos.DominioDelProblema.Entrada;
import patrones.Patrones;
import trazas.Trazas;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * En este objeto se cargan los parametros del algoritmo usado para la resolucion del problema mediante el fichero
 * algorithm.properties, el cual se encuentra en la carpeta src/main/resources
 */
public class ParametrosAlgoritmo {
    private static final String RESOURCE_PATH = "/algorithm.properties";
    /**
     * Fichero que contiene los parametros del algoritmo de resolucion.
     */
    private static Properties propParametrosAlgoritmo = new Properties();
    public SA SA;
    public VNS VNS;
    public String algoritmo;
    /**
     * Nombre de la funcion objetivo que se utiliza en la fase2 del algoritmo.
     */
    protected String funcionFitnessFase2;

    /**
     * <p>
     * Tiempo máximo total que le permitimos al algoritmo ejecutarse,
     * puesto que en nuestro sistema es crucial el tiempo,
     * no debemos superar el umbral marcado.
     * Leído en minutos, se pasa a milisegundos.
     * </p>
     */
    private long maxMilisecondsAllowed;

    private double ponderacionFitness1;
    private double ponderacionFitness2;
    private double ponderacionFitness3;
    private double ponderacionFitness4;


    public ParametrosAlgoritmo(String propFileParametersAlgorithm) {
        loadProperties(propFileParametersAlgorithm);
        ininicializarResto();
    }

    public ParametrosAlgoritmo() {
        loadProperties(RESOURCE_PATH);
        ininicializarResto();
    }

    private static String getString(String propertie) {
        return propParametrosAlgoritmo.getProperty(propertie);
    }

    public void initializeNeighborStructures(Entrada entrada, Patrones patrones,
                                             Parametros parametros,
                                             ParametrosAlgoritmo parametrosAlgoritmo) {

        String texto = getString(VNS.NEIGHBOR_STRUCTURES);
        String[] nombresMovimientos = texto.split(",");

        List<NeighborStructure> result = new ArrayList<>();
        for (String id : nombresMovimientos) {
            // FACTORY METHOD
            result.add(MoveFactory.createNeighborhood(id, entrada, patrones, parametros, parametrosAlgoritmo));
        }
        VNS.neighborStructures = result;
    }

    private void loadProperties(String resourcePath) {
        try {
            propParametrosAlgoritmo.load(
                    ParametrosAlgoritmo.class.getResourceAsStream(resourcePath)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ininicializarResto() {
        this.algoritmo = getString("algoritmo");
        this.maxMilisecondsAllowed = getInteger("maxTimeAllowed") * 60 * 1000;

        this.funcionFitnessFase2 = getString("funcionFitnessFase2");

        this.ponderacionFitness1 = getDouble("ponderacionFitness1");
        this.ponderacionFitness2 = getDouble("ponderacionFitness2");
        this.ponderacionFitness3 = getDouble("ponderacionFitness3");
        this.ponderacionFitness4 = getDouble("ponderacionFitness4");

        this.SA = new SA(); // inicializar variables de cada metaheuristica
        this.VNS = new VNS();
    }

    private double getDouble(String propertie) {
        return Double.parseDouble(propParametrosAlgoritmo.getProperty(propertie));
    }

    private int getInteger(String propertie) {
        return Integer.parseInt(propParametrosAlgoritmo.getProperty(propertie));
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public String getFuncionFitnessFase2() {
        return funcionFitnessFase2;
    }

    public void setFuncionFitnessFase2(String funcionFitnessFase2) {
        funcionFitnessFase2 = funcionFitnessFase2;
    }

    /**
     * <p>
     * Tiempo máximo total que le permitimos al VNS ejecutarse,
     * puesto que en nuestro sistema es crucial el tiempo,
     * no debemos superar el umbral marcado.
     * </p>
     */
    public long getMaxMilisecondsAllowed() {
        return maxMilisecondsAllowed;
    }


    //
    // HACK: método temporal para el deploy en forma de JAR (Mayo 2019)
    //
    public void sobreescribirParametrosViaExterna(String propFileExternoParametros) {
        Properties parametrosExternos = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(propFileExternoParametros);
            parametrosExternos.load(input);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (Object s : parametrosExternos.keySet()) {
            String key = (String) s;
            String value = parametrosExternos.getProperty(key);
            switch (key) {
                case "algoritmo":
                    this.algoritmo = value;
                    break;
                case "tiempoMaximo":
                    this.maxMilisecondsAllowed = Integer.parseInt(value) * 60 * 1000;
                    break;
                case "ponderacionFitness1":
                    this.ponderacionFitness1 = Double.parseDouble(value);
                    break;
                case "ponderacionFitness2":
                    this.ponderacionFitness2 = Double.parseDouble(value);
                    break;
                case "ponderacionFitness3":
                    this.ponderacionFitness3 = Double.parseDouble(value);
                    break;
                case "ponderacionFitness4":
                    this.ponderacionFitness4 = Double.parseDouble(value);
                    break;
                case "numeroDelCasoParaResolver":
                    break;
                default:
                    System.err.println("El parámetro \"" + key + "\" no se reconoce como parámetro interno del sistema.");
                    break;
            }
        }
    }

    public double getPonderacionFitness1() {
        return ponderacionFitness1;
    }

    public double getPonderacionFitness2() {
        return ponderacionFitness2;
    }

    public double getPonderacionFitness3() {
        return ponderacionFitness3;
    }

    public double getPonderacionFitness4() {
        return ponderacionFitness4;
    }

    public class SA {
        /**
         * Temperatura inicial del recocido simulado.
         */
        private double temperaturaInicial = getDouble("temperaturaInicial");
        /**
         * Porcentaje de descenso de la temperatura inicial.
         */
        private double descensoTemperatura = getDouble("descensoTemperatura");
        /**
         * Numero de iteraciones hasta que se produce un descenso de la temperatura.
         */
        private int iteracionesTemperaturaL = getInteger("iteracionesTemperatura");
        /**
         * Porcentaje de mejora que el algoritmo debe superar en X iteraciones para que este no finalice.
         */
        private double condicionParadaPorcent = getDouble("condicionParadaPorcent");
        /**
         * Numero de ciclos necesarios para que se compruebe el porcentaje de mejora necesario para finalizar el
         * algoritmo.
         */
        private int condicionParadaCiclos = getInteger("condicionParadaCiclos");
        /**
         * Condicion de parada adiccional, la cual evita que finalice el algoritmo en las fases iniciales con
         * temperatura elevada por no mejorar la solucion durante el proceso de exploracion.
         * Mientras se acepte un porcentaje de soluciones superior al establecido el algoritmo sigue iterando.
         */
        private double condicionParadaNumeroMejoras = getDouble("condicionParadaNumeroMejoras");
        /**
         * Numero maximo de slots que se permiten en los movimiento utilizados para generar soluciones del entorno.
         * Esta unidad se representa en minutos.
         */
        private int tamañoMaxMov = getInteger("tamañoMaxMov");
        /**
         * Numero minimo de slots que se permiten en los movimiento utilizados para generar soluciones del entorno.
         * Esta unidad se representa en minutos.
         */
        private int tamañoMinMov = getInteger("tamañoMinMov");
        /**
         * Nombre del movimiento escogido para generar soluciones del entorno por el algoritmo.
         */
        private String movimientosEntorno = getString("movimientosEntorno");
        /**
         * Si el movimiento esta formado por dos sub-movimientos, la probabilidad de eleccion del segundo movimiento
         * frente al primero.
         */
        private double porcentajeEleccionMov = getDouble("porcentajeEleccionMov");

        private String movimientosEntornoGreedy = getString("movimientosEntornoGreedy");

        {
            Trazas.setnTmnsIntervalos(((tamañoMaxMov - tamañoMinMov) / 3));
        }

        public double getTemperaturaInicial() {
            return temperaturaInicial;
        }

        public void setTemperaturaInicial(double temperaturaInicial) {
            this.temperaturaInicial = temperaturaInicial;
        }

        public double getDescensoTemperatura() {
            return descensoTemperatura;
        }

        public void setDescensoTemperatura(double descensoTemperatura) {
            this.descensoTemperatura = descensoTemperatura;
        }

        public int getIteracionesTemperaturaL() {
            return iteracionesTemperaturaL;
        }

        public void setIteracionesTemperaturaL(int iteracionesTemperaturaL) {
            this.iteracionesTemperaturaL = iteracionesTemperaturaL;
        }

        public double getCondicionParadaPorcent() {
            return condicionParadaPorcent;
        }

        public void setCondicionParadaPorcent(double condicionParadaPorcent) {
            this.condicionParadaPorcent = condicionParadaPorcent;
        }

        public int getCondicionParadaCiclos() {
            return condicionParadaCiclos;
        }

        public void setCondicionParadaCiclos(int condicionParadaCiclos) {
            this.condicionParadaCiclos = condicionParadaCiclos;
        }

        public double getCondicionParadaNumeroMejoras() {
            return condicionParadaNumeroMejoras;
        }

        public void setCondicionParadaNumeroMejoras(double condicionParadaNumeroMejoras) {
            this.condicionParadaNumeroMejoras = condicionParadaNumeroMejoras;
        }

        public int getTamañoMaxMov() {
            return tamañoMaxMov;
        }

        public void setTamañoMaxMov(int tamañoMaxMov) {
            this.tamañoMaxMov = tamañoMaxMov;
        }

        public int getTamañoMinMov() {
            return tamañoMinMov;
        }

        public void setTamañoMinMov(int tamañoMinMov) {
            this.tamañoMinMov = tamañoMinMov;
        }

        public String getMovimientosEntorno() {
            return movimientosEntorno;
        }

        public void setMovimientosEntorno(String movimientosEntorno) {
            this.movimientosEntorno = movimientosEntorno;
        }

        public double getPorcentajeEleccionMov() {
            return porcentajeEleccionMov;
        }

        public void setPorcentajeEleccionMov(double porcentajeEleccionMov) {
            this.porcentajeEleccionMov = porcentajeEleccionMov;
        }

        public String getMovimientosEntornoGreedy() {
            return movimientosEntornoGreedy;
        }

        public void setMovimientosEntornoGreedy(String movimientosEntornoGreedy) {
            this.movimientosEntornoGreedy = movimientosEntornoGreedy;
        }
    }

    public class VNS {

        private static final String NEIGHBOR_STRUCTURES = "neighborStructures";
        private static final String NUM_MAX_ITERACIONES_BUSQUEDA_LOCAL = "numMaxIteracionesBusquedaLocal";

        private List<NeighborStructure> neighborStructures;
        private int numMaxIteracionesBusquedaLocal = getInteger("numMaxIteracionesBusquedaLocal");


        /**
         * Conjunto de estructuras de vecindad que serán empleadas por el VNS en el
         * orden estricto en el que se encuentran en la lista.
         */
        public List<NeighborStructure> getNeighborStructures() {
            return neighborStructures;
        }

        /**
         * Número maximo de iteraciones que pueden producirse en la busqueda local sin encontrar mejoría
         * en la búsqueda.
         *
         * Este parametro se utiliza debido a que la Busqueda Local es estocástica, y no determinista
         */
        public int getNumMaxIteracionesBusquedaLocal() {
            return numMaxIteracionesBusquedaLocal;
        }

    }

}

