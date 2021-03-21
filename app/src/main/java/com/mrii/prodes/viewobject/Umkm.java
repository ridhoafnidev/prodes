package com.mrii.prodes.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

    @Entity(primaryKeys = "umkmId")
    public class Umkm {

        @NonNull
        @SerializedName("user_id")
        public String userId;

        @SerializedName("umkm_id")
        public String umkmId;

        @SerializedName("kepala_desa_id")
        public String kepalaDesaId;

        @SerializedName("nama_umkm")
        public String namaUmkm;

        @SerializedName("nama_pemilik")
        public String namaPemilik;

        @SerializedName("kode_akses")
        public String kodeAkses;

        @SerializedName("nomor_wa")
        public String nomorWa;

        @SerializedName("alamat")
        public String alamat;

        @SerializedName("provinsi")
        public String provinsi;

        @SerializedName("kabupaten")
        public String kabupaten;

        @SerializedName("kecamatan")
        public String kecamatan;

        @SerializedName("kelurahan")
        public String kelurahan;

        @SerializedName("nama_kades")
        public String namaKades;

        @SerializedName("provinsi_id")
        public String provinsiId;

        @SerializedName("kab_kota_id")
        public String kabKotaId;

        @SerializedName("kecamatan_id")
        public String kecamatanId;

        @SerializedName("kel_desa_id")
        public String kelDesaId;

        public Umkm(@NonNull String userId, String umkmId, String kepalaDesaId, String namaUmkm,String namaPemilik,
                    String kodeAkses, String nomorWa, String alamat, String provinsi, String kabupaten,
                    String kecamatan, String kelurahan, String namaKades, String provinsiId, String kabKotaId,
                    String kecamatanId) {
            this.userId = userId;
            this.umkmId = umkmId;
            this.kepalaDesaId = kepalaDesaId;
            this.namaUmkm = namaUmkm;
            this.namaPemilik = namaPemilik;
            this.kodeAkses = kodeAkses;
            this.nomorWa = nomorWa;
            this.alamat = alamat;
            this.provinsi = provinsi;
            this.kabupaten = kabupaten;
            this.kecamatan = kecamatan;
            this.kelurahan = kelurahan;
            this.namaKades = namaKades;
            this.provinsiId = provinsiId;
            this.kabKotaId = kabKotaId;
    }

}
