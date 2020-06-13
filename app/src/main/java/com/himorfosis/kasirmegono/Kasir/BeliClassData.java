package com.himorfosis.kasirmegono.Kasir;

public class BeliClassData {

    private Integer id_produk;
    private Integer jumlah_produk;
    private Integer harga_produk;
    private Integer bayar;
    private String nama_produk;
    private String gambar;

    public BeliClassData (Integer id_produk, Integer jumlah_produk, Integer harga_produk,
                           Integer bayar, String nama_produk, String gambar) {

        super();
        this.id_produk = id_produk;
        this.jumlah_produk = jumlah_produk;
        this.harga_produk = harga_produk;
        this.bayar = bayar;
        this.nama_produk = nama_produk;
        this.gambar = gambar;

    }

    public Integer getId_produk() {
        return id_produk;
    }

    public void setId_produk(Integer id_produk) {
        this.id_produk = id_produk;
    }

    public Integer getJumlah_produk() {
        return jumlah_produk;
    }

    public void setJumlah_produk(Integer jumlah_produk) {
        this.jumlah_produk = jumlah_produk;
    }

    public Integer getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(Integer harga_produk) {
        this.harga_produk = harga_produk;
    }

    public Integer getBayar() {
        return bayar;
    }

    public void setBayar(Integer bayar) {
        this.bayar = bayar;
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
}
