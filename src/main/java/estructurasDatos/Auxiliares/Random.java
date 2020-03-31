package estructurasDatos.Auxiliares;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

public class Random {

    private static XoRoShiRo128PlusRandom random = new XoRoShiRo128PlusRandom(20);

    public static int nextInt() {
        return random.nextInt();
    }

    public static int nextInt(int n) {
        return random.nextInt(n);
    }

    public static double nextDouble(){
        return random.nextDouble();
    }

    public static void reset(){
        random = new XoRoShiRo128PlusRandom(1);
    }

}
