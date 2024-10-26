
package com.skibidypaintproject.Entities;

public class EquiposCapacidad {

    private Long id;
    private String etiquetasFila;
    private String maxCapacidadLoteDia;
    private String maxCapacidadLoteSemana;
    private int numeroEquipos;

    public EquiposCapacidad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtiquetasFila() {
        return etiquetasFila;
    }

    public void setEtiquetasFila(String etiquetasFila) {
        this.etiquetasFila = etiquetasFila;
    }

    public String getMaxCapacidadLoteDia() {
        return maxCapacidadLoteDia;
    }

    public void setMaxCapacidadLoteDia(String maxCapacidadLoteDia) {
        this.maxCapacidadLoteDia = maxCapacidadLoteDia;
    }

    public String getMaxCapacidadLoteSemana() {
        return maxCapacidadLoteSemana;
    }

    public void setMaxCapacidadLoteSemana(String maxCapacidadLoteSemana) {
        this.maxCapacidadLoteSemana = maxCapacidadLoteSemana;
    }

    public int getNumeroEquipos() {
        return numeroEquipos;
    }

    public void setNumeroEquipos(int numeroEquipos) {
        this.numeroEquipos = numeroEquipos;
    }

    // Método toString

    @Override
    public String toString() {
        return "EquiposCapacidad{" +
            "id='" + id + '\'' +
            "etiquetasFila='" + etiquetasFila + '\'' +
            ", maxCapacidadLoteDia='" + maxCapacidadLoteDia + '\'' +
            ", maxCapacidadLoteSemana='" + maxCapacidadLoteSemana + '\'' +
            ", numeroEquipos=" + numeroEquipos +
            '}';
    }
}