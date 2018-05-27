package com.example.anthony_pc.tarea6;

import android.graphics.Bitmap;

/**
 * Created by Anthony-PC on 22/5/2018.
 */

public class Producto {

    private String id;
    private String nombre;
    private int precio;
    private Bitmap foto;
    private String descripcion;

    public Producto(String id, String nombre, int precio,  String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public Producto(String nombre, int precio,  String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }
    public Producto(String nombre, int precio,  String descripcion, Bitmap image) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.foto = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
