package com.skibidypaintproject.Controllers;

import java.io.File;
import com.skibidypaintproject.Utils.AlertUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;

public class PlanificadorController {
    @FXML
    private Button ArchivoOrigenButton;
    @FXML
    private Button Planificador;
    @FXML
    private Label NombreArchivo;

    private File archivoOrigen;

    private ExcelManager excelManager = new ExcelManager();

    @FXML
    private void selectArchivoOrigen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo origen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx"));
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
            // TODO: logica de planificación
            System.out.println(excelManager.readExcelPlanProd(archivoOrigen.getPath()));
            AlertUtil.showAlert("Éxito", "Planificación realizada", null);
        } else if (archivoOrigen == null) {
            AlertUtil.showAlert("Error", "No se ha seleccionado ningun archivo para planificar", null);
        } else {
            AlertUtil.showAlert("Error", "No se pudo realizar la planificación", null);
        }
    }
}
