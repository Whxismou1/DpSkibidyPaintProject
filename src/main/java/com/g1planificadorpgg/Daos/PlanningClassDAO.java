package com.g1planificadorpgg.Daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.g1planificadorpgg.Controllers.BBDDController;
import com.g1planificadorpgg.Entities.Equipo;
import com.g1planificadorpgg.Entities.PlaningClass;
import com.g1planificadorpgg.Entities.SpecialClasses;

public class PlanningClassDAO {

    private final Connection connection;
    private static final Logger logger = LogManager.getLogger(UserDAO.class);

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
            logger.warn("Error obteniendo los equipos de la base de datos");
            e.printStackTrace();
        }
        logger.info("Equipos obtenidos correctamente de la base de datos");
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
            logger.warn("Error obteniendo las planing classes de la base de datos");
            e.printStackTrace();
        }
        logger.info("Planng classes obtenidas correctamente de la base de datos");
        return planingClasses;
    }

    public List<SpecialClasses> obtenerSpecialClasses() {
        List<SpecialClasses> specialClasses = new ArrayList<>();
        String query = "SELECT * FROM special_classes";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                SpecialClasses specialClass = new SpecialClasses();
                specialClass.setId(rs.getInt("id"));
                specialClass.setItemCode(rs.getString("item_code"));
                specialClass.setTamanoMax(rs.getInt("tamano_max"));
                specialClass.setEquipo(rs.getString("equipo"));
                specialClass.setStandar(rs.getInt("standar"));
                specialClasses.add(specialClass);
            }
        } catch (Exception e) {
            logger.warn("Error obteniendo las special classes de la base de datos");
            e.printStackTrace();
        }
        logger.info("Special classes obtenidas correctamente de la base de datos");
        return specialClasses;
    }
}
