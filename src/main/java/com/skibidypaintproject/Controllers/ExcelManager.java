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
    
    private void asignarValorEquiposAsignPorCriterio(EquiposAsignPorCriterio equipos, String nombreColumna, Cell celda) {
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
