package app.adie.reservation.entity;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class User implements Serializable {
    String id, nama, email,phone,alamat;


    public User() {
    }

    public User(String id, String nama, String email,String phone,String alamat) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.phone = phone;
        this.alamat = alamat;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return nama;
    }

    public void setName(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
