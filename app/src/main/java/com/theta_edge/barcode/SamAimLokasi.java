package com.theta_edge.barcode;

/**
 * Created by devspeks on 12/19/2017.
 */
public class SamAimLokasi {
    private Long pkid;
    private String kod;
    private String perihal;


    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public String getKod() {
        return kod;
    }

    public void setPerihal(String perihal) {
        this.perihal = perihal;
    }

    public String getPerihal() {
        return perihal;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }
}
