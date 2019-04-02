package algorithms.vnsPablo;

public class NeighborhoodHandler {
    private int neighborhood = 0;
    private int nList[];
    private int size = 3;
    public int wide = 5;
    public int laps = 0;

    public NeighborhoodHandler(int ns[]) {
        this.nList = ns;
    }

    public int getNeigborhood() {
        return nList[neighborhood];
    }

    public void setNeighborhood(int n) {
        this.neighborhood = n;
        return;
    }

    public void nextNeighborhood() {

        if ((nList[neighborhood] == 3 || nList[neighborhood] == 0 || nList[neighborhood] == 1 || nList[neighborhood] == 2) && (size < 54)) {
			/*Random rand = new Random();
	    	
	    	int n = rand.nextInt(2);
			neighborhood = n;*/
            increaseSize();
        } else {
            if (neighborhood < (nList.length - 1)) {
                neighborhood++;
                size = 3;
                wide = 5 + laps * 5;

            } else {
                neighborhood = 0;
                size = 3;
                laps++;
                System.out.println("Laps [" + laps + "]");
                wide = 5 + laps * 5;
            }
        }
    }

    public void resetNeighborhood() {
		/*Random rand = new Random();
    	
    	int n = rand.nextInt(2);*/
        neighborhood = 0;
        size = 3;
        wide = 5;
        laps = 0;
    }

    public void increaseSize() {
        size += 3;
    }

    public void resetSize() {
        size = 3;
    }

    public int getSize() {
        return this.size;
    }


}
