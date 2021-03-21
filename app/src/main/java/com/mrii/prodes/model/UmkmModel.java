package com.mrii.prodes.model;

import com.google.gson.annotations.SerializedName;

public class UmkmModel {
    @SerializedName("id")
    private String id;
    @SerializedName("kepala_desa_id")
    private String kepalaDesaId;
    @SerializedName("nama_umkm")
    private String namaUmkm;
    @SerializedName("nama_pemilik")
    private String namaPemilik;
    @SerializedName("kode_akses")
    private String kodeAkses;
    @SerializedName("nomor_wa")
    private String nomorWa;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("provinsi")
    private String provinsi;
    @SerializedName("kabupaten")
    private String kabupaten;
    @SerializedName("kecamatan")
    private String kecamatan;
    @SerializedName("kelurahan")
    private String kelurahan;
    @SerializedName("nama_kades")
    private String namaKades;
    @SerializedName("no_hp_kades")
    private String noHpKades;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKepalaDesaId() {
        return kepalaDesaId;
    }

    public void setKepalaDesaId(String kepalaDesaId) {
        this.kepalaDesaId = kepalaDesaId;
    }

    public String getNamaUmkm() {
        return namaUmkm;
    }

    public void setNamaUmkm(String namaUmkm) {
        this.namaUmkm = namaUmkm;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public String getKodeAkses() {
        return kodeAkses;
    }

    public void setKodeAkses(String kodeAkses) {
        this.kodeAkses = kodeAkses;
    }

    public String getNomorWa() {
        return nomorWa;
    }

    public void setNomorWa(String nomorWa) {
        this.nomorWa = nomorWa;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getNamaKades() {
        return namaKades;
    }

    public void setNamaKades(String namaKades) {
        this.namaKades = namaKades;
    }

    public String getNoHpKades() {
        return noHpKades;
    }

    public void setNoHpKades(String noHpKades) {
        this.noHpKades = noHpKades;
    }
}
