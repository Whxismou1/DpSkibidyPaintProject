package com.skibidypaintproject.Controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.skibidypaintproject.Entities.EquiposAsignPorCriterio;
import com.skibidypaintproject.Entities.EquiposCapacidad;
import com.skibidypaintproject.Entities.PlanProd;

/**
 * Clase para gestionar operaciones relacionadas con la lectura y escritura de
 * archivos Excel.
 * Utiliza Apache POI para trabajar con archivos Excel (.xlsx).
 */
public class ExcelManager {

    Logger logger = LogManager.getLogger(ExcelManager.class);

    /**
     * Lee datos del archivo Excel y los mapea a una lista de objetos PlanProd.
     *
     * @param path Ruta del archivo Excel.
     * @return Lista de objetos PlanProd leídos del archivo.
     * @throws IOException           Si ocurre un error al leer o procesar el
     *                               archivo Excel.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     * @see PlanProd
     */
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

    /**
     * Lee datos del archivo Excel y los mapea a una lista de objetos
     * EquiposAsignPorCriterio.
     *
     * @param filePath Ruta del archivo Excel.
     * @return Lista de objetos EquiposAsignPorCriterio leídos del archivo.
     * @throws IOException           Si ocurre un error al leer o procesar el
     *                               archivo Excel.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     * @see EquiposAsignPorCriterio
     */
    public List<EquiposAsignPorCriterio> readExcelEquiposAsignPorCriterio(String filePath) {
        FileInputStream f = null;
        XSSFWorkbook libro = null;
        List<EquiposAsignPorCriterio> entradasExcel = new ArrayList<>();
        try {
            f = new FileInputStream(filePath);
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

    /**
     * Lee datos del archivo Excel y los mapea a una lista de objetos
     * EquiposCapacidad.
     *
     * @param filePath Ruta del archivo Excel.
     * @return Lista de objetos EquiposCapacidad leídos del archivo.
     * @throws IOException           Si ocurre un error al leer o procesar el
     *                               archivo Excel.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     * @see EquiposCapacidad
     */
    public List<EquiposCapacidad> readExcelEquiposCapacidad(String filePath) {
        FileInputStream f = null;
        XSSFWorkbook libro = null;
        List<EquiposCapacidad> entradasExcel = new ArrayList<>();
        try {
            f = new FileInputStream(filePath);
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

    /**
     * Obtiene el valor de una celda en formato String, manejando diferentes tipos
     * de datos.
     *
     * @param celda Celda a procesar.
     * @return Valor de la celda en formato String, o null si está vacía.
     */
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

    /**
     * Asigna valores a un objeto PlanProd según el nombre de columna y la celda
     * correspondiente.
     *
     * @param planProd      Objeto PlanProd a rellenar.
     * @param nombreColumna Nombre de la columna.
     * @param celda         Celda con el valor.
     */
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

                planProd.setPlannedQuantityUom1((int) Double.parseDouble(getCellValue(celda)));
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
            case "Tech_Group___Desc":
                planProd.setTechGroupDesc(getCellValue(celda));
                break;
            case "Tech_Sub_Group___Desc":
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

    /**
     * Asigna valores a un objeto EquiposAsignPorCriterio según el nombre de columna
     * y la celda correspondiente.
     *
     * @param equipos       Objeto EquiposAsignPorCriterio a rellenar.
     * @param nombreColumna Nombre de la columna.
     * @param celda         Celda con el valor.
     */
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

    /**
     * Asigna valores a un objeto EquiposCapacidad según el nombre de columna y la
     * celda correspondiente.
     *
     * @param equipos       Objeto EquiposCapacidad a rellenar.
     * @param nombreColumna Nombre de la columna.
     * @param celda         Celda con el valor.
     */
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

    /**
     * Escribe una lista de objetos PlanProd en un archivo Excel.
     *
     * @param listaPlanProd Lista de objetos PlanProd.
     * @param workbook      Objeto Workbook donde escribir los datos.
     * @see PlanProd
     */
    public void writeExcel(List<PlanProd> listaPlanProd, Workbook workbook) {
        Sheet sheet = workbook.createSheet("Mejor Planificacion");
        String[] encabezados = {
                "ID", "PPG Planning Class", "Item", "PPG Inventory Class", "Item Description", "Plant",
                "BX Ref", "Batch Status", "Routing Code", "Sales Order Number", "Sales Order Line Number",
                "Customer Name", "Customer Batch Number", "Planned Quantity UOM1", "UOM1", "Planned Quantity KG",
                "BX Start", "BX End", "Required Completion Date", "Item Type", "Batch Notes", "Item Notes",
                "PPG Item Strategic", "PPG Item Fleet", "Standard Prod Time", "Textbox8", "Planned Quantity Bulk",
                "Count Planned Quantity Bulk", "Planned Quantity FG", "Count Planned Quantity FG",
                "Planned Quantity Int",
                "Count Planned Quantity Int", "Planned Quantity Total", "Count Planned Quantity Total", "Item_",
                "PPG Inventory Class_", "PPG Planning Class_", "Item Description_", "Plant_", "BX Ref_",
                "Batch Status_", "Planned Quantity UOM1_", "UOM1_", "Planned Quantity KG_", "BX Start_", "BX End_",
                "Required Completion Date_", "Item Type_", "Batch Notes_", "Tech Group Desc", "Tech Sub Group Desc",
                "Standard Prod Time_", "Item__", "PPG Inventory Class__", "PPG Planning Class__", "Item Description__",
                "Plant__", "BX Ref__", "Batch Status__", "Product Line Desc", "Tech Group Desc_",
                "Tech Sub Group Desc_",
                "PPG Planning Class Description", "Formula Code", "Formula Version", "Routing Code_",
                "Sales Order Number1",
                "Sales Order Line Number_", "Customer Name_", "Planned Quantity UOM1__", "UOM1__",
                "Planned Quantity KG__",
                "BX Start__", "BX End__", "Required Completion Date__", "Item Type__", "Batch Notes__", "Item Notes_",
                "PPG Item Strategic_", "PPG Item Fleet_", "Standard Prod Time__"
        };
        // Escribir los encabezados en la primera fila
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < encabezados.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(encabezados[i]);
        }

        // Escribir los datos de la lista de PlanProd
        int filaActual = 1;
        for (PlanProd planProd : listaPlanProd) {
            Row row = sheet.createRow(filaActual++);
            Field[] fields = PlanProd.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    Object valor = fields[i].get(planProd);
                    String valorString = valor != null ? valor.toString() : "";
                    Cell celda = row.createCell(i);
                    celda.setCellValue(valorString);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Genera una vista tipo Gantt en un archivo Excel a partir de una lista de
     * objetos PlanProd.
     *
     * @param listaPlanProd Lista de objetos PlanProd.
     * @param libro         Objeto Workbook donde generar la vista.
     * @see PlanProd
     */
    public void ganttExcel(List<PlanProd> listaPlanProd, Workbook libro) {
        Sheet hoja = libro.createSheet("Gantt");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        listaPlanProd.sort((t1, t2) -> LocalDate.parse(t1.getBxStart(), formatter)
                .compareTo(LocalDate.parse(t2.getBxStart(), formatter)));

        Set<LocalDate> fechas = listaPlanProd.stream()
                .flatMap(t -> List
                        .of(LocalDate.parse(t.getBxStart(), formatter), LocalDate.parse(t.getBxEnd(), formatter))
                        .stream())
                .collect(Collectors.toCollection(TreeSet::new));

        // Crear encabezado solo con las fechas exactas
        Row encabezado = hoja.createRow(0);
        encabezado.createCell(0).setCellValue("ID");

        int colNum = 1;
        for (LocalDate fecha : fechas) {
            Cell cell = encabezado.createCell(colNum++);
            cell.setCellValue(fecha.toString());
        }

        // Convertir el conjunto de fechas en una lista para obtener los índices
        var listaFechas = fechas.stream().collect(Collectors.toList());

        // Generador de colores aleatorios
        Random random = new Random();

        // Rellenar las filas con los datos de PlanProd y la barra de Gantt
        int filaNum = 1;
        for (PlanProd tarea : listaPlanProd) {
            Row fila = hoja.createRow(filaNum++);
            fila.createCell(0).setCellValue(tarea.getId()); // Columna para el ID

            // Generar un color aleatorio para cada tarea
            CellStyle estiloDuracion = libro.createCellStyle();
            byte red = (byte) random.nextInt(256);
            byte green = (byte) random.nextInt(256);
            byte blue = (byte) random.nextInt(256);
            XSSFColor color = new XSSFColor(new byte[] { red, green, blue }, null);
            ((XSSFCellStyle) estiloDuracion).setFillForegroundColor(color);
            estiloDuracion.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            estiloDuracion.setAlignment(HorizontalAlignment.LEFT);

            // Convertir bxStart y bxEnd a LocalDate
            LocalDate fechaInicioTarea = LocalDate.parse(tarea.getBxStart(), formatter);
            LocalDate fechaFinTarea = LocalDate.parse(tarea.getBxEnd(), formatter);

            // Obtener los índices de las fechas de inicio y fin en la lista de fechas
            // únicas
            int indiceInicio = listaFechas.indexOf(fechaInicioTarea) + 1;
            int indiceFin = listaFechas.indexOf(fechaFinTarea) + 1;

            // Rellenar la barra de duración en el rango de fechas exactas
            for (int i = indiceInicio; i <= indiceFin; i++) {
                Cell celdaDuracion = fila.createCell(i);
                if (i == indiceInicio) {
                    celdaDuracion.setCellValue(tarea.getId());
                }
                celdaDuracion.setCellStyle(estiloDuracion);
            }
        }
    }

    /**
     * Guarda un archivo Excel en una ubicación específica.
     *
     * @param workbook Objeto Workbook a guardar.
     * @param filePath Ruta donde se guardará el archivo.
     * @throws IOException Si ocurre un error al guardar el archivo Excel.
     */
    public void saveWorkbook(Workbook workbook, String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            logger.warn("Error al guardar el archivo Excel");
            e.printStackTrace();
        }
    }
}
