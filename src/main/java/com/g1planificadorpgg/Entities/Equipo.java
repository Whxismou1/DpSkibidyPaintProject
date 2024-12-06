package com.g1planificadorpgg.Entities;

public class Equipo {

    // CREATE TABLE IF NOT EXISTS equipos (
    // id INT AUTO_INCREMENT PRIMARY KEY,
    // etiquetas_de_fila NCHAR(255) NULL,
    // max_capacidad_lote_dia double NULL,
    // max_capacidad_lote_semana FLOAT NULL,
    // numero_equipos INT NULL
    private int id;
    private String etiquetasDeFila;
    private double maxCapacidadLoteDia;
    private double maxCapacidadLoteSemana;
    private int numeroEquipos;

    public Equipo() {
    }

    public Equipo(String etiquetasDeFila, double maxCapacidadLoteDia, double maxCapacidadLoteSemana,
            int numeroEquipos) {
        this.etiquetasDeFila = etiquetasDeFila;
        this.maxCapacidadLoteDia = maxCapacidadLoteDia;
        this.maxCapacidadLoteSemana = maxCapacidadLoteSemana;
        this.numeroEquipos = numeroEquipos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEtiquetasDeFila() {
        return etiquetasDeFila;
    }

    public void setEtiquetasDeFila(String etiquetasDeFila) {
        this.etiquetasDeFila = etiquetasDeFila;
    }

    public double getMaxCapacidadLoteDia() {
        return maxCapacidadLoteDia;
    }

    public void setMaxCapacidadLoteDia(double maxCapacidadLoteDia) {
        this.maxCapacidadLoteDia = maxCapacidadLoteDia;
    }

    public double getMaxCapacidadLoteSemana() {
        return maxCapacidadLoteSemana;
    }

    public void setMaxCapacidadLoteSemana(double maxCapacidadLoteSemana) {
        this.maxCapacidadLoteSemana = maxCapacidadLoteSemana;
    }

    public int getNumeroEquipos() {
        return numeroEquipos;
    }

    public void setNumeroEquipos(int numeroEquipos) {
        this.numeroEquipos = numeroEquipos;
    }

    @Override
    public String toString() {
        return "Equipo [etiquetasDeFila=" + etiquetasDeFila + ", id=" + id + ", maxCapacidadLoteDia="
                + maxCapacidadLoteDia + ", maxCapacidadLoteSemana=" + maxCapacidadLoteSemana + ", numeroEquipos="
                + numeroEquipos + "]";
    }

}
