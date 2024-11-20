package com.skibidypaintproject.Controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.Desktop;

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
            logger.info("Archivo origen seleccionado: " + archivoOrigen.getPath());
        }
        System.out.println("Archivo origen seleccionado");
    }

    @FXML
    private void planificar() {
        if (archivoOrigen != null) {
            List<PlanProd> plan = new ArrayList<PlanProd>();
            plan = excelManager.readExcelPlanProd(archivoOrigen.getPath());
            logger.info("Planificaciones cargadas correctamente: " + plan.size());

            listaEquipos = pcDAO.obtenerEquipos();
            logger.info("Equipos cargados correctamente: " + listaEquipos.size());
            listaPlaningClasses = pcDAO.obtenerPlaningClasses();

            // for (PlaningClass planProd : listaPlaningClasses) {
            // System.out.println(planProd.toString());
            // break;
            // }

            for (PlaningClass pc : listaPlaningClasses) {
                if (pc.getTecnologia() == null) {
                    pc.setTecnologia("");
                }
            }
            logger.info("Planning classes cargadas correctamente: " + listaPlaningClasses.size());

            System.out.println("Planificaciones leídas:" + plan.size());
            System.out.println("Equipos leídos:" + listaEquipos.size());
            System.out.println("PlaningClasses leídas:" + listaPlaningClasses.size());

            List<PlanProd> planificacionOptima = generarMejorPlanificacion2(plan,
                    listaPlaningClasses, listaEquipos);
            Workbook wb = new XSSFWorkbook();
            excelManager.ganttExcel(planificacionOptima, wb);
            excelManager.writeExcel(planificacionOptima, wb);
            excelManager.saveWorkbook(wb, "src/main/resources/PlanificacionOptima.xlsx");
            System.out.println("Planificación óptima:" + planificacionOptima.size());
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


    public List<PlanProd> generarMejorPlanificacion2(List<PlanProd> planificaciones, List<PlaningClass> planingClasses,
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

    /**
     * Método
     * que genera la mejor planificación de producción en función de la capacidad
     * real de
     * los equipos
     */

    public List<PlanProd> generarMejorPlanificacions(List<PlanProd> planificaciones, List<PlaningClass> planingClasses,
            List<Equipo> equipos) {

        // Ordenar por fecha de finalización requerida
        planificaciones.sort(Comparator.comparing(PlanProd::getRequiredCompletionDate));

        List<PlanProd> planificacionOptima = new ArrayList<>();

        for (PlanProd planProd : planificaciones) {
            Optional<PlaningClass> planingClassOpt = planingClasses.stream()
                    .filter(pc -> pc.getPlaningClass().equals(planProd.getPpgPlanningClass()))
                    .findFirst();

            if (planingClassOpt.isPresent()) {
                PlaningClass planingClass = planingClassOpt.get();

                // Equipos compatibles según el tipo de equipo y capacidad en kilos
                List<Equipo> equiposCompatibles = equipos.stream()
                        .filter(equipo -> equipo.getEtiquetasDeFila().equals(planingClass.getEquipo()) &&
                                planingClass.getTamanoMax() >= planProd.getPlannedQuantityUom1()) // Compatibilidad en
                                                                                                  // kilos
                        .collect(Collectors.toList());

                if (!equiposCompatibles.isEmpty()) {
                    System.out.println("Encontrado equipo compatible para: " +
                            planProd.getItem());

                    double cantidadRestante = planProd.getPlannedQuantityUom1(); // Usar cantidad en kilos
                    for (Equipo equipo : equiposCompatibles) {
                        // Calcular producción diaria y semanal en función del número de equipos y
                        // capacidad en kilos
                        double produccionDiaria = equipo.getMaxCapacidadLoteDia() *
                                planingClass.getTamanoMax() * equipo.getNumeroEquipos();
                        double produccionSemanal = equipo.getMaxCapacidadLoteSemana() *
                                planingClass.getTamanoMax() * equipo.getNumeroEquipos();

                        // Verificar si la cantidad restante se puede cubrir con la producción semanal
                        if (cantidadRestante <= produccionSemanal) {
                            int diasNecesarios = (int) Math.ceil(cantidadRestante / produccionDiaria);

                            if (diasNecesarios <= 7) {
                                // Se puede completar dentro de la semana
                                planificacionOptima.add(planProd);
                                cantidadRestante = 0;
                                break;
                            } else {
                                // Agregar producción parcial para esta semana y continuar con la cantidad
                                // restante
                                planificacionOptima.add(planProd);
                                cantidadRestante -= produccionSemanal;
                            }
                        }
                    }

                    // Si no se pudo completar la cantidad restante
                    if (cantidadRestante > 0) {
                        System.out.println("La planificación de " + planProd.getItem()
                                + " no se pudo completar con los equipos disponibles.");
                    }
                }
            }
        }
        return planificacionOptima;
    }

    @FXML
    private void initialize(){
        margenPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1));
    }
}
/**
 * Método que genera la mejor planificación de producción en función de la fecha
 * y ocupada
 */

// import java.time.LocalDate;

// public List<PlanProd> generarMejorPlanificacion(List<PlanProd>
// planificaciones, List<PlaningClass> planingClasses,
// List<Equipo> equipos) {

// // Ordenar planificaciones por fecha de finalización requerida
// planificaciones.sort(Comparator.comparing(PlanProd::getRequiredCompletionDate));

// List<PlanProd> planificacionOptima = new ArrayList<>();

// for (PlanProd planProd : planificaciones) {
// Optional<PlaningClass> planingClassOpt = planingClasses.stream()
// .filter(pc -> pc.getPlaningClass().equals(planProd.getPpgPlanningClass()))
// .findFirst();

// if (planingClassOpt.isPresent()) {
// PlaningClass planingClass = planingClassOpt.get();

// // Equipos compatibles según el tipo de equipo y capacidad en kilos
// List<Equipo> equiposCompatibles = equipos.stream()
// .filter(equipo ->
// equipo.getEtiquetasDeFila().equals(planingClass.getEquipo()) &&
// planingClass.getTamañoMax() >= planProd.getPlannedQuantityKg()) //
// Compatibilidad en kilos
// .collect(Collectors.toList());

// if (!equiposCompatibles.isEmpty()) {
// double cantidadRestante = planProd.getPlannedQuantityKg();

// for (Equipo equipo : equiposCompatibles) {
// double produccionDiaria = equipo.getMaxCapacidadLoteDia() *
// planingClass.getTamañoMax() * equipo.getNumeroEquipos();
// double produccionSemanal = equipo.getMaxCapacidadLoteSemana() *
// planingClass.getTamañoMax() * equipo.getNumeroEquipos();

// // Verificar si la cantidad restante se puede cubrir con la producción
// semanal
// if (cantidadRestante <= produccionSemanal) {
// int diasNecesarios = (int) Math.ceil(cantidadRestante / produccionDiaria);

// // Comprobar si el equipo está disponible en los próximos `diasNecesarios`
// días
// LocalDate fechaInicio =
// planProd.getRequiredCompletionDate().minusDays(diasNecesarios);
// boolean equipoDisponible = true;

// for (int i = 0; i < diasNecesarios; i++) {
// LocalDate dia = fechaInicio.plusDays(i);
// if (equipo.isOcupado(dia)) { // Método que verifica si el equipo está ocupado
// en esa fecha
// equipoDisponible = false;
// break;
// }
// }

// if (equipoDisponible) {
// // Agregar la planificación a la lista óptima
// planificacionOptima.add(planProd);

// // Registrar la ocupación del equipo para los días requeridos
// for (int i = 0; i < diasNecesarios; i++) {
// equipo.setOcupado(fechaInicio.plusDays(i)); // Método que marca el equipo
// como ocupado
// }

// cantidadRestante = 0;
// break;
// } else {
// System.out.println("Equipo no disponible para " + planProd.getItem());
// }
// }
// }

// if (cantidadRestante > 0) {
// System.out.println("No se pudo completar la planificación para " +
// planProd.getItem() + " debido a la disponibilidad de los equipos.");
// }
// }
// }
// }
// return planificacionOptima;
// }

/**
 * Método que genera la mejor planificación de producción en función de las
 * batch
 */

// import java.time.LocalDate;
// import java.util.HashMap;
// import java.util.Map;

// public List<PlanProd> generarMejorPlanificacion(List<PlanProd>
// planificaciones, List<PlaningClass> planingClasses,
// List<Equipo> equipos) {

// // Ordenar planificaciones por fecha de finalización requerida y prioridad en
// Batch_Status
// planificaciones.sort(Comparator.comparing(PlanProd::getRequiredCompletionDate)
// .thenComparing(plan -> !plan.getBatchStatus().equalsIgnoreCase("WIP") &&
// !plan.getBatchStatus().equalsIgnoreCase("Pending")));

// List<PlanProd> planificacionOptima = new ArrayList<>();

// for (PlanProd planProd : planificaciones) {
// Optional<PlaningClass> planingClassOpt = planingClasses.stream()
// .filter(pc -> pc.getPlaningClass().equals(planProd.getPpgPlanningClass()))
// .findFirst();

// if (planingClassOpt.isPresent()) {
// PlaningClass planingClass = planingClassOpt.get();

// List<Equipo> equiposCompatibles = equipos.stream()
// .filter(equipo ->
// equipo.getEtiquetasDeFila().equals(planingClass.getEquipo()) &&
// planingClass.getTamañoMax() >= planProd.getPlannedQuantityKg())
// .collect(Collectors.toList());

// if (!equiposCompatibles.isEmpty()) {
// double cantidadRestante = planProd.getPlannedQuantityKg();

// for (Equipo equipo : equiposCompatibles) {
// double produccionDiaria = equipo.getMaxCapacidadLoteDia() *
// planingClass.getTamañoMax() * equipo.getNumeroEquipos();
// double produccionSemanal = equipo.getMaxCapacidadLoteSemana() *
// planingClass.getTamañoMax() * equipo.getNumeroEquipos();

// if (cantidadRestante <= produccionSemanal) {
// int diasNecesarios = (int) Math.ceil(cantidadRestante / produccionDiaria);

// // Verificar disponibilidad en los días requeridos
// LocalDate fechaInicio =
// planProd.getRequiredCompletionDate().minusDays(diasNecesarios);
// boolean equipoDisponible = true;

// for (int i = 0; i < diasNecesarios; i++) {
// LocalDate dia = fechaInicio.plusDays(i);
// if (equipo.isOcupado(dia)) {
// equipoDisponible = false;
// break;
// }
// }

// if (equipoDisponible) {
// planificacionOptima.add(planProd);

// // Registrar la ocupación para los días necesarios
// for (int i = 0; i < diasNecesarios; i++) {
// equipo.setOcupado(fechaInicio.plusDays(i));
// }
// cantidadRestante = 0;
// break;
// } else {
// System.out.println("Equipo no disponible para " + planProd.getItem() + " con
// Batch_Status: " + planProd.getBatchStatus());
// }
// }
// }

// if (cantidadRestante > 0) {
// if (planProd.getBatchStatus().equalsIgnoreCase("Pending")) {
// System.out.println("No se pudo completar la planificación para " +
// planProd.getItem() + " debido a la disponibilidad de los equipos.
// Batch_Status: Pending");
// } else {
// System.out.println("Replanificando producción de menor prioridad para " +
// planProd.getItem());
// }
// }
// }
// }
// }
// return planificacionOptima;
// }
