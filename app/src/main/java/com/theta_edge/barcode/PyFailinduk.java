package com.theta_edge.barcode;

/**
 * Created by devspeks on 12/18/2017.
 */
public class PyFailinduk {
    private Long pkid;
    private String nama;
    private String jawatan;

    public void setJawatan(String jawatan) {
        this.jawatan = jawatan;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Long getPkid() {
        return pkid;
    }

    public String getNama() {
        return nama;
    }

    public String getJawatan() {
        return jawatan;
    }
}
