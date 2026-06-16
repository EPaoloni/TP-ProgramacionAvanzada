/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import exceptions.NombreApellidoInvalidoException;
import exceptions.PromedioInvalidoException;
import org.apache.commons.lang3.StringUtils;
import persona.Alumno;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author g.guzman
 */
public final class AlumnoUtils {
    
    private static final String DELIM = "\t";
    
    private AlumnoUtils() {
    }
    
    public static String alumno2String(Alumno alumno) {
        
        String dniFormatted = StringUtils.leftPad(String.valueOf(alumno.getDni()), 8, '0');
        String nombreFormatted = StringUtils.rightPad(alumno.getNombre(), 15, StringUtils.SPACE);
        String apellidoFormatted = StringUtils.rightPad(alumno.getApellido() != null ? alumno.getApellido() : "", 15, StringUtils.SPACE);
        String promedioFormatted = StringUtils.leftPad(String.format("%.2f", alumno.getPromedio()), 5, StringUtils.SPACE);
        String cantMatAprobFormatted = StringUtils.leftPad(String.valueOf(alumno.getCantMatAprob()), 3, '0');
        
        return dniFormatted + DELIM + nombreFormatted + DELIM + apellidoFormatted + DELIM +
                promedioFormatted + DELIM + cantMatAprobFormatted + DELIM +
                alumno.getFecIngStr() + DELIM +
                alumno.getEstado();
    } 
    
    public static Alumno string2Alumno(String alumnoStr) throws NombreApellidoInvalidoException {
        Alumno alumno = new Alumno();
        if (alumnoStr == null || alumnoStr.isEmpty()) {
            return null;
        }
        String[] campos = alumnoStr.split(DELIM, -1);
        
        if (campos.length < 7) {
            throw new NombreApellidoInvalidoException("Formato de línea inválido: faltan campos");
        }
        
        int index = 0;
        
        String dniStr = campos[index++].trim();
        if (dniStr.isEmpty()) {
            throw new NombreApellidoInvalidoException("DNI no puede estar vacío");
        }
        alumno.setDni(Integer.parseInt(dniStr));
        
        String nombreStr = campos[index++].trim();
        alumno.setNombre(nombreStr);
        
        String apellidoStr = campos[index++].trim();
        alumno.setApellido(apellidoStr);
        
        String promedioStr = campos[index++].trim();
        double promedio = 0.0;
        if (!promedioStr.isEmpty()) {
            promedio = Double.parseDouble(promedioStr.replace(",", "."));
        }
        
        String cantMatAprobStr = campos[index++].trim();
        short cantMatAprob = 0;
        if (!cantMatAprobStr.isEmpty()) {
            cantMatAprob = Short.parseShort(cantMatAprobStr);
        }
        
        try {
            alumno.setPromedio(promedio);
        } catch (PromedioInvalidoException ex) {
            Logger.getLogger(AlumnoUtils.class.getName()).log(Level.WARNING, "Promedio inválido: " + promedio, ex);
        }
        alumno.setCantMatAprob(cantMatAprob);
        
        String fecIngStr = campos[index++].trim();
        if (!fecIngStr.isEmpty()) {
            String[] fechaParts = fecIngStr.split("/");
            if (fechaParts.length == 3) {
                try {
                    int day = Integer.parseInt(fechaParts[0]);
                    int month = Integer.parseInt(fechaParts[1]);
                    int year = Integer.parseInt(fechaParts[2]);
                    alumno.setFecIng(LocalDate.of(year, month, day));
                } catch (Exception e) {
                    Logger.getLogger(AlumnoUtils.class.getName()).log(Level.WARNING, "Fecha inválida: " + fecIngStr, e);
                }
            }
        }
        
        char estado = 'A';
        if (index < campos.length) {
            String estadoStr = campos[index].trim();
            if (!estadoStr.isEmpty()) {
                estado = estadoStr.charAt(0);
            }
        }
        alumno.setEstado(estado);
        
        return alumno;
    }
    
}
