package com.skibidypaintproject.Controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.Desktop;

import com.skibidypaintproject.App;
import com.skibidypaintproject.Daos.PlanningClassDAO;
import com.skibidypaintproject.Entities.Equipo;
import com.skibidypaintproject.Entities.PlanProd;
import com.skibidypaintproject.Entities.PlaningClass;
import com.skibidypaintproject.Utils.AlertUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
    @FXML
    private Spinner<Integer> margenPicker;
    private File archivoOrigen;

    private static final Logger logger = LogManager.getLogger(PlanificadorController.class);
    private ExcelManager excelManager = new ExcelManager();

    private PlanningClassDAO pcDAO = new PlanningClassDAO();

    private List<Equipo> listaEquipos = new ArrayList<Equipo>();

    private List<PlaningClass> listaPlaningClasses = new ArrayList<PlaningClass>();

    @FXML
    private void logOut() throws Throwable{
        logger.info("Logging out");
        App.setRoot("login");
    }

    @FXML
    public void initialize() {
        // Configura el Spinner con un rango y un valor por defecto
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0);
        margenPicker.setValueFactory(valueFactory);
    }


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
            logger.error("The origin file could not be selected");
        } else {
            AlertUtil.showAlert("Éxito", "Archivo origen seleccionado", ArchivoOrigenButton.getScene().getWindow());
            NombreArchivo.setText("Archivo seleccionado: " + archivoOrigen.getName());
            logger.info("The origin file was selected: " + archivoOrigen.getPath());
            System.out.println("Archivo origen seleccionado");
        }
    }

    @FXML
    private void planificar() {
        if (archivoOrigen != null) {
            List<PlanProd> plan = new ArrayList<PlanProd>();
            plan = excelManager.readExcelPlanProd(archivoOrigen.getPath());
            logger.info("Plannifications read correctly: " + plan.size());
            listaEquipos = pcDAO.obtenerEquipos();
            logger.info("Equipments read correctly: " + listaEquipos.size());
            listaPlaningClasses = pcDAO.obtenerPlaningClasses();


            for (PlaningClass pc : listaPlaningClasses) {
                if (pc.getTecnologia() == null) {
                    pc.setTecnologia("");
                }
            }
            logger.info("PlanningClasses read and preproccess correctly: " + listaPlaningClasses.size());

            // System.out.println("Planificaciones leídas:" + plan.size());
            // System.out.println("Equipos leídos:" + listaEquipos.size());
            // System.out.println("PlaningClasses leídas:" + listaPlaningClasses.size());
            logger.info("Starting optimal planning generation");
            System.out.println("Generando planificación óptima...");
            List<PlanProd> planificacionOptima = generarMejorPlanificacion(plan,
                    listaPlaningClasses, listaEquipos);
            logger.info("Optimal planning generated: " + planificacionOptima.size());

            logger.info("Starting to write the optimal planning to the excel file");
            System.out.println("Guardando planificación óptima...");
            Workbook wb = new XSSFWorkbook();
            excelManager.ganttExcel(planificacionOptima, wb);
            excelManager.writeExcel(planificacionOptima, wb);
            excelManager.saveWorkbook(wb, "src/main/resources/PlanificacionOptima.xlsx");
            logger.info("Optimal planning written to the excel file");
            System.out.println("Planificación óptima guardada");
            if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Abre el archivo con la aplicación predeterminada
                desktop.open(new File("src/main/resources/PlanificacionOptima.xlsx"));
            } catch (IOException e) {
                System.err.println("No se pudo abrir el archivo: " + e.getMessage());
            }
            
        } 

            AlertUtil.showAlert("Éxito", "Planificación realizada", null);
        } else if (archivoOrigen == null) {
            AlertUtil.showAlert("Error", "No se ha seleccionado ningun archivo para planificar", null);
            logger.error("No origin file selected");
        } else {
            AlertUtil.showAlert("Error", "No se pudo realizar la planificación", null);
            logger.error("Error generating planning");
        }
    }

    /**
     * Clase auxiliar para manejar intervalos de fechas en los planes de producción
     */
    private static class Intervalo {
        LocalDate inicio;
        LocalDate fin;

        Intervalo(LocalDate inicio, LocalDate fin) {
            this.inicio = inicio;
            this.fin = fin;
        }

        boolean seSolapa(Intervalo otro) {
            return !inicio.isAfter(otro.fin) && !fin.isBefore(otro.inicio);
        }
    }


    public List<PlanProd> generarMejorPlanificacion(List<PlanProd> planificaciones, List<PlaningClass> planingClasses,
            List<Equipo> equipos) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int margen=margenPicker.getValue();
        // Se preprocesan los equipos para tener una lista con la cantidad de
        // equipos reales
        List<Equipo> equiposPreproces = new ArrayList<>();
        for (Equipo equipo : equipos) {
            for (int i = 0; i < equipo.getNumeroEquipos(); i++) {
                equiposPreproces.add(equipo);
            }
        }

        // Lista para almacenar la planificación óptima
        List<PlanProd> planificacionOptima = new ArrayList<>();

        // Se ordenan las planificaciones por fecha de finalización requerida
        planificaciones.sort(Comparator.comparing(PlanProd::getRequiredCompletionDate));

        // Counter maquinas ocupadas
        HashMap<String, List<Intervalo>> ocupacionEquipos = new HashMap<String, List<Intervalo>>();

        // Se itera sobre cada plan de producción
        for (PlanProd planProdActual : planificaciones) {
            int cantidadKgProd = planProdActual.getPlannedQuantityUom1();
            String planningClassActual = planProdActual.getPpgPlanningClass();
            String plantaActual = planProdActual.getPlant();

            // Determinar la tecnología del equipo a partir del Routing_Code
            String typeMaterial = planProdActual.getRoutingCode().endsWith("C") ? "Opaco" : "Metalico";

            List<Equipo> equiposCompatibles = equiposPreproces.stream()
                    .filter(eq -> {
                        Optional<PlaningClass> planningClassOpt = planingClasses.stream()
                                .filter(pc -> pc.getPlaningClass().equals(planningClassActual) &&
                                        pc.getPlanta().equals(plantaActual) &&
                                        pc.getTecnologia().equals(typeMaterial))
                                .findFirst();
                        return planningClassOpt.isPresent()
                                && planningClassOpt.get().getEquipo().equals(eq.getEtiquetasDeFila());
                    }).collect(Collectors.toList());

            for (Equipo equipoActual : equiposCompatibles) {
                int tamanioMax = planingClasses.stream()
                        .filter(pc -> pc.getPlaningClass().equals(planningClassActual) &&
                                pc.getPlanta().equals(plantaActual) &&
                                pc.getTecnologia().equals(typeMaterial) &&
                                pc.getEquipo().equals(equipoActual.getEtiquetasDeFila()))
                        .findFirst().get().getTamanoMax();

                int numLotesRequeridos = (int) Math.ceil((double) cantidadKgProd / tamanioMax);

                double capacidadLotDia = equipoActual.getMaxCapacidadLoteDia();
                double capacidadLotSemana = equipoActual.getMaxCapacidadLoteSemana();

                int diasNecesarios = (int) Math.ceil(numLotesRequeridos / capacidadLotDia);
                int semanasNecesarias = (int) Math.ceil(numLotesRequeridos / capacidadLotSemana);

                LocalDate fechaInicio = LocalDate.parse(planProdActual.getRequiredCompletionDate(), formatter)
                        .minusDays(diasNecesarios+margen);
                LocalDate fechaFin = fechaInicio.plusDays(diasNecesarios);
                boolean equipoDisponible = true;

                Intervalo nuevoIntervalo = new Intervalo(fechaInicio, fechaFin);
                List<Intervalo> intervalosOcupados = ocupacionEquipos.getOrDefault(equipoActual, new ArrayList<>());

                boolean seSolapa = intervalosOcupados.stream().anyMatch(intervalo -> intervalo.seSolapa(nuevoIntervalo));

                if (!seSolapa) {
                    intervalosOcupados.add(nuevoIntervalo);
                    ocupacionEquipos.put(equipoActual.getEtiquetasDeFila(), intervalosOcupados);

                    planProdActual.setBxStart(fechaInicio.format(formatter));
                    planProdActual.setBxEnd(fechaFin.format(formatter));

                    planificacionOptima.add(planProdActual);
                    break;
                }

            }
        }
        logger.info("Planificación óptima generada");
        return planificacionOptima;
    }

}