package com.arcasolutions.api.model;

import java.util.HashMap;

public class EventSchedule extends HashMap<String, int[]> {

    public int[] getJan() {
        return get("Jan");
    }

    public int[] getFeb() {
        return get("Feb");
    }

    public int[] getMar() {
        return get("Mar");
    }

    public int[] getApr() {
        return get("Apr");
    }

    public int[] getMay() {
        return get("May");
    }

    public int[] getJun() {
        return get("Jun");
    }

    public int[] getJul() {
        return get("Jul");
    }

    public int[] getAug() {
        return get("Aug");
    }

    public int[] getSep() {
        return get("Sep");
    }

    public int[] getOct() {
        return get("Oct");
    }

    public int[] getNov() {
        return get("Nov");
    }

    public int[] getDec() {
        return get("DEc");
    }


}
