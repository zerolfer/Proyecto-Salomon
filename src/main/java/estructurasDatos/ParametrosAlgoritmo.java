package estructurasDatos;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveFactory;
import trazas.Trazas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * En este objeto se cargan los parametros del algoritmo usado para la resolucion del problema mediante el fichero
 * algorithm.properties, el cual se encuentra en la carpeta src/main/resources
 */
public class ParametrosAlgoritmo {
    private static final String RESOURCE_PATH = "/algorithm.properties";
    public String algoritmo;
    public SA SA;
    public VNS VNS;
    /**
     * Nombre de la funcion objetivo que se utiliza en la fase2 del algoritmo.
     */
    protected String funcionFitnessFase2;
    /**
     * Nombre de la funcion objetivo que se utiliza en la fase3 del algoritmo.
     */
    protected String funcionFitnessFase3;
    /**
     * Fichero que contiene los parametros del algoritmo de resolucion.
     */
    private Properties propParametrosAlgoritmo = new Properties();

    public ParametrosAlgoritmo(String propFileParametersAlgorithm) {
        loadProperties(propFileParametersAlgorithm);
        ininicializarResto();
    }

    public ParametrosAlgoritmo() {
        loadProperties(RESOURCE_PATH);
        ininicializarResto();
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
        this.funcionFitnessFase2 = getString("funcionFitnessFase2");
        this.funcionFitnessFase3 = getString("funcionFitnessFase3");

        this.SA = new SA(); // inicializar variables de cada metaheuristica
        this.VNS = new VNS();
    }

    private double getDouble(String propertie) {
        return Double.parseDouble(propParametrosAlgoritmo.getProperty(propertie));
    }

    private int getInteger(String propertie) {
        return Integer.parseInt(propParametrosAlgoritmo.getProperty(propertie));
    }

    private String getString(String propertie) {
        return propParametrosAlgoritmo.getProperty(propertie);
    }

    public String getAlgoritmo() {
        return getString("algoritmo");
    }

    public String getFuncionFitnessFase2() {
        return funcionFitnessFase2;
    }

    public void setFuncionFitnessFase2(String funcionFitnessFase2) {
        funcionFitnessFase2 = funcionFitnessFase2;
    }

    public String getFuncionFitnessFase3() {
        return funcionFitnessFase3;
    }

    public void setFuncionFitnessFase3(String funcionFitnessFase3) {
        this.funcionFitnessFase3 = funcionFitnessFase3;
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

        // leido en minutos, lo pasamos a milisegundos
        private long maxMilisecondsAllowed = getInteger("maxTimeAllowed")*60*1000;

        private List<NeighborStructure> neighborStructures = initializeNeighborStructures("neighborStructures");

        // . . .

        private List<NeighborStructure> initializeNeighborStructures(String neighborStructures) {

            String texto = getString(neighborStructures);
            String[] nombresMovimientos = texto.split(",");

            List<NeighborStructure> result = new ArrayList<>();
            for (String id : nombresMovimientos) {
                result.add(MoveFactory.createNeighborhood(id)); // FACTORY METHOD
            }
            return result;
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

        /**
         * Conjunto de estructuras de vecindad que serán empleadas por el VNS en el
         * orden estricto en el que se encuentran en la lista.
         */
        public List<NeighborStructure> getNeighborStructures() {
            return neighborStructures;
        }
    }

}

