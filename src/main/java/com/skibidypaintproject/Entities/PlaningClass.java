package com.skibidypaintproject.Entities;

// CREATE TABLE IF NOT EXISTS planning_classes (
//     id INT AUTO_INCREMENT PRIMARY KEY,
//     planning_class NCHAR(255) NULL,
//     planta NCHAR(255) NULL,
//     tecnologia NCHAR(255) NULL,
// 	tamano_max INT NULL,
// 	equipo VARCHAR(255) NULL,
//     capacidad_lote_dia FLOAT NULL,
//     capacidad_lote_semana FLOAT NULL
public class PlaningClass {
    private int id;
    private String planingClass;
    private String planta;
    private String tecnologia;
    private int tamanoMax;
    private String equipo;
    private float capacidadLoteDia;
    private float capacidadLoteSemana;

    public PlaningClass() {
    }

    public PlaningClass(String planingClass, String planta, String tecnologia, int tamanoMax, String equipo,
            float capacidadLoteDia, float capacidadLoteSemana) {
        this.planingClass = planingClass;
        this.planta = planta;
        this.tecnologia = tecnologia;
        this.tamanoMax = tamanoMax;
        this.equipo = equipo;
        this.capacidadLoteDia = capacidadLoteDia;
        this.capacidadLoteSemana = capacidadLoteSemana;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaningClass() {
        return planingClass;
    }

    public void setPlaningClass(String planingClass) {
        this.planingClass = planingClass;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public int getTamanoMax() {
        return tamanoMax;
    }

    public void setTamanoMax(int tamanoMax) {
        this.tamanoMax = tamanoMax;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public float getCapacidadLoteDia() {
        return capacidadLoteDia;
    }

    public void setCapacidadLoteDia(float capacidadLoteDia) {
        this.capacidadLoteDia = capacidadLoteDia;
    }

    public float getCapacidadLoteSemana() {
        return capacidadLoteSemana;
    }

    public void setCapacidadLoteSemana(float capacidadLoteSemana) {
        this.capacidadLoteSemana = capacidadLoteSemana;
    }

    @Override
    public String toString() {
        return "PlaningClass [capacidadLoteDia=" + capacidadLoteDia + ", capacidadLoteSemana=" + capacidadLoteSemana
                + ", equipo=" + equipo + ", id=" + id + ", planta=" + planta + ", planingClass=" + planingClass
                + ", tamanoMax=" + tamanoMax + ", tecnologia=" + tecnologia + "]";
    }

}
