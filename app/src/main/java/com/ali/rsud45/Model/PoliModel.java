package com.ali.rsud45.Model;

public class PoliModel {
    String poli_id, poli;

    public PoliModel(String poli_id, String poli) {
        this.poli_id = poli_id;
        this.poli = poli;
    }

    public String getPoli_id() {
        return poli_id;
    }

    public void setPoli_id(String poli_id) {
        this.poli_id = poli_id;
    }

    public String getPoli() {
        return poli;
    }

    public void setPoli(String poli) {
        this.poli = poli;
    }
}
