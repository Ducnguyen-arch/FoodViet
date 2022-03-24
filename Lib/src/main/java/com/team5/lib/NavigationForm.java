package com.team5.lib;

public class NavigationForm {
    int hinhminhhoa;
    String noidung;

    public NavigationForm() {
    }

    public int getHinhminhhoa() {
        return hinhminhhoa;
    }

    public void setHinhminhhoa(int hinhminhhoa) {
        this.hinhminhhoa = hinhminhhoa;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public NavigationForm(int hinhminhhoa, String noidung) {
        this.hinhminhhoa = hinhminhhoa;
        this.noidung = noidung;
    }
}
