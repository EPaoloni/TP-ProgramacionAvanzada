/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.alumnogui;

import exceptions.NombreApellidoInvalidoException;
import org.apache.commons.lang3.StringUtils;
import exceptions.PromedioInvalidoException;
import persona.Alumno;

/**
 *
 * @author g.guzman
 */
public final class AlumnoMapper {

    private AlumnoMapper() {
    }

    public static AlumnoDTO entity2Dto(Alumno alu){
        AlumnoDTO dto = new AlumnoDTO();
        dto.setDni(String.valueOf(alu.getDni()));
        dto.setNombre(StringUtils.defaultString(alu.getNombre()).trim());
        dto.setFecNac(alu.getFecNac());
        dto.setFecIng(alu.getFecIng());
        dto.setApellido(alu.getApellido());
        dto.setPromedio(String.valueOf(alu.getPromedio()));
        dto.setCantMatAprob(String.valueOf(alu.getCantMatAprob()));
        dto.setEstado(alu.getEstado());

        return dto;
    }

    public static Alumno dto2Entity(AlumnoDTO dto) throws NombreApellidoInvalidoException, PromedioInvalidoException{
        Alumno alu = new Alumno();
        alu.setDni(Integer.valueOf(dto.getDni()));
        alu.setNombre(dto.getNombre());
        alu.setApellido(dto.getApellido());
        alu.setFecNac(dto.getFecNac());
        alu.setFecIng(dto.getFecIng());
        alu.setPromedio(Double.parseDouble(dto.getPromedio().replace(",", ".")));
        alu.setCantMatAprob(Short.parseShort(dto.getCantMatAprob()));
        alu.setEstado(dto.getEstado());


        return alu;
    }
    
}
