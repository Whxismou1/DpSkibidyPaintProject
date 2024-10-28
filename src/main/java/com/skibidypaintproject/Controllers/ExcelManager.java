package com.skibidypaintproject.Controllers;

import com.skibidypaintproject.Entities.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class ExcelManager {

    public List<PlanProd> readExcelPlanProd(String path) {
        FileInputStream f = null;
        XSSFWorkbook libro = null;
        List<PlanProd> entradasExcel = new ArrayList<>();
        try {
            f = new FileInputStream(path);
            libro = new XSSFWorkbook(f);
            XSSFSheet hojaPlanProd = libro.getSheetAt(0);

            Row encabezado = hojaPlanProd.getRow(0);

            if (encabezado != null) {
                List<String> nombresColumnas = new ArrayList<>();
                Iterator<Cell> celdasEncabezado = encabezado.cellIterator();
                while (celdasEncabezado.hasNext()) {
                    Cell celda = celdasEncabezado.next();
                    nombresColumnas.add(celda.getStringCellValue());
                }

                Iterator<Row> filas = hojaPlanProd.iterator();
                filas.next(); // Saltar la primera fila (encabezado)
                Long actualId = 2L;
                while (filas.hasNext()) {
                    Row fila = filas.next();
                    Iterator<Cell> celdas = fila.cellIterator();
                    PlanProd planProd = new PlanProd();
                    planProd.setId(actualId);
                    // Recorremos las celdas de la fila
                    while (celdas.hasNext()) {
                        Cell celda = celdas.next();
                        int indiceCelda = celda.getColumnIndex();
                        String nombreColumna = nombresColumnas.get(indiceCelda);
                        asignarValorPlanProd(planProd, nombreColumna, celda);
                    }

                    // Agregar el contribuyente a la lista
                    entradasExcel.add(planProd);
                    actualId++;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (libro != null) {
                    libro.close();
                }
                if (f != null) {
                    f.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entradasExcel;
    }

    public List<EquiposAsignPorCriterio> readExcelEquiposAsignPorCriterio() {
        FileInputStream f = null;
        XSSFWorkbook libro = null;
        List<EquiposAsignPorCriterio> entradasExcel = new ArrayList<>();
        try {
            f = new FileInputStream("src/resources/Plan Producción PPG - Versión ULE.xlsx");
            libro = new XSSFWorkbook(f);
            XSSFSheet hojaEquipos = libro.getSheetAt(1);

            Row encabezado = hojaEquipos.getRow(3);

            if (encabezado != null) {
                List<String> nombresColumnas = new ArrayList<>();
                Iterator<Cell> celdasEncabezado = encabezado.cellIterator();
                while (celdasEncabezado.hasNext()) {
                    Cell celda = celdasEncabezado.next();
                    nombresColumnas.add(celda.getStringCellValue());
                }

                Iterator<Row> filas = hojaEquipos.iterator();
                filas.next(); // Saltar la primera fila (encabezado)
                Long actualId = 2L;
                while (filas.hasNext()) {
                    Row fila = filas.next();
                    Iterator<Cell> celdas = fila.cellIterator();
                    EquiposAsignPorCriterio equipos = new EquiposAsignPorCriterio();
                    equipos.setId(actualId);
                    // Recorremos las celdas de la fila
                    while (celdas.hasNext()) {
                        Cell celda = celdas.next();
                        int indiceCelda = celda.getColumnIndex();
                        String nombreColumna = nombresColumnas.get(indiceCelda);
                        asignarValorEquiposAsignPorCriterio(equipos, nombreColumna, celda);
                    }

                    // Agregar el contribuyente a la lista
                    entradasExcel.add(equipos);
                    actualId++;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (libro != null) {
                    libro.close();
                }
                if (f != null) {
                    f.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entradasExcel;
    }

    public List<EquiposCapacidad> readExcelEquiposCapacidad() {
        FileInputStream f = null;
        XSSFWorkbook libro = null;
        List<EquiposCapacidad> entradasExcel = new ArrayList<>();
        try {
            f = new FileInputStream("src/resources/Plan Producción PPG - Versión ULE.xlsx");
            libro = new XSSFWorkbook(f);
            XSSFSheet hojaEquipos = libro.getSheetAt(1);

            Row encabezado = hojaEquipos.getRow(3);

            if (encabezado != null) {
                List<String> nombresColumnas = new ArrayList<>();
                Iterator<Cell> celdasEncabezado = encabezado.cellIterator();
                while (celdasEncabezado.hasNext()) {
                    Cell celda = celdasEncabezado.next();
                    nombresColumnas.add(celda.getStringCellValue());
                }

                Iterator<Row> filas = hojaEquipos.iterator();
                filas.next(); // Saltar la primera fila (encabezado)
                Long actualId = 2L;
                while (filas.hasNext()) {
                    Row fila = filas.next();
                    Iterator<Cell> celdas = fila.cellIterator();
                    EquiposCapacidad equipos = new EquiposCapacidad();
                    equipos.setId(actualId);
                    // Recorremos las celdas de la fila
                    while (celdas.hasNext()) {
                        Cell celda = celdas.next();
                        int indiceCelda = celda.getColumnIndex();
                        String nombreColumna = nombresColumnas.get(indiceCelda);
                        asignarValorEquiposCapacidad(equipos, nombreColumna, celda);
                    }

                    // Agregar el contribuyente a la lista
                    entradasExcel.add(equipos);
                    actualId++;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (libro != null) {
                    libro.close();
                }
                if (f != null) {
                    f.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entradasExcel;
    }

    private String getCellValue(Cell celda) {
        if (celda == null) {
            return null;
        }

        switch (celda.getCellType()) {
            case STRING:
                String value = celda.getStringCellValue();
                return value.isEmpty() ? null : value;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(celda)) {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    return dateFormat.format(celda.getDateCellValue());
                } else {
                    return String.valueOf(celda.getNumericCellValue());
                }

            case BLANK:
                return null;
            default:
                return null;
        }

    }

    private void asignarValorPlanProd(PlanProd planProd, String nombreColumna, Cell celda) {
        switch (nombreColumna) {
            case "PPG_Planning_Class":
                planProd.setPpgPlanningClass(getCellValue(celda));
                break;
            case "Item":
                planProd.setItem(getCellValue(celda));
                break;
            case "PPG_Inventory_Class":
                planProd.setPpgInventoryClass(getCellValue(celda));
                break;
            case "Item_Description":
                planProd.setItemDescription(getCellValue(celda));
                break;
            case "Plant":
                planProd.setPlant(getCellValue(celda));
                break;
            case "Bx_Ref_":
                planProd.setBxRef(getCellValue(celda));
                break;
            case "Batch_Status":
                planProd.setBatchStatus(getCellValue(celda));
                break;
            case "Routing_Code":
                planProd.setRoutingCode(getCellValue(celda));
                break;
            case "Sales_order_number":
                planProd.setSalesOrderNumber(getCellValue(celda));
                break;
            case "Sales_Order_Line_Number":
                planProd.setSalesOrderLineNumber(getCellValue(celda));
                break;
            case "Customer_name":
                planProd.setCustomerName(getCellValue(celda));
                break;
            case "Customer_Batch_N_":
                planProd.setCustomerBatchNumber(getCellValue(celda));
                break;
            case "Planned_Quantity_Uom1":
                planProd.setPlannedQuantityUom1(getCellValue(celda));
                break;
            case "Uom1":
                planProd.setUom1(getCellValue(celda));
                break;
            case "Planned_Quantity_KG":
                planProd.setPlannedQuantityKg(getCellValue(celda));
                break;
            case "Bx_Start":
                planProd.setBxStart(getCellValue(celda));
                break;
            case "Bx_End":
                planProd.setBxEnd(getCellValue(celda));
                break;
            case "Required_Completion_Date":
                planProd.setRequiredCompletionDate(getCellValue(celda));
                break;
            case "Item_Type":
                planProd.setItemType(getCellValue(celda));
                break;
            case "Batch_Notes":
                planProd.setBatchNotes(getCellValue(celda));
                break;
            case "Item_Notes":
                planProd.setItemNotes(getCellValue(celda));
                break;
            case "PPG_Item_Strategic":
                planProd.setPpgItemStrategic(getCellValue(celda));
                break;
            case "PPG_Item_Fleet":
                planProd.setPpgItemFleet(getCellValue(celda));
                break;
            case "Standard_Prod_Time":
                planProd.setStandardProdTime(getCellValue(celda));
                break;
            case "Textbox8":
                planProd.setTextbox8(getCellValue(celda));
                break;
            case "Planned_Quantity_BULK":
                planProd.setPlannedQuantityBulk(getCellValue(celda));
                break;
            case "Count_Planned_Quantity_BULK":
                planProd.setCountPlannedQuantityBulk(getCellValue(celda));
                break;
            case "Planned_Quantity_FG":
                planProd.setPlannedQuantityFg(getCellValue(celda));
                break;
            case "Count_Planned_Quantity_FG":
                planProd.setCountPlannedQuantityFg(getCellValue(celda));
                break;
            case "Planned_Quantity_INT":
                planProd.setPlannedQuantityInt(getCellValue(celda));
                break;
            case "Count_Planned_Quantity_INT":
                planProd.setCountPlannedQuantityInt(getCellValue(celda));
                break;
            case "Planned_Quantity_TOTAL":
                planProd.setPlannedQuantityTotal(getCellValue(celda));
                break;
            case "Count_Planned_Quantity__TOTAL":
                planProd.setCountPlannedQuantityTotal(getCellValue(celda));
                break;
            case "Product_Line_Desc":
                planProd.setProductLineDesc(getCellValue(celda));
                break;
            case "Tech_Group_Desc":
                planProd.setTechGroupDesc(getCellValue(celda));
                break;
            case "Tech_Sub_Group_Desc":
                planProd.setTechSubGroupDesc(getCellValue(celda));
                break;
            case "PPG_Planning_Class_Description":
                planProd.setPpgPlanningClassDescription(getCellValue(celda));
                break;
            case "Formula_Code":
                planProd.setFormulaCode(getCellValue(celda));
                break;
            case "Formula_Version":
                planProd.setFormulaVersion(getCellValue(celda));
                break;
            case "Routing_Code_":
                planProd.setRoutingCode_(getCellValue(celda));
                break;
            case "Sales_order_number1":
                planProd.setSalesOrderNumber1(getCellValue(celda));
                break;
            case "Sales_Order_Line_Number_":
                planProd.setSalesOrderLineNumber_(getCellValue(celda));
                break;
            case "Customer_name_":
                planProd.setCustomerName_(getCellValue(celda));
                break;
            case "Planned_Quantity_Uom1_":
                planProd.setPlannedQuantityUom1_(getCellValue(celda));
                break;
            case "Uom1_":
                planProd.setUom1_(getCellValue(celda));
                break;
            case "Planned_Quantity_KG_":
                planProd.setPlannedQuantityKg_(getCellValue(celda));
                break;
            case "Bx_Start_":
                planProd.setBxStart_(getCellValue(celda));
                break;
            case "Bx_End_":
                planProd.setBxEnd_(getCellValue(celda));
                break;
            case "Required_Completion_Date_":
                planProd.setRequiredCompletionDate_(getCellValue(celda));
                break;
            case "Item_Type_":
                planProd.setItemType_(getCellValue(celda));
                break;
            case "Batch_Notes_":
                planProd.setBatchNotes_(getCellValue(celda));
                break;
            case "Item_Notes_":
                planProd.setItemNotes_(getCellValue(celda));
                break;
            case "PPG_Item_Strategic_":
                planProd.setPpgItemStrategic_(getCellValue(celda));
                break;
            case "PPG_Item_Fleet_":
                planProd.setPpgItemFleet_(getCellValue(celda));
                break;
            case "Standard_Prod_Time_":
                planProd.setStandardProdTime_(getCellValue(celda));
                break;
            default:
                System.out.println("Columna no reconocida: " + nombreColumna);
                break;
        }
    }

    private void asignarValorEquiposAsignPorCriterio(EquiposAsignPorCriterio equipos, String nombreColumna,
            Cell celda) {
        switch (nombreColumna) {
            case "Planning Class":
                equipos.setPlanningClass(getCellValue(celda));
                break;
            case "Planta":
                equipos.setPlanta(getCellValue(celda));
                break;
            case "Tecnología":
                equipos.setTecnologia(getCellValue(celda));
                break;
            case "Tamaño Max":
                equipos.setTamanoMax(getCellValue(celda));
                break;
            case "Equipo":
                equipos.setEquipo(getCellValue(celda));
                break;
            case "Capacidad(Lote)":
                equipos.setCapacidadLote(getCellValue(celda));
                break;
            case "Capacidad (lote/semana)":
                equipos.setCapacidadLoteSemana(getCellValue(celda));
                break;
            default:
                System.out.println("Columna no reconocida: " + nombreColumna);
                break;
        }
    }

    private void asignarValorEquiposCapacidad(EquiposCapacidad equipos, String nombreColumna, Cell celda) {
        switch (nombreColumna) {
            case "Etiquetas de fila":
                equipos.setEtiquetasFila(getCellValue(celda));
                break;
            case "Máx. de Capacidad (Lote/día)":
                equipos.setMaxCapacidadLoteDia(getCellValue(celda));
                break;
            case "Máx. de Capacidad (lote/semana)":
                equipos.setMaxCapacidadLoteSemana(getCellValue(celda));
                break;
            case "Nº Equipos":
                equipos.setNumeroEquipos(Integer.parseInt(getCellValue(celda)));
                break;
            default:
                System.out.println("Columna no reconocida: " + nombreColumna);
                break;
        }
    }
}
