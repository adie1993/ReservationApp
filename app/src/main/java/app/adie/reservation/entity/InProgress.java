package app.adie.reservation.entity;

import java.io.Serializable;

public class InProgress extends Pesan implements Serializable {
    public String kodebook;
    public String cabangAsal;
    public String cabangTujuan;
    public int harga;
    public int diskon;
    public String buktitransfer;
    public String no_seat;
    public String nama_penumpang;
    public String total;
    public String jamBerangkat;
    public String id_pemesanan;
    public String tanggalBerangkat;
}
