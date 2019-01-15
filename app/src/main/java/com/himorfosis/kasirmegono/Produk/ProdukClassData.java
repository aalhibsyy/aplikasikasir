package com.himorfosis.kasirmegono.Produk;

import android.content.Intent;

public class ProdukClassData {

    private Integer id_produk;
    private String nama_produk;
    private Integer id_kategori_produk;
    private Integer id_harga;
    private String gambar;
    private Integer harga;
    private Integer harga_gojek;
    private Integer harga_grab;
    private String kategori;

    public Integer getId_produk() {
        return id_produk;
    }

    public void setId_produk(Integer id_produk) {
        this.id_produk = id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public Integer getId_harga() {
        return id_harga;
    }

    public void setId_harga(Integer id_harga) {
        this.id_harga = id_harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Integer getId_kategori_produk() {
        return id_kategori_produk;
    }

    public void setId_kategori_produk(Integer id_kategori_produk) {
        this.id_kategori_produk = id_kategori_produk;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Integer getHarga_gojek() {
        return harga_gojek;
    }

    public void setHarga_gojek(Integer harga_gojek) {
        this.harga_gojek = harga_gojek;
    }

    public Integer getHarga_grab() {
        return harga_grab;
    }

    public void setHarga_grab(Integer harga_grab) {
        this.harga_grab = harga_grab;
    }
}
