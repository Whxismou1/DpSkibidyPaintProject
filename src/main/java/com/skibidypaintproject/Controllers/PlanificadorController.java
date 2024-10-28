package com.skibidypaintproject.Controllers;

import java.io.File;

import com.skibidypaintproject.Entities.PlanProd;
import com.skibidypaintproject.Utils.AlertUtil;
import java.util.List;
import java.util.ArrayList;


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
            // TODO: logica de planificación
            List <PlanProd> plan = new ArrayList<PlanProd>();
            plan = excelManager.readExcelPlanProd(archivoOrigen.getPath());
            for (PlanProd p : plan) {
                AreaElems.appendText(p.toString() + "\n");
            }
            AlertUtil.showAlert("Éxito", "Planificación realizada", null);
        } else if (archivoOrigen == null) {
            AlertUtil.showAlert("Error", "No se ha seleccionado ningun archivo para planificar", null);
        } else {
            AlertUtil.showAlert("Error", "No se pudo realizar la planificación", null);
        }
    }
}
