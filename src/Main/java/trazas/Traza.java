package trazas;

import java.util.ArrayList;

public class Traza {
    private int tmn;
    private ArrayList<ArrayList<String>> info;
    private int orden;
    private long time;

    public Traza(int tmn, int orden, ArrayList<ArrayList<String>> info, long time) {
        this.tmn = tmn;
        this.orden = orden;
        this.info = info;
        this.setTime(time);

    }

    public int getTmn() {
        return tmn;
    }

    public void setTmn(int tmn) {
        this.tmn = tmn;
    }

    public ArrayList<ArrayList<String>> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ArrayList<String>> info) {
        this.info = info;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}

