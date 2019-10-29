package estructurasDatos.Auxiliares;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

public class Random {

    private static XoRoShiRo128PlusRandom random = new XoRoShiRo128PlusRandom();

    public static int nextInt() {
        return random.nextInt();
    }

    public static int nextInt(int n) {
        return random.nextInt(n);
    }

    public static double nextDouble(){
        return random.nextDouble();
    }

}
