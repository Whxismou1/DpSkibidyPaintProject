package com.skibidypaintproject.Entities;

public class SpecialClasses {

    private int id;
    private String itemCode;
    private int tamanoMax;
    private String equipo;
    private int standar;

    public SpecialClasses() {
    }

    public SpecialClasses(String itemCode, int tamanoMax, String equipo, int standar) {
        this.itemCode = itemCode;
        this.tamanoMax = tamanoMax;
        this.equipo = equipo;
        this.standar = standar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public int getStandar() {
        return standar;
    }

    public void setStandar(int standar) {
        this.standar = standar;
    }

    @Override
    public String toString() {
        return "SpecialClasses{" +
                "id=" + id +
                ", itemCode='" + itemCode + '\'' +
                ", tamanoMax=" + tamanoMax +
                ", equipo='" + equipo + '\'' +
                ", standar=" + standar +
                '}';
    }
}
