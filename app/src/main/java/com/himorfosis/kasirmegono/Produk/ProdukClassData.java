package com.himorfosis.kasirmegono.Produk;

import android.content.Intent;

public class ProdukClassData {


    private String nama_produk;
    private Integer id_kategori_produk;

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    private Integer stok;
    private String gambar;
    private Integer harga;
    private String kategori;
    private Integer id_produk;

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

}
