
package com.skibidypaintproject.Entities;

public class EquiposAsignPorCriterio {

    private Long id;
    private String planningClass;
    private String planta;
    private String tecnologia;
    private String tamanoMax;
    private String equipo;
    private String capacidadLote;
    private String capacidadLoteSemana;

    public EquiposAsignPorCriterio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanningClass() {
        return planningClass;
    }

    public void setPlanningClass(String planningClass) {
        this.planningClass = planningClass;
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

    public String getTamanoMax() {
        return tamanoMax;
    }

    public void setTamanoMax(String tamanoMax) {
        this.tamanoMax = tamanoMax;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getCapacidadLote() {
        return capacidadLote;
    }

    public void setCapacidadLote(String capacidadLote) {
        this.capacidadLote = capacidadLote;
    }

    public String getCapacidadLoteSemana() {
        return capacidadLoteSemana;
    }

    public void setCapacidadLoteSemana(String capacidadLoteSemana) {
        this.capacidadLoteSemana = capacidadLoteSemana;
    }

    @Override
    public String toString() {
        return "EquiposAsignPorCriterio{" +
        "id='" + id + '\'' +
        "planningClass='" + planningClass + '\'' +
        ", planta='" + planta + '\'' +
        ", tecnologia='" + tecnologia + '\'' +
        ", tamanoMax='" + tamanoMax + '\'' +
        ", equipo='" + equipo + '\'' +
        ", capacidadLote='" + capacidadLote + '\'' +
        ", capacidadLoteSemana='" + capacidadLoteSemana + '\'' +
        '}';
    }
}