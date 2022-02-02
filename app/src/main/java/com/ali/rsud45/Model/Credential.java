package com.ali.rsud45.Model;

public class Credential {

    private int  role, off_id;
    private long exp;
    private String id, nama, username, off_name;

    public Credential(String id, int role, int off_id, long exp, String nama, String username, String off_name) {
        this.id = id;
        this.role = role;
        this.off_id = off_id;
        this.exp = exp;
        this.nama = nama;
        this.username = username;
        this.off_name = off_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getOff_id() {
        return off_id;
    }

    public void setOff_id(int off_id) {
        this.off_id = off_id;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOff_name() {
        return off_name;
    }

    public void setOff_name(String off_name) {
        this.off_name = off_name;
    }
}