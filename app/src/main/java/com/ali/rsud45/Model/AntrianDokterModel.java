package com.ali.rsud45.Model;

public class AntrianDokterModel {
    String date, total_pasien;

    public AntrianDokterModel(String date, String total_pasien) {
        this.date = date;
        this.total_pasien = total_pasien;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal_pasien() {
        return total_pasien;
    }

    public void setTotal_pasien(String total_pasien) {
        this.total_pasien = total_pasien;
    }
}
