package app.adie.reservation.entity;

import java.io.Serializable;

public class Cancel extends Pesan implements Serializable {
    public String kodebook;
    public String kodetiket;
    public String cabangAsal;
    public String cabangTujuan;
    public int harga;
    public int diskon;
    public int uangkem;
    public String no_seat;
    public String nama_penumpang;
    public String total;
    public String jamBerangkat;
    public String id_pemesanan;
    public String tanggalBerangkat;
}
