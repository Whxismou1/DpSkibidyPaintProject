package com.skibidypaintproject.Controllers;

import java.io.File;

import com.skibidypaintproject.Daos.PlanningClassDAO;
import com.skibidypaintproject.Entities.Equipo;
import com.skibidypaintproject.Entities.PlanProd;
import com.skibidypaintproject.Entities.PlaningClass;
import com.skibidypaintproject.Utils.AlertUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class PlanificadorController {
    @FXML
    private Button ArchivoOrigenButton;
    @FXML
    private Button Planificador;
    @FXML
    private Label NombreArchivo;
    @FXML
    private TextArea AreaElems;
    private File archivoOrigen;

    private ExcelManager excelManager = new ExcelManager();

    private PlanningClassDAO pcDAO = new PlanningClassDAO();

    private List<Equipo> listaEquipos = new ArrayList<Equipo>();

    private List<PlaningClass> listaPlaningClasses = new ArrayList<PlaningClass>();

    @FXML
    private void selectArchivoOrigen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo origen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        archivoOrigen = fileChooser.showOpenDialog(ArchivoOrigenButton.getScene().getWindow());
        if (archivoOrigen == null) {
            AlertUtil.showAlert("Error", "No se pudo seleccionar el archivo origen",
                    ArchivoOrigenButton.getScene().getWindow());
        } else {
            AlertUtil.showAlert("Éxito", "Archivo origen seleccionado", ArchivoOrigenButton.getScene().getWindow());
            NombreArchivo.setText("Archivo seleccionado: " + archivoOrigen.getName());
        }
        System.out.println("Archivo origen seleccionado");
    }

    @FXML
    private void planificar() {
        if (archivoOrigen != null) {
            List<PlanProd> plan = new ArrayList<PlanProd>();
            plan = excelManager.readExcelPlanProd(archivoOrigen.getPath());

            listaEquipos = pcDAO.obtenerEquipos();
            listaPlaningClasses = pcDAO.obtenerPlaningClasses();

            System.out.println("Planificaciones leídas:" + plan.size());
            System.out.println("Equipos leídos:" + listaEquipos.size());
            System.out.println("PlaningClasses leídas:" + listaPlaningClasses.size());
            List<PlanProd> planificacionOptima = generarMejorPlanificacion(plan,
                    listaPlaningClasses, listaEquipos);

            System.out.println("Planificación óptima:" + planificacionOptima.size());

            AlertUtil.showAlert("Éxito", "Planificación realizada", null);
        } else if (archivoOrigen == null) {
            AlertUtil.showAlert("Error", "No se ha seleccionado ningun archivo para planificar", null);
        } else {
            AlertUtil.showAlert("Error", "No se pudo realizar la planificación", null);
        }
    }

    public List<PlanProd> generarMejorPlanificacion(List<PlanProd> planificaciones, List<PlaningClass> planingClasses,
            List<Equipo> equipos) {

        planificaciones.sort(Comparator.comparing(PlanProd::getRequiredCompletionDate));

        List<PlanProd> planificacionOptima = new ArrayList<>();

        for (PlanProd planProd : planificaciones) {
            Optional<PlaningClass> planingClassOpt = planingClasses.stream()
                    .filter(pc -> pc.getPlaningClass().equals(planProd.getPpgPlanningClass()))
                    .findFirst();

            if (planingClassOpt.isPresent()) {
                PlaningClass planingClass = planingClassOpt.get();
                List<Equipo> equiposCompatibles = equipos.stream()
                        .filter(equipo -> equipo.getEtiquetasDeFila().equals(planingClass.getEquipo()) &&
                                equipo.getMaxCapacidadLoteDia() >= planProd.getPlannedQuantityUom1())
                        .collect(Collectors.toList());

                if (!equiposCompatibles.isEmpty()) {
                    System.out.println("aaaaaaa");
                    double cantidadRestante = planProd.getPlannedQuantityUom1();
                    for (Equipo equipo : equiposCompatibles) {
                        double produccionDiaria = equipo.getMaxCapacidadLoteDia() * equipo.getNumeroEquipos();
                        double produccionSemanal = equipo.getMaxCapacidadLoteSemana() * equipo.getNumeroEquipos();

                        if (cantidadRestante <= produccionSemanal) {
                            int diasNecesarios = (int) Math.ceil(cantidadRestante / produccionDiaria);
                            if (diasNecesarios <= 7) {

                                planificacionOptima.add(planProd);
                                cantidadRestante = 0;
                                break;
                            } else {

                                planificacionOptima.add(planProd);
                                cantidadRestante -= produccionSemanal;
                            }
                        }
                    }
                    if (cantidadRestante > 0) {
                        System.out.println("La planificación de " + planProd.getItem()
                                + " no se pudo completar con los equipos disponibles.");
                    }
                }
            }
        }
        return planificacionOptima;
    }
}
