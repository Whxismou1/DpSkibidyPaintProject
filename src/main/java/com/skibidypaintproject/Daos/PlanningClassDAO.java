package com.skibidypaintproject.Daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.skibidypaintproject.Controllers.BBDDController;
import com.skibidypaintproject.Entities.Equipo;
import com.skibidypaintproject.Entities.PlaningClass;

public class PlanningClassDAO {

    private final Connection connection;

    public PlanningClassDAO() {
        this.connection = BBDDController.getInstance().getConnection();
    }

    public List<Equipo> obtenerEquipos() {
        List<Equipo> equipos = new ArrayList<>();
        String query = "SELECT * FROM equipos";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Equipo equipo = new Equipo();
                equipo.setId(rs.getInt("id"));
                equipo.setEtiquetasDeFila(rs.getString("etiquetas_de_fila"));
                equipo.setMaxCapacidadLoteDia(rs.getDouble("max_capacidad_lote_dia"));
                equipo.setMaxCapacidadLoteSemana(rs.getDouble("max_capacidad_lote_semana"));
                equipo.setNumeroEquipos(rs.getInt("numero_equipos"));
                equipos.add(equipo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return equipos;
    }

    public List<PlaningClass> obtenerPlaningClasses() {
        List<PlaningClass> planingClasses = new ArrayList<>();
        String query = "SELECT * FROM planning_classes";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                PlaningClass planingClass = new PlaningClass();
                planingClass.setId(rs.getInt("id"));
                planingClass.setPlaningClass(rs.getString("planning_class"));
                planingClass.setPlanta(rs.getString("planta"));
                planingClass.setTecnologia(rs.getString("tecnologia"));
                planingClass.setTamanoMax(rs.getInt("tamano_max"));
                planingClass.setEquipo(rs.getString("equipo"));
                planingClass.setCapacidadLoteDia(rs.getFloat("capacidad_lote_dia"));
                planingClass.setCapacidadLoteSemana(rs.getFloat("capacidad_lote_semana"));
                planingClasses.add(planingClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planingClasses;
    }
}
