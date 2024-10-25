package com.negocio.semana04comsumorest.entity;

public class Medico {
    private int id_medico;
    private String nombre;

    // Getters y Setters
    public int getId_medico() {
        return id_medico;
    }

    public void setId_medico(int id_medico) {
        this.id_medico = id_medico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
